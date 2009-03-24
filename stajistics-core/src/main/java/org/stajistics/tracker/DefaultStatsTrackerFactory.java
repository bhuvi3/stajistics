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
package org.stajistics.tracker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stajistics.session.StatsSession;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public class DefaultStatsTrackerFactory implements StatsTrackerFactory {

    private static Logger logger = LoggerFactory.getLogger(DefaultStatsTrackerFactory.class);

    @Override
    @SuppressWarnings("unchecked")
    public <T extends StatsTracker> T createStatsTracker(final StatsSession session,
                                                         final Class<T> trackerClass) {
        if (session == null) {
            throw new NullPointerException("session");
        }

        if (trackerClass == null) {
            throw new NullPointerException("trackerClass");
        }

        if (trackerClass == TimeDurationTracker.class) {
            return (T)new TimeDurationTracker(session);
        }

        if (trackerClass == ConcurrentAccessTracker.class) {
            return (T)new ConcurrentAccessTracker(session);
        }

        if (trackerClass == HitFrequencyTracker.class) {
            return (T)new HitFrequencyTracker(session);
        }

        if (trackerClass == ManualTracker.class) {
            return (T)new ManualTracker(session);
        }

        if (trackerClass == ThreadBlockTimeTracker.class) {
            return (T)new ThreadBlockTimeTracker(session);
        }

        if (trackerClass == ThreadCPUTimeTracker.class) {
            return (T)new ThreadCPUTimeTracker(session);
        }

        if (trackerClass == ThreadWaitTimeTracker.class) {
            return (T)new ThreadWaitTimeTracker(session);
        }

        if (trackerClass == GarbageCollectionTimeTracker.class) {
            return (T)new GarbageCollectionTimeTracker(session);
        }

        /* TODO: Creating trackers reflectively for client-defined types 
         * is unacceptable for performance. Figure something out.
         */
        return createReflectively(session, trackerClass);
    }

    @SuppressWarnings("unchecked")
    private <T extends StatsTracker> T createReflectively(final StatsSession session,
                                                          final Class<T> trackerClass) {

        if (logger.isWarnEnabled()) {
            logger.warn("Creating tracker reflectively, time for a new factory: " + trackerClass.getName());
        }

        try {
            Constructor<StatsTracker> ctor = (Constructor<StatsTracker>)trackerClass.getConstructor(new Class[] { StatsSession.class });

            return (T)ctor.newInstance(new Object[] { session });

        } catch (SecurityException e) {
            throw new RuntimeException(e);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(trackerClass.getSimpleName() + 
                                       " must define a public constructor accepting a single " + 
                                       StatsSession.class.getSimpleName() + 
                                       " parameter", e);

        } catch (InstantiationException e) {
            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(trackerClass.getSimpleName() + 
                                       " must define a public constructor accepting a single " + 
                                       StatsSession.class.getSimpleName() + 
                                       " parameter", e);

        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            }

            throw new RuntimeException(cause);
        }
    }
}
