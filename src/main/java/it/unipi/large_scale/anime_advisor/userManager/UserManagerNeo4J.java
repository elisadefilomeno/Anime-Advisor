package it.unipi.large_scale.anime_advisor.userManager;

import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.exceptions.DuplicateUserException;
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
    public boolean createUser(User u) throws Exception {
        if(checkIfPresent(u)) {
            throw new DuplicateUserException();
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
            return true;

        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }

    }

    @Override
    public User getUserByUsername(String username) {

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
            return user;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateUser(User u) {
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
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteUser(User u) {
        if(u.getUsername()==null){
            System.out.println("Username not inserted, unable to delete");
            return false;
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
            return true;

        }catch(Exception ex){
            ex.printStackTrace();
            return false;
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

}
