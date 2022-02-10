package main;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerNeo4J;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.*;
import it.unipi.large_scale.anime_advisor.reviewManager.ReviewManagerNeo4J;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;

//da rimuovere

public class GraphMain {

    public static void main(String[] args) throws Exception {
        DbManagerNeo4J dbNeo4J = new DbManagerNeo4J();
        UserManagerNeo4J um = new UserManagerNeo4J(dbNeo4J);
        AnimeManagerNeo4J am = new AnimeManagerNeo4J(dbNeo4J);
        ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);


        User e= new User();
        e.setUsername("Elisa");
        e.setGender("Female");
        e.setPassword("psw");
        e.setIs_admin(false); // default = false
        e.setLogged_in(true); //default = false
        User p= new User();
        p.setUsername("Francesca");
        p.setGender("Female");
        p.setPassword("psw");
        p.setIs_admin(true); // default = false
        p.setLogged_in(true); //default = false

//        um.createUser(e);
//        um.createUser(p);
//        um.deleteUser(e);
//        e.setPassword("new_psw");
//        um.updateUser(e);
//        System.out.println(um.getUserByUsername("elisa"));

        Anime a = new Anime();
        a.setAnime_name("Naruto");
//        am.createAnime(a.getAnime_name());
        Anime b = new Anime();
        b.setAnime_name("Heidi");
        am.createAnime(b.getAnime_name());
        Anime c = new Anime();
        c.setAnime_name("Sailor Moon");
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
        um.followUser("Pippo", "elisa");
//        um.followUser("Pippo", "Francesca");

        um.unfollowUser("Francesca", "elisa");

          am.unlikeAnime("Elisa", "Heidi");
//          am.likeAnime("Francesca", "Sailor Moon");
//          am.likeAnime("Francesca", "Sailor Moon");
//          am.unlikeAnime("Pippo", "Naruto");
//        Set<User> followed1 = am.getLikedUsers("Naruto");
//        Set<String> followed = um.getLikedAnime(p);
        Review re = new Review();
        re.setTitle("My review");
        re.setText("Good anime");
        Review r = new Review();
        r.setTitle("Title");
        r.setText("good");

//        rm.createReview(re,c, e);
//        rm.createReview(r,b, p);
//        rm.deleteReview("title","Naruto","Francesca");
//        Set<Review> reviews = um.getWrittenReviews(p);
//        System.out.println(reviews.size());
//        for (Review u:reviews){
//            System.out.println(u);
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

        System.out.println(am.getVerySuggestedAnime("Pippo"));
        System.out.println(am.getSuggestedAnimeMediumPriority("Pippo"));
        System.out.println(am.getCommentedByFriendAnime("Pippo"));
        System.out.println(am.getNSuggestedAnime("Pippo",10));
        System.out.println(um.getVerySuggestedUsers("Pippo"));
        System.out.println(um.getSuggestedUsersLowPriority("Pippo"));
//        System.out.println(um.getNSuggestedUsers("Pippo", 5));
//        am.followAnime("Elisa", "Heidi");
//        am.followAnime("Elisa", "Sailor Moon");
//        am.followAnime("Pippo", "Heidi");
//        am.followAnime("Laura", "Heidi");
//        am.followAnime("Ari", "Heidi");
//
//        am.followAnime("e", "Sailor Moon");



//        um.followUser("elisa","Pippo");
//        um.followUser("elisa","Ari");
//        um.followUser("elisa","Laura");

        dbNeo4J.closeNeo4J();
        }

    }

