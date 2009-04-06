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

import org.stajistics.session.StatsSessionFactory;
import org.stajistics.tracker.StatsTracker;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public class DefaultStatsConfigBuilder extends DefaultStatsKeyBuilder implements StatsConfigBuilder {

    protected Class<? extends StatsTracker> trackerClass;
    protected StatsSessionFactory sessionFactory;
    protected String unit;
    protected String description;

    public DefaultStatsConfigBuilder(final String name) {
        super(name);
    }

    public DefaultStatsConfigBuilder(final StatsKey key) {
        super(key);

        StatsConfig config = Stats.getInstance()
                                  .getConfigManager()
                                  .getConfig(key);
        if (config != null) {
            trackerClass = config.getTrackerClass();
            sessionFactory = config.getSessionFactory();
            unit = config.getUnit();
            description = config.getDescription();
        }
    }

    @Override
    public StatsConfigBuilder withAttribute(final String name, final String value) {
        return (StatsConfigBuilder)super.withAttribute(name, value);
    }

    @Override
    public StatsConfigBuilder withAttribute(final String name, final Boolean value) {
        return (StatsConfigBuilder)super.withAttribute(name, value);
    }

    @Override
    public StatsConfigBuilder withAttribute(final String name, final Integer value) {
        return (StatsConfigBuilder)super.withAttribute(name, value);
    }

    @Override
    public StatsConfigBuilder withAttribute(final String name, final Long value) {
        return (StatsConfigBuilder)super.withAttribute(name, value);
    }

    @Override
    public StatsConfigBuilder withSessionFactory(final StatsSessionFactory sessionFactory) {
        if (sessionFactory == null) {
            throw new NullPointerException("sessionFactory");
        }

        this.sessionFactory = sessionFactory;
        return this;
    }

    @Override
    public StatsConfigBuilder withTracker(final Class<? extends StatsTracker> trackerClass) {
        if (trackerClass == null) {
            throw new NullPointerException("trackerClass");
        }

        this.trackerClass = trackerClass;
        return this;
    }

    @Override
    public StatsConfigBuilder withUnit(final String unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }

        this.unit = unit;
        return this;
    }

    @Override
    public StatsConfigBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    @Override
    public StatsKey newKey() {

        StatsKey key = super.newKey();

        StatsConfig config = null;

        if (trackerClass != null || sessionFactory != null || unit != null || description != null) {
            config = DefaultStatsConfig.createDefaultConfig(unit, 
                                                            trackerClass, 
                                                            sessionFactory, 
                                                            description);
        }

        StatsConfigManager configManager = Stats.getInstance().getConfigManager();
        configManager.setConfig(key, config);

        return key;
    }
}
