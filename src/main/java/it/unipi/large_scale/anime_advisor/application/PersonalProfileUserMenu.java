package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDBAgg;
import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerMongoDBCRUD;
import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;


import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;


public class PersonalProfileUserMenu {
    private UserManagerNeo4J userManagerNeo4J;
    private Interface anInterface;
    private AnimeManagerNeo4J animeManagerNeo4J;
    private ViewAnimeMenu viewAnimeMenu;
    private ViewUserMenu viewUserMenu;
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
        Set<String> followed_users = userManagerNeo4J.getFollowedUsers(user);
        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("Followed Users:");
        HashMap<Integer, String> user_map_to_access_users= new HashMap<>();
        anInterface = new Interface();
        int key = 0;
        if(!followed_users.isEmpty()){
            for(String user: followed_users){
                key++;
                user_map_to_access_users.put(key, user);
            }
            anInterface.printResults(user_map_to_access_users);
        }
        else
            System.out.println("You don't follow any user");

        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("What would you like to do?");
        System.out.println("Digit:");
        System.out.println("0) Go Back to your profile");
        System.out.println("1) View specific User info");

        System.out.println(GREEN+"**************************************"+RESET);
        System.out.println("Write your command here:");
        Scanner sc =new Scanner(System.in);
        int value_case=0;
        try{
            value_case = Integer.parseInt(sc.nextLine());
        }
        catch(Exception e){
            System.out.println("ATTENTION! Wrong command");
            this.showMenu();
        }
        switch (value_case) {
            case 1 -> {
                System.out.println("Insert the number of the user you want to visit: ");
                int user_number = 0;
                try{
                    user_number = Integer.parseInt(sc.nextLine());
                }
                catch(Exception e){
                    System.out.println("ATTENTION! Wrong command");
                    this.showMenu();
                }
                if(!user_map_to_access_users.containsKey(user_number)){
                    System.out.println("ATTENTION! Wrong number");
                    this.showMenu();
                }
                User u = new User();
                u.setUsername(user_map_to_access_users.get(user_number));
                viewUserMenu = new ViewUserMenu();
                viewUserMenu.showMenu(u);
            }
            case 0 -> this.showMenu();
            default -> System.out.println("ATTENTION! Wrong command");
        }
    }

    private void viewFollowedAnime() {
        Set<String> followed_anime = userManagerNeo4J.getFollowedAnime(user);
        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("Followed Anime:");
        HashMap<Integer, String> user_map_to_access_anime= new HashMap<>();
        anInterface = new Interface();
        int key = 0;
        if(!followed_anime.isEmpty()){
            for(String anime: followed_anime){
                key++;
                user_map_to_access_anime.put(key, anime);
            }
            anInterface.printResults(user_map_to_access_anime);
        }
        else
            System.out.println("You don't follow any anime");

        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("What would you like to do?");
        System.out.println("Digit:");
        System.out.println("0) Go Back to your profile");
        System.out.println("1) View specific Anime info");

        System.out.println(GREEN+"**************************************"+RESET);
        System.out.println("Write your command here:");
        Scanner sc =new Scanner(System.in);
        int value_case=0;
        try{
            value_case = Integer.parseInt(sc.nextLine());
        }
        catch(Exception e){
            System.out.println("ATTENTION! Wrong command");
            this.showMenu();
        }
        switch (value_case) {
            case 1 -> {
                System.out.println("Insert the number of the anime you want to visit: ");
                int anime_number = 0;
                try{
                    anime_number = Integer.parseInt(sc.nextLine());
                }
                catch(Exception e){
                    System.out.println("ATTENTION! Wrong command");
                    this.showMenu();
                }
                if(!user_map_to_access_anime.containsKey(anime_number)){
                    System.out.println("ATTENTION! Wrong number");
                    this.showMenu();
                }
                Anime anime = new Anime();
                anime.setAnime_name(user_map_to_access_anime.get(anime_number));
                viewAnimeMenu = new ViewAnimeMenu();
                viewAnimeMenu.showMenu(anime);
            }
            case 0 -> this.showMenu();
            default -> System.out.println("ATTENTION! Wrong command");
        }
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
