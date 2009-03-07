/* Copyright 2009 The Stajistics Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.stajistics.session;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stajistics.Stats;
import org.stajistics.StatsKey;
import org.stajistics.data.DataSet;
import org.stajistics.data.DefaultDataSet;
import org.stajistics.data.MutableDataSet;
import org.stajistics.event.StatsEventType;
import org.stajistics.session.collector.DataCollector;
import org.stajistics.tracker.StatsTracker;
import org.stajistics.util.AtomicDouble;

/**
 * 
 * 
 * 
 * @author The Stajistics Project
 */
public class ConcurrentStatsSession implements StatsSession {

    private static final long serialVersionUID = -5265957157097835416L;

    private static final Logger logger = LoggerFactory.getLogger(ConcurrentStatsSession.class);

    private static final DecimalFormat DECIMAL_FORMAT;
    static {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        DECIMAL_FORMAT = new DecimalFormat("0.###", dfs);
        DECIMAL_FORMAT.setGroupingSize(Byte.MAX_VALUE);
    }

    protected final StatsKey key;

    protected volatile boolean enabled = true;

    protected final AtomicLong hits = new AtomicLong(0);
    protected final AtomicLong firstHitStamp = new AtomicLong(0);
    protected final AtomicLong lastHitStamp = new AtomicLong(0);
    protected final AtomicLong commits = new AtomicLong(0);

    protected final AtomicReference<Double> first = new AtomicReference<Double>(null);
    protected final AtomicDouble last = new AtomicDouble(Double.NaN);
    protected final AtomicDouble min = new AtomicDouble(Double.MAX_VALUE);
    protected final AtomicDouble max = new AtomicDouble(Double.MIN_VALUE);
    protected final AtomicDouble sum = new AtomicDouble(0);

    protected final List<DataCollector> dataCollectors = new LinkedList<DataCollector>();

    public ConcurrentStatsSession(final StatsKey key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        this.key = key;
    }

    @Override
    public void open(final StatsTracker tracker, 
                     long now) {

        if (!enabled) {
            return;
        }

        if (now < 0) {
            now = System.currentTimeMillis();
        }

        hits.incrementAndGet();

        if (firstHitStamp.get() == 0) {
            firstHitStamp.compareAndSet(0, now);
        }
        lastHitStamp.set(now);

        if (logger.isDebugEnabled()) {
            logger.debug("Open: " + this);
        }

        fireOpenEvent(this, tracker);
    }

    protected void fireOpenEvent(final StatsSession session,
                                 final StatsTracker tracker) {
        Stats.getEventManager()
             .fireEvent(StatsEventType.TRACKER_OPENED, session, tracker);
    }

    @Override
    public long getHits() {
        return hits.get();
    }

    @Override
    public long getFirstHitStamp() {
        return firstHitStamp.get();
    }

    @Override
    public long getLastHitStamp() {
        return lastHitStamp.get();
    }

    @Override
    public long getCommits() {
        return commits.get();
    }

    @Override
    public StatsKey getKey() {
        return key;
    }

    @Override
    public void update(final StatsTracker tracker, long now) {

        if (!enabled) {
            return;
        }

        if (now < 0) {
            now = System.currentTimeMillis();
        }

        double currentValue = tracker.getValue();
        double tmp;

        commits.incrementAndGet();

        // First
        if (first.get() == null) {
            first.compareAndSet(null, new Double(currentValue));
        }

        // Last
        last.set(currentValue);

        // Min
        for (;;) {
            tmp = min.get();
            if (currentValue < tmp) {
                if (min.compareAndSet(tmp, currentValue)) {
                    break;
                }
            } else {
                break;
            }
        }

        // Max
        for (;;) {
            tmp = max.get();
            if (currentValue > tmp) {
                if (max.compareAndSet(tmp, currentValue)) {
                    break;
                }
            } else {
                break;
            }
        }

        // Sum
        sum.getAndAdd(currentValue);

        for (DataCollector dataCollector : dataCollectors) {
            dataCollector.update(this, tracker, now);
        }

        if (logger.isInfoEnabled()) {
            logger.info("Commit: " + this);
        }

        fireUpdateEvent(this, tracker);
    }

    protected void fireUpdateEvent(final StatsSession session,
                                   final StatsTracker tracker) {
        Stats.getEventManager()
             .fireEvent(StatsEventType.TRACKER_COMMITTED, session, tracker);
    }

    @Override
    public double getFirst() {
        Double firstValue = first.get();

        if (firstValue == null) {
            return Double.NaN;
        }

        return firstValue.doubleValue();
    }

    @Override
    public double getLast() {
        return this.last.get();
    }

    @Override
    public double getMin() {
        return this.min.get();
    }

    @Override
    public double getMax() {
        return this.max.get();
    }

    public double getSum() {
        return this.sum.get();
    }

    @Override
    public DataSet dataSet() {

        MutableDataSet dataSet = new DefaultDataSet();

        dataSet.setField(Attributes.HITS, getHits());
        dataSet.setField(Attributes.FIRST_HIT_STAMP, new Date(getFirstHitStamp()));
        dataSet.setField(Attributes.LAST_HIT_STAMP, new Date(getLastHitStamp()));
        dataSet.setField(Attributes.COMMITS, getCommits());
        dataSet.setField(Attributes.FIRST, getFirst());
        dataSet.setField(Attributes.LAST, getLast());
        dataSet.setField(Attributes.MIN, getMin());
        dataSet.setField(Attributes.MAX, getMax());
        dataSet.setField(Attributes.SUM, getSum());

        for (DataCollector dataCollector : dataCollectors) {
            dataCollector.getData(this, dataSet);
        }

        return dataSet;
    }

    @Override
    public StatsSession snapshot() {
        return new ImmutableStatsSession(this);
    }

    @Override
    public void clear() {
        hits.set(0);
        firstHitStamp.set(0);
        lastHitStamp.set(0);
        commits.set(0);
        first.set(null);
        last.set(0);
        min.set(0);
        max.set(0);
        sum.set(0);

        for (DataCollector dataCollector : dataCollectors) {
            dataCollector.clear();
        }
    }

    @Override
    public StatsSession addDataCollector(final DataCollector dataCollector) {
        if (dataCollector == null) {
            throw new NullPointerException("dataCollector");
        }

        dataCollectors.add(dataCollector);

        return this;
    }

    @Override
    public void removeDataCollector(final DataCollector dataCollector) {
        if (dataCollector == null) {
            return;
        }

        dataCollectors.remove(dataCollector);
    }

    @Override
    public List<DataCollector> getDataCollectors() {
        return Collections.unmodifiableList(dataCollectors);
    }

    protected void appendStat(final StringBuilder buf,
                              final String name,
                              final Object value) {
        buf.append(',');
        buf.append(name);
        buf.append('=');

        if (value instanceof Double) {
            buf.append(DECIMAL_FORMAT.format(value));
        } else {
            buf.append(String.valueOf(value));
        }
    }

    public String toString() {

        StringBuilder buf = new StringBuilder(512);

        buf.append(StatsSession.class.getSimpleName());
        buf.append("[key=");
        buf.append(key);

        DataSet dataSet = dataSet();

        for (String name : dataSet.getFieldNames()) {
            appendStat(buf, name, dataSet.getField(name));
        }

        buf.append(']');

        return buf.toString();
    }

}
