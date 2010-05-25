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
package org.stajistics.management;

import org.stajistics.StatsConfig;
import org.stajistics.StatsKey;
import org.stajistics.StatsManager;
import org.stajistics.event.EventHandler;
import org.stajistics.event.EventType;
import org.stajistics.session.StatsSession;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public class StatsManagementEventHandler implements EventHandler {

    private final StatsManager statsManager;
    private final StatsManagement statsManagement;

    public StatsManagementEventHandler(final StatsManager statsManager,
                                       final StatsManagement statsManagement) {
        if (statsManager == null) {
            throw new NullPointerException("statsManager");
        }
        if (statsManagement == null) {
            throw new NullPointerException("statsManagement");
        }

        this.statsManager = statsManager;
        this.statsManagement = statsManagement;
    }

    @Override
    public void handleStatsEvent(final EventType eventType, 
                                 final StatsKey key, 
                                 final Object target) {
        if (eventType == EventType.SESSION_CREATED) {
            statsManagement.registerSessionMBean(statsManager, (StatsSession)target);

        } else if (eventType == EventType.SESSION_DESTROYED) {
            statsManagement.unregisterSessionMBeanIfNecessary(statsManager, key);

        } else if (eventType == EventType.CONFIG_CREATED) {
            statsManagement.registerConfigMBean(statsManager, key, (StatsConfig)target);

        } else if (eventType == EventType.CONFIG_DESTROYED) {
            statsManagement.unregisterConfigMBeanIfNecessary(statsManager, key);

        } else if (eventType == EventType.CONFIG_CHANGED) {
            statsManagement.unregisterConfigMBeanIfNecessary(statsManager, key);
            statsManagement.registerConfigMBean(statsManager, key, (StatsConfig)target);
        }
    }
}
