package it.unipi.large_scale.anime_advisor.animeManager;

import com.mongodb.client.MongoCollection;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import org.bson.Document;

public class AnimeManagerMondoDBAgg {

    //top 10 anime by specified field
    public void topTenAnimeByField(Anime anime, MongoCollection<Document>doc,String field){

    }
    //entity with the highes average score in an year or all over the years
    public void highAvgEntity (Anime anime, MongoCollection<Document>doc,String entity, int year){

    }

    //entity with the most users following theire products
    public void mostFollowersEntity(Anime anime, MongoCollection<Document>doc,String entity){

    }

    //most followed genres
    public void mostFollowedGenres(Anime anime, MongoCollection<Document>doc,String entity){

    }

    //number of Entity's production by type
    public void entityProdbyType(Anime anime, MongoCollection<Document>doc,String entity, int year){

    }

}
