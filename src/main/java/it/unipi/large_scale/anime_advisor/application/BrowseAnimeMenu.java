package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDBAgg;
import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDBCRUD;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public void showMenu() {
        //menu with extra option for admin
        if (user.getIs_admin() && user!=null) {
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println(GREEN + "BROWSE ANIME PAGE" + RESET);
            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println("1) Find anime by name");
            System.out.println("2) Find anime by genre");
            System.out.println("3) Advanced search");
            System.out.println("4) Update Anime");
            System.out.println("5) Delete Anime");
            System.out.println("0) Go back");
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println("Write your command here:");

            Scanner sc = new Scanner(System.in);
            int value_case = -1;
            try {
                value_case = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");
                    this.showMenu();
                }
            switch (value_case) {
                case 1:
                    this.browseAnimeTitle();
                    break;
                case 2:
                        this.researchByGenre();
                        break;
                    //case 3 : profileUserMenu.showMenu();
                case 0:
                    return;
                default:
                    System.out.println("Wrong command!");
                    break;
            }
        }


        //menu for NORMAL USER
        else {

            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println(GREEN + "BROWSE ANIME PAGE" + RESET);
            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println("1) Find anime by name");
            System.out.println("2) Find anime by genre");
            System.out.println("3) Advanced search");
            System.out.println("0) Go back");
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println("Write your command here:");

            Scanner sc = new Scanner(System.in);
            int value_case = -1;
            try {
                value_case = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");
                this.showMenu();
            }
            switch (value_case) {
                case 1:
                    this.browseAnimeTitle();
                    break;
                case 2:
                    this.researchByGenre();
                    break;
                //case 3 : profileUserMenu.showMenu();
                case 0:
                    return;
                default:
                    System.out.println("Wrong command!");
                    break;
            }
        }
    }


    public void browseAnimeTitle(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert anime title: ");
        String scan= sc.nextLine();
        Anime t=new Anime();
        t.setAnime_name(scan);
        anime=crud.readAnime(t,anime_collection);
        if(anime!=null)
            animeMenu.showMenu(anime);
        else{
            System.out.println("Invalid name\n");
            this.showMenu();
        }
    }

    public void researchByGenre(){
            Scanner sc = new Scanner(System.in);
        int select = -1;
        List<String> genresChoosen= new ArrayList<>();
            HashMap<Integer,String>animeResults;
            int pos=0;
            while(select!=0) {
                int yn=-1; //YES OR NO VALUE
                String genreList = ("Insert one or more genres from the following list or presso 0 to go back:\n" +
                        "1)Action\t2)Drama\t3)Gag\t4)Mecha\t5)Sports\n" +
                        "6)Slice of Life\t7)Music\t8)Thriller\t9)Shoujo\t10)Hentai\n" +
                        "11)Shounen\t12)Seinen\t13)Josei\t14)Isekai\t15)Ecchi");
                System.out.println(genreList);
                try {
                    select = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("ATTENTION! Wrong command");
                    continue;
                }
                if (!(select >= 1) && !(select <= 15) && (select != 0)) {
                    System.out.println("Please chose a genre or press 0 to go stop");
                    continue;
                }
                if (select == 1) {
                    genresChoosen.add("Action");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 2) {
                    genresChoosen.add("Drama");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 3) {
                    genresChoosen.add("Gag");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 4) {
                    genresChoosen.add("Mecha");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 5) {
                    genresChoosen.add("Sports");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 6) {
                    genresChoosen.add("Slice of Life");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 7) {
                    genresChoosen.add("Music");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 8) {
                    genresChoosen.add("Thriller");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 9) {
                    genresChoosen.add("Shoujo");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 10) {
                    genresChoosen.add("Hentai");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 11) {
                    genresChoosen.add("Shounen");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 12) {
                    genresChoosen.add("Seinen");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 13) {
                    genresChoosen.add("Josei");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 14) {
                    genresChoosen.add("Isekai");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try{
                            yn = Integer.parseInt(sc.nextLine());
                        }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                            continue;
                        }                    }
                    if (yn == 1)
                        continue;
                    else
                        break;
                }
                if (select == 15) {
                    genresChoosen.add("Ecchi");
                    pos++;
                    while (yn != 1 && yn != 2) {

                        System.out.println("Do you want to select more genres?\n1)YES 2)NO");
                        try {
                            yn = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Wrong input!");
                            continue;
                        }
                    }

                    if (yn == 1)
                        continue;
                    else
                        break;
                }

            } //End while
                if(pos==0){
                    this.showMenu();}
                else{
                    String[] arrayGenres= new String[pos];
                    for(int i=0;i<genresChoosen.size();i++)
                        arrayGenres[i]=genresChoosen.get(i);
                    //Insertion of the animes founded into an hashmap and selection of
                    //an anime to visit
                    animeResults= aggregation.topTenAnimeByField(anime_collection,"genre",0,null,arrayGenres,0);
                    crud.printAnimeResults(animeResults);
                    int x=0;
                    int animecheck=0;
                    while(x==0) {
                        int animechoosen=-1;
                        System.out.println("Do you want to go to one of these anime's page?\n" +
                                "1)YES 2)NO");
                        try {
                            select = Integer.parseInt(sc.nextLine());
                        } catch (Exception b) {
                            System.out.println("Attention! Wrong command!");
                            continue;
                        }
                        if(select!=1 && select!=2)
                            continue;
                        if(select==2)
                            break;
                        if(select==1){
                            while (animecheck==0) {
                                System.out.println("Insert the number of the anime you want to visit");
                                try {
                                    animechoosen = Integer.parseInt(sc.nextLine());
                                    animecheck = 1;
                                } catch (NumberFormatException a) {
                                    System.out.println("Attention! Wrong command!");
                                    animecheck = 0;
                                    continue;
                                }
                            }
                            Anime anime=new Anime();
                            anime.setAnime_name(animeResults.get(animechoosen));
                            animeMenu.showMenu(anime);
                            return;
                        }
                    }
                }
            }
        }












