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
package org.stajistics;

import org.junit.Test;
import org.stajistics.bootstrap.DefaultStatsManagerFactory;
import org.stajistics.configuration.DefaultStatsConfigManager;
import org.stajistics.configuration.StatsConfig;
import org.stajistics.configuration.StatsConfigManager;
import org.stajistics.event.EventManager;

/**
 *
 * @author The Stajistics Project
 */
public class Issue31Test extends AbstractStajisticsTestCase {

    @Test
    public void testConfigChangedEventIsNotFiredWhenUpdatingRootConfig() {
        StatsManager statsManager = new DefaultStatsManagerFactory().createManager(StatsConstants.DEFAULT_NAMESPACE);
        EventManager mockEventManager = mockery.mock(EventManager.class);

        StatsConfigManager configManager = new DefaultStatsConfigManager(mockEventManager,
                                                                         statsManager.getKeyFactory());

        StatsConfig defaultRootConfig = configManager.getRootConfig();

        StatsConfig newRootConfig = statsManager.getConfigBuilderFactory()
                                                .createConfigBuilder(defaultRootConfig)
                                                .withDescription("dummy")
                                                .newConfig();
        configManager.setRootConfig(newRootConfig);
    }
}
