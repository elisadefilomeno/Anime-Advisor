package it.unipi.large_scale.anime_advisor.menu;

import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.*;
import it.unipi.large_scale.anime_advisor.userManager.UserManager;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;
import org.neo4j.driver.Session;

import java.time.LocalDate;

import static org.neo4j.driver.Values.parameters;

public class GraphMain {
    public static void main(String[] args) throws Exception {
        DbManagerNeo4J dbNeo4J = new DbManagerNeo4J();
        UserManager um = new UserManagerNeo4J();
        LocalDate birthday =  LocalDate.of(1999,1,1);

        User u= new User();
        u.setUsername("Francesca");
        u.setBirthday(birthday);
        u.setGender("female");
        u.setPassword("psw");
        u.setIs_admin(true);
        u.setLogged_in(true);

        um.createUser(u);
        dbNeo4J.closeNeo4J();
        }

    }

