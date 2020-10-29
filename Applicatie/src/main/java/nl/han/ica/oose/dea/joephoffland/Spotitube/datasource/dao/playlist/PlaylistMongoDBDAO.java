package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.playlist;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBObject;
import com.mongodb.DBCollection;
import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao.GlobalMongoDBDAO;
import nl.han.ica.oose.dea.joephoffland.Spotitube.domain.Playlist;
import nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions.InternalServerErrorException;

import javax.enterprise.inject.Default;
import java.util.*;

import static com.mongodb.client.model.Accumulators.push;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;

@Default
public class PlaylistMongoDBDAO extends GlobalMongoDBDAO implements IPlaylistDAO {
    @Override
    public List<Playlist> getPlaylists(int userId) {
        DBCollection playlistsCollection = getDatabase().getCollection("playlists");

        Iterable<DBObject> mongoPlaylists = playlistsCollection.aggregate((DBObject) Arrays.asList(
            unwind("$tracks"),
            lookup("tracks", "tracks", "_id", "tracksDocs"),
            unwind("$tracksDocs"),
            group("$_id",
                push("name", "$name"),
                push("owner", "$owner"),
                sum("totalDuration", "$tracksDocs.duration")
            )
        )).results();

        List<Playlist> playlists = new ArrayList<>();
        for (DBObject playlist : mongoPlaylists) {
            Playlist currentPL = new Playlist();
            currentPL.setId((int) playlist.get("id"));
            currentPL.setName((String) playlist.get("name"));
            currentPL.setOwner(((int) playlist.get("owner")) == userId);
            currentPL.setTotalDuration((int) playlist.get("totalDuration"));
            playlists.add(currentPL);
        }

        return playlists;
    }


    @Override
    public void addPlaylist(String name, int userId) {
        DBCollection playlistsCollection = getDatabase().getCollection("playlists");

        Cursor lastPlaylistCursor = playlistsCollection.find().sort(new BasicDBObject("_id", 1)).limit(1);
        int lastId = 0;
        if(lastPlaylistCursor.hasNext()) {
            lastId = (int) lastPlaylistCursor.next().get("_id");
        }

        DBObject playlist = new BasicDBObject("_id", lastId + 1)
                .append("name", name)
                .append("owner", userId)
                .append("tracks", new ArrayList<DBObject>());

        playlistsCollection.insert(playlist);
    }

    @Override
    public int checkOwnershipAndExistens(int playlistId, int userId) {
        return 0;
    }

    @Override
    public void deletePlaylist(int playlistId) {

    }

    @Override
    public void editPlaylist(String name, int playlistId) {

    }
}
