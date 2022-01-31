package it.unipi.large_scale.anime_advisor.animeManager;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.MongoException;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.result.UpdateResult;


import it.unipi.large_scale.anime_advisor.entity.Anime;

import java.util.Arrays;

public class AnimeManagerMongoDB implements AnimeManager<MongoCollection<Document>>{


    //Check if the document is present with case insensitivity option
    public boolean checkIfPresent(Anime anime,MongoCollection<Document> collection){
        Document doc= collection.find(eq("name",new Document("$regex",anime.getAnime_name()).append("$options","i"))).first();
        if(doc!=null)
            return true;
        else
            return false;
    }

    @Override
    //Creates a new document and put it into the collection specified
    public void createAnime(Anime anime, MongoCollection<Document> collection) {
        if(checkIfPresent(anime,collection)==true){
            System.out.println("Documento già presente\n");
            return;}
    Document doc= new Document ("name",anime.getAnime_name())
            .append("episodes",anime.getEpisodes())
            .append("premiered",anime.getPremiered())
            .append("genre", Arrays.asList(anime.getGenre()))
            .append("type",anime.getType())
            .append("source",anime.getSource())
            .append("studio", Arrays.asList(anime.getStudio()))
            .append("producer",Arrays.asList(anime.getProducer()))
            .append("licensor",Arrays.asList(anime.getLicensor()))
            .append("scored",0)
            .append("scoredBy",0)
            .append("members",0);
    collection.insertOne(doc);
    System.out.println("Document inserted correctly");
    }

    @Override
    //Find and print if present the document specified in the collection
    public void readAnime(Anime anime, MongoCollection<Document> collection) {
        if(checkIfPresent(anime,collection)==false){
            System.out.println("Documento inesistente\n");
            return;}
       Document result= collection.find(eq("name",new Document("$regex",anime.getAnime_name()).append("$options","i"))).first();
        System.out.println(result.toJson());

    }

    @Override
    public void updateAnime(Anime anime, MongoCollection<Document> db) {

    }

    //Find a document and update it's parameters
    public void updateAnimeName(Anime anime,MongoCollection<Document> collection,String newName) {
        if(checkIfPresent(anime,collection)==true){
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
            System.out.println("Cannot Update: Document not present");
    }
    public void updateAnimeEpisodes(Anime anime,MongoCollection<Document> collection,int ep) {
        if(checkIfPresent(anime,collection)==true){
            Bson query= new Document().append("episodes",anime.getEpisodes());
            Bson update= Updates.set("episodes",ep);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not present");

    }
    public void updateAnimePremiered(Anime anime,MongoCollection<Document> collection,int prem) {
        if(checkIfPresent(anime,collection)==true){
            Bson query= new Document().append("premiered",anime.getPremiered());
            Bson update= Updates.set("premiered",prem);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not present");


    }
    public void updateAnimeGenre(Anime anime,MongoCollection<Document> collection) {

    }
    public void updateAnimeType(Anime anime,MongoCollection<Document> collection,String t) {
        if(checkIfPresent(anime,collection)==true){
            Bson query= new Document().append("type",anime.getType());
            Bson update= Updates.set("type",t);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not present");

    }
    public void updateAnimeSource(Anime anime,MongoCollection<Document> collection,String s) {
        if(checkIfPresent(anime,collection)==true){
            Bson query= new Document().append("source",anime.getSource());
            Bson update= Updates.set("source",s);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not present");

    }
    public void updateAnimeStudio(Anime anime,MongoCollection<Document> collection) {

    }
    public void updateAnimeProducer(Anime anime,MongoCollection<Document> collection) {

    }
    public void updateAnimeLicensor(Anime anime,MongoCollection<Document> collection) {

    }
    //update the general score
    public void updateAnimeScored(Anime anime,MongoCollection<Document> collection,double score) {
        if(checkIfPresent(anime,collection)==true){
            Bson query= new Document().append("scored",anime.getScored());
            Bson update= Updates.set("scored",score);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not present");

    }
    //Update the mean of the average score when someone vote
    public void updateAnimeMeanScored(Anime anime,MongoCollection<Document> collection,double score) {
        if(checkIfPresent(anime,collection)==true){
            Bson query= new Document().append("scored",anime.getScored());
            double newScore= (anime.getScoredby()*anime.getScored()+score)/(anime.getScoredby());
            Bson update= Updates.set("scored",newScore);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not present");

    }
    //Increment number of users who voted the anime
    public void updateAnimeIncScoredBy(Anime anime,MongoCollection<Document> collection) {
        if(checkIfPresent(anime,collection)==true){
            Bson query= new Document().append("scoredBy",anime.getScoredby());
            Bson update= Updates.set("scoredBy",anime.getScoredby()+1);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not present");

    }
    //update the total number of the users who voted the anime
    public void updateAnimeScoredBy(Anime anime,MongoCollection<Document> collection,int sb) {
        if(checkIfPresent(anime,collection)==true){
            Bson query= new Document().append("scoredBy",anime.getScoredby());
            Bson update= Updates.set("scoredBy",sb);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not present");

    }
    //Increment the number of users who follow the anime when someone follow it
    public void updateAnimeIncMembers(Anime anime,MongoCollection<Document> collection) {
        if(checkIfPresent(anime,collection)==true){
            Bson query= new Document().append("members",anime.getMembers());
            Bson update= Updates.set("members",anime.getMembers()+1);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not present");

    }
    //update the number of members  value
    public void updateAnimeMembers(Anime anime,MongoCollection<Document> collection,int m) {
        if(checkIfPresent(anime,collection)==true){
            Bson query= new Document().append("members",anime.getMembers());
            Bson update= Updates.set("members",m);
            try {
                UpdateResult result = collection.updateOne(query, update);
                System.out.println("Document updated\n");
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
        else
            System.out.println("Cannot Update: Document not present");

    }

    @Override
    //Find a document in the collection and deletes it
    public void deleteAnime(Anime anime,MongoCollection<Document> collection) {
        if(checkIfPresent(anime,collection)) {
            DeleteResult delete = collection.deleteOne(eq("name", anime.getAnime_name()));
            System.out.println("Documento eliminato");
        }
        else{
            System.out.println("Documento non trovato. Impossibile eliminare");
        }


    }
}
