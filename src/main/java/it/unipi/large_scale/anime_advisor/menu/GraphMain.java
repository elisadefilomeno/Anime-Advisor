package it.unipi.large_scale.anime_advisor.menu;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerNeo4J;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.*;
import it.unipi.large_scale.anime_advisor.userManager.UserManager;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;

import java.time.LocalDate;

//da rimuovere

public class GraphMain {
    public static void main(String[] args) throws Exception {
        DbManagerNeo4J dbNeo4J = new DbManagerNeo4J();
        UserManager um = new UserManagerNeo4J();
        AnimeManagerNeo4J am = new AnimeManagerNeo4J();
        LocalDate birthday =  LocalDate.of(1999,11,25);


//        User e= new User();
//        e.setUsername("Elisa");
//        e.setBirthday(birthday); // default = 1900/01/01
//        e.setGender("female");
//        e.setPassword("psw");
//        e.setIs_admin(true); // default = false
//        e.setLogged_in(true); //default = false
//
//        um.createUser(e);
////        um.deleteUser(e);
//        e.setPassword("new_password");
//        um.updateUser(e);
//        User elisa = um.getUserByUsername("Elisa");
//        System.out.println(elisa.toString());

        Anime a = new Anime();
        a.setAnime_name("Sailor Moon");
//        am.createAnime(a, dbNeo4J);
//        System.out.println(am.checkIfPresent(a, dbNeo4J));
        am.deleteAnime(a, dbNeo4J);


        dbNeo4J.closeNeo4J();
        }

    }

