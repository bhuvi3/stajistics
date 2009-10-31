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
package org.stajistics.tracker.span;

import org.stajistics.tracker.StatsTracker;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public interface SpanTracker extends StatsTracker {

    /**
     * Begin tracking statistics related to some span. The span being
     * measured will be different depending on the context and the tracker implementation,
     * but is usually related to time. The measurement of the span should eventually
     * be stopped by a call to {@link #commit()}. After this call {@link #isTracking()} will
     * return <tt>true</tt>. This method does nothing except log a warning if
     * called when this tracker is already tracking.
     *
     * @return <tt>this</tt>.
     */
    SpanTracker start();

    /**
     * Finish tracking statistics related to some span that was initiated by a 
     * call to {@link #track()}. After this call {@link #isTracking()} will return <tt>false</tt>.
     * This method does nothing except log a warning if called when this tracker is
     * not already tracking.
     *
     * @return <tt>this</tt>.
     */
    SpanTracker stop();

    /**
     * Determine if this tracker is actively collecting statistics from a prior call to
     * {@link #track()}. If <tt>true</tt> is returned, this tracker is awaiting a call to
     * {@link #commit()} to finish collecting statistics.
     *
     * @return <tt>true</tt> if statistics are being collected, <tt>false</tt> otherwise.
     */
    boolean isTracking();
}