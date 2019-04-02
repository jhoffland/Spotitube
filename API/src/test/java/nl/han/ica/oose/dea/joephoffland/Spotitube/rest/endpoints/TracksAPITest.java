package nl.han.ica.oose.dea.joephoffland.Spotitube.rest.endpoints;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlistTrack.IPlaylistTrackDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.user.IUserDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.domain.Track;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.TrackDTO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.rest.dto.TracksDTO;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TracksAPITest {
    TracksAPI tracksAPI;
    IUserDAO userDAO;
    IPlaylistTrackDAO trackDAO;

    String mockedToken;
    Track mockedTrack;
    List<Track> mockedTrackList;

    @Before
    public void setUp() throws Exception {
        tracksAPI = new TracksAPI();
        userDAO = mock(IUserDAO.class);
        trackDAO = mock(IPlaylistTrackDAO.class);

        tracksAPI.setUserDAO(userDAO);
        tracksAPI.setTrackDAO(trackDAO);

        mockedToken = "1234-1234-1234";

        mockedTrack = new Track();
        mockedTrack.setId(2);
        mockedTrack.setTitle("aa");
        mockedTrack.setPerformer("bb");
        mockedTrack.setDuration(200);

        mockedTrackList = new ArrayList<>();
        mockedTrackList.add(mockedTrack);
    }

    @Test
    public void getTracks() {
        Response tracksApiResp = tracksAPI.getTracks(null, "1");
        assertEquals(400, tracksApiResp.getStatus());

        try {
            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(-1);
            tracksApiResp = tracksAPI.getTracks(mockedToken, "1");
            assertEquals(401, tracksApiResp.getStatus());

            when(userDAO.getUserIdFromToken(mockedToken)).thenReturn(1);

            tracksApiResp = tracksAPI.getTracks(mockedToken, "a");
            assertEquals(400, tracksApiResp.getStatus());

            when(trackDAO.getTracks(any(Integer.class))).thenReturn(mockedTrackList);
            tracksApiResp = tracksAPI.getTracks(mockedToken, "2");
            assertEquals(200, tracksApiResp.getStatus());

            when(trackDAO.getTracks(any(Integer.class))).thenThrow(new InternalServerErrorException());
            tracksApiResp = tracksAPI.getTracks(mockedToken, "2");
            assertEquals(500, tracksApiResp.getStatus());
        } catch(InternalServerErrorException e) {
            fail();
        }
    }

    @Test
    public void tracksDTO() {
        TracksDTO tracksDTO = TracksAPI.tracksDTO(mockedTrackList);
        List<TrackDTO> trackDTOList = tracksDTO.tracks;

        assertEquals(mockedTrackList.size(), trackDTOList.size());
        assertNull(trackDTOList.get(0).publicationDate);

        mockedTrack.setPublicationDate(new Date());

        tracksDTO = TracksAPI.tracksDTO(mockedTrackList);
        trackDTOList = tracksDTO.tracks;

        String inputDate = new SimpleDateFormat("dd-MM-yyyy").format(mockedTrackList.get(0).getPublicationDate());
        assertEquals(inputDate, trackDTOList.get(0).publicationDate);
    }
}