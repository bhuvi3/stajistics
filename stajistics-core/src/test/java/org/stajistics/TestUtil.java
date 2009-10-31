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

import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.stajistics.event.StatsEventManager;
import org.stajistics.session.StatsSessionManager;
import org.stajistics.snapshot.StatsSnapshotManager;
import org.stajistics.tracker.StatsTracker;
import org.stajistics.tracker.StatsTrackerLocator;
import org.stajistics.tracker.manual.ManualTracker;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public abstract class TestUtil {

    private TestUtil() {}

    public static void buildStatsKeyExpectations(final Mockery mockery,
                                                 final StatsKey mockKey,
                                                 final String keyName) {
        buildStatsKeyExpectations(mockery, mockKey, keyName, null, null);
    }

    public static void buildStatsKeyExpectations(final Mockery mockery,
                                                 final StatsKey mockKey,
                                                 final String keyName,
                                                 final String attrName,
                                                 final String attrValue) {
        mockery.checking(new Expectations() {{
            allowing(mockKey).getName(); will(returnValue(keyName));
            allowing(mockKey).getAttribute(with((String)null)); will(returnValue(null));

            if (attrName == null) {
                allowing(mockKey).getAttribute((String)with(anything())); will(returnValue(null));
                allowing(mockKey).getAttributeCount(); will(returnValue(0));
                allowing(mockKey).getAttributes(); will(returnValue(Collections.emptyMap()));
            } else {
                allowing(mockKey).getAttribute(with(attrName)); will(returnValue(attrValue));
                allowing(mockKey).getAttributeCount(); will(returnValue(1));
                allowing(mockKey).getAttributes(); will(returnValue(Collections.singletonMap(attrName, attrValue)));
            }
        }});
    }

    /**
     * TODO: this needs more work to be useful
     *
     * @param mockery
     * @param mockKey
     * @return
     */
    public static StatsManager createMockStatsManager(final Mockery mockery,
                                                      final StatsKey mockKey) {
        final StatsManager mockManager = mockery.mock(StatsManager.class);

        final StatsConfigManager mockConfigManager = mockery.mock(StatsConfigManager.class);
        final StatsEventManager mockEventManager = mockery.mock(StatsEventManager.class);
        final StatsSessionManager mockSessionManager = mockery.mock(StatsSessionManager.class);
        final StatsSnapshotManager mockSnapshotManager = mockery.mock(StatsSnapshotManager.class);
        final StatsTrackerLocator mockTrackerLocator = mockery.mock(StatsTrackerLocator.class);
        final StatsKeyFactory mockKeyFactory = mockery.mock(StatsKeyFactory.class);
        final StatsConfigFactory mockConfigFactory = mockery.mock(StatsConfigFactory.class);

        final StatsTracker mockTracker = mockery.mock(StatsTracker.class);
        final ManualTracker mockManualTracker = mockery.mock(ManualTracker.class);

        mockery.checking(new Expectations() {{
            allowing(mockManager).getConfigFactory(); will(returnValue(mockConfigFactory));
            allowing(mockManager).getConfigManager(); will(returnValue(mockConfigManager));
            allowing(mockManager).getEventManager(); will(returnValue(mockEventManager));
            allowing(mockManager).getKeyFactory(); will(returnValue(mockKeyFactory));
            allowing(mockManager).getSessionManager(); will(returnValue(mockSessionManager));
            allowing(mockManager).getSnapshotManager(); will(returnValue(mockSnapshotManager));
            allowing(mockManager).getTrackerLocator(); will(returnValue(mockTrackerLocator));

            allowing(mockTrackerLocator).getTracker(with(mockKey)); will(returnValue(mockTracker));
            allowing(mockTrackerLocator).getManualTracker(with(mockKey)); will(returnValue(mockManualTracker));
        }});

        return mockManager;
    }
}
