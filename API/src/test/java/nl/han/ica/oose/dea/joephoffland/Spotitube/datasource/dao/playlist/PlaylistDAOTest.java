package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlist;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.DAOTestSetup;
import nl.han.ica.oose.dea.joephoffland.Spotitube.domain.Playlist;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class PlaylistDAOTest extends DAOTestSetup {
    PlaylistDAO playlistDAO;

    @Before
    public void playlistSetUp() {
        playlistDAO = new PlaylistDAO();
        playlistDAO.setDataSource(dataSource);
    }

    @Test
    public void getPlaylists() {
        try {
            String expectedSQL = "SELECT p.id AS id, p.name AS name, " +
                    "(p.owner = ?)AS owner, " +
                    "(SELECT SUM(duration) FROM Tracks tr " +
                    "JOIN PlaylistTrack pt ON tr.id = pt.track " +
                    "WHERE pt.playlist = p.id) AS totalDuration " +
                    "FROM Playlists p";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            int userId = 1;

            List<Playlist> playlists = playlistDAO.getPlaylists(userId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, userId);
            verify(preparedStatement).executeQuery();

            assertEquals(0, playlists.size());

            Playlist mockedPlaylist = new Playlist();
            mockedPlaylist.setId(1);
            mockedPlaylist.setName("test");
            mockedPlaylist.setOwner(true);
            mockedPlaylist.setTotalDuration(256);

            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getInt("id")).thenReturn(mockedPlaylist.getId());
            when(resultSet.getString("name")).thenReturn(mockedPlaylist.getName());
            when(resultSet.getBoolean("owner")).thenReturn(mockedPlaylist.isOwner());
            when(resultSet.getInt("totalDuration")).thenReturn(mockedPlaylist.getTotalDuration());

            playlists = playlistDAO.getPlaylists(1);

            assertEquals(1, playlists.size());

            Playlist actualPlaylist = playlists.get(0);
            assertEquals(mockedPlaylist.getId(), actualPlaylist.getId());
            assertEquals(mockedPlaylist.getName(), actualPlaylist.getName());
            assertEquals(mockedPlaylist.isOwner(), actualPlaylist.isOwner());
            assertEquals(mockedPlaylist.getTotalDuration(), actualPlaylist.getTotalDuration());

            when(dataSource.getConnection()).thenThrow(new SQLException());
            try {
                playlistDAO.getPlaylists(1);
                fail();
            } catch(InternalServerErrorException ignored) { }
        } catch(InternalServerErrorException e) {
            fail();
        } catch(SQLException e){
            fail();
        }
    }

    @Test
    public void addPlaylist() {
        try {
            String expectedSQL = "INSERT INTO Playlists (name, owner) VALUES (?, ?)";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);

            String plName = "Playlist";
            int userId = 0;

            playlistDAO.addPlaylist(plName, userId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, plName);
            verify(preparedStatement).setInt(2, userId);
            verify(preparedStatement).execute();

            when(dataSource.getConnection()).thenThrow(new SQLException());
            try {
                playlistDAO.addPlaylist(plName, userId);
                fail();
            } catch(InternalServerErrorException ignored) { }
        } catch(InternalServerErrorException e) {
            fail();
        } catch(SQLException e){
            fail();
        }
    }

    @Test
    public void checkOwnershipAndExistens() {
        try {
            String expectedSQL = "SELECT (owner = ?) AS isOwner FROM Playlists WHERE id = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            int plId = 1;
            int userId = 0;

            int owner = playlistDAO.checkOwnershipAndExistens(plId, userId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, userId);
            verify(preparedStatement).setInt(2, plId);
            verify(preparedStatement).executeQuery();

            assertEquals(-1, owner);

            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getBoolean("isOwner")).thenReturn(true);
            owner = playlistDAO.checkOwnershipAndExistens(plId, userId);
            assertEquals(1, owner);

            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getBoolean("isOwner")).thenReturn(false);
            owner = playlistDAO.checkOwnershipAndExistens(plId, userId);
            assertEquals(0, owner);

            when(dataSource.getConnection()).thenThrow(new SQLException());
            try {
                playlistDAO.checkOwnershipAndExistens(plId, userId);
                fail();
            } catch(InternalServerErrorException ignored) {}
        } catch(InternalServerErrorException e) {
            fail();
        } catch(SQLException e){
            fail();
        }
    }

    @Test
    public void deletePlaylist() {
        try {
            String expectedSQL = "DELETE FROM Playlists WHERE id = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);

            int plId = 2;

            playlistDAO.deletePlaylist(2);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, plId);
            verify(preparedStatement).execute();

            when(dataSource.getConnection()).thenThrow(new SQLException());
            try {
                playlistDAO.deletePlaylist(plId);
                fail();
            } catch(InternalServerErrorException ignored) { }
        } catch(InternalServerErrorException e) {
            fail();
        } catch(SQLException e){
            fail();
        }
    }

    @Test
    public void editPlaylist() {
        try {
            String expectedSQL = "UPDATE Playlists SET name = ? WHERE id = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);

            String plName = "plName";
            int plId = 2;

            playlistDAO.editPlaylist(plName, plId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, plName);
            verify(preparedStatement).setInt(2, plId);
            verify(preparedStatement).execute();

            when(dataSource.getConnection()).thenThrow(new SQLException());
            try {
                playlistDAO.editPlaylist(plName, plId);
                fail();
            } catch(InternalServerErrorException ignored) { }
        } catch(InternalServerErrorException e) {
            fail();
        } catch(SQLException e){
            fail();
        }
    }
}