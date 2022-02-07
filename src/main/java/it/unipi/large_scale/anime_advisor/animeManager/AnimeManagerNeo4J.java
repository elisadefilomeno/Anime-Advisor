package it.unipi.large_scale.anime_advisor.animeManager;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.User;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.util.*;

import static org.neo4j.driver.Values.parameters;

public class AnimeManagerNeo4J{
    private final DbManagerNeo4J dbNeo4J;

    public AnimeManagerNeo4J(DbManagerNeo4J dbNeo4J) {
        this.dbNeo4J = dbNeo4J;
    }

    public void createAnime(String anime_title) {
        if(checkIfPresent(anime_title)){
            System.out.println("Anime already present\n");
        return;
        }

        try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MERGE (a:Anime {title: $title})",
                        parameters(
                                "title", anime_title
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
        // just read the anime title, consider to delete this function
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

    public void updateAnimeTitle(String old_title, String new_title){
        if(checkIfPresent(new_title)){
            System.out.println("Cannot update, new title is already an existing anime\n");
            return;
        }
        try(Session session= dbNeo4J.getDriver().session()){

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ( "MATCH (a:Anime) WHERE a.title = $title " +
                                "SET a.title=$new_title",
                        parameters(
                                "title", old_title,
                                "new_title", new_title
                        )
                );
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to update anime due to an error");
        }
        System.out.println("Anime correctly updated");


    }

    public void deleteAnime(String anime_title) {
        if(anime_title==null){
            System.out.println("Anime title not inserted, unable to delete");
            return;
        }
        if(!checkIfPresent(anime_title)){
            System.out.println("Cannot delete, anime not present in database");
            return;
        }

        try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (a:Anime) WHERE a.title=$title DETACH DELETE a",
                        parameters(
                                "title", anime_title
                        )
                );
                return null;
            });

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    public boolean checkIfPresent(String anime_title){
        if(anime_title==null){
            System.out.println("Anime title not inserted");
            return false;
        }

        try(Session session= dbNeo4J.getDriver().session()){
            int anime_count;
            anime_count = session.readTransaction(tx -> {
                Result result = tx.run( "MATCH (a:Anime) WHERE a.title=$title RETURN count(a) as anime_count",
                        parameters(
                                "title", anime_title
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


    //ANALYTICS

    public Set<String> getVerySuggestedAnime(String username){
        // set can't contain duplicate title strings

        Set<String> very_sugggested_anime = new HashSet<String>();

        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx -> {
                Result result = tx.run (
                        "MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:FOLLOWS]->(a:Anime)<-[:REFERRED_TO]-(r:Review)<-[:CREATED]-(u2)" +
                                " WHERE NOT (u1)-[:FOLLOWS]->(a) AND u1.username = $username " +
                                "RETURN a.title LIMIT 20",
                        parameters(
                                "username", username
                        ));
                while(result.hasNext()){
                    org.neo4j.driver.Record r= result.next();
                    if(!r.get("a.title").isNull()){
                        String anime_title=r.get("a.title").asString();
                        very_sugggested_anime.add(anime_title);
                    }

                }
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to get very suggested anime due to an error");
        }
        return very_sugggested_anime;
    }

    public Set<String> getSuggestedAnimeMediumPriority(String username){
        // set can't contain duplicate title strings
        Set<String> sugggested_anime = new HashSet<String>();

        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx -> {
                Result result = tx.run (
                        "MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:FOLLOWS]->(a:Anime)" +
                                " WHERE NOT (u1)-[:FOLLOWS]->(a) AND u1.username = $username " +
                                "RETURN a.title LIMIT 20",
                        parameters(
                                "username", username
                        ));
                while(result.hasNext()){
                    org.neo4j.driver.Record r= result.next();
                    if(!r.get("a.title").isNull()){
                        String anime_title=r.get("a.title").asString();
                        sugggested_anime.add(anime_title);
                    }

                }
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to get suggested anime due to an error");
        }
        return sugggested_anime;
    }

    public Set<String> getCommentedByFriendAnime(String username){
        // set can't contain duplicate title strings

        Set<String> sugggested_anime = new HashSet<String>();

        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx -> {
                Result result = tx.run (
                        "MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:CREATED]->(r:Review)-[:REFERRED_TO]->(a:Anime)" +
                                " WHERE NOT (u1)-[:FOLLOWS]->(a) AND u1.username = $username " +
                                "RETURN a.title LIMIT 20",
                        parameters(
                                "username", username
                        ));
                while(result.hasNext()){
                    org.neo4j.driver.Record r= result.next();
                    if(!r.get("a.title").isNull()){
                        String anime_title=r.get("a.title").asString();
                        sugggested_anime.add(anime_title);
                    }

                }
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to get suggested anime due to an error");
        }
        return sugggested_anime;
    }

    public List<String> getNSuggestedAnime(String username, int number_of_suggested){
        Set<String> verySuggestedAnime = getVerySuggestedAnime(username);
        List<String> suggested_anime = new ArrayList<String>(verySuggestedAnime);

        if(suggested_anime.size()<number_of_suggested){
            Set<String> medium_suggested_anime = getSuggestedAnimeMediumPriority(username);
            for(String title: medium_suggested_anime){
                if(!suggested_anime.contains(title) && suggested_anime.size()<number_of_suggested){
                    suggested_anime.add(title);
                }
            }
        }
        if(suggested_anime.size()<number_of_suggested){
            Set<String> commented_by_friend_anime = getCommentedByFriendAnime(username);
            for(String title: commented_by_friend_anime){
                if(!suggested_anime.contains(title) && suggested_anime.size()<number_of_suggested){
                    suggested_anime.add(title);
                }
            }
        }

        return suggested_anime;
    }
}
