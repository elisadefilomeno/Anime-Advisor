package it.unipi.large_scale.anime_advisor.animeManager;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.result.UpdateResult;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;


import it.unipi.large_scale.anime_advisor.entity.Anime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class AnimeManagerMongoDBCRUD{


    //Check if the document is present with case insensitivity option
    public boolean checkIfPresent(Anime anime,MongoCollection<Document> collection){
        Document doc= collection.find(eq("name",new Document("$regex",anime.getAnime_name()).append("$options","i"))).first();
        return doc != null;
    }
    //check if an element is present in the array field of an anime
    public boolean checkIfPresentinArray(Anime anime,MongoCollection<Document> collection,String array,String el){
        BasicDBObject query= new BasicDBObject("name",anime.getAnime_name()).append(array,el);

        return collection.countDocuments(query) != 0;
    }

    public void printDoc(Document doc){
        //System.out.println(doc.toJson());
        System.out.println("Name:" + doc.get("name"));
        System.out.println("Episodes:" + doc.get("episodes"));
        System.out.println("Premiered:" + doc.get("premiered"));
        System.out.println("Genres:" + doc.get("genre"));
        System.out.println("Type:" + doc.get("type"));
        System.out.println("Source:" + doc.get("source"));
        System.out.println("Score:" + doc.get("scored"));
        System.out.println("Scored By:" + doc.get("scoredBy"));
        System.out.println("Followers:" + doc.get("members"));
        System.out.println("Studio:" + doc.get("studio"));
        System.out.println("Producer:" + doc.get("producer"));
        System.out.println("Licensor:" + doc.get("licensor"));
    }

    //Creates a new document and put it into the collection specified
    public void createAnime(Anime anime, MongoCollection<Document> collection) {
        if(checkIfPresent(anime, collection)){
            System.out.println("Document already present\n");
            return;}
        else {
            Document doc = new Document("name", anime.getAnime_name())
                    .append("episodes", anime.getEpisodes())
                    .append("premiered", anime.getPremiered())
                    .append("genre", Arrays.asList(anime.getGenre()))
                    .append("type", anime.getType())
                    .append("source", anime.getSource())
                    .append("studio", Arrays.asList(anime.getStudio()))
                    .append("producer", Arrays.asList(anime.getProducer()))
                    .append("licensor", Arrays.asList(anime.getLicensor()))
                    .append("scored", 0.0)
                    .append("scoredBy", 0)
                    .append("members", 0);
            collection.insertOne(doc);
             System.out.println("Document inserted correctly\n");
             return;
        }
    }

    //Find and print if present the document specified in the collection Return anime result
    public Anime readAnime(Anime anime, MongoCollection<Document> collection) {
        if(!checkIfPresent(anime, collection)){
            System.out.println("Document not found\n");
            return null;}
       Document result= collection.find(eq("name",new Document("$regex",anime.getAnime_name()).append("$options","i"))).first();
       // printDoc(result);
        printDoc(result);
        Anime a=new Anime();
        a.setAnime_name(result.get("name").toString());
        return a;
    }
    //Return an hashmap with the number(key) of anime names (value) having the specified genre
    public HashMap<Integer,String> findAnimeByGenre(String genre, MongoCollection<Document> collection) {
        HashMap<Integer,String> results=new HashMap<Integer, String>();
       try (MongoCursor<Document> cursor = collection.find(eq("genre",genre)).iterator())
       {
           int temp=1;
           while (cursor.hasNext()){
               results.put(temp,cursor.next().get("name").toString());
               temp++;
           }
             return results;  }
       catch(MongoException e){System.out.println("Error during search");return null;}
    }


    public void updateAnime(Anime anime, MongoCollection<Document> db) {

    }


    //Find a document and update it's parameters
    public void updateAnimeName(Anime anime,MongoCollection<Document> collection,String newName) {
        if(collection.find(eq("name",new Document("$regex",newName).append("$options","i"))).first()!=null){
            System.out.println("A document with this name is already present. Cannot update\n");
            return;
        }
        if(checkIfPresent(anime, collection)){
        Bson query= new Document().append("name",anime.getAnime_name());
        Bson update= Updates.set("name",newName);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not found\n");
    }

    //update the anime episodes field
    public void updateAnimeEpisodes(Anime anime,MongoCollection<Document> collection,int ep) {
        if(checkIfPresent(anime, collection)){
            Bson query= new Document().append("name",anime.getAnime_name());
            Bson update= Updates.set("episodes",ep);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not found\n");

    }

    //update the anime premiered field
    public void updateAnimePremiered(Anime anime,MongoCollection<Document> collection,int prem) {
        if(checkIfPresent(anime, collection)){
            Bson query= new Document().append("name",anime.getAnime_name());
            Bson update= Updates.set("premiered",prem);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not found\n");


    }
    //add one element in the genre array of the anime
    public void updateAnimeGenreAddOne(Anime anime,MongoCollection<Document> collection,String g) {
        if(checkIfPresent(anime, collection)){
            if(!checkIfPresentinArray(anime, collection, "genre", g)){
                Bson query= eq("name",anime.getAnime_name());
                Bson update= push("genre",g);
                UpdateResult result= collection.updateOne(query,update);
                System.out.println("Document updated\n");}
            else
                System.out.println("Genre already present");
        }
        else
            System.out.println("Cannot update: Document not found\n");
    }
    //delete one element from the genre array of the anime
    public void updateAnimeGenreDeleteOne(Anime anime,MongoCollection<Document> collection,String g){
        if(checkIfPresent(anime, collection)){
            if(checkIfPresentinArray(anime, collection, "genre", g)) {
                Bson query = eq("name", anime.getAnime_name());
                Bson update = pull("genre", g);
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            }
            else
                System.out.println("Element in genre not found\n");

        }
        else
            System.out.println("Cannot delete: Document not found\n");
    }

    //update the anime type field
    public void updateAnimeType(Anime anime,MongoCollection<Document> collection,String t) {
        if(checkIfPresent(anime, collection)){
            Bson query= new Document().append("name",anime.getAnime_name());
            Bson update= Updates.set("type",t);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not found\n");

    }
    //update the anime source field
    public void updateAnimeSource(Anime anime,MongoCollection<Document> collection,String s) {
        if(checkIfPresent(anime, collection)){
            Bson query= new Document().append("name",anime.getAnime_name());
            Bson update= Updates.set("source",s);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not found\n");

    }
    //Add a studio in the anime studio array
    public void updateAnimeStudioAddOne(Anime anime,MongoCollection<Document> collection,String nstudio) {
        if(checkIfPresent(anime, collection)){
            if(!checkIfPresentinArray(anime, collection, "studio", nstudio)){
                Bson query= eq("name",anime.getAnime_name());
                Bson update= push("studio",nstudio);
                UpdateResult result= collection.updateOne(query,update);
                System.out.println("Document updated\n");}
            else
                System.out.println("Studio already present");
        }
        else
            System.out.println("Cannot update: Document not found\n");
    }
    //Delete a studio from the anime studio array
    public void updateAnimeStudioDeleteOne(Anime anime,MongoCollection<Document> collection,String studio) {
            if(checkIfPresent(anime, collection)){
                if(checkIfPresentinArray(anime, collection, "studio", studio)) {
                    Bson query = eq("name", anime.getAnime_name());
                    Bson update = pull("studio", studio);
                    UpdateResult result = collection.updateOne(query, update);
                    System.out.println("Document updated\n");
                }
                else
                    System.out.println("Element in studio not found\n");

            }
            else
                System.out.println("Cannot delete: Document not found\n");

    }

    public void updateAnimeProducerAddOne(Anime anime,MongoCollection<Document> collection,String p) {
        if(checkIfPresent(anime, collection)){
            if(!checkIfPresentinArray(anime, collection, "producer", p)){
                Bson query= eq("name",anime.getAnime_name());
                Bson update= push("producer",p);
                UpdateResult result= collection.updateOne(query,update);
                System.out.println("Document updated\n");}
            else
                System.out.println("Producer already present");
        }
        else
            System.out.println("Cannot update: Document not found\n");
    }
    public void updateAnimeProducerDeleteOne(Anime anime,MongoCollection<Document> collection,String p) {
        if(checkIfPresent(anime, collection)){
            if(checkIfPresentinArray(anime, collection, "producer", p)) {
                Bson query = eq("name", anime.getAnime_name());
                Bson update = pull("producer", p);
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            }
            else
                System.out.println("Element in producer not found\n");

        }
        else
            System.out.println("Cannot delete: Document not found\n");
    }
    public void updateAnimeLicensorAddOne(Anime anime,MongoCollection<Document> collection,String l) {
        if(checkIfPresent(anime, collection)){
            if(!checkIfPresentinArray(anime, collection, "licensor", l)){
                Bson query= eq("name",anime.getAnime_name());
                Bson update= push("licensor",l);
                UpdateResult result= collection.updateOne(query,update);
                System.out.println("Document updated\n");}
            else
                System.out.println("Licensor already present");
        }
        else
            System.out.println("Cannot update: Document not found\n");
    }
    public void updateAnimeLicensorDeleteOne(Anime anime,MongoCollection<Document> collection,String l) {
        if(checkIfPresent(anime, collection)){
            if(checkIfPresentinArray(anime, collection, "licensor", l)) {
                Bson query = eq("name", anime.getAnime_name());
                Bson update = pull("licensor", l);
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            }
            else
                System.out.println("Element in licensor not found\n");

        }
        else
            System.out.println("Cannot delete: Document not found\n");
    }
    //update the general score
    public void updateAnimeScored(Anime anime,MongoCollection<Document> collection,double score) {
        if(checkIfPresent(anime, collection)){
            Bson query= new Document().append("name",anime.getAnime_name());
            Bson update= Updates.set("scored",score);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not found\n");

    }
    //Update the mean of the average score when someone vote USE IT ONLY AFTER THE FIRST VOTE
    public void updateAnimeMeanScored(Anime anime,MongoCollection<Document> collection,double score) {
        if(checkIfPresent(anime, collection)){
            Document doc= collection.find(eq("name",anime.getAnime_name())).first();
            double scored=doc.getDouble("scored");
            int scoredBy=doc.getInteger("scoredBy");
            double newScore= (scoredBy*scored+score)/(scoredBy+1);
            System.out.println("score="+scored+"\nsb="+scoredBy+"\nnew Score=");
            Bson query= new Document().append("name",anime.getAnime_name());
            Bson update= Updates.set("scored",newScore);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
                updateAnimeIncScoredBy(anime,collection);
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not found\n");

    }
    //Increment number of users who voted the anime
    public void updateAnimeIncScoredBy(Anime anime,MongoCollection<Document> collection) {
        if(checkIfPresent(anime, collection)){
            Bson query=new Document().append("name",anime.getAnime_name());
            Bson update= Updates.inc("scoredBy",1);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not found\n");

    }
    //update the total number of the users who voted the anime
    public void updateAnimeScoredBy(Anime anime,MongoCollection<Document> collection,int sb) {
        if(checkIfPresent(anime, collection)){
            Bson query= new Document().append("name",anime.getAnime_name());
            Bson update= Updates.set("scoredBy",sb);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not found\n");

    }
    //Increment the number of users who follow the anime when someone follow it
    public void updateAnimeIncMembers(Anime anime,MongoCollection<Document> collection) {
        if(checkIfPresent(anime, collection)){
            Bson query= new Document().append("name",anime.getAnime_name());
            Bson update= Updates.inc("members",1);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not found\n");

    }
    //update the number of members  value
    public void updateAnimeMembers(Anime anime,MongoCollection<Document> collection,int m) {
        if(checkIfPresent(anime, collection)){
            Bson query= new Document().append("name",anime.getAnime_name());
            Bson update= Updates.set("members",m);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not found\n");

    }

    //Find a document in the collection and deletes it
    public boolean deleteAnime(Anime anime,MongoCollection<Document> collection) {
        if(checkIfPresent(anime,collection)) {
            DeleteResult delete = collection.deleteOne(eq("name", anime.getAnime_name()));
            System.out.println("Document eliminated\n");
            return  true;
        }
        else{
            System.out.println("Document not found. Can't delete\n");
            return false;
        }
    }
}
