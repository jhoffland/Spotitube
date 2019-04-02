package nl.han.ica.oose.dea.joephoffland.Spotitube.rest.utils;

import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.ErrorDTO;

import javax.ws.rs.core.Response;

public class ErrorResponse {
    public static Response get(int status, String message) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.status = status;
        errorDTO.message = message;

        return Response.status(status).entity(errorDTO).build();
    }
}
