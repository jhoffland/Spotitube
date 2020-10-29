package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlistTrack;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.GlobalSQLDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.utils.UsefulFunctions;
import nl.han.ica.oose.dea.joephoffland.Spotitube.domain.Track;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistTrackSQLDAO extends GlobalSQLDAO implements IPlaylistTrackDAO {
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public List<Track> getTracks(int notInPlaylistId) throws InternalServerErrorException {
        String getTracksSQL =  "SELECT t.id AS id, title, performer, duration, album, playcount, " +
                "publicationDate, description, pt.offlineAvailable AS offlineAvailable " +
                "FROM Tracks t LEFT JOIN PlaylistTrack pt ON t.id = pt.track " +
                "WHERE pt.playlist != ? OR pt.playlist IS NULL";
        if(notInPlaylistId < 0)
            getTracksSQL = "SELECT * FROM Tracks";

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(getTracksSQL);
            if(notInPlaylistId >= 0)
                statement.setInt(1, notInPlaylistId);
            ResultSet rsTracks = statement.executeQuery();

            return resultSetToTrackDAOList(rsTracks);
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public List<Track> getTracksForPlaylist(int playlistId) throws InternalServerErrorException {
        String getTracksSQL =  "SELECT t.id AS id, title, performer, duration, album, playcount, " +
                "publicationDate, description, pt.offlineAvailable AS offlineAvailable " +
                "FROM Tracks t RIGHT JOIN PlaylistTrack pt ON t.id = pt.track " +
                "WHERE pt.playlist = ?";

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(getTracksSQL);
            statement.setInt(1, playlistId);
            ResultSet rsTracks = statement.executeQuery();
            return resultSetToTrackDAOList(rsTracks);
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public boolean trackAlreadyConnectedToPlaylist(int playlistId, int trackId) throws InternalServerErrorException {
        String getPlaylistTrackSQL =  "SELECT * FROM PlaylistTrack WHERE playlist = ? AND track = ?";

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(getPlaylistTrackSQL);
            statement.setInt(1, playlistId);
            statement.setInt(2, trackId);
            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public void connectTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable)
            throws InternalServerErrorException {
        String insertPlaylistTrackSQL =  "INSERT INTO PlaylistTrack (playlist, track, offlineAvailable) " +
                "VALUES (?, ?, ?)";

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(insertPlaylistTrackSQL);
            statement.setInt(1, playlistId);
            statement.setInt(2, trackId);
            statement.setBoolean(3, offlineAvailable);
            statement.execute();
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public void disconnectTrackFromPlaylist(int playlistId, int trackId) throws InternalServerErrorException {
        String deletePlaylistTrackSQL =  "DELETE FROM PlaylistTrack WHERE playlist = ? AND track = ?";

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(deletePlaylistTrackSQL);
            statement.setInt(1, playlistId);
            statement.setInt(2, trackId);
            statement.execute();
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw new InternalServerErrorException();
        }
    }

    public List<Track> resultSetToTrackDAOList(ResultSet rsTracks) throws SQLException {
        List<Track> tracks = new ArrayList<>();

        while (rsTracks.next()) {
            Track currentTr = new Track();
            currentTr.setId(rsTracks.getInt("id"));
            currentTr.setTitle(rsTracks.getString("title"));
            currentTr.setPerformer(rsTracks.getString("performer"));
            currentTr.setDuration(rsTracks.getInt("duration"));
            currentTr.setAlbum(rsTracks.getString("album"));
            currentTr.setPlaycount(rsTracks.getInt("playcount"));
            currentTr.setPublicationDate(rsTracks.getDate("publicationDate"));
            currentTr.setDescription(rsTracks.getString("description"));
            if(UsefulFunctions.hasColumn(rsTracks, "offlineAvailable"))
                currentTr.setOfflineAvailable(rsTracks.getBoolean("offlineAvailable"));
            tracks.add(currentTr);
        }

        return tracks;
    }
}
