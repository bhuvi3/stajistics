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
package org.stajistics.snapshot.binding.impl.jibx;

import java.util.HashMap;
import java.util.Map;

import org.stajistics.snapshot.binding.ApplicationEnvironment;

/**
 * 
 * @author The Stajistics Project
 */
public class ApplicationEnvironmentImpl implements ApplicationEnvironment {

    private HashMap<String,String> properties = new HashMap<String,String>();

    @Override
    public Map<String,String> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof ApplicationEnvironmentImpl) && equals((ApplicationEnvironmentImpl)obj);
    }

    public boolean equals(final ApplicationEnvironmentImpl other) {
        if ((properties == null || properties.isEmpty()) &&
            (other.properties == null || other.properties.isEmpty())) {
            return true;
        }
        return properties.equals(other.properties);
    }
}
