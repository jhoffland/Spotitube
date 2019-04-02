package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.user;

import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;

public interface IUserDAO {
    public String login(String user, String password) throws InternalServerErrorException;
    public int getUserIdFromToken(String token) throws InternalServerErrorException;
}
