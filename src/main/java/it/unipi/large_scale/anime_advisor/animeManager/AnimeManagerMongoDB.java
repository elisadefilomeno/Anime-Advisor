package it.unipi.large_scale.anime_advisor.animeManager;
import com.mongodb.client.*;
import org.bson.Document;

import it.unipi.large_scale.anime_advisor.entity.Anime;

import java.util.Arrays;

public class AnimeManagerMongoDB implements AnimeManager<MongoCollection<Document>>{

    public boolean checkIfPresent(Anime anime,MongoCollection<Document> collection){
        long found= collection.countDocuments(Document.parse("name:"+anime.getAnime_name()));
        if(found==1){return true;}
        return false;

    }

    @Override
    //Crea un documento e lo inserisce nella collezione collection
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
            .append("scored",anime.getScored())
            .append("scoredBy",anime.getScoredby())
            .append("members",anime.getMembers());
    collection.insertOne(doc);

    }

    @Override
    //Trova un documento nella collezione collection e stampa l'oggetto se trovato
    public void readAnime(Anime anime, MongoCollection<Document> collection) {
        if(checkIfPresent(anime,collection)==false){
            System.out.println("Documento inesistente\n");
            return;}
        collection.find(Document.parse("name:"+anime.getAnime_name()));


    }

    @Override
    //Trova un documento nella collezione e ne aggiorna i parametri
    public void updateAnime(Anime anime,MongoCollection<Document> collection) {

    }

    @Override
    //Trova un documento nella collezione e lo elimina
    public void deleteAnime(Anime anime,MongoCollection<Document> collection) {

    }
}
