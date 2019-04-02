package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlistTrack;


import nl.han.ica.oose.dea.joephoffland.Spotitube.domain.Track;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IPlaylistTrackDAO {
    List<Track> getTracks(int notInPlaylistId) throws InternalServerErrorException;
    List<Track> getTracksForPlaylist(int playlistId) throws InternalServerErrorException;
    boolean trackAlreadyConnectedToPlaylist(int playlistId, int trackId) throws InternalServerErrorException;
    void connectTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable)
            throws InternalServerErrorException;
    void disconnectTrackFromPlaylist(int playlistId, int trackId)
            throws InternalServerErrorException;
}
