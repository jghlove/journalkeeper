/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.journalkeeper.sql.state.jdbc;

import io.journalkeeper.sql.client.domain.ResultSet;
import io.journalkeeper.sql.exception.SQLException;
import io.journalkeeper.sql.state.SQLExecutor;
import io.journalkeeper.sql.state.SQLTransactionExecutor;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * JDBCSQLExecutor
 * author: gaohaoxiang
 * date: 2019/8/1
 */
public class JDBCSQLExecutor implements SQLExecutor {

    private DataSourceFactory dataSourceFactory;
    private JDBCExecutor executor;

    private DataSource dataSource;

    public JDBCSQLExecutor(Path path, Properties properties, DataSourceFactory dataSourceFactory,
                           JDBCExecutor executor) {
        this.dataSourceFactory = dataSourceFactory;
        this.executor = executor;
        this.dataSource = dataSourceFactory.createDataSource(path, properties);
    }

    @Override
    public String insert(String sql, List<Object> params) {
        Connection connection = getConnection();
        try {
            return executor.insert(connection, sql, params);
        } finally {
            releaseConnection(connection);
        }
    }

    @Override
    public int update(String sql, List<Object> params) {
        Connection connection = getConnection();
        try {
            return executor.update(connection, sql, params);
        } finally {
            releaseConnection(connection);
        }
    }

    @Override
    public int delete(String sql, List<Object> params) {
        Connection connection = getConnection();
        try {
            return executor.delete(connection, sql, params);
        } finally {
            releaseConnection(connection);
        }
    }

    @Override
    public ResultSet query(String sql, List<Object> params) {
        Connection connection = getConnection();
        try {
            return executor.query(connection, sql, params);
        } finally {
            releaseConnection(connection);
        }
    }

    @Override
    public SQLTransactionExecutor beginTransaction() {
        Connection connection = getTransactionConnection();
        JDBCSQLTransactionExecutor transactionExecutor = new JDBCSQLTransactionExecutor(connection, executor);
        return transactionExecutor;
    }

    @Override
    public void close() {
        if (dataSource instanceof Closeable) {
            try {
                ((Closeable) dataSource).close();
            } catch (IOException e) {
                throw new SQLException(e);
            }
        }
    }

    protected Connection getTransactionConnection() {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            return connection;
        } catch (java.sql.SQLException e) {
            throw new SQLException(e);
        }
    }

    protected Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (java.sql.SQLException e) {
            throw new SQLException(e);
        }
    }

    protected void releaseConnection(Connection connection) {
        try {
            connection.close();
        } catch (java.sql.SQLException e) {
            throw new SQLException();
        }
    }
}