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
package org.stajistics;

import static org.junit.Assert.assertNotNull;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public class DefaultStatsConfigFactoryTest {

    // TODO: more

    private Mockery mockery;
    private StatsConfigManager mockConfigManager;
    private StatsConfigFactory configFactory;

    @Before
    public void setUp() {
        mockery = new Mockery();
        mockConfigManager = mockery.mock(StatsConfigManager.class);

        configFactory = new DefaultStatsConfigFactory(mockConfigManager);
    }

    @Test
    public void testCreateConfigBuilder() {
        assertNotNull(configFactory.createConfigBuilder());
    }

    @Test
    public void testCreateConfigBuilderWithTemplate() {
        final StatsConfig config = mockery.mock(StatsConfig.class);
        mockery.checking(new Expectations() {{
            ignoring(config);
        }});

        assertNotNull(configFactory.createConfigBuilder(config));
    }

}