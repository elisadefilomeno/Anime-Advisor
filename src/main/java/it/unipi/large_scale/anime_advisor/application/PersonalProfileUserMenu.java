package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;


import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;


public class PersonalProfileUserMenu {
    private UserManagerNeo4J userManagerNeo4J;
    private BrowseAnimeMenu browseAnimeMenu;
    private Registered_Home_page registered_home_page;
    
    
    public void showMenu(){
        userManagerNeo4J = new UserManagerNeo4J(dbNeo4J);

        System.out.println("TO DO: VISUALIZE PROFILE");
        System.out.println(GREEN+"**************************************"+RESET);
        System.out.println(GREEN+"YOUR PROFILE:"+RESET);
        StringBuilder string = new StringBuilder();
        if(user.getIs_admin())
            string.append("Status: Admin\n");
        else
            string.append("Status: Not an Admin\n");
        string.append(
                "Username: " + user.getUsername() + "\n" +
                        "Password: " + user.getPassword() + "\n" +
                        "Gender: " + user.getGender()
        );

        System.out.println(string);
        System.out.println(GREEN+"**************************************"+RESET);
        System.out.println("What would you like to do?");
        System.out.println("Digit:");
        System.out.println("0) Go Back To Home Page");
        System.out.println("1) Modify your profile");
        System.out.println("2) View all the anime you are following");
        System.out.println("3) View all the users you are following");
        System.out.println("4) View all the reviews you have posted");

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
            case 1 -> modifyProfile();
            case 2 -> viewFollowedAnime();
            case 3 -> viewFollowedUsers();
            case 4 -> viewPostedReviews();
            case 0 -> {
                this.registered_home_page = new Registered_Home_page();
                registered_home_page.showMenu();
            }
            default -> System.out.println("ATTENTION! Wrong command");
        }
    }

    private void viewPostedReviews() {
    }

    private void viewFollowedUsers() {
    }

    private void viewFollowedAnime() {
    }

    private void modifyProfile() {
        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("What would you like to modify?");
        System.out.println("Digit:");
        System.out.println("0) Go Back");
        System.out.println("1) Modify your username");
        System.out.println("2) Modify your password");
        System.out.println("3) Modify your gender");
        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("Write your command here:");
        Scanner sc = new Scanner(System.in);
        int value_case;
        try {
            value_case = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("ATTENTION! Wrong command");
            value_case = 0;
        }
        switch (value_case) {
            case 1 -> {
                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println("Write your new username here:");
                Scanner scanner = new Scanner(System.in);
                String new_username = sc.nextLine();
                user = userManagerNeo4J.modifyUsername(user, new_username);
                showMenu();
            }
            case 2 -> viewFollowedAnime(); // TO DO
            case 3 -> viewFollowedUsers(); // TO DO
            case 4 -> viewPostedReviews();// TO DO
            case 0 -> showMenu();
            default -> {
                System.out.println("ATTENTION! Wrong command");
                showMenu();
            }

        }


    }
}
