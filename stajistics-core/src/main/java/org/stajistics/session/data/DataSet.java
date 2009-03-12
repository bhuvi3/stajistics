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
package org.stajistics.session.data;

import java.util.Set;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public interface DataSet {

    /**
     * Common field names
     */
    public interface Field {
        public static final String HITS = "hits";
        public static final String FIRST_HIT_STAMP = "firstHitStamp";
        public static final String LAST_HIT_STAMP = "lastHitStamp";
        public static final String COMMITS = "commits";
        public static final String FIRST = "first";
        public static final String LAST = "last";
        public static final String MIN = "min";
        public static final String MAX = "max";
        public static final String SUM = "sum";
        public static final String ARITHMETIC_MEAN = "aMean";
        public static final String GEOMETRIC_MEAN = "gMean";
        public static final String HARMONIC_MEAN = "hMean";
        public static final String QUADRATIC_MEAN = "qMean";
        public static final String STANDARD_DEVIATION = "stdDev";
    }

    Object getField(String name);

    Set<String> getFieldNames();

    int size();

    boolean isEmpty();

}
