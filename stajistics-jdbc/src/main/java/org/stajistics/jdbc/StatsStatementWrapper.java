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
package org.stajistics.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.stajistics.Stats;
import org.stajistics.StatsKey;
import org.stajistics.jdbc.decorator.AbstractStatementDecorator;
import org.stajistics.tracker.StatsTracker;

/**
 * 
 * @author The Stajistics Project
 *
 */
public class StatsStatementWrapper extends AbstractStatementDecorator {

    private final Connection connection;

    private final StatsTracker openClosedTracker;

    public StatsStatementWrapper(final Statement delegate,
                                 final Connection connection) {
        this(delegate, connection, JDBCStatsKeyConstants.STATEMENT
                                                        .buildCopy()
                                                        .withNameSuffix("open")
                                                        .newKey());
    }

    public StatsStatementWrapper(final Statement delegate,
                                 final Connection connection,
                                 final StatsKey openClosedKey) {
        super(delegate);

        if (connection == null) {
            throw new NullPointerException("connection");
        }
        if (openClosedKey == null) {
            throw new NullPointerException("openClosedKey");
        }

        this.connection = connection;
        openClosedTracker = Stats.track(openClosedKey);
    }

    private void handleSQL(final String sql) {
        
    }

    @Override
    public void close() throws SQLException {
        try {
            super.close();
        } finally {
            openClosedTracker.commit();
        }
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        handleSQL(sql);
        return delegate().execute(sql);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys)
            throws SQLException {
        handleSQL(sql);
        return delegate().execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        handleSQL(sql);
        return delegate().execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames)
            throws SQLException {
        handleSQL(sql);
        return execute(sql, columnNames);
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        handleSQL(sql);
        return new StatsResultSetWrapper(delegate().executeQuery(sql));
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return new StatsResultSetWrapper(delegate().getGeneratedKeys());
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return new StatsResultSetWrapper(delegate().getResultSet());
    }
    
    @Override
    public int executeUpdate(String sql) throws SQLException {
        handleSQL(sql);
        return delegate().executeUpdate(sql);
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys)
            throws SQLException {
        handleSQL(sql);
        return delegate().executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes)
            throws SQLException {
        handleSQL(sql);
        return delegate().executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames)
            throws SQLException {
        handleSQL(sql);
        return delegate().executeUpdate(sql, columnNames);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }
    
}
