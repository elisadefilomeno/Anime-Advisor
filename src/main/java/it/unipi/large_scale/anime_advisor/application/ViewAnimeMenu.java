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
        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println(GREEN + "ANIME MENU" + RESET);
        System.out.println("What would you like to do?");
        System.out.println("Digit:");
        System.out.println("1) Follow anime");
        System.out.println("2) Unfollow anime");
        System.out.println("3) Review anime"); //io
        System.out.println("4) Vote anime");
        System.out.println("0) Go back");
        System.out.println(GREEN + "**************************************" + RESET);        //1) vede info anime,



        Scanner sc = new Scanner(System.in);
        int value_case = -1;
        try {
            value_case = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("ATTENTION! Wrong command");
            this.showMenu(anime);
        }
        switch (value_case) {
            case 1:{
                AnimeManagerNeo4J am =new AnimeManagerNeo4J(dbNeo4J);
                am.likeAnime(user,anime.getAnime_name());
                this.showMenu(anime);
            }
            case 2:{
                AnimeManagerNeo4J am =new AnimeManagerNeo4J(dbNeo4J);
                am.unlikeAnime(user,anime.getAnime_name());
                this.showMenu(anime);

            }
            case 3:     browseReviewsMenu.showMenu(anime);


            case 4: {
                int score=-1;
                    while (!(score>=0)&&!(score<=10)) {
                        try {
                            System.out.println("Insert a score between 1 and 10 or press 0 to go back");
                            score = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException n) {
                            System.out.println("Wrong input.");
                        }
                    }
                        if(score==0){
                            this.showMenu(anime);
                        }
                        else{
                            crud.updateAnimeMeanScored(anime,anime_collection,score);
                            System.out.println("Thank you for voting!");
                            this.showMenu(anime);
                        }

                this.showMenu(anime);
            }

            case 5:return; //DA PROVARE
            case 0:
                backMenu.showMenu();
                break;
            // 2) vuoi lasciare una recensione?
            // 3) vuoi votare l'anime?
            // 4) vuoi seguire l'anime?
            // 5) unfollow
            // 6) vuoi vedere le recensioni?
            // 7 e 8) if user è admin mostra l'opzione di eliminare l'anime o modificarlo
        }
    } //MENU

    public void updateSection(Anime anime){



    }




} //CLASS
