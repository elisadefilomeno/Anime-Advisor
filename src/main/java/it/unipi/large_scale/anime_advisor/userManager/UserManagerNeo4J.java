package it.unipi.large_scale.anime_advisor.userManager;

import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.User;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.util.*;

import static it.unipi.large_scale.anime_advisor.menu.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.menu.ConsoleColors.RESET;
import static org.neo4j.driver.Values.parameters;

public class UserManagerNeo4J {
    static DbManagerNeo4J dbNeo4J;

    public UserManagerNeo4J() {
        this.dbNeo4J = new DbManagerNeo4J();
    }

    public void createUser(User u) {
        //Controllo gia presente anche in SignIn
        if(checkIfPresent(u.getUsername())) {
            System.out.println("User already present");
        }

         try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MERGE (u:User {username: $username, " +
                                "password: $password, gender: $gender, " +
                                "logged_in: $logged_in, is_admin: $logged_in})",
                        parameters(
                                "username", u.getUsername(),
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

    public void readUser(User u) {
        try (Session session = dbNeo4J.getDriver().session()) {
            User user;
            user = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User) WHERE u.username=$username " +
                                "RETURN u.birthday, u.password, u.gender, u.logged_in, u.is_admin",
                        parameters(
                                "username", u.getUsername()
                        )
                );
                User read_user = new User();
                if (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();

                    String password = r.get("u.password").asString();
                    String gender = r.get("u.gender").asString();
                    boolean logged_in = r.get("u.logged_in").asBoolean();
                    boolean is_admin = r.get("u.is_admin").asBoolean();


                    read_user.setUsername(u.getUsername());
                    read_user.setGender(gender);
                    read_user.setPassword(password);
                    read_user.setIs_admin(is_admin);
                    read_user.setLogged_in(logged_in);
                }
                return read_user;
            });
            System.out.println("User info:");
            System.out.println(user.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Unable to get user due to an error");
        }

    }

    public void updateUser(User u) {
        try (Session session = dbNeo4J.getDriver().session()) {

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (u:User) WHERE u.username = $username " +
                                "SET u.password=$password, u.gender=$gender, " +
                                "u.logged_in=$logged_in, u.is_admin=$is_admin",
                        parameters(
                                "username", u.getUsername(),
                                "password", u.getPassword(),
                                "gender", u.getGender(),
                                "logged_in", u.getLogged_in(),
                                "is_admin", u.getIs_admin()
                        )
                );
                return null;
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Unable to update user due to an error");
        }
        System.out.println("User correctly updated");

    }


    public void deleteUser(User u) {
        if (u.getUsername() == null) {
            System.out.println("Username not inserted, unable to delete");
        }
        if (!checkIfPresent(u.getUsername())) {
            System.out.println("Cannot delete, user not present in database");
        }

        try (Session session = dbNeo4J.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (u:User) WHERE u.username=$username DETACH DELETE u",
                        parameters(
                                "username", u.getUsername()
                        )
                );
                return null;
            });
            System.out.println("User deleted correctly\n");


        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Unable to delete user due to an error\n");
        }
    }

    //Funzione che controlla tramite il nome se l'utente è presente o meno nel db
    public boolean checkIfPresent(String name) {
        if (name == null) {
            System.out.println("Username not inserted");
            return false;
        }

        try (Session session = dbNeo4J.getDriver().session()) {
            int user_count;
            user_count = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User) WHERE u.username=$username RETURN count(u) as user_count",
                        parameters(
                                "username", name
                        )
                );
                if (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();
                    return (r.get("user_count").asInt());
                }

                return null;
            });
            return (user_count > 0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public User getUserByUsername(String username) {
        User user=new User();
        try (Session session = dbNeo4J.getDriver().session()) {

            user = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User) WHERE u.username=$username " +
                                "RETURN  u.password, u.gender, u.logged_in, u.is_admin",
                        parameters(
                                "username", username
                        )
                );
                if (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();
                    String password = r.get("u.password").asString();
                    String gender = r.get("u.gender").asString();
                    boolean logged_in = r.get("u.logged_in").asBoolean();
                    boolean is_admin = r.get("u.is_admin").asBoolean();
                    User u = new User();
                    u.setUsername(username);
                    u.setGender(gender);
                    u.setPassword(password);
                    u.setIs_admin(is_admin);
                    u.setLogged_in(logged_in);
                    return u;
                }
                else
                   return null;
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Unable to get user due to an error");

        }

        return user;
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

    //Check LogIn is correct
    public boolean checkLogIn(String name,String psw){

        //Controlli per verificare se nome o password sono null
        if (name == null) {
            System.out.println("Username not inserted");
            return false;
        }
        else if (psw == null) {
            System.out.println("Username not inserted");
            return false;
        }

        try (Session session = dbNeo4J.getDriver().session()) {
            int user_count;
            user_count = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User) WHERE u.username=$username AND u.password=$password RETURN count(u) as user_count",
                        parameters(
                                "username", name,
                                "password",psw
                        )
                );
                if (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();
                    return (r.get("user_count").asInt());
                }
                else
                    return null;
            });
            if (user_count > 0)
                return true;
            else
                return false;

        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //Set della variabile che verifica se un utente è loggato o no
    public void updateLog(boolean log,String username) {
        try (Session session = dbNeo4J.getDriver().session()) {

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (u:User) WHERE u.username = $username " +
                                "SET  u.logged_in=$logged_in",
                        parameters(
                                "username",username,
                                "logged_in", log
                        )
                );
                return null;
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Unable to update user due to an error");
        }
        System.out.println("User correctly updated");

    }

    //SignIn
    public  void signUp(){
        UserManagerNeo4J um = new UserManagerNeo4J();
        User u = new User();
        User u_check = new User ();
        String name_user,password_user,gender;
        Scanner sc = new Scanner(System.in);

        System.out.println("Register new User, insert name/gender/password ");
        System.out.println("Enter username: ");

        name_user = sc.nextLine();
        int temp=1;
        while(temp==1){
            if(um.checkIfPresent(name_user)){
                System.out.println("Username is alredy present!");
                System.out.println("Insert new username:");
                continue;
            }

            if (name_user== null || name_user=="") {
                System.out.println("Invalid Username !!! \n");
                System.out.println("Re-insert the username or press 0 to go back:");
                name_user = sc.nextLine();
                continue;

            }
           else {
                if(name_user.equals("0")){
                    return;
                }
               temp=0;
            }

        }

        //Inserire funzione che controlla se è presente gia l'utente
        System.out.println("Insert your password: ");
        password_user = sc.nextLine();
        int temp_psw=1;
        while(temp_psw==1){

            if (password_user== null || password_user=="") {
                System.out.println("Invalid Password !!! \n");
                System.out.println("Re-insert the password or press 0 to go back:");
                password_user = sc.nextLine();
                continue;

            }
            else {
                if(password_user.equals("0")){
                    return;
                }
                temp_psw=0;
            }

        }
        u.setUsername(name_user);
        u.setPassword(password_user);
        int tempg=0;
        while(tempg==0) {
            System.out.println("Insert gender:");
            System.out.println("1) Female");
            System.out.println("2) Male");
            System.out.println("3) Other");
            System.out.println("4) I prefer not to specify");
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println("Write your command here:");

            try {
                int gender_case = Integer.valueOf(sc.nextLine());

                switch ((gender_case)) {
                    case 1: {
                        u.setGender("Female");
                        tempg=1;
                        break;
                    }
                    case 2: {
                        u.setGender("Male");
                        tempg=1;
                        break;
                    }

                    case 3: {
                        u.setGender("Other");
                        tempg=1;
                        break;
                    }

                    case 4: {
                        u.setGender("Not specified");
                        tempg=1;
                        break;
                    }
                    default: {
                        System.out.println("Invalid option\n");
                        continue;

                    }
                }
            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");
            }
        }

        //Default set log_in and is_admin to false
        u.setLogged_in(false);
        u.setIs_admin(false);
        um.createUser(u);
        System.out.println("Register Successfully");



    }

    //LogIn
    public User logIn(){
        UserManagerNeo4J um = new UserManagerNeo4J();
        String userName,passwordUser;
        User logUser= new User();
        Scanner sc = new Scanner(System.in);

        System.out.println("LogIn... Insert username and password... ");
        System.out.println("Enter username: ");
        userName = sc.nextLine();

        System.out.println("Insert your password: ");
        passwordUser = sc.nextLine();

        if( checkLogIn(userName,passwordUser)){
            System.out.println("LogIn Successfully!!! ");
            //Salvo l'user in una variabile
            logUser= um.getUserByUsername(userName);
            //Set log_in true
            logUser.setLogged_in(true);
            um.updateLog(logUser.getLogged_in(),logUser.getUsername());

        }
        else{
            System.out.println("Username or password wrong!!! ");
            return null;
        }
        return logUser;

    }

    //LogOut
    public void logOut(User u){
        UserManagerNeo4J um = new UserManagerNeo4J();

        if(u.getLogged_in()){
            u.setLogged_in(false);
            um.updateLog(u.getLogged_in(),u.getUsername());

        }
        else{
            System.out.println("LogOut is alredy been");
        }
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
