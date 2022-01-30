package it.unipi.large_scale.anime_advisor.menu;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import it.unipi.large_scale.anime_advisor.dbManager.*;
import org.bson.Document;

public class Main {

    public static void main(String[] args){
        DbManagerMongoDB mongoM=new DbManagerMongoDB("mongodb://localhost:27017");
        mongoM.startMongo("Anime_Advisor");
        MongoCollection<Document> collection= mongoM.getCollection("anime");
        collection.find().forEach(doc->System.out.println(doc.toJson()));
        mongoM.closeMongo();




    }

}
