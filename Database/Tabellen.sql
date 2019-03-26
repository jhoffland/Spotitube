DROP TABLE IF EXISTS
  PlaylistTrack, Tracks, Playlists, Users;

CREATE TABLE Users (
  username  VARCHAR(50)   NOT NULL,
  password  VARCHAR(250)  NOT NULL,
  token     VARCHAR(14)   NOT NULL, /* token format: 1234-1234-1234 */
  CONSTRAINT  pk_username PRIMARY KEY (username)
);

CREATE TABLE Playlists (
  id    INT           NOT NULL  AUTO_INCREMENT,
  name  VARCHAR(50)   NOT NULL,
  owner VARCHAR(50)   NOT NULL,
  CONSTRAINT  pk_playlistId     PRIMARY KEY (id),
  CONSTRAINT  fk_playlistOwner  FOREIGN KEY (owner)
              REFERENCES  Users(username)
              ON DELETE   RESTRICT
              ON UPDATE   CASCADE
);

CREATE TABLE Tracks (
  id              INT           NOT NULL  AUTO_INCREMENT,
  performer       VARCHAR(250)  NOT NULL,
  title           VARCHAR(250)  NOT NULL,
  url             BLOB          NOT NULL,
  duration        INT           NOT NULL,
  album           VARCHAR(250)  NULL,     /* only for SONG  */
  publicationDate DATE          NULL,     /* only for VIDEO */
  description     TEXT          NULL,     /* only for VIDEO */
  CONSTRAINT  pk_trackId  PRIMARY KEY (id)
);

CREATE TABLE PlaylistTrack (
  playlist          INT     NOT NULL,
  track             INT     NOT NULL,
  offlineAvailable  BOOLEAN NOT NULL  DEFAULT FALSE,
  CONSTRAINT  fk_playlist FOREIGN KEY (playlist)
              REFERENCES  Playlists(id)
              ON DELETE   RESTRICT
              ON UPDATE   CASCADE,
  CONSTRAINT  fk_track    FOREIGN KEY (track)
              REFERENCES  Tracks(id)
              ON DELETE   RESTRICT
              ON UPDATE   CASCADE
);