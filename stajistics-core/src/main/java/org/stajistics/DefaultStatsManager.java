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
package org.stajistics;

import org.stajistics.event.StatsEventManager;
import org.stajistics.event.SynchronousStatsEventManager;
import org.stajistics.management.DefaultStatsManagement;
import org.stajistics.management.StatsManagement;
import org.stajistics.management.StatsManagementEventHandler;
import org.stajistics.session.DefaultStatsSessionManager;
import org.stajistics.session.StatsSessionManager;
import org.stajistics.snapshot.DefaultStatsSnapshotManager;
import org.stajistics.snapshot.StatsSnapshotManager;
import org.stajistics.task.ThreadPoolTaskService;
import org.stajistics.task.TaskService;
import org.stajistics.tracker.DefaultStatsTrackerLocator;
import org.stajistics.tracker.StatsTrackerLocator;

/**
 * The default implementation of {@link StatsManager}. Clients typically do not
 * instantiate this class directly. Instead use {@link Stats#getManager()}.
 *
 * @author The Stajistics Project
 */
public class DefaultStatsManager implements StatsManager {

    private static final String PROP_MANAGEMENT_ENABLED = StatsManagement.class.getName() + ".enabled";

    private volatile boolean enabled = true;

    protected final StatsConfigManager configManager;
    protected final StatsSessionManager sessionManager;
    protected final StatsEventManager eventManager;
    protected final StatsSnapshotManager snapshotManager;
    protected final StatsTrackerLocator trackerLocator;
    protected final StatsKeyFactory keyFactory;
    protected final StatsConfigFactory configFactory;
    protected final TaskService taskService;

    /**
     * Construct a DefaultStatsManager using the given set of managers.
     *
     * @param configManager The {@link StatsConfigManager} to use. Must not be <tt>null</tt>.
     * @param sessionManager The {@link StatsSessionManager} to use. Must not be <tt>null</tt>.
     * @param eventManager The {@link StatsEventManager} to use. Must not be <tt>null</tt>.
     * @param snapshotManager The {@link StatsSnapshotManager} to use. Must not be <tt>null</tt>.
     * @param trackerLocator The {@link StatsTrackerLocator} to use. Must not be <tt>null</tt>.
     * @param keyFactory The {@link StatsKeyFactory} to use. Must not be <tt>null</tt>.
     * @param configFactory The {@link StatsConfigFactory} to use. Must not be <tt>null</tt>.
     * @param taskService The {@link TaskService} to use. Must not be <tt>null</tt>.
     * @throws NullPointerException If any parameter is <tt>null</tt>.
     */
    public DefaultStatsManager(final StatsConfigManager configManager,
                               final StatsSessionManager sessionManager,
                               final StatsEventManager eventManager,
                               final StatsSnapshotManager snapshotManager,
                               final StatsTrackerLocator trackerLocator,
                               final StatsKeyFactory keyFactory,
                               final StatsConfigFactory configFactory,
                               final TaskService taskService) {

        if (configManager == null) {
            throw new NullPointerException("configManager");
        }
        if (sessionManager == null) {
            throw new NullPointerException("sessionManager");
        }
        if (eventManager == null) {
            throw new NullPointerException("eventManager");
        }
        if (snapshotManager == null) {
            throw new NullPointerException("snapshotManager");
        }
        if (trackerLocator == null) {
            throw new NullPointerException("trackerLocator");
        }
        if (keyFactory == null) {
            throw new NullPointerException("keyFactory");
        }
        if (configFactory == null) {
            throw new NullPointerException("configFactory");
        }
        if (taskService == null) {
            throw new NullPointerException("taskService");
        }

        this.keyFactory = keyFactory;
        this.configManager = configManager;
        this.sessionManager = sessionManager;
        this.snapshotManager = snapshotManager;
        this.trackerLocator = trackerLocator;
        this.eventManager = eventManager;
        this.configFactory = configFactory;
        this.taskService = taskService;
    }

    /**
     * Create an instance of DefaultStatsManager supplying the default manager implementations.
     * Initializes a {@link DefaultStatsManagement} and configures it in the default manner.
     *
     * @return A DefaultStatsManager instance, never <tt>null</tt>.
     */
    public static DefaultStatsManager createWithDefaults() {

        DefaultStatsManager manager = new Builder().createManager();

        if (StatsProperties.getBooleanProperty(PROP_MANAGEMENT_ENABLED, true)) {
            StatsManagement management = new DefaultStatsManagement();
            management.registerConfigManagerMBean(manager);
            management.registerSessionManagerMBean(manager);
            management.registerSnapshotMBean(manager);

            StatsManagementEventHandler eventHandler = new StatsManagementEventHandler(manager, management);
            manager.getEventManager()
                   .addGlobalEventHandler(eventHandler);
        }

        return manager;
    }

    @Override
    public StatsConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public StatsSessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public StatsEventManager getEventManager() {
        return eventManager;
    }

    @Override
    public StatsSnapshotManager getSnapshotManager() {
        return snapshotManager;
    }

    @Override
    public StatsTrackerLocator getTrackerLocator() {
        return trackerLocator;
    }

    @Override
    public StatsKeyFactory getKeyFactory() {
        return keyFactory;
    }

    @Override
    public StatsConfigFactory getConfigFactory() {
        return configFactory;
    }

    @Override
    public TaskService getTaskService() {
        return taskService;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        this.trackerLocator.setEnabled(enabled); // ??????
    }

    public void shutdown() {
        setEnabled(false);
        taskService.shutdown();
    }

    /* NESTED CLASSES */

    public static class Builder {

        protected StatsConfigManager configManager = null;
        protected StatsSessionManager sessionManager = null;
        protected StatsEventManager eventManager = null;
        protected StatsSnapshotManager snapshotManager = null;
        protected StatsTrackerLocator trackerLocator = null;
        protected StatsKeyFactory keyFactory = null;
        protected StatsConfigFactory configFactory = null;
        protected TaskService taskService = null;

        public Builder withConfigManager(final StatsConfigManager configManager) {
            if (configManager == null) {
                throw new NullPointerException("configManager");
            }

            this.configManager = configManager;
            return this;
        }

        public Builder withSessionManager(final StatsSessionManager sessionManager) {
            if (sessionManager == null) {
                throw new NullPointerException("sessionManager");
            }

            this.sessionManager = sessionManager;
            return this;
        }

        public Builder withEventManager(final StatsEventManager eventManager) {
            if (eventManager == null) {
                throw new NullPointerException("eventManager");
            }

            this.eventManager = eventManager;
            return this;
        }

        public Builder withSnapshotManager(final StatsSnapshotManager snapshotManager) {
            if (snapshotManager == null) {
                throw new NullPointerException("snapshotManager");
            }

            this.snapshotManager = snapshotManager;
            return this;
        }

        public Builder withTrackerLocator(final StatsTrackerLocator trackerLocator) {
            if (trackerLocator == null) {
                throw new NullPointerException("trackerLocator");
            }

            this.trackerLocator = trackerLocator;
            return this;
        }

        public Builder withKeyFactory(final StatsKeyFactory keyFactory) {
            if (keyFactory == null) {
                throw new NullPointerException("keyFactory");
            }

            this.keyFactory = keyFactory;
            return this;
        }

        public Builder withConfigFactory(final StatsConfigFactory configFactory) {
            if (configFactory == null) {
                throw new NullPointerException("configFactory");
            }

            this.configFactory = configFactory;
            return this;
        }

        public Builder withTaskService(final TaskService taskService) {
            if (taskService == null) {
                throw new NullPointerException("taskService");
            }

            this.taskService = taskService;
            return this;
        }

        public DefaultStatsManager createManager() {

            StatsKeyFactory keyFactory = this.keyFactory;

            StatsEventManager eventManager = this.eventManager;
            StatsConfigManager configManager = this.configManager;
            StatsSessionManager sessionManager = this.sessionManager;
            StatsSnapshotManager snapshotManager = this.snapshotManager;
            StatsTrackerLocator trackerLocator = this.trackerLocator;
            StatsConfigFactory configFactory = this.configFactory;
            TaskService taskService = this.taskService;

            if (keyFactory == null) {
                keyFactory = new DefaultStatsKeyFactory();
            }

            if (eventManager == null) {
                eventManager = new SynchronousStatsEventManager();
            }

            if (configManager == null) {
                configManager = new DefaultStatsConfigManager(eventManager, keyFactory);
            }

            if (sessionManager == null) {
                sessionManager = new DefaultStatsSessionManager(configManager, eventManager);
            }

            if (snapshotManager == null) {
                snapshotManager = DefaultStatsSnapshotManager.createWithDefaults();
            }

            if (trackerLocator == null) {
                trackerLocator = new DefaultStatsTrackerLocator(configManager, sessionManager);
            }

            if (configFactory == null) {
                configFactory = new DefaultStatsConfigFactory(configManager);
            }

            if (taskService == null) {
                taskService = new ThreadPoolTaskService();
            }

            DefaultStatsManager manager = new DefaultStatsManager(configManager,
                                                                  sessionManager,
                                                                  eventManager,
                                                                  snapshotManager,
                                                                  trackerLocator,
                                                                  keyFactory,
                                                                  configFactory,
                                                                  taskService);

            return manager;
        }
    }
}
