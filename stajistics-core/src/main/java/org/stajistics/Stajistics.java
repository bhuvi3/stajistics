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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author The Stajistics Project
 *
 */
public final class Stajistics {

    private static final String PROPS_FILE = "library.properties";
    private static final String PROP_NAME = "library.name";
    private static final String PROP_VERSION = "library.version";

    private static String name;
    private static String version;
    static {
        Properties props = new Properties();
        try {
            ClassLoader classLoader = Stajistics.class.getClassLoader();
            InputStream in = classLoader.getResourceAsStream(PROPS_FILE);

            if (in != null) {
                props.load(in);

                name = props.getProperty(PROP_NAME);
                version = props.getProperty(PROP_VERSION);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (name == null) {
            name = "";
        }
        if (version == null) {
            version = "";
        }
    }

    private Stajistics() {}

    public static String getName() {
        return name;
    }

    public static String getVersion() {
        return version;
    }
}
