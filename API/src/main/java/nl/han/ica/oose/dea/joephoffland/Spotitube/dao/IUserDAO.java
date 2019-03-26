package nl.han.ica.oose.dea.joephoffland.Spotitube.dao;

import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InvalidLoginException;

public interface IUserDAO {
    public String login(String user, String password) throws InvalidLoginException, InternalServerErrorException;
}
