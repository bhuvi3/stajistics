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
package org.stajistics.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 *
 * @author The Stajistics Project
 */
public class DefaultDataSetTest extends AbstractDataContainerTestCase {

    @Override
    protected DataSet createDataContainer() {
        return new DefaultDataSet(-1L, false);
    }

    @Override
    protected DataSet dc() {
        return (DataSet)super.dc();
    }

    @Test
    public void testGetMetaData() {
        assertNotNull(dc().getMetaData());
    }

    @Test
    public void testGetCollectionTimeStamp() {
        assertEquals(-1L, dc().getCollectionTimeStamp());
    }

    @Test
    public void testIsSessionDrained() {
        assertEquals(false, dc().isSessionDrained());
    }
}
