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
package org.stajistics.tracker;

import java.lang.management.ThreadInfo;

import org.stajistics.session.StatsSession;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public class ThreadBlockTimeTracker extends AbstractThreadInfoStatsTracker {

    private long startBlockTime;

    public ThreadBlockTimeTracker(final StatsSession session) {
        super(session);

        ensureContentionMonitoringEnabled();
    }

    @Override
    protected void trackImpl(final long now) {
        ThreadInfo threadInfo = getCurrentThreadInfo();
        if (threadInfo != null) {
            startBlockTime = threadInfo.getBlockedTime();
            if (startBlockTime > -1) {
                super.trackImpl(now);
            }

        } else {
            startBlockTime = -1;
        }
    }

    @Override
    protected void commitImpl(final long now) {
        ThreadInfo threadInfo = getCurrentThreadInfo();
        if (threadInfo != null && startBlockTime > -1) {
            long endBlockTime = threadInfo.getBlockedTime();
            if (endBlockTime > -1) {
                value = endBlockTime - startBlockTime;
                super.commitImpl(now);
            }
        }
    }

    @Override
    public StatsTracker reset() {
        super.reset();

        startBlockTime = -1;

        return this;
    }
}
