package it.unipi.large_scale.anime_advisor.menu;
import com.mongodb.client.MongoCollection;
import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDB;
import it.unipi.large_scale.anime_advisor.dbManager.*;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import org.bson.Document;

//da rimuovere


public class Main {

    public static void main(String[] args){

        DbManagerMongoDB mongoM=new DbManagerMongoDB("mongodb://localhost:27017");
        mongoM.startMongo("Anime_Advisor");
        //mongoM viene usato come oggetto nei metodi da Terminale che richiedono le operazioni su
        //MongoDB, come le CRUD e le aggregazioni nonchè apertura e chiusura del Database
        //**METODI DI PROVA DI MONDOSB (DA ELIMINARE POI) **
        MongoCollection<Document> collection= mongoM.getCollection("anime");
        //collection.find().forEach(doc->System.out.println(doc.toJson()));
        AnimeManagerMongoDB am= new AnimeManagerMongoDB();
        Anime anime=new Anime();
        anime.setAnime_name("Anime Test");
        anime.setEpisodes(20);
        anime.setPremiered(1999);
        String[] g={"Action","Adventure"};
        String[] s={"Trigger"};
        String[] p={"sd","asdsa"};
        String[] l={"llic","lic2"};
        anime.setGenre(g);
        anime.setType("TV");
        anime.setSource("Manga");
        anime.setStudio(s);
        anime.setProducer(p);
        anime.setLicensor(l);
        anime.setScoredby(12);
        anime.setScoredby(111);
        anime.setMembers(111);
        am.createAnime(anime,collection);



        if(am.checkIfPresent(anime,collection)==true)
            System.out.println("Presente");
        else
            System.out.println("Non presente");

        am.readAnime(anime,collection);
        am.updateAnimeName(anime,collection,"rotocalco");
        anime.setAnime_name("rotocalco");
        am.readAnime(anime,collection);
        am.updateAnimeStudioAddOne(anime,collection,"Studio Ghibli");
        am.readAnime(anime,collection);
        Anime cb= new Anime();
        cb.setAnime_name("Kill la Kill");
        am.readAnime(cb,collection);
        am.deleteAnime(anime,collection);
        mongoM.closeMongo();




    }

}
