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
package org.stajistics.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author The Stajistics Project
 */
@RunWith(JMock.class)
public class DefaultStatsSessionManagerTest extends AbstractStatsSessionManagerTestCase {

    @Override
    protected StatsSessionManager createSessionManager() {
        return new DefaultStatsSessionManager(mockConfigManager, mockEventManager);
    }

    @Test
    public void testConstructWithNullConfigManager() {
        try {
            new DefaultStatsSessionManager(null, mockEventManager);
            fail();
        } catch (NullPointerException npe) {
            assertEquals("configManager", npe.getMessage());
        }
    }

    @Test
    public void testConstructWithNullEventManager() {
        try {
            new DefaultStatsSessionManager(mockConfigManager, null);
            fail();
        } catch (NullPointerException npe) {
            assertEquals("eventManager", npe.getMessage());
        }
    }
}
