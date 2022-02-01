package it.unipi.large_scale.anime_advisor.userManager;

import it.unipi.large_scale.anime_advisor.dbManager.DbManager;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.User;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import static org.neo4j.driver.Values.parameters;

public class UserManagerNeo4J implements UserManager{
    DbManagerNeo4J dbNeo4J;

    public UserManagerNeo4J() {
        this.dbNeo4J = new DbManagerNeo4J();
    }

    @Override
    public void createUser(User u) {

    }

    @Override
    public void readUser(User u) {

    }

    @Override
    public void updateUser(User u) {

    }

    @Override
    public boolean deleteUser(User u) {
        if(u.getUsername()==null){
            System.out.println("Username not inserted");
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

}
