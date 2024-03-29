/* Copyright 2009 - 2010 The Stajistics Project
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
package org.stajistics.tracker.manual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stajistics.StatsKey;
import org.stajistics.session.StatsSession;
import org.stajistics.session.StatsSessionManager;
import org.stajistics.tracker.AbstractTracker;
import org.stajistics.tracker.TrackerFactory;
import org.stajistics.util.Misc;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public class DefaultManualTracker extends AbstractTracker implements ManualTracker {

    private static final Logger logger = LoggerFactory.getLogger(DefaultManualTracker.class);

    public static final Factory FACTORY = new Factory();

    public DefaultManualTracker(final StatsSession statsSession) {
        super(statsSession);
    }

    @Override
    public ManualTracker addValue(final double value) {
        this.value += value;
        return this;
    }

    @Override
    public ManualTracker setValue(final double value) {
        this.value = value;
        return this;
    }

    @Override
    public void commit() {
        try {
            final long now = System.currentTimeMillis();
            session.track(this, now);
            session.update(this, now);
        } catch (Exception e) {
            Misc.logHandledException(logger, e, "Caught Exception in commit()");
            Misc.handleUncaughtException(getKey(), e);
        }
    }

    public static class Factory implements TrackerFactory<ManualTracker> {

        @Override
        public ManualTracker createTracker(final StatsKey key,
                                                final StatsSessionManager sessionManager) {
            return new DefaultManualTracker(sessionManager.getOrCreateSession(key));
        }
        
        @Override
        public Class<ManualTracker> getTrackerType() {
            return ManualTracker.class;
        }
    }
}
