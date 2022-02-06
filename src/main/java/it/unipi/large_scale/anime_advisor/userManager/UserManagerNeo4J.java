package it.unipi.large_scale.anime_advisor.userManager;

import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.Review;
import it.unipi.large_scale.anime_advisor.entity.User;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.neo4j.driver.Values.parameters;

public class UserManagerNeo4J implements UserManager{
    DbManagerNeo4J dbNeo4J;

    public UserManagerNeo4J() {
        this.dbNeo4J = new DbManagerNeo4J();
    }

    @Override
    public void createUser(User u) {
        if(checkIfPresent(u)) {
            System.out.println("User already present");
            return;
        }

        try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MERGE (u:User {username: $username, birthday: $birthday, " +
                                "password: $password, gender: $gender, " +
                                "logged_in: $logged_in, is_admin: $logged_in})",
                        parameters(
                                "username", u.getUsername(),
                                "birthday", u.getBirthday(),
                                "password", u.getPassword(),
                                "gender", u.getGender(),
                                "logged_in", u.getLogged_in(),
                                "is_admin", u.getIs_admin()
                        )
                );
                return null;
            });
            System.out.println("User inserted correctly\n");

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to create node due to an error");
        }

    }

    @Override
    public void readUser(User u) {
        try(Session session= dbNeo4J.getDriver().session()){
            User user = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User) WHERE u.username=$username " +
                                "RETURN u.birthday, u.password, u.gender, u.logged_in, u.is_admin",
                        parameters(
                                "username", u.getUsername()
                        )
                );
                if (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();
                    LocalDate birthday = r.get("u.birthday").asLocalDate();
                    String password = r.get("u.password").asString();
                    String gender = r.get("u.gender").asString();
                    boolean logged_in = r.get("u.logged_in").asBoolean();
                    boolean is_admin = r.get("u.is_admin").asBoolean();
                    User read_user = new User();
                    read_user.setUsername(u.getUsername());
                    read_user.setBirthday(birthday);
                    read_user.setGender(gender);
                    read_user.setPassword(password);
                    read_user.setIs_admin(is_admin);
                    read_user.setLogged_in(logged_in);
                    return read_user;
                }

                return null;
            });
            System.out.println("User info:");
            System.out.println(user.toString());
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to get user due to an error");
        }
    }

    @Override
    public void updateUser(User u) {
        try(Session session= dbNeo4J.getDriver().session()){

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ( "MATCH (u:User) WHERE u.username = $username " +
                                "SET u.birthday=$birthday, u.password=$password, u.gender=$gender, " +
                                "u.logged_in=$logged_in, u.is_admin=$is_admin",
                        parameters(
                                "username", u.getUsername(),
                                "birthday", u.getBirthday(),
                                "password", u.getPassword(),
                                "gender", u.getGender(),
                                "logged_in", u.getLogged_in(),
                                "is_admin", u.getIs_admin()
                        )
                );
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to update user due to an error");
        }
        System.out.println("User correctly updated");

    }

    @Override
    public void deleteUser(User u) {
        if(u.getUsername()==null){
            System.out.println("Username not inserted, unable to delete");
            return;
        }
        if(!checkIfPresent(u)){
            System.out.println("Cannot delete, user not present in database");
            return;
        }

        try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:User) WHERE u.username=$username DETACH DELETE u",
                        parameters(
                                "username", u.getUsername()
                        )
                );
                return null;
            });
            System.out.println("User deleted correctly\n");


        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to delete user due to an error\n");
        }
    }

    public boolean checkIfPresent(User u){
        if(u.getUsername()==null){
            System.out.println("Username not inserted");
            return false;
        }

        try(Session session= dbNeo4J.getDriver().session()){
            int user_count;
            user_count = session.readTransaction(tx -> {
                Result result = tx.run( "MATCH (u:User) WHERE u.username=$username RETURN count(u) as user_count",
                        parameters(
                                "username", u.getUsername()
                        )
                );
                if(result.hasNext()){
                    org.neo4j.driver.Record r = result.next();
                    return (r.get("user_count").asInt());
                }

                return null;
            });
            return (user_count>0);
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public User getUserByUsername(String username) {

        try(Session session= dbNeo4J.getDriver().session()){
            User user;
            user = session.readTransaction(tx -> {
                org.neo4j.driver.Result result = tx.run( "MATCH (u:User) WHERE u.username=$username " +
                                "RETURN u.birthday, u.password, u.gender, u.logged_in, u.is_admin",
                        parameters(
                                "username", username
                        )
                );
                if(result.hasNext()){
                    org.neo4j.driver.Record r = result.next();
                    LocalDate birthday= r.get("u.birthday").asLocalDate();
                    String password = r.get("u.password").asString();
                    String gender = r.get("u.gender").asString();
                    boolean logged_in = r.get("u.logged_in").asBoolean();
                    boolean is_admin = r.get("u.is_admin").asBoolean();
                    User u = new User();
                    u.setUsername(username);
                    u.setBirthday(birthday);
                    u.setGender(gender);
                    u.setPassword(password);
                    u.setIs_admin(is_admin);
                    u.setLogged_in(logged_in);
                    return u;
                }

                return null;
            });
            return user;
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to get user due to an error");
        }
        return null;
    }

    public void followAnime(String username, String anime_title){
        try(Session session= dbNeo4J.getDriver().session()){

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ("match (u:User) where u.username= $username " +
                                "match (a:Anime) where a.title=$title " +
                                "merge (u)-[:FOLLOWS]->(a)",
                        parameters(
                                "title", anime_title,
                                "username", username
                        ));
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("unable to follow anime due to an error");
        }
        System.out.println("Correctly followed anime");
    }

    public void unfollowAnime(String username, String anime_title){
        try(Session session= dbNeo4J.getDriver().session()){

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ("match (u:User {username: $username}) -[f:FOLLOWS]-> (b:Anime {title:$title}) " +
                                "delete f",
                        parameters(
                                "title", anime_title,
                                "username", username
                        ));
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("unable to unfollow anime due to an error");
        }
        System.out.println("Correctly unfollowed anime");
    }

    public void followUser(String username, String to_follow_username){
        try(Session session= dbNeo4J.getDriver().session()){

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ("match (u:User) where u.username= $username " +
                                "match (t:User) where t.username=$to_follow_username " +
                                "merge (u)-[:FOLLOWS]->(t)",
                        parameters(
                                "to_follow_username", to_follow_username,
                                "username", username
                        ));
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("unable to follow user due to an error");
        }
        System.out.println("Correctly followed user");
    }

    public void unfollowUser(String username, String to_follow_username){
        try(Session session= dbNeo4J.getDriver().session()){

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ("match (u:User {username: $username}) -[f:FOLLOWS]-> (t:User {username:$to_follow_username}) " +
                                "delete f",
                        parameters(
                                "to_follow_username", to_follow_username,
                                "username", username
                        ));
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("unable to unfollow user due to an error");
        }
        System.out.println("Correctly unfollowed user");
    }

    public Set<User> getFollowedUsers(String username){
        Set<User> followed_users = new HashSet<User>();
        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx->{
                Result result = tx.run("MATCH (followed:User)<-[f:FOLLOWS]-(user:User) " +
                        "WHERE user.username = $username "+
                        "RETURN followed.username, followed.password, followed.logged_in, " +
                                "followed.is_admin, followed.gender, followed.birthday",
                        parameters(
                        "username", username
                ));

                while(result.hasNext()){
                    org.neo4j.driver.Record r= result.next();
                    User followed_user = new User();
                    followed_user.setUsername(r.get("followed.username").asString());
                    followed_user.setPassword(r.get("followed.password").asString());
                    if(!r.get("followed.logged_in").isNull()){
                        followed_user.setLogged_in(r.get("followed.logged_in").asBoolean());
                    }
                    if(!r.get("followed.is_admin").isNull()) {
                        followed_user.setIs_admin(r.get("followed.is_admin").asBoolean());
                    }
                    followed_user.setGender(r.get("followed.gender").asString());
                    followed_user.setBirthday(r.get("followed.birthday").asLocalDate());
                    followed_users.add(followed_user);
                }
                return followed_users;
            });

        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
        return followed_users;
    }

    public Set<Anime> getFollowedAnime(String username){
        Set<Anime> followed_animes = new HashSet<Anime>();
        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx->{
                Result result = tx.run("MATCH (followed:Anime)<-[f:FOLLOWS]-(user:User) " +
                                "WHERE user.username = $username "+
                                "RETURN followed.title",
                        parameters(
                                "username", username
                        ));

                while(result.hasNext()){
                    org.neo4j.driver.Record r= result.next();
                    Anime anime = new Anime();
                    anime.setAnime_name(r.get("followed.title").asString());
                    followed_animes.add(anime);
                }
                return followed_animes;
            });

        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
        return followed_animes;
    }

    public Set<Review> getCreatedReviews(String username){
        Set<Review> reviews = new HashSet<Review>();
        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx->{
                Result result = tx.run("MATCH (r:Review)<-[f:CREATED]-(user:User) " +
                                "WHERE user.username = $username "+
                                "RETURN r.id, r.score, r.text",
                        parameters(
                                "username", username
                        ));

                while(result.hasNext()){
                    org.neo4j.driver.Record r= result.next();
                    Review review = new Review();
                    review.setId(r.get("r.id").asInt());
                    review.setText(r.get("r.text").asString());
                    review.setScore(r.get("r.score").asInt());
                    reviews.add(review);
                }
                return reviews;
            });

        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
        return reviews;
    }

    public Set<String> getVerySuggestedUsers(String username){
        // set can't contain duplicate username strings

        Set<String> very_sugggested_users = new HashSet<String>();

        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx -> {
                Result result = tx.run (
                        "MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:FOLLOWS]->(u3:User)" +
                                " WHERE NOT (u1)-[:FOLLOWS]->(u3) AND u1.username = $username " +
                                "RETURN u3.username LIMIT 20",
                        parameters(
                                "username", username
                        ));
                while(result.hasNext()){
                    org.neo4j.driver.Record r= result.next();
                    if(!r.get("u3.username").isNull()){
                        String suggested_username=r.get("u3.username").asString();
                        very_sugggested_users.add(suggested_username);
                    }

                }
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to get very suggested users due to an error");
        }
        return very_sugggested_users;
    }

    public Set<String> getSuggestedUsersLowPriority(String username){
        // set can't contain duplicate title strings
        Set<String> sugggested_users = new HashSet<String>();

        try(Session session= dbNeo4J.getDriver().session()){

            session.readTransaction(tx -> {
                Result result = tx.run (
                        "MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:FOLLOWS]->(u3:User)-[:FOLLOWS]->(u4:User)" +
                                " WHERE NOT (u1)-[:FOLLOWS]->(u4) AND u1.username = $username " +
                                "RETURN u4.username LIMIT 20",
                        parameters(
                                "username", username
                        ));
                while(result.hasNext()){
                    org.neo4j.driver.Record r= result.next();
                    if(!r.get("u4.username").isNull()){
                        String suggested_username=r.get("u4.username").asString();
                        sugggested_users.add(suggested_username);
                    }

                }
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to get suggested anime due to an error");
        }
        return sugggested_users;
    }

    public List<String> getNSuggestedUsers(String username, int number_of_suggested){
        Set<String> verySuggestedUsers = getVerySuggestedUsers(username);
        List<String> suggested_users = new ArrayList<String>(verySuggestedUsers);

        if(suggested_users.size()<number_of_suggested){
            Set<String> low_suggested_users = getSuggestedUsersLowPriority(username);
            for(String title: low_suggested_users){
                if(!suggested_users.contains(title) && suggested_users.size()<number_of_suggested){
                    suggested_users.add(title);
                }
            }
        }

        return suggested_users;
    }

}
