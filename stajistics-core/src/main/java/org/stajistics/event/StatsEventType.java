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
package org.stajistics.event;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public enum StatsEventType {

    CONFIG_CREATED,
    CONFIG_CHANGED,
    CONFIG_DESTROYED,

    SESSION_CREATED,
    SESSION_CLEARED,
    SESSION_DESTROYED,

    TRACKER_OPENED,
    TRACKER_COMMITTED

}
