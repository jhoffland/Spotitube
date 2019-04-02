package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.user;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.DAOTestSetup;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class UserDAOTest extends DAOTestSetup {
    UserDAO userDAO;

    @Before
    public void userSetUp()  {
        userDAO = new UserDAO();
        userDAO.setDataSource(dataSource);
    }

    @Test
    public void login() {
        String verifyLoginExpectedSQL = "SELECT id FROM Users WHERE username = ? AND password = ?";
        String newTokenExpectedSQL = "CALL newToken(?)";

        try {
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(verifyLoginExpectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            String user = "testGebruiker";
            String password = "testPass";
            int userId = 2;
            String mockedToken = "1234-1234-1234";

            String token = userDAO.login(user, password);

            assertEquals(null, token);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(verifyLoginExpectedSQL);
            verify(preparedStatement).setString(1, user);
            verify(preparedStatement).setString(2, password);
            verify(preparedStatement).executeQuery();
            verify(connection, never()).prepareStatement(newTokenExpectedSQL);

            PreparedStatement newTokenPrepared = mock(PreparedStatement.class);
            ResultSet newTokenRS = mock(ResultSet.class);
            when(resultSet.next()).thenReturn(true);
            when(connection.prepareStatement(newTokenExpectedSQL)).thenReturn(newTokenPrepared);
            when(newTokenPrepared.executeQuery()).thenReturn(newTokenRS);
            when(resultSet.getInt("id")).thenReturn(userId);
            when(newTokenRS.next()).thenReturn(false);

            token = userDAO.login(user, password);

            assertEquals(null, token);

            verify(connection).prepareStatement(newTokenExpectedSQL);
            verify(newTokenPrepared).setInt(1, userId);

            when(newTokenRS.next()).thenReturn(true);
            when(newTokenRS.getString("token")).thenReturn(mockedToken);

            token = userDAO.login(user, password);

            assertEquals(mockedToken, token);

            when(dataSource.getConnection()).thenThrow(new SQLException());
            try {
                userDAO.login(user, password);
                fail();
            } catch(InternalServerErrorException e) { }
        } catch(InternalServerErrorException e) {
            fail();
        } catch(SQLException e){
            fail();
        }
    }

    @Test
    public void getUserIdFromToken() {
        String expectedSQL = "SELECT user FROM Tokens WHERE token = ?";
        try {
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            String token = "1234-1234-1234";
            int expectedUserId = 12;

            int userId = userDAO.getUserIdFromToken(token);

            assertEquals(-1, userId);
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, token);
            verify(preparedStatement).executeQuery();

            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("user")).thenReturn(expectedUserId);
            userId = userDAO.getUserIdFromToken(token);
            assertEquals(expectedUserId, userId);

            when(dataSource.getConnection()).thenThrow(new SQLException());
            try {
                userDAO.getUserIdFromToken(token);
                fail();
            } catch(InternalServerErrorException e) { }
        } catch(InternalServerErrorException e) {
            fail();
        } catch(SQLException e){
            fail();
        }
    }
}