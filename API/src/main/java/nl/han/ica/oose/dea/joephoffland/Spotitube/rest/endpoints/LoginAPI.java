package nl.han.ica.oose.dea.joephoffland.Spotitube.rest.endpoints;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.user.IUserDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.TokenDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.UserDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.utils.ErrorResponse;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginAPI {
    @Inject
    private IUserDAO userDAO;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(UserDTO userDTO) {
        try {
            String token = userDAO.login(userDTO.user, userDTO.password);

            if (token != null) {
                TokenDTO tokenDTO = new TokenDTO();
                tokenDTO.token = token;
                tokenDTO.user = userDTO.user;

                return Response.status(201).entity(tokenDTO).build();
            }

            return ErrorResponse.get(401, "De combinatie gebruikersnaam en wachtwoord klopt niet.");
        } catch(InternalServerErrorException e) {
            return ErrorResponse.get(500, "Er is iets misgegaan op de server, probeer het opnieuw.");
        }
    }
}
