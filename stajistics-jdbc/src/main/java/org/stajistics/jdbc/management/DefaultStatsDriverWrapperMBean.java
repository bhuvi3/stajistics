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
package org.stajistics.jdbc.management;

import org.stajistics.jdbc.StatsDriverWrapper;


/**
 * 
 * @author The Stajistics Project
 *
 */
public class DefaultStatsDriverWrapperMBean implements StatsDriverWrapperMBean {

    private final StatsDriverWrapper driverWrapper;

    public DefaultStatsDriverWrapperMBean(final StatsDriverWrapper driverWrapper) {
        if (driverWrapper == null) {
            throw new NullPointerException("driverWrapper");
        }

        this.driverWrapper = driverWrapper;
    }

    @Override
    public boolean getEnabled() {
        return driverWrapper.isEnabled();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        driverWrapper.setEnabled(enabled);
    }
}