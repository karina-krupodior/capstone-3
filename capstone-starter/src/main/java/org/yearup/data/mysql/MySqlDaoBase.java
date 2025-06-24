package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class MySqlDaoBase
{
    protected final JdbcTemplate jdbcTemplate;

    public MySqlDaoBase(DataSource dataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected Connection getConnection() throws SQLException
    {
        return jdbcTemplate.getDataSource().getConnection();
    }
}
