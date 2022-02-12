package it.unipi.large_scale.anime_advisor.application;

import com.mongodb.client.MongoCollection;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerMongoDB;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;
import org.bson.Document;

public class Main {
    protected static Interface anInterface = new Interface();
    protected static DbManagerNeo4J dbNeo4J;
    protected static UserManagerNeo4J userManagerNeo4J;
    protected static MongoCollection<Document> anime_collection;


    public static void main(String args[]) throws Exception {
        try{
            // connections to databases
            dbNeo4J = new DbManagerNeo4J();
            userManagerNeo4J =new UserManagerNeo4J(dbNeo4J);
            DbManagerMongoDB mongoM=new DbManagerMongoDB("mongodb://localhost:27017");
            mongoM.startMongo("Anime_Advisor");
            anime_collection= mongoM.getCollection("anime");

        } catch (Exception e) {
            System.out.println("Error");
            return;
        }
        anInterface.showMenu();

        dbNeo4J.closeNeo4J();
    }
}
