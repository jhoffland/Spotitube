package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource;

public class MongoDBConfig {
    private String mongoConnectionURI;
    private String databaseName;


    public String getMongoConnectionURI() {
        return mongoConnectionURI;
    }

    public void setMongoConnectionURI(String mongoConnectionURI) {
        this.mongoConnectionURI = mongoConnectionURI;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
