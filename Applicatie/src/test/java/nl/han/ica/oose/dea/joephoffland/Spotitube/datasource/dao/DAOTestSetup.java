package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao;

import org.junit.Before;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;

public class DAOTestSetup {
    protected DataSource dataSource;
    protected Connection connection;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;
    protected SQLException sqlException;

    @Before
    public void setUp() {
        dataSource = mock(DataSource.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        sqlException = mock(SQLException.class);
    }
}
