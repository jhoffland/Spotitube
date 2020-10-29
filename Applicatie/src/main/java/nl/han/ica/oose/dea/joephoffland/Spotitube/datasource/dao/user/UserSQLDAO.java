package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.user;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.GlobalSQLDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserSQLDAO extends GlobalSQLDAO implements IUserDAO {
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public String login(String user, String password) throws InternalServerErrorException {
        String getUSerIdSQL = "SELECT id FROM Users WHERE username = ? AND password = ?";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement getUserIdStatement = connection.prepareStatement(getUSerIdSQL);
            getUserIdStatement.setString(1, user);
            getUserIdStatement.setString(2, password);
            ResultSet rsUserId = getUserIdStatement.executeQuery();
            if(rsUserId.next()) {
                PreparedStatement newTokenStatement = connection.prepareStatement("CALL newToken(?)");
                newTokenStatement.setInt(1, rsUserId.getInt("id"));
                ResultSet rsToken = newTokenStatement.executeQuery();

                if(rsToken.next()) {
                    return rsToken.getString("token");
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public int getUserIdFromToken(String token) throws InternalServerErrorException {
        String getUserIdSQL = "SELECT user FROM Tokens WHERE token = ?";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement getUserIdStatement = connection.prepareStatement(getUserIdSQL);
            getUserIdStatement.setString(1, token);
            ResultSet rsUserId = getUserIdStatement.executeQuery();
            if(rsUserId.next()) {
                return rsUserId.getInt("user");
            }
            return -1;
        } catch(SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.toString(), e);
            throw new InternalServerErrorException();
        }
    }
}
