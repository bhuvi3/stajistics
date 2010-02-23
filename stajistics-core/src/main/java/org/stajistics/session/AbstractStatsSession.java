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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stajistics.StatsKey;
import org.stajistics.StatsProperties;
import org.stajistics.data.DataSet;
import org.stajistics.data.DefaultDataSet;
import org.stajistics.event.StatsEventManager;
import org.stajistics.session.recorder.DataRecorder;

/**
 * 
 * @author The Stajistics Project
 */
public abstract class AbstractStatsSession implements StatsSession {

    private static final String PROP_USE_ZEROS_FOR_EMPTY_VALUE = AbstractStatsSession.class.getName()
            + ".useZerosForEmpty";

    protected static final Double EMPTY_VALUE = StatsProperties.getBooleanProperty(
            PROP_USE_ZEROS_FOR_EMPTY_VALUE, false) ? 0 : Double.NaN;

    private static final Logger logger = LoggerFactory.getLogger(AbstractStatsSession.class);

    protected static final DataRecorder[] EMPTY_DATA_RECORDER_ARRAY = new DataRecorder[0];

    protected static final DecimalFormat DECIMAL_FORMAT;
    static {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        DECIMAL_FORMAT = new DecimalFormat("0.###", dfs);
        DECIMAL_FORMAT.setGroupingSize(Byte.MAX_VALUE);
    }

    protected final StatsKey key;
    protected final StatsEventManager eventManager;

    protected final DataRecorder[] dataRecorders;

    public AbstractStatsSession(final StatsKey key,
                                final StatsEventManager eventManager,
                                final DataRecorder... dataRecorders) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        if (eventManager == null) {
            throw new NullPointerException("eventManager");
        }

        this.key = key;
        this.eventManager = eventManager;

        if (dataRecorders == null) {
            this.dataRecorders = EMPTY_DATA_RECORDER_ARRAY;
        } else {
            this.dataRecorders = dataRecorders;
        }
    }

    @Override
    public StatsKey getKey() {
        return key;
    }

    @Override
    public List<DataRecorder> getDataRecorders() {
        return Collections.unmodifiableList(Arrays.asList(dataRecorders));
    }

    @Override
    public Object getField(String name) {
        // Intern the name to allow fast reference equality checks
        name = name.intern();

        // Check basic fields

        if (name == DataSet.Field.HITS) {
            return getHits();
        }
        if (name == DataSet.Field.FIRST_HIT_STAMP) {
            return getFirstHitStamp();
        }
        if (name == DataSet.Field.LAST_HIT_STAMP) {
            return getLastHitStamp();
        }
        if (name == DataSet.Field.COMMITS) {
            return getCommits();
        }
        if (name == DataSet.Field.FIRST) {
            return getFirst();
        }
        if (name == DataSet.Field.LAST) {
            return getLast();
        }
        if (name == DataSet.Field.MIN) {
            return getMin();
        }
        if (name == DataSet.Field.MAX) {
            return getMax();
        }
        if (name == DataSet.Field.SUM) {
            return getSum();
        }

        // Check DataRecorder fields

        final int dataRecorderCount = dataRecorders.length;
        for (int i = 0; i < dataRecorderCount; i++) {
            try {
                if (dataRecorders[i].getSupportedFieldNames().contains(name)) {
                    Object result = dataRecorders[i].getField(this, name);
                    if (result != null) {
                        return result;
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to getField '" + name + "' from " + dataRecorders[i], e);
            }
        }

        // Not found
        return null;
    }

    @Override
    public DataSet collectData() {

        final DataSet dataSet = new DefaultDataSet();

        dataSet.setField(DataSet.Field.HITS, getHits());
        dataSet.setField(DataSet.Field.FIRST_HIT_STAMP, getFirstHitStamp());
        dataSet.setField(DataSet.Field.LAST_HIT_STAMP, getLastHitStamp());
        long commits = getCommits();
        dataSet.setField(DataSet.Field.COMMITS, commits);
        if (commits > 0) {
            dataSet.setField(DataSet.Field.FIRST, getFirst());
            dataSet.setField(DataSet.Field.LAST, getLast());
            dataSet.setField(DataSet.Field.MIN, getMin());
            dataSet.setField(DataSet.Field.MAX, getMax());
            dataSet.setField(DataSet.Field.SUM, getSum());
        } else {
            dataSet.setField(DataSet.Field.FIRST, EMPTY_VALUE);
            dataSet.setField(DataSet.Field.LAST, EMPTY_VALUE);
            dataSet.setField(DataSet.Field.MIN, EMPTY_VALUE);
            dataSet.setField(DataSet.Field.MAX, EMPTY_VALUE);
            dataSet.setField(DataSet.Field.SUM, 0.0);
        }

        for (DataRecorder dataRecorder : dataRecorders) {
            try {
                dataRecorder.collectData(this, dataSet);
            } catch (Exception e) {
                logger.error("Failed to collectData from " + dataRecorder, e);
            }
        }

        return dataSet;
    }

    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder(512);

        buf.append(StatsSession.class.getSimpleName());
        buf.append("[key=");
        buf.append(key);
        buf.append(",hits=");
        buf.append(getHits());
        buf.append(",firstHitStamp=");
        buf.append(getFirstHitStamp());
        buf.append(",lastHitStamp=");
        buf.append(getLastHitStamp());
        buf.append(",commits=");
        buf.append(getCommits());
        buf.append(",first=");
        buf.append(DECIMAL_FORMAT.format(getFirst()));
        buf.append(",last=");
        buf.append(DECIMAL_FORMAT.format(getLast()));
        buf.append(",min=");
        buf.append(DECIMAL_FORMAT.format(getMin()));
        buf.append(",max=");
        buf.append(DECIMAL_FORMAT.format(getMax()));
        buf.append(",sum=");
        buf.append(DECIMAL_FORMAT.format(getSum()));
        buf.append(']');

        return buf.toString();
    }
}
