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

package org.stajistics.tracker.incident;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.stajistics.session.StatsSession;

/**
 * 
 * @author The Stajistics Project
 *
 */
@RunWith(JMock.class)
public class DefaultIncidentTrackerTest {

    private Mockery mockery;
    private StatsSession mockSession;

    @Before
    public void setUp() throws Exception {
        mockery = new Mockery();
        mockSession = mockery.mock(StatsSession.class);
    }

    @Test
    public void testIncident() {
        final IncidentTracker tracker = new DefaultIncidentTracker(mockSession);

        mockery.checking(new Expectations() {{
            one(mockSession).track(with(tracker), with(any(long.class)));
            one(mockSession).update(with(tracker), with(any(long.class)));
        }});
        
        tracker.incident();
    }
}