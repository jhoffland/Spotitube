package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlist;

import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.GlobalDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.domain.Playlist;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistDAO extends GlobalDAO implements IPlaylistDAO {
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public List<Playlist> getPlaylists(int userId) throws InternalServerErrorException {
        String getPlaylistsSQL =    "SELECT p.id AS id, p.name AS name, " +
                                        "(p.owner = ?)AS owner, " +
                                        "(SELECT SUM(duration) FROM Tracks tr " +
                                            "JOIN PlaylistTrack pt ON tr.id = pt.track " +
                                            "WHERE pt.playlist = p.id) AS totalDuration " +
                                    "FROM Playlists p";

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(getPlaylistsSQL);
            statement.setInt(1, userId);
            ResultSet rsPlaylists = statement.executeQuery();

            List<Playlist> playlists = new ArrayList<>();
            while(rsPlaylists.next()) {
                Playlist currentPL = new Playlist();
                currentPL.setId(rsPlaylists.getInt("id"));
                currentPL.setName(rsPlaylists.getString("name"));
                currentPL.setOwner(rsPlaylists.getBoolean("owner"));
                currentPL.setTotalDuration(rsPlaylists.getInt("totalDuration"));
                playlists.add(currentPL);
            }

            return playlists;
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw new InternalServerErrorException();
        }
    }


    @Override
    public void addPlaylist(String name, int userId) throws InternalServerErrorException {
        String insertPlaylistSQL = "INSERT INTO Playlists (name, owner) VALUES (?, ?)";
        try {
            Connection connection = dataSource.getConnection();;
            PreparedStatement statement = connection.prepareStatement(insertPlaylistSQL);
            statement.setString(1, name);
            statement.setInt(2, userId);
            statement.execute();
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public int checkOwnershipAndExistens(int playlistId, int userId) throws InternalServerErrorException {
        String checkOwnershipSQL = "SELECT (owner = ?) AS isOwner FROM Playlists WHERE id = ?";
        try {
            Connection connection = dataSource.getConnection();;
            PreparedStatement statement = connection.prepareStatement(checkOwnershipSQL);
            statement.setInt(1, userId);
            statement.setInt(2, playlistId);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                return rs.getBoolean("isOwner") ? 1 : 0;
            }
            return -1;
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public void deletePlaylist(int playlistId) throws InternalServerErrorException {
        String deletePlaylistSQL = "DELETE FROM Playlists WHERE id = ?";
        try {
            Connection connection = dataSource.getConnection();;
            PreparedStatement statement = connection.prepareStatement(deletePlaylistSQL);
            statement.setInt(1, playlistId);
            statement.execute();
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public void editPlaylist(String name, int playlistId) throws InternalServerErrorException {
        String updatePlaylistSQL = "UPDATE Playlists SET name = ? WHERE id = ?";
        try {
            Connection connection = dataSource.getConnection();;
            PreparedStatement statement = connection.prepareStatement(updatePlaylistSQL);
            statement.setString(1, name);
            statement.setInt(2, playlistId);
            statement.execute();
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw new InternalServerErrorException();
        }
    }
}
