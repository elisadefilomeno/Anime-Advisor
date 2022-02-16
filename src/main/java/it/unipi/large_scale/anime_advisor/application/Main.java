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
        String connectionString= "mongodb://172.16.4.72:27020," +
                "172.16.4.73:27020," +
                "172.16.4.74:27020" +
                "/?retryWrites=true&w=3&wtimeoutMS=5000&readPreference=nearest";
        try{
            // connections to databases
            dbNeo4J = new DbManagerNeo4J();
            userManagerNeo4J =new UserManagerNeo4J(dbNeo4J);
//            DbManagerMongoDB mongoM=new DbManagerMongoDB("mongodb://localhost:27017");
            DbManagerMongoDB mongoM=new DbManagerMongoDB(connectionString);
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
