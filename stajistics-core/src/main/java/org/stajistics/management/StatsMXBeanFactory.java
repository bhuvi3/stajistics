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

import java.io.Serializable;

import org.stajistics.StatsKey;
import org.stajistics.StatsManager;
import org.stajistics.configuration.StatsConfig;
import org.stajistics.configuration.StatsConfigManager;
import org.stajistics.management.beans.StatsConfigMXBean;
import org.stajistics.management.beans.StatsConfigManagerMXBean;
import org.stajistics.management.beans.StatsManagerMXBean;
import org.stajistics.management.beans.StatsSessionMXBean;
import org.stajistics.management.beans.StatsSessionManagerMXBean;
import org.stajistics.management.beans.TaskServiceMXBean;
import org.stajistics.session.StatsSession;
import org.stajistics.session.StatsSessionManager;
import org.stajistics.task.TaskService;

/**
 *
 *
 *
 * @author The Stajistics Project
 */
public interface StatsMXBeanFactory extends Serializable {

    StatsManagerMXBean createManagerMXBean(StatsManager statsManager);

    StatsConfigManagerMXBean createConfigManagerMXBean(StatsConfigManager configManager);
    
    StatsSessionManagerMXBean createSessionManagerMXBean(StatsSessionManager sessionManager);

    StatsConfigMXBean createConfigMXBean(String namespace, StatsKey key, StatsConfig config);

    StatsSessionMXBean createSessionMXBean(String namespace, StatsSession session);
}
