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

import org.stajistics.StatsKey;
import org.stajistics.StatsManager;
import org.stajistics.StatsManagerRegistry;
import org.stajistics.configuration.StatsConfig;
import org.stajistics.configuration.StatsConfigManager;
import org.stajistics.event.EventHandler;
import org.stajistics.event.EventType;
import org.stajistics.session.StatsSession;
import org.stajistics.session.StatsSessionManager;

/**
 *
 *
 *
 * @author The Stajistics Project
 */
public class StatsManagementEventHandler implements EventHandler {

    private final StatsMXBeanRegistrar mxBeanRegistrar;

    public StatsManagementEventHandler(final StatsMXBeanRegistrar mxBeanRegistrar) {
        if (mxBeanRegistrar == null) {
            throw new NullPointerException("mxBeanRegistrar");
        }

        this.mxBeanRegistrar = mxBeanRegistrar;
    }

    @Override
    public void handleStatsEvent(final EventType eventType,
                                 final StatsKey key,
                                 final Object target) {
        switch (eventType) {
            case STATS_MANAGER_INITIALIZED:
                mxBeanRegistrar.registerManagerMXBean((StatsManager)target);
                break;

            case STATS_MANAGER_SHUTTING_DOWN:
                mxBeanRegistrar.unregisterManagerMXBean((StatsManager)target);
                break;

            case CONFIG_MANAGER_INITIALIZED:
                mxBeanRegistrar.registerConfigManagerMXBean((StatsConfigManager)target);
                break;

            case CONFIG_MANAGER_SHUTTING_DOWN:
                mxBeanRegistrar.unregisterConfigManagerMXBean();
                break;
            
            case SESSION_MANAGER_INITIALIZED:
                mxBeanRegistrar.registerSessionManagerMXBean((StatsSessionManager)target);
                break;

            case SESSION_MANAGER_SHUTTING_DOWN:
                mxBeanRegistrar.unregisterSessionManagerMXBean();
                break;

            case TASK_SERVICE_INITIALIZED:
                // TODO
                break;

            case TASK_SERVICE_SHUTTING_DOWN:
                // TODO
                break;

            case SESSION_CREATED:
                mxBeanRegistrar.registerSessionMXBean((StatsSession)target);
                break;

            case SESSION_DESTROYED:
                mxBeanRegistrar.unregisterSessionMXBeanIfNecessary(key);
                break;

            case CONFIG_CREATED:
                mxBeanRegistrar.registerConfigMXBean(key, (StatsConfig)target);
                break;
                
            case CONFIG_CHANGED:
                mxBeanRegistrar.unregisterConfigMXBeanIfNecessary(key);
                mxBeanRegistrar.registerConfigMXBean(key, (StatsConfig)target);
                break;

            case CONFIG_DESTROYED:
                mxBeanRegistrar.unregisterConfigMXBeanIfNecessary(key);
                break;
        }
    }
}
