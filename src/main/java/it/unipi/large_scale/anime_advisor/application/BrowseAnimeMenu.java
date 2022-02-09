package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDBAgg;
import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDBCRUD;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.User;

import java.util.Scanner;
import it.unipi.large_scale.anime_advisor.entity.User;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.*;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.Main.anime_collection;
import static it.unipi.large_scale.anime_advisor.application.Main.userManagerNeo4J;


public class BrowseAnimeMenu {
    //1) ricerca anime per titolo
    //1) trova anime  per genere
    // 2) ricerca avanzata (aggregazioni + viewMostoFollowedAnime)
    // 3) view most followed anime ( capire se con members di Mongo o contanto archi su Graph)
    // 4) view most reviewed anime (sul grafo-> da decidere se fare come most followed anime)
    // 3) view personalised suggested anime (Neo4J)
    // 4) go back to registered home page
    AnimeManagerMongoDBCRUD crud= new AnimeManagerMongoDBCRUD();
    AnimeManagerMongoDBAgg aggregation=new AnimeManagerMongoDBAgg();
    ViewAnimeMenu animeMenu=new ViewAnimeMenu();
    Anime anime=new Anime();

    public void showMenu(){
        //menu with extra option for admin
        if(user.getIs_admin()){
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println(GREEN+"HOME PAGE"+RESET);
            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println("1) Find anime by name");
            System.out.println("2) Find anime by genre");
            System.out.println("3) Advanced search");
            System.out.println("4) Go back");
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println("Write your command here:");

            Scanner sc =new Scanner(System.in);
            int value_case;
            try{
                value_case = Integer.parseInt(sc.nextLine());
            }
            catch(Exception e){
                System.out.println("ATTENTION! Wrong command");
                return;
            }
            switch (value_case) {
                case 1 :  animeMenu.showMenu();
                case 2: System.out.println("Insert a genre from the following list:\n" +
                        "1)Action\t2)Drama\t3)Comedy\t4)Mecha\t5)Sports\n" +
                        "6)Slice of Life\t7)Music\t8)Thriller\t9)Shoujo\t10)Hentai\n");
                    //case 3 : profileUserMenu.showMenu();
                case 0 : {if(user!=null){
                    userManagerNeo4J.logOut(user);
                    return;
                }}
              //  default -> System.out.println("ATTENTION! Wrong command");
            }
        }


        //menu for NORMAL USER
        else{

            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println(GREEN+"HOME PAGE"+RESET);
            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println("1) Browse Anime");
            System.out.println("2) Browse Users");
            System.out.println("3) Visualize your profile");
            System.out.println("0) Log out");
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println("Write your command here:");

            Scanner sc =new Scanner(System.in);
            int value_case;
            try{
                value_case = Integer.parseInt(sc.nextLine());
            }
            catch(Exception e){
                System.out.println("ATTENTION! Wrong command");
                return;
            }
            switch (value_case) {
               // case 1 -> browseAnimeMenu.showMenu();
              //  case 2 -> browseUsersMenu.showMenu();
              //  case 3 -> profileUserMenu.showMenu();
                case 0 -> {if(user!=null){
                    userManagerNeo4J.logOut(user);
                    return;
                }}
                default -> System.out.println("ATTENTION! Wrong command");
            }
        }



























    }









}
