package it.unipi.large_scale.anime_advisor.animeManager;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import static org.neo4j.driver.Values.parameters;

public class AnimeManagerNeo4J implements AnimeManager<DbManagerNeo4J> {

    @Override
    public void createAnime(Anime anime, DbManagerNeo4J dbNeo4J) {
        if(checkIfPresent(anime, dbNeo4J)){
            System.out.println("Anime already present\n");
        return;
        }

        try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MERGE (a:Anime {title: $title})",
                        parameters(
                                "title", anime.getAnime_name()
                        )
                );
                return null;
            });
            System.out.println("Anime node inserted correctly\n");

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to create node due to an error");
        }
    }

    @Override
    public void readAnime(Anime anime, DbManagerNeo4J dbNeo4J) {

    }

    @Override
    public void updateAnime(Anime anime, DbManagerNeo4J dbNeo4J) {

    }

    @Override
    public void deleteAnime(Anime anime, DbManagerNeo4J dbNeo4J) {
        if(anime.getAnime_name()==null){
            System.out.println("Anime title not inserted, unable to delete");
            return;
        }

        try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (a:Anime) WHERE a.title=$title DETACH DELETE a",
                        parameters(
                                "title", anime.getAnime_name()
                        )
                );
                return null;
            });

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    public boolean checkIfPresent(Anime anime, DbManagerNeo4J dbNeo4J){
        if(anime.getAnime_name()==null){
            System.out.println("Anime title not inserted");
            return false;
        }

        try(Session session= dbNeo4J.getDriver().session()){
            int anime_count;
            anime_count = session.readTransaction(tx -> {
                Result result = tx.run( "MATCH (a:Anime) WHERE a.title=$title RETURN count(a) as anime_count",
                        parameters(
                                "title", anime.getAnime_name()
                        )
                );
                if(result.hasNext()){
                    org.neo4j.driver.Record r = result.next();
                    return (r.get("anime_count").asInt());
                }

                return null;
            });
            return (anime_count>0);
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
