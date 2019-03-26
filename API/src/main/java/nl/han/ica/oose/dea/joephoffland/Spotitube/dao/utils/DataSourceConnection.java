package nl.han.ica.oose.dea.joephoffland.Spotitube.dao.utils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConnection {
    @Resource(name = "jdbc/MySQL/Spotitube")
    private DataSource dataSource;

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
