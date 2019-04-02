package nl.han.ica.oose.dea.joephoffland.Spotitube.rest.endpoints;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlist.IPlaylistDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlistTrack.IPlaylistTrackDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.user.IUserDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.TrackDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.TracksDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.utils.ErrorResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static nl.han.ica.oose.dea.joephoffland.Spotitube.rest.endpoints.TracksAPI.tracksDTO;

@Path("/playlists/{plId}/tracks")
public class PlaylistsTracksAPI {
    @Inject
    private IUserDAO userDAO;

    @Inject
    private IPlaylistTrackDAO trackDAO;

    @Inject
    private IPlaylistDAO playlistDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracksInPlaylist(@PathParam("plId") String plId, @QueryParam("token") String token) {
        if(token == null)
            return ErrorResponse.get(400, "Het token mist.");

        try {
            int userId = userDAO.getUserIdFromToken(token);
            if(userId < 0)
                return ErrorResponse.get(401, "Het token is niet geldig.");

            int playlistId = Integer.parseInt(plId);

            int isOwnerAndExists = playlistDAO.checkOwnershipAndExistens(playlistId, userId);
            if(isOwnerAndExists == -1)
                return ErrorResponse.get(404, "De afspeellijst bestaat niet.");

            return Response.status(200).entity(tracksInPlaylistAsDTO(playlistId)).build();
        } catch(NumberFormatException e) {
            return ErrorResponse.get(400, "Geef een geldig playlist ID op.");
        } catch(InternalServerErrorException e) {
            return ErrorResponse.get(500, "Er is iets misgegaan op de server, probeer het opnieuw.");
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response connectTrackToPlaylist(
            TrackDTO track,
            @PathParam("plId") String plId,
            @QueryParam("token") String token
    ) {
        if(token == null) {
            return ErrorResponse.get(400, "Het token mist.");
        } else if(track.id == -1) {
            return ErrorResponse.get(
                    400,
                    "Geef het ID van de playlistTrack op die aan de playlist gekoppeld moet worden op."
            );
        }

        try {
            int userId = userDAO.getUserIdFromToken(token);
            if(userId < 0)
                return ErrorResponse.get(401, "Het token is niet geldig.");

            int playlistId = Integer.parseInt(plId);

            int isOwnerAndExists = playlistDAO.checkOwnershipAndExistens(playlistId, userId);
            if(isOwnerAndExists == 0) {
                return ErrorResponse.get(
                        403,
                        "Je hebt geen toestemming om de afspeellijst te bewerken."
                );
            } else if(isOwnerAndExists == -1) {
                return ErrorResponse.get(404, "De afspeellijst bestaat niet.");
            }

            if(trackDAO.trackAlreadyConnectedToPlaylist(playlistId, track.id))
                return ErrorResponse.get(400, "De playlistTrack is al aan de playlist gekoppeld.");

            trackDAO.connectTrackToPlaylist(playlistId, track.id, track.offlineAvailable);

            return Response.status(201).entity(tracksInPlaylistAsDTO(playlistId)).build();
        } catch(NumberFormatException e) {
            return ErrorResponse.get(400, "Geef een geldig playlist ID op.");
        } catch(InternalServerErrorException e) {
            return ErrorResponse.get(500, "Er is iets misgegaan op de server, probeer het opnieuw.");
        }
    }

    @DELETE
    @Path("/{trId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response disconnectTrackFromPlaylist(
            @PathParam("plId") String plId,
            @PathParam("trId") String trId,
            @QueryParam("token") String token
    ) {
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
                        "Je hebt geen toestemming om de afspeellijst te bewerken."
                );
            } else if(isOwnerAndExists == -1) {
                return ErrorResponse.get(404, "De afspeellijst bestaat niet.");
            }

            trackDAO.disconnectTrackFromPlaylist(playlistId, Integer.parseInt(trId));

            return Response.status(200).entity(tracksInPlaylistAsDTO(playlistId)).build();
        } catch(NumberFormatException e) {
            return ErrorResponse.get(400, "Geef een geldig playlist ID op.");
        } catch(InternalServerErrorException e) {
            return ErrorResponse.get(500, "Er is iets misgegaan op de server, probeer het opnieuw.");
        }
    }

    private TracksDTO tracksInPlaylistAsDTO(int playlistId) throws InternalServerErrorException {
        return tracksDTO(trackDAO.getTracksForPlaylist(playlistId));
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setTrackDAO(IPlaylistTrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    public void setPlaylistDAO(IPlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }
}


