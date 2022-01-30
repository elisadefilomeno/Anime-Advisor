package it.unipi.large_scale.anime_advisor.dbManager;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import org.bson.Document;

public class DbManagerMongoDB implements DbManager {
    String cstr;
    MongoClient client;
    MongoDatabase db;
    //Costruttore DBMangager per mongo che setta il client
    public DbManagerMongoDB(String cstr){
        this.client=MongoClients.create(cstr);

    }
    //Avviamento del database
    public void startMongo(String db) {
        this.db= this.client.getDatabase(db);
        }

    //Restituzione della Collezione specifiicata
    public MongoCollection<Document> getCollection(String col){
        return this.db.getCollection(col);
    }
    //Chusura del Database
    public void closeMongo(){
        this.client.close();
    }
}
