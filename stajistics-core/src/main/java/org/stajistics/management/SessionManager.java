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
package org.stajistics.management;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stajistics.Stats;
import org.stajistics.session.StatsSession;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public class SessionManager implements SessionManagerMBean {

    protected static final String SESSION_DUMP_LOGGER_NAME = "stajistics.session.dump";

    @Override
    public int getSessionCount() throws IOException {
        return Stats.getSessionManager().getSessions().size();
    }

    @Override
    public void dumpAllSessions() throws IOException {
        Logger logger = LoggerFactory.getLogger(SESSION_DUMP_LOGGER_NAME);
        if (logger.isInfoEnabled()) {
            for (StatsSession session : org.stajistics.Stats.getSessionManager().getSessions()) {
                logger.info(session.toString());
            }
        }
    }

    @Override
    public void clearAllSessions() throws IOException {
        Stats.getSessionManager().clearAllSessions();
    }

    @Override
    public void destroyAllSessions() throws IOException {
        Stats.getSessionManager().clear();
    }

}
