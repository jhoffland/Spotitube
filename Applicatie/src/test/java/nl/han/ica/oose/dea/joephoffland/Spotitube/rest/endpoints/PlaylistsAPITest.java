package nl.han.ica.oose.dea.joephoffland.Spotitube.rest.endpoints;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlist.IPlaylistDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.user.IUserDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.domain.Playlist;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.PlaylistDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.PlaylistsDTO;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlaylistsAPITest {
    PlaylistsAPI playlistsAPI;
    IPlaylistDAO playlistDAO;
    IUserDAO userDAO;

    PlaylistsAPI spyedPlaylistAPI;

    String mockedToken;
    PlaylistDTO mockedPlaylistDTO;
    PlaylistsDTO mockedPlaylistsDTO;


    @Before
    public void setUp() {
        playlistsAPI = new PlaylistsAPI();
        playlistDAO = mock(IPlaylistDAO.class);
        userDAO = mock(IUserDAO.class);

        spyedPlaylistAPI = spy(playlistsAPI);

        playlistsAPI.setPlaylistDAO(playlistDAO);
        playlistsAPI.setUserDAO(userDAO);

        mockedToken = "1234-1234-1234";

        mockedPlaylistDTO = new PlaylistDTO();
        mockedPlaylistDTO.id = 1;
        mockedPlaylistDTO.name = "Test";
        mockedPlaylistDTO.owner = false;

        mockedPlaylistsDTO = new PlaylistsDTO();
        mockedPlaylistsDTO.playlists = new ArrayList<>();
        mockedPlaylistsDTO.playlists.add(mockedPlaylistDTO);
        mockedPlaylistsDTO.length = 123;
    }

    @Test
    public void getPlaylists() {
        Response playlistResp = playlistsAPI.getPlaylists(null);
        assertEquals(400, playlistResp.getStatus());

        try {
            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(-1);
            playlistResp = playlistsAPI.getPlaylists(mockedToken);
            assertEquals(401, playlistResp.getStatus());

            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(1);
            playlistResp = playlistsAPI.getPlaylists(mockedToken);
            assertEquals(200, playlistResp.getStatus());

            when(userDAO.getUserIdFromToken(mockedToken)).thenThrow(new InternalServerErrorException());
            playlistResp = playlistsAPI.getPlaylists(mockedToken);
            assertEquals(500, playlistResp.getStatus());
        } catch(InternalServerErrorException e) {
            fail();
        }
    }

    @Test
    public void addPlaylist() {
        Response playlistResp = playlistsAPI.addPlaylist(mockedPlaylistDTO, null);
        assertEquals(400, playlistResp.getStatus());

        mockedPlaylistDTO.name = "abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcde";
        playlistResp = playlistsAPI.addPlaylist(mockedPlaylistDTO, mockedToken);
        assertEquals(400, playlistResp.getStatus());

        mockedPlaylistDTO.name = null;
        playlistResp = playlistsAPI.addPlaylist(mockedPlaylistDTO, mockedToken);
        assertEquals(400, playlistResp.getStatus());

        mockedPlaylistDTO.name = "Test";


        try {
            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(-1);
            playlistResp = playlistsAPI.addPlaylist(mockedPlaylistDTO, mockedToken);
            assertEquals(401, playlistResp.getStatus());

            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(1);
            playlistResp = playlistsAPI.addPlaylist(mockedPlaylistDTO, mockedToken);
            assertEquals(201, playlistResp.getStatus());

            when(userDAO.getUserIdFromToken(mockedToken)).thenThrow(new InternalServerErrorException());
            playlistResp = playlistsAPI.addPlaylist(mockedPlaylistDTO, mockedToken);
            assertEquals(500, playlistResp.getStatus());
        } catch(InternalServerErrorException e) {
            fail();
        }
    }

    @Test
    public void deletePlaylist() {
        String plId = Integer.toString(mockedPlaylistDTO.id);

        Response playlistResp = playlistsAPI.deletePlaylist(plId, null);
        assertEquals(400, playlistResp.getStatus());

        try {
            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(-1);
            playlistResp = playlistsAPI.deletePlaylist(plId, mockedToken);
            assertEquals(401, playlistResp.getStatus());

            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(1);

            playlistResp = playlistsAPI.deletePlaylist("a", mockedToken);
            assertEquals(400, playlistResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(mockedPlaylistDTO.id, 1)).thenReturn(0);
            playlistResp = playlistsAPI.deletePlaylist(plId, mockedToken);
            assertEquals(403, playlistResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(mockedPlaylistDTO.id, 1)).thenReturn(-1);
            playlistResp = playlistsAPI.deletePlaylist(plId, mockedToken);
            assertEquals(404, playlistResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(mockedPlaylistDTO.id, 1)).thenReturn(1);

            playlistResp = playlistsAPI.deletePlaylist(plId, mockedToken);
            assertEquals(200, playlistResp.getStatus());

            when(userDAO.getUserIdFromToken(mockedToken)).thenThrow(new InternalServerErrorException());
            playlistResp = playlistsAPI.deletePlaylist(plId, mockedToken);
            assertEquals(500, playlistResp.getStatus());
        } catch(InternalServerErrorException e) {
            fail();
        }
    }

    @Test
    public void replacePlaylist() {
        String plId = Integer.toString(mockedPlaylistDTO.id);

        Response playlistResp = playlistsAPI.replacePlaylist(mockedPlaylistDTO, plId, null);
        assertEquals(400, playlistResp.getStatus());

        mockedPlaylistDTO.name = "abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcde";
        playlistResp = playlistsAPI.replacePlaylist(mockedPlaylistDTO, plId, mockedToken);
        assertEquals(400, playlistResp.getStatus());

        mockedPlaylistDTO.name = null;
        playlistResp = playlistsAPI.replacePlaylist(mockedPlaylistDTO, plId, mockedToken);
        assertEquals(400, playlistResp.getStatus());

        mockedPlaylistDTO.name = "Test";

        try {
            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(-1);
            playlistResp = playlistsAPI.replacePlaylist(mockedPlaylistDTO, plId, mockedToken);
            assertEquals(401, playlistResp.getStatus());

            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(1);

            playlistResp = playlistsAPI.replacePlaylist(mockedPlaylistDTO, "a", mockedToken);
            assertEquals(400, playlistResp.getStatus());

            playlistResp = playlistsAPI.replacePlaylist(mockedPlaylistDTO, Integer.toString(mockedPlaylistDTO.id + 3), mockedToken);
            assertEquals(400, playlistResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(mockedPlaylistDTO.id, 1)).thenReturn(0);
            playlistResp = playlistsAPI.replacePlaylist(mockedPlaylistDTO, plId, mockedToken);
            assertEquals(403, playlistResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(mockedPlaylistDTO.id, 1)).thenReturn(-1);
            playlistResp = playlistsAPI.replacePlaylist(mockedPlaylistDTO, plId, mockedToken);
            assertEquals(404, playlistResp.getStatus());

            when(playlistDAO.checkOwnershipAndExistens(mockedPlaylistDTO.id, 1)).thenReturn(1);

            playlistResp = playlistsAPI.replacePlaylist(mockedPlaylistDTO, plId, mockedToken);
            assertEquals(200, playlistResp.getStatus());

            when(userDAO.getUserIdFromToken(mockedToken)).thenThrow(new InternalServerErrorException());
            playlistResp = playlistsAPI.replacePlaylist(mockedPlaylistDTO, plId, mockedToken);
            assertEquals(500, playlistResp.getStatus());
        } catch(InternalServerErrorException e) {
            fail();
        }
    }

    @Test
    public void playlistsDTO() {
        Playlist mockedPlaylist = new Playlist();
        mockedPlaylist.setId(mockedPlaylistDTO.id);
        mockedPlaylist.setName(mockedPlaylistDTO.name);
        mockedPlaylist.setOwner(mockedPlaylistDTO.owner);
        mockedPlaylist.setTotalDuration(mockedPlaylistsDTO.length);

        List<Playlist> mockedPlaylistList = new ArrayList<>();
        mockedPlaylistList.add(mockedPlaylist);

        try {
            when(playlistDAO.getPlaylists(any(Integer.class))).thenReturn(mockedPlaylistList);

            PlaylistsDTO playlistsDTO = playlistsAPI.playlistsDTO(1);

            assertEquals(mockedPlaylistList.size(), playlistsDTO.playlists.size());
            PlaylistDTO playlistDTO = playlistsDTO.playlists.get(0);

            assertEquals(mockedPlaylist.getId(), playlistDTO.id);
            assertEquals(mockedPlaylist.getName(), playlistDTO.name);
            assertEquals(mockedPlaylist.isOwner(), playlistDTO.owner);
            assertEquals(mockedPlaylist.getTotalDuration(), playlistsDTO.length);
        } catch(InternalServerErrorException e) {
            fail();
        }
    }
}