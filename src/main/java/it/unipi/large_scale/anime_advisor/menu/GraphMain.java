package it.unipi.large_scale.anime_advisor.menu;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerNeo4J;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.*;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;

import java.time.LocalDate;
import java.util.Set;

//da rimuovere

public class GraphMain {
    public static void main(String[] args) throws Exception {
        DbManagerNeo4J dbNeo4J = new DbManagerNeo4J();
        UserManagerNeo4J um = new UserManagerNeo4J();
        AnimeManagerNeo4J am = new AnimeManagerNeo4J();
        LocalDate birthday =  LocalDate.of(1999,11,25);


        User e= new User();
        e.setUsername("Elisa");
        e.setBirthday(birthday); // default = 1900/01/01
        e.setGender("female");
        e.setPassword("psw");
        e.setIs_admin(true); // default = false
        e.setLogged_in(true); //default = false
        User p= new User();
        p.setUsername("Pippo");
        p.setBirthday(birthday); // default = 1900/01/01
        p.setGender("male");
        p.setPassword("pippo_psw");
        p.setIs_admin(true); // default = false
        p.setLogged_in(true); //default = false

//        um.createUser(e);
//        um.deleteUser(e);
//        e.setPassword("new_psw");
//        um.updateUser(e);
//        um.getUserByUsername("Elisa");
//
//        Anime a = new Anime();
//        a.setAnime_name("Sailor Moon");
//        am.createAnime(a, dbNeo4J);
//        System.out.println(am.checkIfPresent(a, dbNeo4J));
//        am.deleteAnime(a, dbNeo4J);
        User f = new User();
        f.setUsername("Francesca");
        f.setBirthday(birthday);
        um.updateUser(f);
        um.deleteUser(e);
        um.createUser(e);
        um.createUser(p);

        um.followUser("Francesca", "Elisa");
        um.followUser("Pippo", "Elisa");
        um.followUser("Pippo", "Francesca");


//        um.unfollowUser("Francesca", "Elisa");
        um.followAnime("Francesca", "Sailor Moon");
//        um.unfollowAnime("Francesca", "Sailor Moon");
        Set<User> followed = um.viewFollowedUsers(p);
        System.out.println(followed.size());
        for (User u:followed){
            System.out.println(u.toString());
        }

        dbNeo4J.closeNeo4J();
        }

    }

