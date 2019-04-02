package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao;

import javax.annotation.Resource;
import javax.sql.DataSource;

public class GlobalDAO {
    @Resource(name = "jdbc/MySQL/Spotitube")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
