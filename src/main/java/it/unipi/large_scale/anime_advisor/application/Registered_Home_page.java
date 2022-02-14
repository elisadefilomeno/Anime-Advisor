package it.unipi.large_scale.anime_advisor.application;

import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.*;
import static it.unipi.large_scale.anime_advisor.application.Main.userManagerNeo4J;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;


public class Registered_Home_page {
    private final BrowseAnimeMenu browseAnimeMenu = new BrowseAnimeMenu();
    private final BrowseUsersMenu browseUsersMenu = new BrowseUsersMenu();
    private final PersonalProfileUserMenu profileUserMenu = new PersonalProfileUserMenu();

    public void showMenu(){
        int check=1;
        while(check==1){
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println(GREEN+"HOME PAGE"+RESET);
            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println(GREEN+"1) "+RESET+"Browse Anime");
            System.out.println(GREEN+"2) "+RESET+"Browse Users");
            System.out.println(GREEN+"3) "+RESET+"Visualize your profile");
            System.out.println(GREEN+"0) "+RESET+"Log out");
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println("Write your command here:");

            Scanner sc =new Scanner(System.in);
            int value_case;
            try{
                value_case = Integer.parseInt(sc.nextLine());
            }
            catch(Exception e){
                System.out.println("ATTENTION! Wrong command");
                break;
            }
            switch (value_case) {
                case 1 -> {
                    browseAnimeMenu.showMenu();
                    break;
                    }
                case 2 -> {
                    browseUsersMenu.showMenu();
                    break;
                }
                case 3 -> {
                    profileUserMenu.showMenu();
                    break;
                }
                case 0 -> {
                    if(user!=null){
                        userManagerNeo4J.logOut(user);
                        System.out.println(BLUE+"**************************************"+RESET);
                        System.out.println(BLUE+"GoodBye "+user.getUsername()+" !!!"+RESET);
                        System.out.println(BLUE+"**************************************"+RESET);
                        user=null;
                        check=-1;
                    }
                    return;
                }
                default ->{
                    System.out.println("ATTENTION! Wrong command");
                }
            }
            if(check==-1)
                return;

        }
    }
}
