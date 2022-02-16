package it.unipi.large_scale.anime_advisor.application;


import it.unipi.large_scale.anime_advisor.animeManager.*;
import it.unipi.large_scale.anime_advisor.entity.*;
import org.bson.Document;

import java.util.*;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.*;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.Main.anime_collection;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;


public class BrowseAnimeMenu {

    AnimeManagerMongoDBCRUD crud= new AnimeManagerMongoDBCRUD();
    AnimeManagerMongoDBAgg aggregation=new AnimeManagerMongoDBAgg();
    ViewAnimeMenu animeMenu=new ViewAnimeMenu();
    AnimeManagerNeo4J animeNeo;
    Anime anime=new Anime();
    Interface inte;

    public void showMenu() {

        //menu with extra option for admin
        int stop=0;

        if (checkIsAdmin(user)) {

            while (stop == 0) {

                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println(GREEN + "BROWSE ANIME PAGE" + RESET);
                System.out.println("What would you like to do?");
                System.out.println("Digit:");
                System.out.println(GREEN+"1) "+RESET+"Find anime by name");
                System.out.println(GREEN+"2) "+RESET+"Find anime by genre");
                System.out.println(GREEN+"3) "+RESET+"Advanced search");
                System.out.println(GREEN+"4) "+RESET+"Update Anime");
                System.out.println(GREEN+"5) "+RESET+"Insert Anime");
                System.out.println(GREEN+"6) "+RESET+"Delete Anime");
                System.out.println(GREEN+"0) "+RESET+"Go back");
                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println("Write your command here:");

                Scanner sc = new Scanner(System.in);
                int value_case = -1;
                try {
                    value_case = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("ATTENTION! Wrong command");
                }
                switch (value_case) {
                    case 1: { //FIND BY NAME
                        Anime a = this.findAnime();
                        if (a != null)
                            animeMenu.showMenu(a);

                        break;
                    }
                    case 2: { //FIND BY GENRE
                        Anime a=this.researchByGenre();
                        if(a!=null)
                            animeMenu.showMenu(a);
                        break;
                    }
                    case 3: { //ADVANCED SEARCH
                        this.advancedSearch();
                        break;
                    }
                    case 4: { //UPDATE
                        Anime b = this.findAnime();
                        if (b != null)
                            this.updateAnime(b);

                        break;
                    }
                    case 5: { //INSERT
                        this.createAnimeFromInput();
                        break;
                    }
                    case 6: { //DELETE
                        Anime c = this.findAnime();
                        if (c != null)
                            this.deleteAnimeFromInput(c);

                        break;
                    }
                    case 0: {
                        stop = 1;
                        break;
                    }
                    default: {
                        System.out.println("Wrong command!");
                        break;
                    }
                }

                if (stop == 1)
                    return;
            }//CHECK STOP
        } //IF ADMIN


        //menu for NORMAL USER
        else {
            while (stop == 0) {

                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println(GREEN + "BROWSE ANIME PAGE" + RESET);
                System.out.println("What would you like to do?");
                System.out.println("Digit:");
                System.out.println(GREEN+"1) "+RESET+"Find anime by name");
                System.out.println(GREEN+"2) "+RESET+"Find anime by genre");
                System.out.println(GREEN+"3) "+RESET+"Advanced search");
                System.out.println(GREEN+"0) "+RESET+"Go back");
                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println("Write your command here:");

                Scanner sc = new Scanner(System.in);
                int value_case = -1;
                try {
                    value_case = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("ATTENTION! Wrong command");

                }
                switch (value_case) {
                    case 1: { //FIND BY NAME
                        Anime a = this.findAnime();
                        if (a != null) {
                            animeMenu.showMenu(a);
                        }
                        break;
                    }
                    case 2: { //FIND BY GENRE
                        this.researchByGenre();
                        break;
                    }
                    case 3: { //ADVANCED SEARCH
                        this.advancedSearch();
                        break;
                    }
                    case 0: {
                        stop = 1;
                        break;
                    }
                    default: {
                        System.out.println("Wrong command!");
                        break;
                    }
                }
                if (stop == 1)
                    return;
            }//CHECK STOP
        } //IF USER
    } //SHOW MENU

    public Anime pickAnime(HashMap<Integer,String> results){
        Interface inte=new Interface();
        inte.printResults(results);
        Anime anime=new Anime();
        int x=0;
        int select=-1;
        Scanner sc=new Scanner(System.in);
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
                    if (animechoosen<1 || animechoosen>results.size()){
                        System.out.println("Error! Wrong input!");
                        animecheck=0;
                        continue;
                    }
                }
                anime.setAnime_name(results.get(animechoosen));
                return anime;
            }
        }

            return null;} //RESEARCH BY GENRE



    public Anime findAnime(){
        Scanner sc = new Scanner(System.in);
        Anime a= new Anime();
        System.out.println("Insert anime title: ");
        int pos=-1;
        String scan= sc.nextLine();
        a.setAnime_name(scan);
        HashMap<Integer,String> results= crud.findResults(a,anime_collection);

        while(pos==-1) {
            System.out.println("Select the anime to open or press 0 to go back");
            try {
                pos = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Wrong input!");
                continue;
            }
            if (pos == 0 )
                return null;
               //this.showMenu();

            if(pos>results.size()){
                System.out.println("Select a valid input");
                pos=-1;
                continue;
            }
        }
        a.setAnime_name(results.get(pos));

        return a;
    }

    public Anime researchByGenre() {
        Scanner sc = new Scanner(System.in);
        int select = -1;
        List<String> genresChoosen = new ArrayList<>();
        HashMap<Integer, String> animeResults;
        int pos = 0;
        while (select != 0) {
            int yn = -1; //YES OR NO VALUE
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
            if (select == 2) {
                genresChoosen.add("Drama");
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
            if (select == 3) {
                genresChoosen.add("Gag");
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
            if (select == 4) {
                genresChoosen.add("Mecha");
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
            if (select == 5) {
                genresChoosen.add("Sports");
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
            if (select == 6) {
                genresChoosen.add("Slice of Life");
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
            if (select == 7) {
                genresChoosen.add("Music");
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
            if (select == 8) {
                genresChoosen.add("Thriller");
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
            if (select == 9) {
                genresChoosen.add("Shoujo");
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
            if (select == 10) {
                genresChoosen.add("Hentai");
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
            if (select == 11) {
                genresChoosen.add("Shounen");
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
            if (select == 12) {
                genresChoosen.add("Seinen");
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
            if (select == 13) {
                genresChoosen.add("Josei");
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
            if (select == 14) {
                genresChoosen.add("Isekai");
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
        if (pos == 0) {
            return null;
        } else {
            inte = new Interface();
            String[] arrayGenres = new String[pos];
            for (int i = 0; i < genresChoosen.size(); i++)
                arrayGenres[i] = genresChoosen.get(i);
            //Insertion of the animes founded into an hashmap and selection of
            //an anime to visit
            animeResults = aggregation.topTenAnimeByField(anime_collection, "genre", 0, null, arrayGenres, 0);
                  /*  inte.printResults(animeResults);
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
                            //animeMenu.showMenu(anime);
                            return anime;
                        }
                    }
                }
            return null;*/
            return this.pickAnime(animeResults);
        } //RESEARCH BY GENRE
    }

    public void createAnimeFromInput(){
        Scanner sc=new Scanner(System.in);
        String chars;
        Anime anime=new Anime();

        //SET NAME
        this.anime.setAnime_name(this.setString("name"));
        //SET GENRES
        this.anime.setGenre(this.setArray("genre"));
        //SET STUDIO
        this.anime.setStudio(this.setArray("studio"));
        //SET PRODUCER
        this.anime.setProducer(this.setArray("producer"));
        //SET LICENSOR
        this.anime.setLicensor(this.setArray("licensor"));
        //SET MEMBERS
        this.anime.setMembers(0);
        //SET SCORED
        this.anime.setScored(0);
        //SET SCORED BY
        this.anime.setScoredby(0);
        //SET EPISODES
        int ep=0;
        int temp=0;
        while(temp==0) {
            System.out.println("Insert the number of episodes");
            try{
                ep = Integer.parseInt(sc.nextLine());
                temp=1;
            }
                        catch (NumberFormatException e){System.out.println("Wrong input!");
                continue;
            }
        }//WHILE FOR INPUT EPISODES AND CATCH ERRORS
        this.anime.setEpisodes(ep);

        //SET PREMIERED
        ep=0;
        temp=0;
        while(temp==0) {
            System.out.println("Insert the year of premierer");
            try{
                ep = Integer.parseInt(sc.nextLine());
                temp=1;
            }
            catch (NumberFormatException e){System.out.println("Wrong input!");
                continue;
            }
        }//WHILE FOR INPUT EPISODES AND CATCH ERRORS
        this.anime.setPremiered(ep);

        //SET SOURCE1

        this.anime.setSource(this.setString("source"));

        //SET TYPE
        this.anime.setType(this.setString("type"));
        this.anime.toString();
        //Insertion into Mongo & Neo4j
       if( crud.createAnime(this.anime,anime_collection)){ //CREATE ANIME IN MONGO
           animeNeo= new AnimeManagerNeo4J(dbNeo4J);
            if(animeNeo.createAnime(this.anime.getAnime_name())){ //CREATE ANIME IN NEO4J
                System.out.println("Anime inserted!");
                this.anime.toString();
                animeMenu.showMenu(this.anime);
            }
            else{
                if(crud.deleteAnime(this.anime,anime_collection)){
                    System.out.println("Error during the anime creation...\nReturn to menu");
                this.showMenu();}
                else{
                    System.out.println("Error during mongoDB anime deletion");
                }

            }
       }
       else{
           System.out.println("An error has occurred\nAnime not inserted");
           this.showMenu();
       }
        }

        //DELETE ANIME IN MONGO AND NEO4J
    public void deleteAnimeFromInput(Anime temp){
            animeNeo= new AnimeManagerNeo4J(dbNeo4J);
            Document backup_doc= crud.getAnime(temp.getAnime_name(),anime_collection);
            if(crud.deleteAnime(temp,anime_collection)){ //MONGO DELETE
                if(animeNeo.deleteAnime(temp.getAnime_name())) //DELETE NEO4J
                    System.out.println("Anime "+temp.getAnime_name()+" deleted successfully");
                else {
                    System.out.println("Error during the elimination of the element");
                    anime_collection.insertOne(backup_doc);     //IF NEO4J FAILS TO DELETE THE FILE GET REINSERTED INTO MONGO
                }
            }
            else
                System.out.println("Error during the elimination.\nCannot delete");
            return;
    }

    public void updateAnime(Anime anime){

            int check=0;
            while (check==0) { //WHILE FOR KEEPING1 UP THE MODIFIY MENU
                System.out.println(GREEN+"-----------------------------------"+RESET);
                crud.readAnime(anime,anime_collection);
                System.out.println(GREEN+"-----------------------------------"+RESET);
                System.out.println(GREEN+"What do you want to update?"+RESET);
            System.out.println("1)name\n2)episodes\n3)year\n4)source\n"+
                    "5)type\n6)genre\n7)studio\n8)producer\n9)licensor");
            Scanner sc=new Scanner(System.in);
            int input;
                System.out.println("Choose an option 1-9 or Press 0 to go Back");
                try {
                    input = Integer.parseInt(sc.nextLine());
                    if(input<0 || input>9){
                        System.out.println("Wrong input!");
                        continue;
                    }

                } catch (NumberFormatException a) {
                    System.out.println("Attention! Wrong command!");
                    check = 0;
                    continue;
                }
                switch (input){
                    default:this.updateAnime(anime);
                    case 0:{ check=1;
                        break;
                    }
                    case 1:{
                        String newName=new String();
                        System.out.println("Insert new name: ");
                        newName = sc.nextLine();
                        try {
                            crud.updateAnimeName(anime,anime_collection,newName);
                            animeNeo= new AnimeManagerNeo4J(dbNeo4J);
                            animeNeo.updateAnimeTitle(anime.getAnime_name(),newName);
                            anime.setAnime_name(newName);
                        }
                        catch (Exception e){
                            System.out.println("Unable to update");
                        }
                        continue;
                    }//CASE 1
                    case 2:{
                        System.out.println("Insert the number of episodes or Press 0 to go Back");
                        check=-1;
                        int ep=-1;
                        while(check==-1) {
                            try {
                                ep = Integer.parseInt(sc.nextLine());
                                if (ep < 0) {
                                    System.out.println("Invalid input\nPlease insert the number of episodes or press 0 to go back\n");
                                    continue;
                                }
                                check=1;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input\nPlease insert the number of episodes or press 0 to go back");
                                continue;
                            }
                        }
                        if (ep==0)
                            break;
                        crud.updateAnimeEpisodes(anime,anime_collection,ep);
                        continue;
                    }//CASE 2
                    case 3:{
                        System.out.println("Insert the number of the year or Press 0 to go Back");
                        check=-1;
                        int ep=-1;
                        while(check==-1) {
                            try {
                                ep = Integer.parseInt(sc.nextLine());
                                if (ep < 0) {
                                    System.out.println("Invalid input\nPlease insert the number of the year or press 0 to go back:\n");
                                    continue;
                                }
                                check=1;
                            } catch (NumberFormatException e) {
                                System.out.println("Error input\nPlease insert the number of the year of press 0 to go back:\n");
                                continue;
                            }
                        }
                        if (ep==0)
                            continue;
                        crud.updateAnimePremiered(anime,anime_collection,ep);
                        continue;
                    }//CASE 3
                    case 4:{
                        String newName=new String();
                        System.out.println("Insert new source: ");
                        newName=sc.nextLine();
                        try {
                            crud.updateAnimeSource(anime,anime_collection,newName);
                        }
                        catch (Exception e){
                            System.out.println("Unable to update");
                        }
                        continue;
                    }//CASE 4
                    case 5:{
                        String newName=new String();
                        System.out.println("Insert new type: ");
                        newName=sc.nextLine();
                        try {
                            crud.updateAnimeType(anime,anime_collection,newName);
                        }
                        catch (Exception e){
                            System.out.println("Unable to update");
                        }
                        continue;
                    }//CASE 5
                    case 6:{
                        int more=0;
                        int answ=-1;
                        String str=new String();
                        while(more==0){
                            System.out.println("Do you want to insert or delete a genre?\n1)Add\n2)Remove\n0)Go Back");
                            while(answ==-1){
                                try{
                                    answ = Integer.parseInt(sc.nextLine());
                                }
                                catch (NumberFormatException e){
                                    System.out.println("Wrong input!");
                                }
                            }//WHILE QUESTION
                            if(answ==0)
                                    more=1;
                                if(answ==1){
                                    System.out.println("Insert a genre to add");
                                    try{
                                        str= sc.nextLine();
                                    }
                                    catch(Exception e){
                                        System.out.println("An error has occurred");
                                    }
                                    crud.updateAnimeGenreAddOne(anime,anime_collection,str);
                                    answ=-1;
                                }//ADD
                                if(answ==2){
                                    System.out.println("Insert a genre to remove");
                                    str= sc.nextLine();
                                    crud.updateAnimeGenreDeleteOne(anime,anime_collection,str);
                                    answ=-1;
                                }//REMOVE
                        }//WHILE OPERATING
                        continue;
                    }//CASE 6
                    case 7:{
                        int more=0;
                        int answ=-1;
                        String str=new String();
                        while(more==0){
                            System.out.println("Do you want to insert or delete a studio?\n1)Add\n2)Remove\n0)Go Back");
                            while(answ==-1){
                                try{
                                    answ = Integer.parseInt(sc.nextLine());
                                }
                                catch (NumberFormatException e){
                                    System.out.println("Wrong input!\nPlease choose an option:\n");
                                    continue;
                                }
                            }//WHILE QUESTION
                            if(answ==0)
                                more=1;
                            if(answ==1){
                                System.out.println("Insert a studio to add");
                                try{
                                    str= sc.nextLine();
                                }
                                catch(NumberFormatException e){
                                    System.out.println("An error has occurred");
                                }
                                crud.updateAnimeStudioAddOne(anime,anime_collection,str);
                                answ=-1;
                            }//ADD
                            if(answ==2){
                                System.out.println("Insert a studio to remove");
                                str= sc.nextLine();
                                crud.updateAnimeStudioDeleteOne(anime,anime_collection,str);
                                answ=-1;
                            }//REMOVE
                        }//WHILE OPERATING
                        continue;
                    }//CASE 7
                    case 8:{
                        int more=0;
                        int answ=-1;
                        String str=new String();
                        while(more==0){
                            System.out.println("Do you want to insert or delete a producer?\n1)Add\n2)Remove\n0)Go Back");
                            while(answ==-1){
                                try{
                                    answ = Integer.parseInt(sc.nextLine());
                                }
                                catch (NumberFormatException e){
                                    System.out.println("Wrong input!\nPlease choose an option");
                                    continue;
                                }
                            }//WHILE QUESTION
                            if(answ==0)
                                more=1;
                            if(answ==1){
                                System.out.println("Insert a producer to add");
                                try{
                                    str= sc.nextLine();
                                }
                                catch(Exception e){
                                    System.out.println("An error has occurred");
                                }
                                crud.updateAnimeProducerAddOne(anime,anime_collection,str);
                                answ=-1;
                            }//ADD
                            if(answ==2){
                                System.out.println("Insert a producer to remove");
                                str= sc.nextLine();
                                crud.updateAnimeProducerDeleteOne(anime,anime_collection,str);
                                answ=-1;
                            }//REMOVE
                        }//WHILE OPERATING
                        continue;
                    }//CASE 8
                    case 9:{
                        int more=0;
                        int answ=-1;
                        String str=new String();
                        while(more==0){
                            System.out.println("Do you want to insert or delete a licensor?\n1)Add\n2)Remove\n0)Go Back");
                            while(answ==-1){
                                try{
                                    answ = Integer.parseInt(sc.nextLine());
                                }
                                catch (NumberFormatException e){
                                    System.out.println("Wrong input!");
                                    continue;
                                }
                            }//WHILE QUESTION
                            if(answ==0)
                                more=1;
                            if(answ==1){
                                System.out.println("Insert a licensor to add");
                                try{
                                    str= sc.nextLine();
                                }
                                catch(Exception e){
                                    System.out.println("An error has occurred");
                                }
                                crud.updateAnimeLicensorAddOne(anime,anime_collection,str);
                                answ=-1;
                            }//ADD
                            if(answ==2){
                                System.out.println("Insert a licensor to remove");
                                str= sc.nextLine();
                                crud.updateAnimeLicensorDeleteOne(anime,anime_collection,str);
                                answ=-1;
                            }//REMOVE
                        }//WHILE OPERATING
                        continue;
                    }//CASE 9




                }//END SWITCH
            }//WHILE CHECK==0
        }

    public boolean checkIsAdmin(User user){
        if(user==null){
            return false;
        }
        return user.getIs_admin();
    }

    public String setString(String string){
        int ok=-1;
        String chars=null;
        Scanner sc=new Scanner(System.in);
        System.out.println(GREEN+"Insert "+string+": ");
        while(ok==-1) {
            try {
                chars = sc.nextLine().toString();
                ok=1;
            } catch (Exception e) {
                System.out.println("Input error");
                ok=-1;
            }
        }
        System.out.println(chars);
        if(chars!=null)
        return chars;
        else
            System.out.println("Error during the inserting of the "+string);
                    return null;
    }

    public String[] setArray(String field){
        int pos=0;
        int wh=-1;
        int yn=-1;
        String chars;
        Scanner sc=new Scanner(System.in);
        List<String> strChoosen= new ArrayList<>();
        while(wh==-1) {
            System.out.println(GREEN+"Insert a "+field+": ");
            try {
                chars = sc.nextLine();
            } catch (Exception e) {
                System.out.println("Input error");
                return null;
            }
            strChoosen.add(chars);
            pos++;
            while (yn != 1 && yn != 2) {

                System.out.println("Do you want to select more "+field+"s?\n1)YES 2)NO");
                try {
                    yn = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Wrong input!");
                    continue;
                }
                if(yn==1){
                    yn=-1;
                    break;}
                if(yn==2){
                    yn=-1;
                    wh=0;
                    break;}
            }//WHILE Y OR N
        } //WHILE INPUT GENRES
        //CREATE AND INSERT THE ARRAY
        String[] type= new String[pos];
        for(int i=0;i<strChoosen.size();i++){
            type[i]=strChoosen.get(i);
        }
        return type;
    }


   public Anime advancedSearch(){
        int check=0; //CHECK FOR WHILE
        int input=-1; //CHECK FOR AGGREGATION
        int chose=-1; //CHECK FOR OPTIONS IN AGGREGATION

        Scanner sc=new Scanner(System.in);
        String stringInput= new String();
        while(check==0){
            System.out.println(GREEN + "ADVANCED SEARCH MENU" + RESET);
            System.out.println("Choose one of the following options\n" +
                    GREEN+"1)"+RESET+"Top rated animes by field\n" +
                    GREEN+"2)"+RESET+"Studio/Genre/Producer with the highest average\n" +
                    GREEN+"3)"+RESET+"Top followed animes\n" +
                    GREEN+"4)"+RESET+"Number of productions of Studios or Producers\n"+
                    GREEN+"5)"+RESET+"Top reviewed animes \n"+
                    GREEN+"6)"+RESET+"View suggested animes \n");


            System.out.println("Choose an option 1-6 or Press 0 to go Back");
            try {
                input = Integer.parseInt(sc.nextLine());
                if(input<0 || input>6){
                    System.out.println("Wrong input!");
                    continue;
                }
            } catch (NumberFormatException a) {
                System.out.println("Attention! Wrong command!");
                check = 0;
                continue;
            }
            switch (input){
                case 1:{
                    System.out.println("Select a field from\n1)Year\n2)Source\n3)Type\n4)Genre\nPress 0 to go back\n");
                   int answ=-1;
                   int innansw=-1;
                   while(answ==-1) {
                       try {
                           answ = Integer.parseInt(sc.nextLine());
                       } catch (NumberFormatException e) {
                           System.out.println("Attention! Wrong input!");
                           continue;
                       }
                       if(answ==0) return  null;
                       if(answ==1){
                           System.out.println("Please choose a year between 1917-2023");
                           while(innansw==-1){
                               try{
                                   innansw = Integer.parseInt(sc.nextLine());
                               }
                               catch (NumberFormatException e){
                                   System.out.println("Attention! Wrong input!");
                                   innansw=-1;
                                   continue;
                               }
                               if((innansw<1917 || innansw>2023)){
                                   System.out.println("Attention! wrong year specified!");
                                   innansw=-1;
                                   continue;
                               }
                               else {
                                   HashMap<Integer,String> results=new HashMap<>();
                                    results=aggregation.topTenAnimeByField(anime_collection,"premiered",innansw,null,null,10);

                                    Anime tt= ( this.pickAnime(results));
                                    if(tt!=null)
                                        animeMenu.showMenu(tt);
                                    else
                                        return null;
                               }//ELSE GOOD YEAR DATE
                           }//ANSW FOR PICKING AN YEAR
                       }//ANSW =1 YEAR
                       if(answ==2) { //SOURCE
                           System.out.println("Please choose a source  between\n" +
                                   "1)Original\n2)TV\n3)Music\n4)Manga\n5)Visual Novel\n6)Game\n" +
                                   "7)Book\nPress 0 to go back");
                           HashMap<Integer, String> temp = new HashMap<>();
                           temp.put(1, "Original");
                           temp.put(2, "TV");
                           temp.put(3, "Music");
                           temp.put(4, "Manga");
                           temp.put(5, "Visual Novel");
                           temp.put(6, "Game");
                           temp.put(7, "Book");

                           while (innansw == -1) {
                               try {
                                   innansw = Integer.parseInt(sc.nextLine());
                               } catch (NumberFormatException e) {
                                   System.out.println("Attention! Wrong input!");
                                   innansw = -1;
                               }
                               if (innansw < 0 || innansw > 7) {
                                   System.out.println("Attention! wrong input specified!");
                                   innansw = -1;
                                   continue;
                               }
                               if (innansw == 0)
                                   return null;
                               else {
                                   HashMap<Integer, String> results = new HashMap<>();
                                   results = aggregation.topTenAnimeByField(anime_collection, "source", innansw, temp.get(innansw), null, 10);
                                   //return this.pickAnime(results);
                                   Anime tt= ( this.pickAnime(results));
                                   if(tt!=null)
                                       animeMenu.showMenu(tt);
                                   else
                                       return null;
                               }//ELSE GOOD TYPE PICK
                           }//ANSW FOR PICKING A SOURCE
                       }//ANSW =2 SOURCE
                       if(answ==3){ //TYPE
                           System.out.println("Please choose a type between\n" +
                                   "1)TV\n2)Special\n3)OVA\n4)Movie\n5)Web\n6)Music\nPress 0 to go back");
                           HashMap<Integer, String> temp = new HashMap<>();
                           temp.put(1, "TV");
                           temp.put(2, "Special");
                           temp.put(3, "OVA");
                           temp.put(4, "Movie");
                           temp.put(5, "Web");
                           temp.put(6, "Music");

                           while (innansw == -1) {
                               try {
                                   innansw = Integer.parseInt(sc.nextLine());
                               } catch (NumberFormatException e) {
                                   System.out.println("Attention! Wrong input!");
                                   innansw = -1;
                               }
                               if (innansw < 0 || innansw > 6) {
                                   System.out.println("Attention! wrong input specified!");
                                   innansw = -1;
                                   continue;
                               }
                               if (innansw == 0)
                                   return null;
                               else {
                                   HashMap<Integer, String> results = new HashMap<>();
                                   results = aggregation.topTenAnimeByField(anime_collection, "type", innansw, temp.get(innansw), null, 10);
                                   //return this.pickAnime(results);

                                   Anime tt= ( this.pickAnime(results));
                                   if(tt!=null)
                                       animeMenu.showMenu(tt);
                                   else
                                       return null;
                               }//ELSE GOOD TYPE PICK
                           }//ANSW FOR PICKING A TYPE
                       }//ANSW =3 TYPE
                       if(answ==4) {
                           top10byGenre();

                           break;

                       }
                       if(answ==0)
                           return  null;
                   }//ANSWER CHOSE FIELD
                        break;
                }//CASE 1
                case 2:{
                    System.out.println("Select:\n1)Genre\n2)Studio\n3)Producer\nPress 0 to go back");
                    int answ=-1;
                    int innansw=-1;
                    HashMap<Integer,String>temp=new HashMap<>();
                    temp.put(1,"genre");
                    temp.put(2,"studio");
                    temp.put(3,"producer");
                    while(answ==-1) {
                        try {
                            answ = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Attention! Wrong input!");
                            answ = -1;
                        }
                    }
                    if(answ<0 ||answ>3 ){
                        System.out.println("Wrong input");
                        answ=-1;
                    }
                    if(answ==0)
                        return null;
                        else{
                            System.out.println("Select a year between 1917 and 2023 or select 0 for an overall view");
                            while(innansw==-1) {
                                try {
                               //QUI
                                    innansw = Integer.parseInt(sc.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Attention! Wrong input!");
                                    System.out.println("Please choose a valid year or press 0 to go back");
                                    innansw = -1;
                                }
                                if ((innansw<1917 || innansw>2023)&& innansw!=0){
                                    System.out.println("Invalid year");
                                    innansw=-1;
                                }
                            }
                            aggregation.highAvgEntity(anime_collection,temp.get(answ),innansw);
                        }//GENRE STUDIO AND PRODUCER
                break;} //CASE 2
                case 3:{
                    ArrayList<Anime> mostLikedAnime = new ArrayList<>();
                    AnimeManagerNeo4J amn = new AnimeManagerNeo4J(dbNeo4J);
                    mostLikedAnime = amn.mostLikedAnime();
                    int count=0;
                    if(mostLikedAnime.size()==0){
                        System.out.println("No Top 10 ");
                    }
                    for (Anime a : mostLikedAnime){
                        count++;
                        System.out.println(GREEN+count+") "+RESET+a.getAnime_name());
                    }
                    System.out.println(GREEN+ "\nDo you want see one of this anime"+RESET);
                    System.out.println(GREEN+"1)"+RESET+" Yes"+"    "+GREEN+"2)"+RESET+" No");
                    int  caseValue=-1;
                    try{
                        caseValue = Integer.parseInt(sc.nextLine());
                    }
                    catch(Exception e ){
                        e.printStackTrace();
                        System.out.println("Wrong Command!");
                        break;
                    }

                    int check1=1;
                    while(check1==1) {
                        switch (caseValue) {
                            case 1: {
                                System.out.println(GREEN + "What anime do you want see ? " + RESET);
                                int indexAnime = -1;
                                try {
                                    indexAnime = Integer.parseInt(sc.nextLine());
                                    if (indexAnime < 0 || indexAnime > mostLikedAnime.size()) {
                                        System.out.println("Wrong Command!");
                                        //this.advancedSearch();
                                        break;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("Wrong Command!");
                                    break;
                                }
                                ViewAnimeMenu vam = new ViewAnimeMenu();
                                vam.showMenu(mostLikedAnime.get(indexAnime - 1));
                                check1=-1;
                                break;
                            }
                            case 2: {
                                check1=-1;
                                break;

                            }
                            default: {
                                System.out.println("Wrong Command !");
                                check1=-1;
                                break;

                            }
                        }
                        if(check1==-1)
                            break;
                    }
                    check=1;
                    break;

                }//CASE 3
                case 4:{
                    System.out.println("Select:\n1)Studio\n2)Producer\nPress 0 to go back");
                    int answ=-1;
                    HashMap<Integer,String>temp=new HashMap<>();
                    temp.put(1,"studio");
                    temp.put(2,"producer");
                    while(answ==-1) {
                        try {
                            answ = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Attention! Wrong input!");
                            System.out.println("Please select an option:");
                            continue;
                        }

                        if (answ < 0 || answ > 2) {
                            System.out.println("Wrong input");
                            answ = -1;
                        }
                        if (answ == 0)
                            return null;
                    }
                        aggregation.entityProdByType(anime_collection,temp.get(answ));
                        break;
                }//CASE 4
                case 5:{
                    ArrayList<Anime> mostReviewedAnime = new ArrayList<>();
                    AnimeManagerNeo4J amn = new AnimeManagerNeo4J(dbNeo4J);
                    mostReviewedAnime = amn.mostReviewedAnime();
                    int count=0;
                    if(mostReviewedAnime.size()==0){
                        System.out.println("No Top 10 ");
                    }
                    for (Anime a : mostReviewedAnime){
                        count++;
                        System.out.println(GREEN+count+") "+RESET+a.getAnime_name());
                    }
                    System.out.println(GREEN+ "\nDo you want see one of this anime"+RESET);
                    System.out.println(GREEN+"1)"+RESET+" Yes"+"    "+GREEN+"2)"+RESET+" No");
                    int  caseValue=-1;
                    try{
                        caseValue = Integer.parseInt(sc.nextLine());
                    }
                    catch(Exception e ){
                        System.out.println("Wrong Command!");
                        break;

                    }

                    int check1=1;
                    while(check1==1) {
                        switch (caseValue) {
                            case 1: {
                                System.out.println(GREEN + "What anime do you want see ? " + RESET);
                                int indexAnime = -1;
                                try {
                                    indexAnime = Integer.parseInt(sc.nextLine());
                                    if (indexAnime < 0 || indexAnime > mostReviewedAnime.size()) {
                                        System.out.println("Wrong Command!");
                                        break;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("Wrong Command!");
                                    break;
                                }
                                ViewAnimeMenu vam = new ViewAnimeMenu();
                                vam.showMenu(mostReviewedAnime.get(indexAnime - 1));
                                check1=-1;
                                break;
                            }
                            case 2: {
                                check1=-1;
                                break;

                            }
                            default: {
                                System.out.println("Wrong Command !");
                                check1=-1;
                                break;
                            }
                        }
                        if(check1==-1)
                            break;
                    }

                }//CASE 3
                case 6:{
                    if(user!=null) {
                        ArrayList<String> suggestedAnime = new ArrayList<>();
                        AnimeManagerNeo4J am = new AnimeManagerNeo4J(dbNeo4J);
                        suggestedAnime = am.getNSuggestedAnime(user.getUsername(), 10);
                        int count = 1;
                        for (String s : suggestedAnime) {
                            System.out.println(GREEN + count + ") " + RESET + s);
                            count++;
                        }
                        if (suggestedAnime.isEmpty()) {
                            System.out.println("No suggested anime found");
                            break;
                        } else {
                            System.out.println(GREEN + "\nDo you want see one of this anime" + RESET);
                            System.out.println(GREEN + "1)" + RESET + " Yes" + "    " + GREEN + "2)" + RESET + " No");
                            int caseValue = -1;
                            try {
                                caseValue = Integer.parseInt(sc.nextLine());
                            } catch (Exception e) {
                                System.out.println("Wrong Command!");
                                break;

                            }

                            int check1 = 1;
                            while (check1 == 1) {
                                switch (caseValue) {
                                    case 1: {
                                        System.out.println(GREEN + "What anime do you want see ? " + RESET);
                                        int indexAnime = -1;
                                        try {
                                            indexAnime = Integer.parseInt(sc.nextLine());
                                            if (indexAnime < 0 || indexAnime > suggestedAnime.size()) {
                                                System.out.println("Wrong Command!");
                                                break;
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("Wrong Command!");
                                            break;
                                        }
                                        ViewAnimeMenu vam = new ViewAnimeMenu();
                                        Anime a = new Anime();
                                        a.setAnime_name(suggestedAnime.get(indexAnime - 1));
                                        vam.showMenu(a);
                                        check1 = -1;
                                        break;
                                    }
                                    case 2: {
                                        check1 = -1;
                                        break;

                                    }
                                    default: {
                                        System.out.println("Wrong Command !");
                                        check1 = -1;
                                        break;
                                    }
                                }
                                if (check1 == -1)
                                    break;
                            }

                        }
                    }
                    else{
                        System.out.println("You must register to do this operation");
                    }
                }
                case 0:{
                    check=1; break;
                }
                default:{
                    System.out.println("Attention! Wrong command!");
                    break;
                }



            }//SWITCH
        }//WHILE CHECK 0
            return null;
    }

    public Anime top10byGenre() {
        Scanner sc = new Scanner(System.in);
        int select = -1;
        List<String> genresChoosen = new ArrayList<>();
        HashMap<Integer, String> animeResults;
        int pos = 0;
        while (select != 0) {
            int yn = -1; //YES OR NO VALUE
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
            if (select == 2) {
                genresChoosen.add("Drama");
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
            if (select == 3) {
                genresChoosen.add("Gag");
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
            if (select == 4) {
                genresChoosen.add("Mecha");
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
            if (select == 5) {
                genresChoosen.add("Sports");
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
            if (select == 6) {
                genresChoosen.add("Slice of Life");
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
            if (select == 7) {
                genresChoosen.add("Music");
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
            if (select == 8) {
                genresChoosen.add("Thriller");
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
            if (select == 9) {
                genresChoosen.add("Shoujo");
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
            if (select == 10) {
                genresChoosen.add("Hentai");
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
            if (select == 11) {
                genresChoosen.add("Shounen");
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
            if (select == 12) {
                genresChoosen.add("Seinen");
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
            if (select == 13) {
                genresChoosen.add("Josei");
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
            if (select == 14) {
                genresChoosen.add("Isekai");
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
        if (pos == 0) {
            return null;
        } else {
            inte = new Interface();
            String[] arrayGenres = new String[pos];
            for (int i = 0; i < genresChoosen.size(); i++)
                arrayGenres[i] = genresChoosen.get(i);
            //Insertion of the animes founded into an hashmap and selection of
            //an anime to visit
            animeResults = aggregation.topTenAnimeByField(anime_collection, "genre", 0, null, arrayGenres, 1);
            Anime tt= this.pickAnime(animeResults);
            if(tt!=null)
            animeMenu.showMenu(tt);
            return null;

        } //RESEARCH BY GENRE
    }



}

