package nl.han.ica.oose.dea.joephoffland.Spotitube.rest.endpoints;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlistTrack.IPlaylistTrackDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.user.IUserDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.domain.Track;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.TrackDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.TracksDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.utils.ErrorResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Path("/tracks")
public class TracksAPI {
    @Inject
    private IUserDAO userDAO;

    @Inject
    private IPlaylistTrackDAO trackDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks(@QueryParam("token") String token, @QueryParam("forPlaylist") String forPlaylist) {
        if(token == null)
            return ErrorResponse.get(400, "Het token mist.");

        try {
            int userId = userDAO.getUserIdFromToken(token);
            if(userId < 0)
                return ErrorResponse.get(401, "Het token is niet geldig.");

            int playlistId = -1;
            if(forPlaylist != null)
                playlistId = Integer.parseInt(forPlaylist);

            TracksDTO tracks = tracksDTO(trackDAO.getTracks(playlistId));

            return Response.status(200).entity(tracks).build();
        } catch(NumberFormatException e) {
            return ErrorResponse.get(400, "Geef een geldig playlist ID op.");
        } catch(InternalServerErrorException e) {
            return ErrorResponse.get(500, "Er is iets misgegaan op de server, probeer het opnieuw.");
        }
    }

    public static TracksDTO tracksDTO(List<Track> tracks) {
        TracksDTO tracksDTO = new TracksDTO();
        tracksDTO.tracks = new ArrayList<>();

        for(Track track : tracks) {
            TrackDTO trackDTO = new TrackDTO();

            trackDTO.id = track.getId();
            trackDTO.title = track.getTitle();
            trackDTO.performer = track.getPerformer();
            trackDTO.duration = track.getDuration();
            trackDTO.album = track.getAlbum();
            trackDTO.playcount = track.getPlaycount();
            trackDTO.description = track.getDescription();
            trackDTO.offlineAvailable = track.isOfflineAvailable();

            if(track.getPublicationDate() != null)
                trackDTO.publicationDate = new SimpleDateFormat("dd-MM-yyyy").format(track.getPublicationDate());

            tracksDTO.tracks.add(trackDTO);
        }

        return tracksDTO;
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setTrackDAO(IPlaylistTrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }
}


