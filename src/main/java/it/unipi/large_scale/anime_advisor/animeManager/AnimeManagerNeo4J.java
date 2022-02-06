package it.unipi.large_scale.anime_advisor.animeManager;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.User;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.time.LocalDate;
import java.util.*;

import static org.neo4j.driver.Values.parameters;

public class AnimeManagerNeo4J{
    DbManagerNeo4J dbNeo4J;

    public AnimeManagerNeo4J() {
        this.dbNeo4J = new DbManagerNeo4J();
    }

    public void createAnime(Anime anime) {
        if(checkIfPresent(anime)){
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

    public void readAnime(Anime anime) {
        try(Session session= dbNeo4J.getDriver().session()){
            Anime a;
            a = session.readTransaction(tx -> {
                Result result = tx.run( "MATCH (a:Anime) WHERE a.title=$title",
                        parameters(
                                "title", anime.getAnime_name()
                        )
                );
                if(result.hasNext()){
                    org.neo4j.driver.Record r = result.next();
                    String title = r.get("a.title").asString();
                    Anime read_anime = new Anime();
                    read_anime.setAnime_name(title);
                    return read_anime;
                }

                return null;
            });
            System.out.println("Anime info:");
            System.out.println(a.toString());
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to get user due to an error");
        }

    }


    public void deleteAnime(Anime anime) {
        if(anime.getAnime_name()==null){
            System.out.println("Anime title not inserted, unable to delete");
            return;
        }
        if(!checkIfPresent(anime)){
            System.out.println("Cannot delete, anime not present in database");
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

    public boolean checkIfPresent(Anime anime){
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

    public Set<User> getFollowers(String anime_title){
        Set<User> followers = new HashSet<User>();
        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx->{
                Result result = tx.run("MATCH (anime:Anime)<-[f:FOLLOWS]-(follower:User) " +
                                "WHERE anime.title = $title "+
                                "RETURN follower.username, follower.password, follower.logged_in," +
                                "follower.is_admin, follower.gender, follower.birthday",
                        parameters(
                                "title", anime_title
                        ));

                while(result.hasNext()){
                    org.neo4j.driver.Record r= result.next();
                    User follower = new User();
                    follower.setUsername(r.get("follower.username").asString());
                    follower.setPassword(r.get("follower.password").asString());
                    if(!r.get("follower.logged_in").isNull()){
                        follower.setLogged_in(r.get("follower.logged_in").asBoolean());
                    }
                    if(!r.get("follower.is_admin").isNull()) {
                        follower.setIs_admin(r.get("follower.is_admin").asBoolean());
                    }
                    follower.setGender(r.get("follower.gender").asString());
                    follower.setBirthday(r.get("follower.birthday").asLocalDate());
                    followers.add(follower);
                }
                return followers;
            });

        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
        return followers;
    }

    public List<Anime> getSuggestedAnime(User u){
        List<Anime> suggested_anime = new ArrayList<Anime>();


        return suggested_anime;
    }
//List<String anime_title>

}
