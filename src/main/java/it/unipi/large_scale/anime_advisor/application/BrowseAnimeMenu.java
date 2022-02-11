package it.unipi.large_scale.anime_advisor.application;


import it.unipi.large_scale.anime_advisor.animeManager.*;
import it.unipi.large_scale.anime_advisor.entity.*;
import org.bson.Document;

import javax.print.Doc;
import java.util.*;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.*;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.Main.anime_collection;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;


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
    AnimeManagerNeo4J animeNeo;
    Anime anime=new Anime();
    Interface inte;

    public void showMenu() {


        //menu with extra option for admin
        if (checkIsAdmin(user)) {
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println(GREEN + "BROWSE ANIME PAGE" + RESET);
            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println("1) Find anime by name");
            System.out.println("2) Find anime by genre");
            System.out.println("3) Advanced search");
            System.out.println("4) Update Anime");
            System.out.println("5) Insert Anime");
            System.out.println("6) Delete Anime");
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
                case 1: //FIND BY NAME
                    Anime a=this.findAnime();
                    if(a!=null)
                        animeMenu.showMenu(a);
                    else
                        this.showMenu();
                    // this.browseAnimeTitle();
                    break;
                case 2: //FIND BY GENRE
                        this.researchByGenre();
                        break;
                case 3: //ADVANCED SEARCH
                    this.researchByGenre();
                    break;
                case 4: //UPDATE
                    Anime b=this.findAnime();
                    if(b!=null)
                        this.updateAnime(b);
                    else
                        this.showMenu();                    break;
                case 5: //INSERT
                    this.createAnimeFromInput();
                    break;
                case 6: //DELETE
                    Anime c=this.findAnime();
                    if(c!=null)
                        this.deleteAnimeFromInput(c);
                    else
                        this.showMenu();
                    break;
                    //case 3 : profileUserMenu.showMenu();
                case 0:
                    return;
                default:
                    System.out.println("Wrong command!");
                    this.showMenu();
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
                case 1: //FIND BY NAME
                    Anime anime=this.findAnime();
                    if (anime!=null)
                        animeMenu.showMenu(this.findAnime());
                    else
                        this.showMenu();
                    // this.browseAnimeTitle();
                    break;
                case 2: //FIND BY GENRE
                    this.researchByGenre();
                    break;
                case 3: //ADVANCED SEARCH
                    this.researchByGenre();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Wrong command!");
                    break;
            }
        }
    } //SHOW MENU

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
            if (pos == 0)
                return null;
               //this.showMenu();
        }
        if(results==null){
            System.out.println("No document found");
            return null;
        }
        a.setAnime_name(results.get(pos));
        return a;
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
    } //BROWSE ANIME

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
                    inte=new Interface();
                    String[] arrayGenres= new String[pos];
                    for(int i=0;i<genresChoosen.size();i++)
                        arrayGenres[i]=genresChoosen.get(i);
                    //Insertion of the animes founded into an hashmap and selection of
                    //an anime to visit
                    animeResults= aggregation.topTenAnimeByField(anime_collection,"genre",0,null,arrayGenres,0);
                    inte.printResults(animeResults);
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
            } //RESEARCH BY GENRE


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
            System.out.println("What do you want to update?\n1)name\n2)episodes\n3)year\n4)source\n"+
                    "5)type\n6)genre\n7)studio\n8)producer\n9)licensor");
            Scanner sc=new Scanner(System.in);
            int input;
            int check=0;
            while (check==0) {
                System.out.println("Choose an option 1-9 or Press 0 to go Back");
                try {
                    input = Integer.parseInt(sc.nextLine());
                    check = 1;
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
                    case 0: this.showMenu();
                    case 1:{
                        String newName=new String();
                        System.out.println("Insert new name: ");
                        sc.nextLine();
                        try {
                            crud.updateAnimeName(anime,anime_collection,newName);
                            animeNeo= new AnimeManagerNeo4J(dbNeo4J);
                            animeNeo.updateAnimeTitle(anime.getAnime_name(),newName);
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
                                ep = sc.nextInt();
                                if (ep < 0) {
                                    System.out.println("Invalid input");
                                    continue;
                                }
                                check=1;
                            } catch (InputMismatchException e) {
                                System.out.println("Error input");
                                continue;
                            }
                        }
                        if (ep==0)
                            this.showMenu();
                        crud.updateAnimeEpisodes(anime,anime_collection,ep);
                        continue;
                    }//CASE 2
                    case 3:{
                        System.out.println("Insert the number of the year or Press 0 to go Back");
                        check=-1;
                        int ep=-1;
                        while(check==-1) {
                            try {
                                ep = sc.nextInt();
                                if (ep < 0) {
                                    System.out.println("Invalid input");
                                    continue;
                                }
                                check=1;
                            } catch (InputMismatchException e) {
                                System.out.println("Error input");
                                continue;
                            }
                        }
                        if (ep==0)
                            this.showMenu();
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
                        while(more==0){
                            System.out.println("Do you want to insert or delete a genre?\n1)Add\n2)Remove\n0)Go Back");
                            while(answ==-1){
                                try{
                                    answ=sc.nextInt();
                                }
                                catch (InputMismatchException e){
                                    System.out.println("Wrong input!");
                                    continue;
                                }
                            }//WHILE QUESTION
                            if(answ==0)
                                    this.showMenu();
                                if(answ==1){
                                    String str=new String();
                                    System.out.println("Insert a genre to add");
                                    try{str= sc.nextLine();}
                                    catch(Exception e){System.out.println("An error has occurred");}
                                    crud.updateAnimeGenreAddOne(anime,anime_collection,str);

                                }//ADD
                                if(answ==2){
                                    System.out.println("Insert a genre to remove");
                                    String str= sc.nextLine();
                                    crud.updateAnimeGenreDeleteOne(anime,anime_collection,str);
                                }//REMOVE
                            answ=-1;
                        }//WHILE OPERATING
                        continue;
                    }//CASE 6
                    case 7:{
                        int more=0;
                        int answ=-1;
                        while(more==0){
                            System.out.println("Do you want to insert or delete a studio?\n1)Add\n2)Remove\n0)Go Back");
                            while(answ==-1){
                                try{
                                    answ=sc.nextInt();
                                }
                                catch (InputMismatchException e){
                                    System.out.println("Wrong input!");
                                    continue;
                                }
                            }//WHILE QUESTION
                            if(answ==0)
                                this.showMenu();
                            if(answ==1){
                                System.out.println("Insert a studio to add");
                                String str= sc.nextLine();
                                crud.updateAnimeStudioAddOne(anime,anime_collection,str);

                            }//ADD
                            if(answ==2){
                                System.out.println("Insert a studio to remove");
                                String str= sc.nextLine();
                                crud.updateAnimeStudioDeleteOne(anime,anime_collection,str);
                            }//REMOVE
                            answ=-1;
                        }//WHILE OPERATING
                        continue;
                    }//CASE 7
                    case 8:{
                        int more=0;
                        int answ=-1;
                        while(more==0){
                            System.out.println("Do you want to insert or delete a producer?\n1)Add\n2)Remove\n0)Go Back");
                            while(answ==-1){
                                try{
                                    answ=sc.nextInt();
                                }
                                catch (InputMismatchException e){
                                    System.out.println("Wrong input!");
                                    continue;
                                }
                            }//WHILE QUESTION
                            if(answ==0)
                                this.showMenu();
                            if(answ==1){
                                System.out.println("Insert a producer to add");
                                String str= sc.nextLine();
                                crud.updateAnimeProducerAddOne(anime,anime_collection,str);

                            }//ADD
                            if(answ==2){
                                System.out.println("Insert a producer to remove");
                                String str= sc.nextLine();
                                crud.updateAnimeProducerDeleteOne(anime,anime_collection,str);
                            }//REMOVE
                           answ=-1;
                        }//WHILE OPERATING
                        continue;
                    }//CASE 8
                    case 9:{
                        int more=0;
                        int answ=-1;
                        while(more==0){
                            System.out.println("Do you want to insert or delete a licensor?\n1)Add\n2)Remove\n0)Go Back");
                            while(answ==-1){
                                try{
                                    answ=sc.nextInt();
                                }
                                catch (InputMismatchException e){
                                    System.out.println("Wrong input!");
                                    continue;
                                }
                            }//WHILE QUESTION
                            if(answ==0)
                                this.showMenu();
                            if(answ==1){
                                System.out.println("Insert a licensor to add");
                                String str= sc.nextLine();
                                crud.updateAnimeLicensorAddOne(anime,anime_collection,str);

                            }//ADD
                            if(answ==2){
                                System.out.println("Insert a licensor to remove");
                                String str= sc.nextLine();
                                crud.updateAnimeLicensorDeleteOne(anime,anime_collection,str);
                            }//REMOVE
                           answ=-1;
                        }//WHILE OPERATING
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



}






 /*  public void deleteAnimeFromInput(){
        System.out.println("Insert the Anime name to delete: ");
        Scanner sc=new Scanner(System.in);
        String name=null;
        animeNeo= new AnimeManagerNeo4J(dbNeo4J);
        try{
        name= sc.nextLine().toString();}
        catch(Exception e){
            System.out.println("Invalid input");
        }
        Anime temp=new Anime();
        temp.setAnime_name(name);
        Document backup_doc= crud.getAnime(name,anime_collection);
        if(crud.deleteAnime(temp,anime_collection)){ //MONGO DELETE
            if(animeNeo.deleteAnime(name)) //DELETE NEO4J
                System.out.println("Anime "+name+" deleted successfully");
            else {
                System.out.println("Error during the elimination of the element");
                anime_collection.insertOne(backup_doc);     //IF NEO4J FAILS TO DELETE THE FILE GET REINSERTED INTO MONGO
            }
        }
        else
            System.out.println("Error during the elimination.\nCannot delete");
        this.showMenu();
    }  */





