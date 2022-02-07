package main;
import com.mongodb.client.MongoCollection;
import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDBAgg;
import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDBCRUD;
import it.unipi.large_scale.anime_advisor.dbManager.*;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import org.bson.Document;

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
        AnimeManagerMongoDBCRUD am= new AnimeManagerMongoDBCRUD();
        AnimeManagerMongoDBAgg amg=new AnimeManagerMongoDBAgg();
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
        anime.setScoredby(0);
        anime.setScored(0.0);
        anime.setMembers(0);
        amc.createAnime(anime,collection);
        amc.readAnime(anime,collection);
        //TEST MONGO CRUD
    amg.entityProdByType(collection,"studio",0);



        mongoM.closeMongo();




    }

}
