package main;
import com.mongodb.client.MongoCollection;
import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDBCRUD;
import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDBAgg;
import it.unipi.large_scale.anime_advisor.dbManager.*;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import org.bson.Document;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

//da rimuovere


public class MongoMain {

    public static void main(String[] args){

        DbManagerMongoDB mongoM=new DbManagerMongoDB("mongodb://localhost:27017");
        mongoM.startMongo("Anime_Advisor");
        //mongoM viene usato come oggetto nei metodi da Terminale che richiedono le operazioni su
        //MongoDB, come le CRUD e le aggregazioni nonchè apertura e chiusura del Database
        //**METODI DI PROVA DI MONDOSB (DA ELIMINARE POI) **
        MongoCollection<Document> collection= mongoM.getCollection("anime");
        //collection.find().forEach(doc->System.out.println(doc.toJson()));
        AnimeManagerMongoDBCRUD amc= new AnimeManagerMongoDBCRUD();
        AnimeManagerMongoDBAgg amg=new AnimeManagerMongoDBAgg();
        Anime anime=new Anime();
        Anime a=new Anime();
        a.setAnime_name("akira");
        HashMap<Integer,String> r;
        HashMap<String,Double> b;

       /* anime.setAnime_name("Anime Test");
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
        anime.setScoredby(0);
        anime.setScored(0.0);
        anime.setMembers(0);
        amc.createAnime(anime,collection); */
        //TEST MONGO CRUD
        //amg.entityProdByType(collection,"studio",1999);

        System.out.println("studio 0");

       amg.highAvgEntity(collection,"studio",0);

        System.out.println("studio 2000");
        amg.highAvgEntity(collection,"studio",2000);

        System.out.println("genre 0 ");
        amg.highAvgEntity(collection,"genre",2000);

        System.out.println("genre 2000");
        amg.highAvgEntity(collection,"genre",0);

        System.out.println("producer 0");
        amg.highAvgEntity(collection,"producer",2000);

        System.out.println("producer 2000");
        amg.highAvgEntity(collection,"producer",2000);



        mongoM.closeMongo();






    }

}
