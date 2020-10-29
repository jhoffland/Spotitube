package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.dao;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.MongoDBConfig;

import javax.annotation.Resource;

public class GlobalMongoDBDAO {
    @Resource(name = "MongoDBConfig")
    private MongoDBConfig mongoDBConfig;

    private DB database;

    public void setMongoDBConfig(MongoDBConfig mongoDBConfig) {
        this.mongoDBConfig = mongoDBConfig;

        MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoDBConfig.getMongoConnectionURI()));
        this.database = mongoClient.getDB(mongoDBConfig.getDatabaseName());
    }

    public DB getDatabase() {
        return database;
    }
}
