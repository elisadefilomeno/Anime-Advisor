package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDBCRUD;
import it.unipi.large_scale.anime_advisor.entity.Anime;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import it.unipi.large_scale.anime_advisor.application.BrowseAnimeMenu;

import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.Main.anime_collection;

import static it.unipi.large_scale.anime_advisor.application.Interface.user;

public class ViewAnimeMenu {
    public void showMenu(Anime anime) {
        AnimeManagerMongoDBCRUD crud = new AnimeManagerMongoDBCRUD();
        BrowseAnimeMenu backMenu = new BrowseAnimeMenu();
        crud.readAnime(anime, anime_collection);
        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println(GREEN + "ANIME MENU" + RESET);
        System.out.println("What would you like to do?");
        System.out.println("Digit:");
        System.out.println("1) Follow anime");
        System.out.println("2) Review");
        System.out.println("3) Update anime");
        System.out.println("4) Delete anime");
        System.out.println("5) Go back");
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
            case 1:
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                backMenu.showMenu();
                break;
            // 2) vuoi lasciare una recensione?
            // 3) vuoi votare l'anime?
            // 4) vuoi seguire l'anime?
            // 5) unfollow
            // 6) vuoi vedere le recensioni?
            // 7 e 8) if user è admin mostra l'opzione di eliminare l'anime o modificarlo
        }
    }
}