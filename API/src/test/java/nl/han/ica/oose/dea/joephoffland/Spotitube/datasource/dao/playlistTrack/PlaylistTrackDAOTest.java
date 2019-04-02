package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlistTrack;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.DAOTestSetup;
import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.utils.UsefulFunctions;
import nl.han.ica.oose.dea.joephoffland.Spotitube.domain.Track;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ UsefulFunctions.class })
public class PlaylistTrackDAOTest extends DAOTestSetup {
    PlaylistTrackDAO playlistTrackDAO;
    PlaylistTrackDAO spyedPlaylistTrackDAO;
    Track mockedTrack;
    List<Track> tracksList = new ArrayList<>();

    public PlaylistTrackDAOTest() {
    }


    @Before
    public void playlistTrackSetUp() {
        playlistTrackDAO = new PlaylistTrackDAO();
        playlistTrackDAO.setDataSource(dataSource);
        spyedPlaylistTrackDAO = spy(playlistTrackDAO);
        mockStatic(UsefulFunctions.class);

        mockedTrack = new Track();
        mockedTrack.setId(1);
        mockedTrack.setTitle("test");
        mockedTrack.setPerformer("TEST");
        mockedTrack.setDuration(256);
        mockedTrack.setAlbum("2wsew");
        mockedTrack.setPlaycount(0);
        mockedTrack.setPublicationDate(new Date());
        mockedTrack.setDescription("adsf");

        tracksList.add(mockedTrack);
    }

    @Test
    public void getTracks() {
        String expectedSQL = "SELECT t.id AS id, title, performer, duration, album, playcount, " +
                "publicationDate, description, pt.offlineAvailable AS offlineAvailable " +
                "FROM Tracks t LEFT JOIN PlaylistTrack pt ON t.id = pt.track " +
                "WHERE pt.playlist != ? OR pt.playlist IS NULL";
        try {
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            doReturn(tracksList).when(spyedPlaylistTrackDAO).resultSetToTrackDAOList(resultSet);

            int plId = 1;

            playlistTrackDAO.getTracks(plId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, plId);
            verify(preparedStatement).executeQuery();

            expectedSQL = "SELECT * FROM Tracks";

            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            plId = -1;
            playlistTrackDAO.getTracks(plId);
            verify(preparedStatement, never()).setInt(1, plId);

            when(dataSource.getConnection()).thenThrow(new SQLException());
            try {
                playlistTrackDAO.getTracks(plId);
                fail();
            } catch(InternalServerErrorException e) { }
        } catch(InternalServerErrorException e) {
            fail();
        } catch(SQLException e){
            fail();
        }
    }

    @Test
    public void getTracksForPlaylist() {
        try {
            String expectedSQL = "SELECT t.id AS id, title, performer, duration, album, playcount, " +
                    "publicationDate, description, pt.offlineAvailable AS offlineAvailable " +
                    "FROM Tracks t RIGHT JOIN PlaylistTrack pt ON t.id = pt.track " +
                    "WHERE pt.playlist = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            doReturn(tracksList).when(spyedPlaylistTrackDAO).resultSetToTrackDAOList(resultSet);

            int plId = 1;

            playlistTrackDAO.getTracksForPlaylist(plId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, plId);
            verify(preparedStatement).executeQuery();

            when(dataSource.getConnection()).thenThrow(new SQLException());
            try {
                playlistTrackDAO.getTracksForPlaylist(plId);
                fail();
            } catch(InternalServerErrorException e) { }
        } catch(InternalServerErrorException e) {
            fail();
        } catch(SQLException e){
            fail();
        }
    }

    @Test
    public void trackAlreadyConnectedToPlaylist() {
        try {
            String expectedSQL = "SELECT * FROM PlaylistTrack WHERE playlist = ? AND track = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            int plId = 1;
            int trackId = 2;

            when(resultSet.next()).thenReturn(true);
            boolean trackAlreadyConnectedToPlaylist = playlistTrackDAO.trackAlreadyConnectedToPlaylist(plId, trackId);

            assertEquals(true, trackAlreadyConnectedToPlaylist);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, plId);
            verify(preparedStatement).setInt(2, trackId);
            verify(preparedStatement).executeQuery();

            when(resultSet.next()).thenReturn(false);
            trackAlreadyConnectedToPlaylist = playlistTrackDAO.trackAlreadyConnectedToPlaylist(plId, trackId);
            assertEquals(false, trackAlreadyConnectedToPlaylist);

            when(dataSource.getConnection()).thenThrow(new SQLException());
            try {
                playlistTrackDAO.trackAlreadyConnectedToPlaylist(plId, trackId);
                fail();
            } catch(InternalServerErrorException e) { }
        } catch(InternalServerErrorException e) {
            fail();
        } catch(SQLException e){
            fail();
        }
    }

    @Test
    public void connectTrackToPlaylist() {
        try {
            String expectedSQL = "INSERT INTO PlaylistTrack (playlist, track, offlineAvailable) " +
                    "VALUES (?, ?, ?)";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);

            int plId = 1;
            int trackId = 2;
            boolean offlineAvailable = true;

            playlistTrackDAO.connectTrackToPlaylist(plId, trackId, offlineAvailable);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, plId);
            verify(preparedStatement).setInt(2, trackId);
            verify(preparedStatement).setBoolean(3, offlineAvailable);
            verify(preparedStatement).execute();

            when(dataSource.getConnection()).thenThrow(new SQLException());
            try {
                playlistTrackDAO.connectTrackToPlaylist(plId, trackId, offlineAvailable);
                fail();
            } catch(InternalServerErrorException e) { }
        } catch(InternalServerErrorException e) {
            fail();
        } catch(SQLException e){
            fail();
        }
    }

    @Test
    public void disconnectTrackFromPlaylist() {
        try {
            String expectedSQL = "DELETE FROM PlaylistTrack WHERE playlist = ? AND track = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);

            int plId = 1;
            int trackId = 2;

            playlistTrackDAO.disconnectTrackFromPlaylist(plId, trackId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, plId);
            verify(preparedStatement).setInt(2, trackId);
            verify(preparedStatement).execute();

            when(dataSource.getConnection()).thenThrow(new SQLException());
            try {
                playlistTrackDAO.disconnectTrackFromPlaylist(plId, trackId);
                fail();
            } catch(InternalServerErrorException e) { }
        } catch(InternalServerErrorException e) {
            fail();
        } catch(SQLException e){
            fail();
        }
    }

    @Test
    public void resultSetToTrackDAOList() {
        try {
            when(resultSet.next()).thenReturn(false);
            List <Track> tracks = playlistTrackDAO.resultSetToTrackDAOList(resultSet);
            assertEquals(tracks.size(), 0);

            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getInt("id")).thenReturn(mockedTrack.getId());
            when(resultSet.getString("title")).thenReturn(mockedTrack.getTitle());
            when(resultSet.getString("performer")).thenReturn(mockedTrack.getPerformer());
            when(resultSet.getInt("duration")).thenReturn(mockedTrack.getDuration());
            when(resultSet.getString("album")).thenReturn(mockedTrack.getAlbum());
            when(resultSet.getInt("playcount")).thenReturn(mockedTrack.getPlaycount());
            when(resultSet.getDate("publicationDate")).
                    thenReturn(new java.sql.Date(mockedTrack.getPublicationDate().getTime()));
            when(resultSet.getString("description")).thenReturn(mockedTrack.getDescription());
            when(UsefulFunctions.hasColumn(resultSet, "offlineAvailable")).thenReturn(true);
            when(UsefulFunctions.hasColumn(resultSet, "offlineAvailable")).thenReturn(true);
            when(resultSet.getBoolean("offlineAvailable")).thenReturn(true);

            tracks = playlistTrackDAO.resultSetToTrackDAOList(resultSet);

            assertEquals(tracks.size(), 1);

            Track actualTrack = tracks.get(0);
            assertEquals(mockedTrack.getId(), actualTrack.getId());
            assertEquals(mockedTrack.getTitle(), actualTrack.getTitle());
            assertEquals(mockedTrack.getPerformer(), actualTrack.getPerformer());
            assertEquals(mockedTrack.getDuration(), actualTrack.getDuration());
            assertEquals(mockedTrack.getAlbum(), actualTrack.getAlbum());
            assertEquals(mockedTrack.getPlaycount(), actualTrack.getPlaycount());
            assertEquals(mockedTrack.getPublicationDate().getTime(), actualTrack.getPublicationDate().getTime());
            assertEquals(mockedTrack.getDescription(), actualTrack.getDescription());
            assertEquals(true, actualTrack.isOfflineAvailable());

            when(UsefulFunctions.hasColumn(resultSet, "offlineAvailable")).thenReturn(false);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            tracks = playlistTrackDAO.resultSetToTrackDAOList(resultSet);
            assertEquals(1, tracks.size());
            assertEquals(false, tracks.get(0).isOfflineAvailable());
        } catch(SQLException e) {
            fail();
        }
    }
}