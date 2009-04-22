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

import org.stajistics.event.StatsEventManager;
import org.stajistics.session.StatsSessionManager;
import org.stajistics.tracker.CompositeStatsTracker;
import org.stajistics.tracker.ManualStatsTracker;
import org.stajistics.tracker.NullTracker;
import org.stajistics.tracker.StatsTracker;
import org.stajistics.tracker.StatsTrackerFactory;

/**
 * Acts as an aggregator of other managers as well as a factory for various entities in
 * statistics collection. 
 *
 * @author The Stajistics Project
 */
public interface StatsManager {

    /**
     * Get the {@link StatsConfigManager}.
     *
     * @return The {@link StatsConfigManager}, never <tt>null</tt>.
     */
    StatsConfigManager getConfigManager();

    /**
     * Get the {@link StatsSessionManager}.
     *
     * @return The @link StatsSessionManager}, never <tt>null</tt>.
     */
    StatsSessionManager getSessionManager();

    /**
     * Get the {@link StatsEventManager}.
     *
     * @return The {@link StatsEventManager}, never <tt>null</tt>.
     */
    StatsEventManager getEventManager();

    /**
     * Determine if statistics collection is enabled.
     *
     * @return <tt>true</tt> if statistics collection is enabled, <tt>false</tt> otherwise.
     */
    boolean isEnabled();

    /**
     * Enabled or disable statistics collection.
     *
     * @param enabled <tt>true</tt> to enable statistics collection, <tt>false</tt> to disable.
     */
    void setEnabled(boolean enabled);

    /**
     * Obtain a {@link StatsTracker} for the given <tt>key</tt> that can be
     * used to collect statistics. If statistics collection is disabled, 
     * a safe no-op {@link NullTracker} instance is returned.
     *
     * @param key The {@link StatsKey} for which to return a tracker.
     * @return A {@link StatsTracker}. Never <tt>null</tt>.
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>.
     */
    StatsTracker getTracker(StatsKey key);

    /**
     * Obtain a {@link StatsTracker} for the given set of <tt>keys</tt> that can be used to
     * collect statistics. A {@link StatsTracker} is obtained for each key in <tt>keys</tt>
     * and all are wrapped in a {@link CompositeStatsTracker} instance.
     *
     * @param keys The {@link StatsKey}s for which to return a tracker.
     * @return A {@link StatsTracker}, never <tt>null</tt>.
     * @see CompositeStatsTracker
     */
    StatsTracker getTracker(StatsKey... keys);

    /**
     * Obtain a {@link ManualStatsTracker} for a given {@link StatsKey} that can be used to
     * report manually collected data. Regardless of the given <tt>key</tt>s configured
     * {@link StatsTrackerFactory}, a ManualStatsTracker instance is returned.
     *
     * @param key The {@link StatsKey} for which to return a manual tracker.
     * @return A {@link ManualStatsTracker}, never <tt>null</tt>.
     */
    ManualStatsTracker getManualTracker(StatsKey key);

    /**
     * Create a new {@link StatsKey} from the given <tt>name</tt>.
     *
     * @param name The name of the key to create.
     * @return A new {@link StatsKey}, never <tt>null</tt>.
     */
    StatsKey createKey(String name);

    /**
     * Create a new {@link StatsKeyBuilder} which can create a new {@link StatsKey}
     * for the given <tt>name</tt>.
     *
     * @param name The name of the key that the builder will create.
     * @return A {@link StatsKeyBuilder} which can be used to define key attributes, never <tt>null</tt>.
     */
    StatsKeyBuilder createKeyBuilder(String name);

    /**
     * Create a new {@link StatsKeyBuilder} which can create a new {@link StatsKey}.
     * The builder is initialized with the name and attributes of the given {@link StatsKey}
     * <tt>template</tt>.
     *
     * @param template The key with which to initialize the {@link StatsKeyBuilder}.
     * @return A {@link StatsKeyBuilder} which can be used to define key attributes, never <tt>null</tt>.
     */
    StatsKeyBuilder createKeyBuilder(StatsKey template);

    /**
     * Create a new {@link StatsConfigBuilder} which can assemble various configurations.
     *
     * @return A {@link StatsKeyBuilder} which can be used to specify configuration, never <tt>null</tt>.
     */
    StatsConfigBuilder createConfigBuilder();

    /**
     * Create a new {@link StatsConfigBuilder} which can assemble various configurations. The 
     * builder is initialized from the given configuration <tt>template</tt>.
     *
     * @param template The configuration with which to initialize the builder.
     * @return A {@link StatsKeyBuilder} which can be used to specify configuration, never <tt>null</tt>.
     */
    StatsConfigBuilder createConfigBuilder(StatsConfig template);

}
