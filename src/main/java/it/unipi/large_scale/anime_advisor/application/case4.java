package it.unipi.large_scale.anime_advisor.application;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.*;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;

public class case4 {
    public void viewSuggestedUsers(){
        UserManagerNeo4J userManagerNeo4J = new UserManagerNeo4J(dbNeo4J);
        List<String> suggested_users = userManagerNeo4J.getNSuggestedUsers(user.getUsername(),5);
        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("Suggested Users:");
        HashMap<Integer, String> user_map_to_access_users= new HashMap<>();
        Interface anInterface = new Interface();
        int key = 0;
        if(!suggested_users.isEmpty()){
            for(String user: suggested_users){
                key++;
                user_map_to_access_users.put(key, user);
            }
            anInterface.printResults(user_map_to_access_users);
        }
        else {
            System.out.println("You don't have any suggested user!");
            this.showMenu();
        }

        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("What would you like to do?");
        System.out.println("Digit:");
        System.out.println("0) Go Back");
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
                ViewUserMenu viewUserMenu = new ViewUserMenu();
                viewUserMenu.showMenu(u);
            }
            case 0 -> this.showMenu();
            default -> {System.out.println("ATTENTION! Wrong command");
                this.showMenu();
        }
    }
}
