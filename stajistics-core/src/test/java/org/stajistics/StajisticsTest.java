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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @author The Stajistics Project
 *
 */
public class StajisticsTest {

    @Test
    public void testGetName() {
        assertNotNull(Stajistics.getName());
        assertTrue(Stajistics.getName().length() > 0);
    }

    @Test
    public void testGetVersion() {
        assertNotNull(Stajistics.getVersion());
        assertTrue(Stajistics.getVersion().length() > 0);
    }
}
