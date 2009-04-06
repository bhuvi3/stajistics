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

import java.util.Collections;
import java.util.Set;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public final class NullDataSet implements DataSet {

    private static final long serialVersionUID = 7206590288937897600L;

    private static final NullDataSet instance = new NullDataSet();

    private NullDataSet() {}

    public static NullDataSet getInstance() {
        return instance;
    }

    @Override
    public Object getField(String name) {
        return null;
    }

    @Override
    public Set<String> getFieldNames() {
        return Collections.emptySet();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int size() {
        return 0;
    }

}
