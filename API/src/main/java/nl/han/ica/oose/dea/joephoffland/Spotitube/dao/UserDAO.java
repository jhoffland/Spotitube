package nl.han.ica.oose.dea.joephoffland.Spotitube.dao;

import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InvalidLoginException;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements IUserDAO {
    @Resource(name = "jdbc/MySQL/Spotitube")
    private DataSource dataSource;

    @Override
    public String login(String user, String password) throws InvalidLoginException, InternalServerErrorException {
        String getTokenSQL = "SELECT token FROM Users WHERE username = ? AND password = ?";
        try {
            Connection connection = dataSource.getConnection();;
            PreparedStatement statement = connection.prepareStatement(getTokenSQL);
            statement.setString(1, user);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();

            while(rs.next()) {
                return rs.getString("token");
            }
            throw new InvalidLoginException();
        } catch(SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
