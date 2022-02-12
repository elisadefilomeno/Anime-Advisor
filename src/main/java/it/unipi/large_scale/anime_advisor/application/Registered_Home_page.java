package it.unipi.large_scale.anime_advisor.application;

import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Main.userManagerNeo4J;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;


public class Registered_Home_page {
    private final BrowseAnimeMenu browseAnimeMenu = new BrowseAnimeMenu();
    private final BrowseUsersMenu browseUsersMenu = new BrowseUsersMenu();
    private final PersonalProfileUserMenu profileUserMenu = new PersonalProfileUserMenu();

    public void showMenu(){
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
            case 1 -> browseAnimeMenu.showMenu();
            case 2 -> browseUsersMenu.showMenu();
            case 3 -> profileUserMenu.showMenu();
            case 0 -> {if(user!=null){
                userManagerNeo4J.logOut(user);
                Interface interf = new Interface();
                interf.showMenu();
            }}
            default -> System.out.println("ATTENTION! Wrong command");
        }
    }
}
