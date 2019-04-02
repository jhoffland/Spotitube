package nl.han.ica.oose.dea.joephoffland.Spotitube.rest.endpoints;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.user.IUserDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.ErrorDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.TokenDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.UserDTO;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginAPITest {
    LoginAPI loginAPI;
    UserDTO userDTO;
    IUserDAO userDAO;

    @Before
    public void setUp() {
        loginAPI = new LoginAPI();
        userDAO = mock(IUserDAO.class);

        userDTO = new UserDTO();
        userDTO.user = "testU";
        userDTO.password = "testP";
    }

    @Test
    public void login() {
        try {
            String token = "1234-1234-1234";
            loginAPI.setUserDAO(userDAO);
            when(userDAO.login(userDTO.user, userDTO.password)).thenReturn(token);

            Response response = loginAPI.login(userDTO);
            TokenDTO tokenDTO = (TokenDTO) response.getEntity();

            assertEquals(token, tokenDTO.token);
            assertEquals(userDTO.user, tokenDTO.user);
            assertEquals(201, response.getStatus());

            when(userDAO.login(userDTO.user, userDTO.password)).thenReturn(null);
            response = loginAPI.login(userDTO);
            assertEquals(401, response.getStatus());

            when(userDAO.login(userDTO.user, userDTO.password)).thenThrow(new InternalServerErrorException());
            response = loginAPI.login(userDTO);
            assertEquals(500, response.getStatus());
        } catch(InternalServerErrorException e) {
            fail();
        }

    }
}