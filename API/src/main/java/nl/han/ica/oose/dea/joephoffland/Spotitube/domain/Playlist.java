package nl.han.ica.oose.dea.joephoffland.Spotitube.domain;

import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private boolean owner;
    private List<Track> tracks;

    private int totalDuration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }
}
