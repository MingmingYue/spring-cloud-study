package com.ming.tcc.transaction.spring.repository;

import com.ming.tcc.transaction.repository.JdbcTransactionRepository;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.Connection;

/**
 * SpringJdbc事务库
 * Create on 2019-06-08.
 */
public class SpringJdbcTransactionRepository extends JdbcTransactionRepository {

    @Override
    protected Connection getConnection() {
        return DataSourceUtils.getConnection(this.getDataSource());
    }

    @Override
    protected void releaseConnection(Connection con) {
        DataSourceUtils.releaseConnection(con, this.getDataSource());
    }
}
