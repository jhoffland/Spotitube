package nl.han.ica.oose.dea.joephoffland.Spotitube.rest;

import nl.han.ica.oose.dea.joephoffland.Spotitube.dao.IUserDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InvalidLoginException;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.ErrorDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.TokenDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.UserDTO;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginApi {
    @Inject
    private IUserDAO userDAO;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(UserDTO userDTO) {
        try {
            String token = userDAO.login(userDTO.user, userDTO.password);

            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.token = token;
            tokenDTO.user = userDTO.user;

            return Response.status(200).entity(tokenDTO).build();
        } catch(InvalidLoginException e) {
            ErrorDTO errorMessage = new ErrorDTO();
            errorMessage.status = 401;
            errorMessage.message = "De combinatie gebruikersnaam en wachtwoord klopt niet.";
            return Response.status(errorMessage.status).entity(errorMessage).build();
        }
    }
}
