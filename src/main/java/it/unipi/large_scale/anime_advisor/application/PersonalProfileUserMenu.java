package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;


import java.util.Scanner;
import java.util.Set;

import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;


public class PersonalProfileUserMenu {
    private UserManagerNeo4J userManagerNeo4J;
    private AnimeManagerNeo4J animeManagerNeo4J;
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
        string.append("Username: ").append(user.getUsername()).append("\n")
                .append("Password: ").append(user.getPassword()).append("\n")
                .append("Gender: ").append(user.getGender());

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
        System.out.println("TO DO: viewPostedReviews");

    }

    private void viewFollowedUsers() {
        System.out.println("TO DO: viewFollowedUsers");

    }

    private void viewFollowedAnime() {
        animeManagerNeo4J = new AnimeManagerNeo4J(dbNeo4J);
        Set<String> followed_anime = userManagerNeo4J.getFollowedAnime(user);
        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("Followed Anime:");
        System.out.println(followed_anime);
        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("What would you like to do?");
        System.out.println("Digit:");
        System.out.println("0) Go Back");
        System.out.println("1) View specific Anime info");
        System.out.println("2) See next anime followed");


        System.out.println("TO DO: viewFollowedAnime");

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
                String new_username = sc.nextLine();
                user = userManagerNeo4J.modifyUsername(user, new_username);
                showMenu();
            }
            case 2 -> {
                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println("Write your new password here:");
                String new_password = sc.nextLine();
                User updated_user = userManagerNeo4J.updateUser(
                        new User(user.getUsername(),new_password, user.getGender(),
                                user.getLogged_in(), user.getIs_admin()));
                if(!(updated_user == null)){
                    user = updated_user;
                }
                showMenu();
            }
            case 3 -> {
                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println("Choose your new gender:");
                System.out.println("1) Female");
                System.out.println("2) Male");
                System.out.println("3) Other");
                System.out.println("4) I prefer not to specify");
                System.out.println(GREEN+"**************************************"+RESET);
                System.out.println("Write your command here:");
                String new_gender = user.getGender();

                try {
                    int gender_case = Integer.parseInt(sc.nextLine());
                    switch ((gender_case)) {
                        case 1 -> new_gender ="Female";
                        case 2 -> new_gender="Male";
                        case 3 -> new_gender="Other";
                        case 4 -> new_gender="Not specified";
                        default -> {
                            System.out.println("Invalid option!");
                            showMenu();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("ATTENTION! Wrong command");
                    showMenu();
                }
                
                User updated_user = userManagerNeo4J.updateUser(
                        new User(user.getUsername(),user.getPassword(), new_gender,
                                user.getLogged_in(), user.getIs_admin()));
                if(!(updated_user == null)){
                    user = updated_user;
                }
                showMenu();
            }
            case 0 -> showMenu();
            default -> {
                System.out.println("ATTENTION! Wrong command");
                showMenu();
            }

        }


    }
}
