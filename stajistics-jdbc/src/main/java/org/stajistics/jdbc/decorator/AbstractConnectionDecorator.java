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
package org.stajistics.jdbc.decorator;

import java.sql.*;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author The Stajistics Project
 *
 */
public abstract class AbstractConnectionDecorator extends AbstractWrapper implements Connection {

    private final Connection delegate;

    public AbstractConnectionDecorator(final Connection delegate) {
        if (delegate == null) {
            throw new NullPointerException("delegate");
        }

        this.delegate = delegate;
    }

    @Override
    protected final Connection delegate() {
        return delegate;
    }

    /**
     * @throws SQLException
     * @see java.sql.Connection#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        delegate().clearWarnings();
    }

    /**
     * @throws SQLException
     * @see java.sql.Connection#close()
     */
    public void close() throws SQLException {
        delegate().close();
    }

    /**
     * @throws SQLException
     * @see java.sql.Connection#commit()
     */
    public void commit() throws SQLException {
        delegate().commit();
    }

    /**
     * @param typeName
     * @param elements
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createArrayOf(java.lang.String, java.lang.Object[])
     */
    public Array createArrayOf(String typeName, Object[] elements)
            throws SQLException {
        return delegate().createArrayOf(typeName, elements);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createBlob()
     */
    public Blob createBlob() throws SQLException {
        return delegate().createBlob();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createClob()
     */
    public Clob createClob() throws SQLException {
        return delegate().createClob();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createNClob()
     */
    public NClob createNClob() throws SQLException {
        return delegate().createNClob();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createSQLXML()
     */
    public SQLXML createSQLXML() throws SQLException {
        return delegate().createSQLXML();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createStatement()
     */
    public Statement createStatement() throws SQLException {
        return delegate().createStatement();
    }

    /**
     * @param resultSetType
     * @param resultSetConcurrency
     * @param resultSetHoldability
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createStatement(int, int, int)
     */
    public Statement createStatement(int resultSetType,
                                     int resultSetConcurrency,
                                     int resultSetHoldability)
            throws SQLException {
        return delegate().createStatement(resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    /**
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createStatement(int, int)
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return delegate().createStatement(resultSetType, resultSetConcurrency);
    }

    /**
     * @param typeName
     * @param attributes
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
     */
    public Struct createStruct(String typeName, Object[] attributes)
            throws SQLException {
        return delegate().createStruct(typeName, attributes);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getAutoCommit()
     */
    public boolean getAutoCommit() throws SQLException {
        return delegate().getAutoCommit();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getCatalog()
     */
    public String getCatalog() throws SQLException {
        return delegate().getCatalog();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getClientInfo()
     */
    public Properties getClientInfo() throws SQLException {
        return delegate().getClientInfo();
    }

    /**
     * @param name
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getClientInfo(java.lang.String)
     */
    public String getClientInfo(String name) throws SQLException {
        return delegate().getClientInfo(name);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getHoldability()
     */
    public int getHoldability() throws SQLException {
        return delegate().getHoldability();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getMetaData()
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        return delegate().getMetaData();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getTransactionIsolation()
     */
    public int getTransactionIsolation() throws SQLException {
        return delegate().getTransactionIsolation();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getTypeMap()
     */
    public Map<String,Class<?>> getTypeMap() throws SQLException {
        return delegate().getTypeMap();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        return delegate().getWarnings();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#isClosed()
     */
    public boolean isClosed() throws SQLException {
        return delegate().isClosed();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#isReadOnly()
     */
    public boolean isReadOnly() throws SQLException {
        return delegate().isReadOnly();
    }

    /**
     * @param timeout
     * @return
     * @throws SQLException
     * @see java.sql.Connection#isValid(int)
     */
    public boolean isValid(int timeout) throws SQLException {
        return delegate().isValid(timeout);
    }

    /**
     * @param sql
     * @return
     * @throws SQLException
     * @see java.sql.Connection#nativeSQL(java.lang.String)
     */
    public String nativeSQL(String sql) throws SQLException {
        return delegate().nativeSQL(sql);
    }

    /**
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @param resultSetHoldability
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
     */
    public CallableStatement prepareCall(String sql,
                                         int resultSetType,
                                         int resultSetConcurrency,
                                         int resultSetHoldability)
            throws SQLException {
        return delegate().prepareCall(sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    /**
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
     */
    public CallableStatement prepareCall(String sql,
                                         int resultSetType,
                                         int resultSetConcurrency)
            throws SQLException {
        return delegate().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * @param sql
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareCall(java.lang.String)
     */
    public CallableStatement prepareCall(String sql) throws SQLException {
        return delegate().prepareCall(sql);
    }

    /**
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @param resultSetHoldability
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
     */
    public PreparedStatement prepareStatement(String sql,
                                              int resultSetType,
                                              int resultSetConcurrency,
                                              int resultSetHoldability)
            throws SQLException {
        return delegate().prepareStatement(sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    /**
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
     */
    public PreparedStatement prepareStatement(String sql,
                                              int resultSetType,
                                              int resultSetConcurrency)
            throws SQLException {
        return delegate().prepareStatement(sql, resultSetType,
                resultSetConcurrency);
    }

    /**
     * @param sql
     * @param autoGeneratedKeys
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String, int)
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        return delegate().prepareStatement(sql, autoGeneratedKeys);
    }

    /**
     * @param sql
     * @param columnIndexes
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
     */
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException {
        return delegate().prepareStatement(sql, columnIndexes);
    }

    /**
     * @param sql
     * @param columnNames
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
     */
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        return delegate().prepareStatement(sql, columnNames);
    }

    /**
     * @param sql
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String)
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return delegate().prepareStatement(sql);
    }

    /**
     * @param savepoint
     * @throws SQLException
     * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
     */
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        delegate().releaseSavepoint(savepoint);
    }

    /**
     * @throws SQLException
     * @see java.sql.Connection#rollback()
     */
    public void rollback() throws SQLException {
        delegate().rollback();
    }

    /**
     * @param savepoint
     * @throws SQLException
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     */
    public void rollback(Savepoint savepoint) throws SQLException {
        delegate().rollback(savepoint);
    }

    /**
     * @param autoCommit
     * @throws SQLException
     * @see java.sql.Connection#setAutoCommit(boolean)
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        delegate().setAutoCommit(autoCommit);
    }

    /**
     * @param catalog
     * @throws SQLException
     * @see java.sql.Connection#setCatalog(java.lang.String)
     */
    public void setCatalog(String catalog) throws SQLException {
        delegate().setCatalog(catalog);
    }

    /**
     * @param properties
     * @throws SQLClientInfoException
     * @see java.sql.Connection#setClientInfo(java.util.Properties)
     */
    public void setClientInfo(Properties properties)
            throws SQLClientInfoException {
        delegate().setClientInfo(properties);
    }

    /**
     * @param name
     * @param value
     * @throws SQLClientInfoException
     * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
     */
    public void setClientInfo(String name, String value)
            throws SQLClientInfoException {
        delegate().setClientInfo(name, value);
    }

    /**
     * @param holdability
     * @throws SQLException
     * @see java.sql.Connection#setHoldability(int)
     */
    public void setHoldability(int holdability) throws SQLException {
        delegate().setHoldability(holdability);
    }

    /**
     * @param readOnly
     * @throws SQLException
     * @see java.sql.Connection#setReadOnly(boolean)
     */
    public void setReadOnly(boolean readOnly) throws SQLException {
        delegate().setReadOnly(readOnly);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#setSavepoint()
     */
    public Savepoint setSavepoint() throws SQLException {
        return delegate().setSavepoint();
    }

    /**
     * @param name
     * @return
     * @throws SQLException
     * @see java.sql.Connection#setSavepoint(java.lang.String)
     */
    public Savepoint setSavepoint(String name) throws SQLException {
        return delegate().setSavepoint(name);
    }

    /**
     * @param level
     * @throws SQLException
     * @see java.sql.Connection#setTransactionIsolation(int)
     */
    public void setTransactionIsolation(int level) throws SQLException {
        delegate().setTransactionIsolation(level);
    }

    /**
     * @param map
     * @throws SQLException
     * @see java.sql.Connection#setTypeMap(java.util.Map)
     */
    public void setTypeMap(Map<String,Class<?>> map) throws SQLException {
        delegate().setTypeMap(map);
    }

}
