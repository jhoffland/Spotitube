package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlist;

import nl.han.ica.oose.dea.joephoffland.Spotitube.domain.Playlist;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;

import java.util.List;

public interface IPlaylistDAO {
    List<Playlist> getPlaylists(int userId) throws InternalServerErrorException;
    void addPlaylist(String name, int userId) throws InternalServerErrorException;
    int checkOwnershipAndExistens(int playlistId, int userId) throws InternalServerErrorException;
    void deletePlaylist(int playlistId) throws InternalServerErrorException;
    void editPlaylist(String name, int playlistId) throws InternalServerErrorException;
}
