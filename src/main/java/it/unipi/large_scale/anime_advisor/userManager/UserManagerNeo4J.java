package it.unipi.large_scale.anime_advisor.userManager;

import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.User;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import java.time.LocalDate;

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
            User user;
            user = session.readTransaction(tx -> {
                Result result = tx.run( "MATCH (u:User) WHERE u.username=$username " +
                                "RETURN u.birthday, u.password, u.gender, u.logged_in, u.is_admin",
                        parameters(
                                "username", u.getUsername()
                        )
                );
                if(result.hasNext()){
                    org.neo4j.driver.Record r = result.next();
                    LocalDate birthday= r.get("u.birthday").asLocalDate();
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
        }
        if(!checkIfPresent(u)){
            System.out.println("Cannot delete, user not present in database");
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

    public void getUserByUsername(String username) {

        try(Session session= dbNeo4J.getDriver().session()){
            User user;
            user = session.readTransaction(tx -> {
                Result result = tx.run( "MATCH (u:User) WHERE u.username=$username " +
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
            System.out.println("User info:");
            System.out.println(user.toString());
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to get user due to an error");
        }
    }

    public void followAnime(String username, String anime_title, DbManagerNeo4J dbNeo4J){
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


    public void unfollowAnime(String username, String anime_title, DbManagerNeo4J dbNeo4J){
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
    public void followUser(String username, String to_follow_username, DbManagerNeo4J dbNeo4J){
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


    public void unfollowUser(String username, String to_follow_username, DbManagerNeo4J dbNeo4J){
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


}
