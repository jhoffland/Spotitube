package nl.han.ica.oose.dea.joephoffland.Spotitube.rest.endpoints;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlist.IPlaylistDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.user.IUserDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.domain.Playlist;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.PlaylistDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.PlaylistsDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.utils.ErrorResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/playlists")
public class PlaylistsAPI {
    @Inject
    private IPlaylistDAO playlistDAO;

    @Inject
    private IUserDAO userDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists(@QueryParam("token") String token) {
        if(token == null)
            return ErrorResponse.get(400, "Het token mist.");

        try {
            int userId = userDAO.getUserIdFromToken(token);
            if(userId < 0)
                return ErrorResponse.get(401, "Het token is niet geldig.");

            return Response.status(200).entity(playlistsDTO(userId)).build();
        } catch(InternalServerErrorException e) {
            return ErrorResponse.get(500, "Er is iets misgegaan op de server, probeer het opnieuw.");
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPlaylist(PlaylistDTO playlist, @QueryParam("token") String token) {
        if(token == null) {
            return ErrorResponse.get(400, "Het token mist.");
        } else if(playlist.name == null || playlist.name.length() > 50) {
            return ErrorResponse.get(
                    400,
                    "Geef een naam voor de nieuwe playlist van maximaal 50 tekens op."
            );
        }

        try {
            int userId = userDAO.getUserIdFromToken(token);
            if(userId < 0)
                return ErrorResponse.get(401, "Het token is niet geldig.");

            playlistDAO.addPlaylist(playlist.name, userId);
            return Response.status(201).entity(playlistsDTO(userId)).build();
        } catch(InternalServerErrorException e) {
            return ErrorResponse.get(500, "Er is iets misgegaan op de server, probeer het opnieuw.");
        }
    }

    @DELETE
    @Path("/{plId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("plId") String plId, @QueryParam("token") String token) {
        if(token == null)
            return ErrorResponse.get(400, "Het token mist.");

        try {
            int userId = userDAO.getUserIdFromToken(token);
            if(userId < 0)
                return ErrorResponse.get(401, "Het token is niet geldig.");

            int playlistId = Integer.parseInt(plId);

            int isOwnerAndExists = playlistDAO.checkOwnershipAndExistens(playlistId, userId);
            if(isOwnerAndExists == 0) {
                return ErrorResponse.get(
                        403,
                        "Je hebt geen toestemming om de afspeellijst te verwijderen."
                );
            } else if(isOwnerAndExists == -1) {
                return ErrorResponse.get(404, "De afspeellijst bestaat niet.");
            }

            playlistDAO.deletePlaylist(playlistId);

            return Response.status(200).entity(playlistsDTO(userId)).build();
        } catch(NumberFormatException e) {
            return ErrorResponse.get(400, "Geef een geldig playlist ID op.");
        } catch(InternalServerErrorException e) {
            return ErrorResponse.get(500, "Er is iets misgegaan op de server, probeer het opnieuw.");
        }
    }

    @PUT
    @Path("/{plId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response replacePlaylist(
            PlaylistDTO playlist,
            @PathParam("plId") String plId,
            @QueryParam("token") String token
    ) {
        if(token == null) {
            return ErrorResponse.get(400, "Het token mist.");
        } else if(playlist.name == null || playlist.name.length() > 50) {
            return ErrorResponse.get(400, "Geef een naam voor de playlist van maximaal 50 tekens op.");
        }

        try {
            int userId = userDAO.getUserIdFromToken(token);
            if(userId < 0)
                return ErrorResponse.get(401, "Het token is niet geldig.");

            int playlistId = Integer.parseInt(plId);
            if(playlistId != playlist.id)
                return ErrorResponse.get(
                        400,
                        "Het playlist ID uit de URI komt niet overeen met het ID in de requestbody."
                );

            int isOwnerAndExists = playlistDAO.checkOwnershipAndExistens(playlistId, userId);
            if(isOwnerAndExists == 0) {
                return ErrorResponse.get(
                        403,
                        "Je hebt geen toestemming om de afspeellijst te bewerken."
                );
            } else if(isOwnerAndExists == -1) {
                return ErrorResponse.get(404, "De afspeellijst bestaat niet.");
            }

            playlistDAO.editPlaylist(playlist.name, playlistId);

            return Response.status(200).entity(playlistsDTO(userId)).build();
        } catch(NumberFormatException e) {
            return ErrorResponse.get(400, "Geef een geldig playlist ID op.");
        } catch(InternalServerErrorException e) {
            return ErrorResponse.get(500, "Er is iets misgegaan op de server, probeer het opnieuw.");
        }
    }

    public PlaylistsDTO playlistsDTO(int userId) throws InternalServerErrorException {
        List<Playlist> playlists = playlistDAO.getPlaylists(userId);

        PlaylistsDTO playlistsDTO = new PlaylistsDTO();
        playlistsDTO.playlists = new ArrayList<>();
        int totalDuration = 0;
        for(Playlist curPL : playlists) {
            PlaylistDTO playlistDTO = new PlaylistDTO();
            playlistDTO.id = curPL.getId();
            playlistDTO.name = curPL.getName();
            playlistDTO.owner = curPL.isOwner();

            playlistsDTO.playlists.add(playlistDTO);

            totalDuration += curPL.getTotalDuration();
        }
        playlistsDTO.length = totalDuration;

        return playlistsDTO;
    }

    public void setPlaylistDAO(IPlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
