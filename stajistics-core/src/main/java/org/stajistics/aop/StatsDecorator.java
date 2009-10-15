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
package org.stajistics.aop;

import java.util.concurrent.Callable;

import org.stajistics.Stats;
import org.stajistics.StatsKey;
import org.stajistics.tracker.StatsTracker;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public class StatsDecorator {

    private static void rethrow(final Throwable t) {
        if (t instanceof RuntimeException) {
            throw (RuntimeException)t;
        }
        if (t instanceof Error) {
            throw (Error)t;
        }

        throw new RuntimeException(t);
    }

    public static Runnable wrap(final Runnable r,
                                final StatsKey key) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    StatsTracker tracker = Stats.track(key);
                    try {
                        r.run();
                    } finally {
                        tracker.commit();
                    }

                } catch (Throwable t) {
                    Stats.failure(key, t);
                    rethrow(t);
                }
            }
        };
    }

    public static <T> Callable<T> wrap(final Callable<T> c,
                                       final StatsKey key) {
        return new Callable<T>() {
            @Override
            public T call() throws Exception {
                try {
                    StatsTracker tracker = Stats.track(key);
                    try {
                        return c.call();
                    } finally {
                        tracker.commit();
                    }

                } catch (Throwable t) {
                    Stats.failure(key, t);
                    rethrow(t);
                    return null; // Can't get here, silly compiler
                }
            }
        };
    }

}
