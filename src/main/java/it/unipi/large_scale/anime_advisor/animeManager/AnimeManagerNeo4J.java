package it.unipi.large_scale.anime_advisor.animeManager;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.Review;
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

    public boolean createAnime(String anime_title) {
        if(checkIfPresent(anime_title)){
            System.out.println("Anime already present\n");
        return false;
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
            System.out.println("Anime inserted correctly\n");

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to create node due to an error");
            return false;
        }
        return true;
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

    public boolean deleteAnime(String anime_title) {
        if(anime_title==null){
            System.out.println("Anime title not inserted, unable to delete");
            return false;
        }
        if(!checkIfPresent(anime_title)){
            System.out.println("Cannot delete, anime not present in database");
            return false;
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
            System.out.println("Anime deleted correctly\n");


        }catch(Exception ex){
            ex.printStackTrace();
        }
        return true;
    }

    public void likeAnime(String username, String anime_title){
        if(!checkIfPresent(anime_title)){
            System.out.println("Anime not present in the database, cannot like it");
            return;
        }
        if(checkIfAlreadyLiked(username,anime_title)){
            System.out.println("You arleady like this anime!");
            return;
        }
        try(Session session= dbNeo4J.getDriver().session()){

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ("match (u:User) where u.username= $username " +
                                "match (a:Anime) where a.title=$title " +
                                "merge (u)-[:LIKE]->(a)",
                        parameters(
                                "title", anime_title,
                                "username", username
                        ));
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("unable to like anime due to an error");
        }
        System.out.println("Correctly liked anime");
    }

    public void unlikeAnime(String username, String anime_title){
        if(!checkIfPresent(anime_title)){
            System.out.println("Anime not present in the database, cannot unlike it");
            return;
        }
        if(!checkIfAlreadyLiked(username,anime_title)){
            System.out.println("You didn't like this anime!");
            return;
        }
        try(Session session= dbNeo4J.getDriver().session()){

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ("match (u:User {username: $username}) -[f:LIKE]-> (b:Anime {title:$title}) " +
                                "delete f",
                        parameters(
                                "title", anime_title,
                                "username", username
                        ));
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("unable to unlike anime due to an error");
        }
        System.out.println("Correctly unliked anime");
    }

    public boolean checkIfAlreadyLiked(String username, String anime_title) {
        if (anime_title == null || username == null) {
            System.out.println("Invalid names");
            return false;
        }

        try (Session session = dbNeo4J.getDriver().session()) {
            int count;
            count = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[:LIKE]->(a:Anime) " +
                                "WHERE u.username=$username and  a.title=$title " +
                                "RETURN count(a) as count",
                        parameters(
                                "username", username,
                                "title", anime_title
                        )
                );
                if (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();
                    return (r.get("count").asInt());
                }

                return null;
            });
            return (count > 0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
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
                else{
                    System.out.println("ERROR!");
                    return null;

                }


            });
            return (anime_count>0);
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public Set<User> getLikedUsers(String anime_title){
        Set<User> likers = new HashSet<User>();
        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx->{
                Result result = tx.run("MATCH (anime:Anime)<-[f:LIKE]-(liker:User) " +
                                "WHERE anime.title = $title "+
                                "RETURN liker.username, liker.password, liker.logged_in," +
                                "liker.is_admin, liker.gender",
                        parameters(
                                "title", anime_title
                        ));

                while(result.hasNext()){
                    org.neo4j.driver.Record r= result.next();
                    User liker = new User();
                    liker.setUsername(r.get("liker.username").asString());
                    liker.setPassword(r.get("liker.password").asString());
                    if(!r.get("liker.logged_in").isNull()){
                        liker.setLogged_in(r.get("liker.logged_in").asBoolean());
                    }
                    if(!r.get("liker.is_admin").isNull()) {
                        liker.setIs_admin(r.get("liker.is_admin").asBoolean());
                    }
                    liker.setGender(r.get("liker.gender").asString());
                    likers.add(liker);
                }
                return likers;
            });

        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
        return likers;
    }

    public Anime getAnimeFromReview(Review rev){
        Anime a ;
        try(Session session= dbNeo4J.getDriver().session()) {

            a = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (r:Review{title:$title})-[t:REFERRED_TO]->(a:Anime) "+
                                "RETURN a.title ",

                        parameters(
                        "title", rev.getTitle()
                        )

                );
                Anime anime = new Anime();
                if (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();

                    anime.setAnime_name(r.get("a.title").asString());

                    return anime;
                }
                else{
                    System.out.println("Anime not found");
                    return null;
                }

            });

        }
        return a;

    }

    public int getNFolloerAnime (String title){
        int numberFollow;
        try(Session session= dbNeo4J.getDriver().session()) {

            numberFollow = session.readTransaction(tx -> {
                Result result = tx.run("MATCH p=(a:Anime{title:$title})<-[:LIKE]-(u:User) "+
                                "RETURN COUNT(p) as n_follow",
                        parameters(
                                "title", title
                        )
                );
                int n;
                if(result.hasNext()){
                    org.neo4j.driver.Record r = result.next();
                    n = r.get("n_follow").asInt();
                    return n;
                }
                else{
                    System.out.println("ERROR");
                    return null;
                }
            });

        }
        return numberFollow;
    }
    //ANALYTICS
    public NavigableMap<String, Integer> getTop10MostLikedAnime(){
        TreeMap<String, Integer> n_most_liked_anime_map = new TreeMap<>();
        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx -> {
                Result result = tx.run (
                        "MATCH(u:User)-[f:LIKE]->(a:Anime) " +
                                "WITH a, count(u) AS count " +
                                "ORDER BY count DESC " +
                                "RETURN a.title, count LIMIT 10"
                );
                while(result.hasNext()){
                    org.neo4j.driver.Record r= result.next();
                    if(!r.get("a.title").isNull()){
                        String anime_title=r.get("a.title").asString();
                        int n_likers = r.get("count").asInt();
                        n_most_liked_anime_map.put(anime_title, n_likers);
                    };
                }
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to get very suggested anime due to an error");
        }
        return n_most_liked_anime_map;
    }


    public Set<String> getVerySuggestedAnime(String username){
        // They have the highest priority, given a user u1 if u1 is following user u2
        // and U2 liked an anime a and has also writted on a, then a is very suggested to u1.

        Set<String> very_sugggested_anime = new HashSet<String>();

        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx -> {
                Result result = tx.run (
                        "MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:LIKE]->(a:Anime)<-[:REFERRED_TO]-(r:Review)<-[:WRITE]-(u2)" +
                                " WHERE NOT (u1)-[:LIKE]->(a) AND u1.username = $username " +
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
        // They have the second priority level, if a user u1 is following user u2 and u2 liked
        // an anime a, then a is suggested to u1.

        Set<String> sugggested_anime = new HashSet<String>();

        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx -> {
                Result result = tx.run (
                        "MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:LIKE]->(a:Anime)" +
                                " WHERE NOT (u1)-[:LIKE]->(a) AND u1.username = $username " +
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
        // They have the lowest priority level, if a user u1 follows a user u2
        // who has written a review on an anime a, then a is suggested as "commented by a friend" at u1.

        Set<String> sugggested_anime = new HashSet<String>();

        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx -> {
                Result result = tx.run (
                        "MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:WRITE]->(r:Review)-[:REFERRED_TO]->(a:Anime)" +
                                " WHERE NOT (u1)-[:LIKE]->(a) AND u1.username = $username " +
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
        // suggest N Anime to a user considering three levels of priorities to anime

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

    public void likeAnime (User u,String titleAnime){

        if(checkIfIsAlredyLike(u,titleAnime)){
            System.out.println("User alredy follows this anime\n");
            return;
        }

        try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:User{ username: $username}), (a:Anime{title: $title})" +
                                " MERGE (u)-[:LIKE]->(a)",
                        parameters(
                                "title", titleAnime,
                                "username",u.getUsername()
                        )
                );
                return null;
            });
            System.out.println("User follows the anime\n");

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Error");

        }

    }

    public boolean checkIfIsAlredyLike(User u, String titleAnime) {


        try (Session session = dbNeo4J.getDriver().session()) {
            int count;
            count = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[p:LIKE]->(a:Anime) " +
                                "WHERE u.username=$user AND  a.title=$title " +
                                "RETURN count(p) as count",
                        parameters(
                                "user", u.getUsername(),
                                "title", titleAnime
                        )
                );
                if (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();
                    return (r.get("count").asInt());
                }
                else{
                    System.out.println("Error!");
                    return null;
                }


            });
            return (count > 0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void unlikeAnime (User u,String titleAnime){

        if(!checkIfIsAlredyLike(u,titleAnime)){
            System.out.println("Anime is not followed \n");
            return;
        }

        try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH p=(u:User{username:$username})-[r:LIKE]->(a:Anime{title:$title}) DELETE r",
                        parameters(
                                "title", titleAnime,
                                "username",u.getUsername()
                        )
                );
                return null;
            });
            System.out.println("Anime is not follow \n");

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Error");

        }

    }

    public ArrayList<Anime> mostLikedAnime(){
        ArrayList<Anime> listAnime ;
        try(Session session= dbNeo4J.getDriver().session()) {

            listAnime = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (a:Anime)<-[t:LIKE]-(u:User) RETURN "+
                                "Count(t),a.title Order By count(t) DESC LIMIT 10",
                        parameters()
                );
                ArrayList<Anime> list = new ArrayList<>();
                while (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();

                    Anime a = new Anime() ;
                    a.setAnime_name(r.get("a.title").asString());
                    list.add(a);
                }
                return list;
            });

        }
        return listAnime;

    }

    public ArrayList<Anime> mostReviewedAnime(){
        ArrayList<Anime> listAnime ;
        try(Session session= dbNeo4J.getDriver().session()) {

            listAnime = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (a:Anime)<-[t:REFERRED_TO]-(r:Review) RETURN "+
                                "Count(t),a.title Order By COUNT(t) DESC LIMIT 10",
                        parameters()
                );
                ArrayList<Anime> list = new ArrayList<>();
                while (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();

                    Anime a = new Anime() ;
                    a.setAnime_name(r.get("a.title").asString());
                    list.add(a);
                }
                return list;
            });

        }
        return listAnime;

    }


}
