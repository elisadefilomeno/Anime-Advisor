package main;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerNeo4J;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.*;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;

//da rimuovere

public class GraphMain {

    public static void main(String[] args) throws Exception {
        DbManagerNeo4J dbNeo4J = new DbManagerNeo4J();
        UserManagerNeo4J um = new UserManagerNeo4J(dbNeo4J);
        AnimeManagerNeo4J am = new AnimeManagerNeo4J(dbNeo4J);
//        LocalDate birthday =  LocalDate.of(1999,11,25);
//
//
        User e= new User();
        e.setUsername("elisa");
//        e.setBirthday(birthday); // default = 1900/01/01
        e.setGender("Female");
        e.setPassword("psw");
        e.setIs_admin(true); // default = false
        e.setLogged_in(true); //default = false
//        User p= new User();
//        p.setUsername("Pippo");
//        p.setBirthday(birthday); // default = 1900/01/01
//        p.setGender("male");
//        p.setPassword("pippo_psw");
//        p.setIs_admin(true); // default = false
//        p.setLogged_in(true); //default = false
//
//        um.createUser(e);
//        um.deleteUser(e);
//        e.setPassword("new_psw");
//        um.updateUser(e);
//        um.getUserByUsername("Elisa");
//
//        Anime a = new Anime();
//        a.setAnime_name("Naruto");
//        am.createAnime(a.getAnime_name());
//        Anime b = new Anime();
//        b.setAnime_name("Heidi");
//        am.createAnime(b.getAnime_name());
//        Anime c = new Anime();
//        c.setAnime_name("Sailor Moon");
//        am.createAnime(c.getAnime_name());
//        System.out.println(am.checkIfPresent(a.getAnime_name()));
//        am.deleteAnime(a.getAnime_name());
//        User f = new User();
//        f.setUsername("Francesca");
//        f.setBirthday(birthday);
//        um.updateUser(f);
//        um.deleteUser(e);
//        um.createUser(e);
//        um.createUser(p);
//
//        um.followUser("Francesca", "Elisa");
//        um.followUser("Pippo", "Elisa");
//        um.followUser("Pippo", "Francesca");
//
//
//        um.unfollowUser("Francesca", "Elisa");
//        um.followAnime("Francesca", "Naruto");
//        um.followAnime("Pippo", "Sailor Moon");
//        um.unfollowAnime("Francesca", "Sailor Moon");
//        Set<User> followed = am.getFollowers("Sailor Moon");
//        Set<Anime> anime_followed = um.getFollowedAnime("Francesca");
//
//        Set<Review> reviews = um.getCreatedReviews("Elisa");
//        System.out.println(followed.size());
//        for (User u:followed){
//            System.out.println(u.toString());
//        }
//        User l = new User();
//        l.setUsername("Laura");
//        l.setGender("female");
//        l.setPassword("l");
//        User a = new User();
//        a.setUsername("Ari");
//        a.setGender("female");
//        a.setPassword("a");
//        um.createUser(l);
//        um.createUser(a);

//        System.out.println(am.getVerySuggestedAnime("Pippo"));
//        System.out.println(am.getSuggestedAnimeMediumPriority("Pippo"));
//        System.out.println(am.getCommentedByFriendAnime("Pippo"));
//        System.out.println(am.getNSuggestedAnime("Pippo",10));
//        System.out.println(um.getVerySuggestedUsers("Pippo"));
//        System.out.println(um.getSuggestedUsersLowPriority("Pippo"));
//        System.out.println(um.getNSuggestedUsers("Pippo", 5));
//        am.followAnime("Elisa", "Heidi");
//        am.followAnime("Elisa", "Sailor Moon");
//        am.followAnime("Pippo", "Heidi");
//        am.followAnime("Laura", "Heidi");
//        am.followAnime("Ari", "Heidi");
//
//        am.followAnime("e", "Sailor Moon");



        um.followUser("elisa","Pippo");
        um.followUser("elisa","Ari");
        um.followUser("elisa","Laura");

        dbNeo4J.closeNeo4J();
        }

    }

