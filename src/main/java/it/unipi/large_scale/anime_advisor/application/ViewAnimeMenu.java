package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDBCRUD;
import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import it.unipi.large_scale.anime_advisor.application.BrowseAnimeMenu;

import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.Main.anime_collection;

import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;

public class ViewAnimeMenu {
    private final BrowseReviewsMenu browseReviewsMenu = new BrowseReviewsMenu();
    public void showMenu(Anime anime) {
        AnimeManagerMongoDBCRUD crud = new AnimeManagerMongoDBCRUD();
        BrowseAnimeMenu backMenu = new BrowseAnimeMenu();
        crud.readAnime(anime, anime_collection);
        int check=1;
        while(check==1) {
            if(user==null){
                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println("You must register if you want to do more!");
                return;
            }
            else  {
                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println(GREEN + "ANIME MENU" + RESET);
                System.out.println("What would you like to do?");
                System.out.println("Digit:");
                System.out.println(GREEN+"1) "+RESET+"Like anime");
                System.out.println(GREEN+"2) "+RESET+"Unlike anime");
                System.out.println(GREEN+"3) "+RESET+"Review anime"); //io
                System.out.println(GREEN+"4) "+RESET+"Vote anime");
                System.out.println(GREEN+"0) "+RESET+"Go back");
                System.out.println(GREEN + "**************************************" + RESET);        //1) vede info anime,

                Scanner sc = new Scanner(System.in);
                int value_case = -1;
                try {
                    value_case = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("ATTENTION! Wrong command");
                    //this.showMenu(anime);
                }
                switch (value_case) {
                    case 1: {

                        AnimeManagerNeo4J am = new AnimeManagerNeo4J(dbNeo4J);
                        am.likeAnime(user, anime.getAnime_name());
                        break;
                        //this.showMenu(anime);
                    }

                    case 2: {
                        AnimeManagerNeo4J am = new AnimeManagerNeo4J(dbNeo4J);
                        am.unlikeAnime(user, anime.getAnime_name());
                        break;
                        //this.showMenu(anime);

                    }

                    case 3: {
                        browseReviewsMenu.showMenu(anime);
                        break;
                    }

                    case 4: {
                        if(crud.voteAnimeUser(anime,user,anime_collection)){
                            System.out.println("Thank you for voting!");
                            break;
                        }
                        break;
                    }

                    case 0: {
                        //backMenu.showMenu();
                        check = -1;
                        break;
                    }
                    // 2) vuoi lasciare una recensione?
                    // 3) vuoi votare l'anime?
                    // 4) vuoi seguire l'anime?
                    // 5) unfollow
                    // 6) vuoi vedere le recensioni?
                    // 7 e 8) if user è admin mostra l'opzione di eliminare l'anime o modificarlo
                }
                if (check == -1)
                    return;
            }
           // case 3:     browseReviewsMenu.showMenu(anime);


        }
    } //MENU
} //CLASS
