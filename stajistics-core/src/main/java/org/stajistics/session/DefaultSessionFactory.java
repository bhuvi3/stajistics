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
package org.stajistics.session;

import org.stajistics.StatsKey;
import org.stajistics.session.collector.DistributionDataCollector;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public class DefaultSessionFactory implements StatsSessionFactory {

    private static DefaultSessionFactory instance = new DefaultSessionFactory();

    private DefaultSessionFactory() {}

    public static DefaultSessionFactory getInstance() {
        return instance;
    }

    @Override
    public StatsSession createSession(final StatsKey key) {
        return new ConcurrentStatsSession(key, new DistributionDataCollector());
    }
}