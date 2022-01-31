package it.unipi.large_scale.anime_advisor.dbManager;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import org.bson.Document;

public class DbManagerMongoDB implements DbManager {
    MongoClient client;
    MongoDatabase db;
    //Costruttore DBMangager per mongo che setta il client
    public DbManagerMongoDB(String cstr){
        this.client=MongoClients.create(cstr);

    }
    //Avviamento del database
    public void startMongo(String db){
        try{
        this.db= this.client.getDatabase(db);
        } catch(Exception e){
            System.out.println("An error occurred while starting MondoDB server");
        }

        }

    //Restituzione della Collezione specifiicata
    public MongoCollection<Document> getCollection(String col){
        try {
            return this.db.getCollection(col);
        } catch(Exception e){
            System.out.println("Unable to retrieve the collection: "+col);
            return null;

        }
    }
    //Chusura del Database
    public void closeMongo(){
        try {
            this.client.close();
        } catch(Exception e){
            System.out.println("An error occurred while closing MondoDB server");
        }
    }
}
