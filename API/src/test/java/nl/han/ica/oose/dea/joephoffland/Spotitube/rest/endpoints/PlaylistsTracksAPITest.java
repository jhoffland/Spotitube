package nl.han.ica.oose.dea.joephoffland.Spotitube.rest.endpoints;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlist.IPlaylistDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlistTrack.IPlaylistTrackDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.user.IUserDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.TrackDTO;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlaylistsTracksAPITest {
    PlaylistsTracksAPI playlistsTracksAPI;
    IUserDAO userDAO;
    IPlaylistTrackDAO trackDAO;
    IPlaylistDAO playlistDAO;

    TrackDTO mockedTrack;
    String mockedToken;

    @Before
    public void setUp() throws Exception {
        playlistsTracksAPI = new PlaylistsTracksAPI();

        userDAO = mock(IUserDAO.class);
        trackDAO = mock(IPlaylistTrackDAO.class);
        playlistDAO = mock(IPlaylistDAO.class);

        playlistsTracksAPI.setUserDAO(userDAO);
        playlistsTracksAPI.setTrackDAO(trackDAO);
        playlistsTracksAPI.setPlaylistDAO(playlistDAO);

        mockedTrack = new TrackDTO();
//        mockedTrack.id = 2;
        mockedTrack.title = "aa";
        mockedTrack.performer = "bb";
        mockedTrack.duration = 200;

        mockedToken = "1234-1234-1234";
    }

    @Test
    public void getTracksInPlaylist() {
        Response playlistTrackResp = playlistsTracksAPI.getTracksInPlaylist("3", null);
        assertEquals(400, playlistTrackResp.getStatus());

        try {
            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(-1);
            playlistTrackResp = playlistsTracksAPI.getTracksInPlaylist("3", mockedToken);
            assertEquals(401, playlistTrackResp.getStatus());

            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(1);

            playlistTrackResp = playlistsTracksAPI.getTracksInPlaylist("a", mockedToken);
            assertEquals(400, playlistTrackResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(3, 1)).thenReturn(-1);
            playlistTrackResp = playlistsTracksAPI.getTracksInPlaylist("3", mockedToken);
            assertEquals(404, playlistTrackResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(3, 1)).thenReturn(1);

            playlistTrackResp = playlistsTracksAPI.getTracksInPlaylist("3", mockedToken);
            assertEquals(200, playlistTrackResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(3, 1)).thenThrow(new InternalServerErrorException());
            playlistTrackResp = playlistsTracksAPI.getTracksInPlaylist("3", mockedToken);
            assertEquals(500, playlistTrackResp.getStatus());
        } catch(InternalServerErrorException e) {
            fail();
        }
    }

    @Test
    public void connectTrackToPlaylist() {
        Response playlistTrackResp = playlistsTracksAPI.connectTrackToPlaylist(mockedTrack, "3", mockedToken);
        assertEquals(400, playlistTrackResp.getStatus());

        mockedTrack.id = 2;

        playlistTrackResp = playlistsTracksAPI.connectTrackToPlaylist(mockedTrack, "3", null);
        assertEquals(400, playlistTrackResp.getStatus());

        try {
            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(-1);
            playlistTrackResp = playlistsTracksAPI.connectTrackToPlaylist(mockedTrack, "3", mockedToken);
            assertEquals(401, playlistTrackResp.getStatus());

            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(1);

            playlistTrackResp = playlistsTracksAPI.connectTrackToPlaylist(mockedTrack, "a", mockedToken);
            assertEquals(400, playlistTrackResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(3, 1)).thenReturn(0);
            playlistTrackResp = playlistsTracksAPI.connectTrackToPlaylist(mockedTrack, "3", mockedToken);
            assertEquals(403, playlistTrackResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(3, 1)).thenReturn(-1);
            playlistTrackResp = playlistsTracksAPI.connectTrackToPlaylist(mockedTrack, "3", mockedToken);
            assertEquals(404, playlistTrackResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(3, 1)).thenReturn(1);

            when(trackDAO.trackAlreadyConnectedToPlaylist(3, mockedTrack.id)).thenReturn(true);
            playlistTrackResp = playlistsTracksAPI.connectTrackToPlaylist(mockedTrack, "3", mockedToken);
            assertEquals(400, playlistTrackResp.getStatus());

            when(trackDAO.trackAlreadyConnectedToPlaylist(3, mockedTrack.id)).thenReturn(false);

            playlistTrackResp = playlistsTracksAPI.connectTrackToPlaylist(mockedTrack, "3", mockedToken);
            assertEquals(201, playlistTrackResp.getStatus());

            when(trackDAO.trackAlreadyConnectedToPlaylist(3, mockedTrack.id)).thenThrow(new InternalServerErrorException());
            playlistTrackResp = playlistsTracksAPI.connectTrackToPlaylist(mockedTrack, "3", mockedToken);
            assertEquals(500, playlistTrackResp.getStatus());
        } catch(InternalServerErrorException e) {
            fail();
        }
    }

    @Test
    public void disconnectTrackFromPlaylist() {
        Response playlistTrackResp = playlistsTracksAPI.disconnectTrackFromPlaylist("3", "1", null);
        assertEquals(400, playlistTrackResp.getStatus());

        try {
            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(-1);
            playlistTrackResp = playlistsTracksAPI.disconnectTrackFromPlaylist("3", "1", mockedToken);
            assertEquals(401, playlistTrackResp.getStatus());

            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(0);

            playlistTrackResp = playlistsTracksAPI.disconnectTrackFromPlaylist("a", "1", mockedToken);
            assertEquals(400, playlistTrackResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(3, 0)).thenReturn(0);
            playlistTrackResp = playlistsTracksAPI.disconnectTrackFromPlaylist("3", "1", mockedToken);
            assertEquals(403, playlistTrackResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(3, 0)).thenReturn(-1);
            playlistTrackResp = playlistsTracksAPI.disconnectTrackFromPlaylist("3", "1", mockedToken);
            assertEquals(404, playlistTrackResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(3, 0)).thenReturn(1);

            playlistTrackResp = playlistsTracksAPI.disconnectTrackFromPlaylist("3", "a", mockedToken);
            assertEquals(400, playlistTrackResp.getStatus());

            playlistTrackResp = playlistsTracksAPI.disconnectTrackFromPlaylist("3", "1", mockedToken);
            assertEquals(200, playlistTrackResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(3, 0)).thenThrow(new InternalServerErrorException());
            playlistTrackResp = playlistsTracksAPI.disconnectTrackFromPlaylist("3", "1", mockedToken);
            assertEquals(500, playlistTrackResp.getStatus());
        } catch(InternalServerErrorException e) {
            fail();
        }
    }
}