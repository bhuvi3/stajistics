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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
public abstract class AbstractStatsKeyTestCase {

    protected static final String TEST_NAME = "testName";
    protected static final String TEST_UNIT = "testUnit";
    protected static final Map<String,Object> TEST_ATTRIBUTES = new HashMap<String,Object>();

    protected Mockery mockery;
    protected StatsKeyFactory mockKeyFactory;

    @Before
    public void setUp() {
        mockery = new Mockery();
        mockKeyFactory = mockery.mock(StatsKeyFactory.class);
    }

    protected abstract StatsKey createStatsKey(String name,
                                               StatsKeyFactory keyFactory,
                                               Map<String,Object> attributes);

    protected StatsKey createStatsKey(final String name) {
        return createStatsKey(name, 
                              mockKeyFactory,
                              TEST_ATTRIBUTES);
    }

    @Test
    public void testConstructWithNullName() {
        try {
            createStatsKey(null);

            fail("Allowed construction with null name");

        } catch (NullPointerException npe) {
            assertEquals("name", npe.getMessage());
        }
    }

    @Test
    public void testConstructWithNullKeyFactory() {
        try {
            createStatsKey(TEST_NAME, null, Collections.<String,Object>emptyMap());

            fail("Allowed construction with null key factory");

        } catch (NullPointerException npe) {
            assertEquals("keyFactory", npe.getMessage());
        }
    }

    @Test
    public void testConstructWithNullAttributes() {
        try {
            createStatsKey(TEST_NAME, mockKeyFactory, null);

            fail("Allowed construction with null attributes");

        } catch (NullPointerException npe) {
            assertEquals("attributes", npe.getMessage());
        }
    }

    @Test
    public void testBuildCopy() {

        final StatsKey key = createStatsKey(TEST_NAME);
        final StatsKeyBuilder builder = mockery.mock(StatsKeyBuilder.class);

        mockery.checking(new Expectations() {{
            one(mockKeyFactory).createKeyBuilder(key); will(returnValue(builder));
        }});

        assertSame(builder, key.buildCopy());
    }

    @Test
    public void testEqualsSameInstance() {
        StatsKey key = createStatsKey(TEST_NAME);
        assertEquals(key, key);
    }

    @Test
    public void testEqualsNull() {
        StatsKey key = createStatsKey(TEST_NAME);
        assertFalse(key.equals(null));
    }

    @Test
    public void testEqualsDifferentType() {
        StatsKey key = createStatsKey(TEST_NAME);
        assertFalse(key.equals("123"));
    }

    @Test
    public void testEqualsKeyWithSameName() {
        StatsKey key1 = createStatsKey(TEST_NAME);
        StatsKey key2 = createStatsKey(TEST_NAME);
        assertEquals(key1, key2);
    }

    @Test
    public void testEqualsKeyWithDifferentName() {
        StatsKey key1 = createStatsKey(TEST_NAME);
        StatsKey key2 = createStatsKey(TEST_NAME + "2");
        assertFalse(key1.equals(key2));
    }

    @Test
    public void testEqualsKeyWithDifferentAttributes() {
        StatsKey key1 = createStatsKey(TEST_NAME, 
                                       mockKeyFactory,
                                       TEST_ATTRIBUTES);
        StatsKey key2 = createStatsKey(TEST_NAME, 
                                       mockKeyFactory,
                                       Collections.<String,Object>singletonMap("test", "test"));
        assertFalse(key1.equals(key2));
    }

    @Test
    public void testHashCodeNotZero() {
        StatsKey key = createStatsKey(TEST_NAME);
        assertFalse(0 == key.hashCode());
    }

    @Test
    public void testHashCodeWithSameName() {
        StatsKey key1 = createStatsKey(TEST_NAME);
        StatsKey key2 = createStatsKey(TEST_NAME);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentName() {
        StatsKey key1 = createStatsKey(TEST_NAME);
        StatsKey key2 = createStatsKey(TEST_NAME + "2");
        assertFalse(key1.hashCode() == key2.hashCode());
    }

    @Test
    public void testToStringIsNotDefault() {
        StatsKey key = createStatsKey(TEST_NAME);
        assertFalse(key.toString().startsWith(key.getClass().getName() + "@"));
    }

    @Test
    public void testToStringContainsName() {
        StatsKey key = createStatsKey(TEST_NAME);
        assertTrue(key.toString().contains("name=" + key.getName()));
    }

    @Test
    public void testToStringContainsAttributes() {
        StatsKey key = createStatsKey(TEST_NAME, 
                                      mockKeyFactory, 
                                      Collections.<String,Object>singletonMap("name", "value"));
        assertTrue(key.toString().contains("name=value"));
    }
}
