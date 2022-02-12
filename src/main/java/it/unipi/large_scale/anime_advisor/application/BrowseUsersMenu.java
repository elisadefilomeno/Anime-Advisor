package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.Review;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.reviewManager.ReviewManagerNeo4J;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;

import java.util.ArrayList;
import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.*;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;
import static it.unipi.large_scale.anime_advisor.application.Main.userManagerNeo4J;


public class BrowseUsersMenu {
    public void showMenu() {
        // 1) browse users username that starts with  keyword
        // 2) view suggested users to follow (No4J)
        // 3) view most active users (they written lots of reviews)
        // 4) go back to registered home page


    System.out.println(GREEN + "**************************************" + RESET);
    System.out.println(GREEN + "USER MENU" + RESET);
    System.out.println("What would you like to do?");
    System.out.println("Digit:");
    System.out.println("1) Find User by username");
    System.out.println("2) Show your profile");
    System.out.println("3) View most active users");

    System.out.println("0) Exit");
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
        case 1:{
            System.out.println("Insert the username :");
            String userToFind = sc.nextLine();
            if(userToFind==null){
                System.out.println("Insert the name !");
                this.showMenu();
            }

            ArrayList<User> listUsers = new ArrayList<>();
            UserManagerNeo4J um = new UserManagerNeo4J(dbNeo4J);
            listUsers = um.findUserByKeyWord(user.getUsername(),userToFind);

            int count=0;
            for(User u: listUsers){
                count++;
                System.out.println(GREEN+count+")"+RESET+"  "+u.getUsername());
            }
            System.out.println("Choose the profile that you want visualize (click 0 to come back)");
            int indexUser=0;
            try {
                indexUser = Integer.parseInt(sc.nextLine());

                int maxIndex = 0;
                if (listUsers.size() < 11) {
                    maxIndex = listUsers.size();
                } else {
                    maxIndex = 10;
                }

                if (indexUser > maxIndex || indexUser < 0) {
                    System.out.println("ATTENTION! Wrong command");
                    this.showMenu();

                }
                else if (indexUser ==0){
                    this.showMenu();
                }
                else{

                    ViewUserMenu vum = new ViewUserMenu();
                    vum.showMenu(listUsers.get(indexUser-1));
                }

            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");
                this.showMenu();
            }


        }


        case 2:{
            System.out.println("Show your profile");
            ViewUserMenu vum = new ViewUserMenu();
            vum.showMenu(user);
        }

        case 3:{
            System.out.println("Show most active users ");
            ArrayList<String> listNameUsers =  userManagerNeo4J.viewMostActiveUsers();
            int count = 0;
            for (String s : listNameUsers){
                count++;
                System.out.println(GREEN+count+") "+RESET+s);
            }
            System.out.println("Choose the profile that you want visualize (click 0 to come back)");
            int indexUser=0;
            try {
                indexUser = Integer.parseInt(sc.nextLine());

                int maxIndex = 0;
                if (listNameUsers.size() < 11) {
                    maxIndex = listNameUsers.size();
                } else {
                    maxIndex = 10;
                }

                if (indexUser > maxIndex || indexUser < 0) {
                    System.out.println("ATTENTION! Wrong command");
                    this.showMenu();

                }
                else if (indexUser ==0){
                    this.showMenu();
                }
                else{

                    ViewUserMenu vum = new ViewUserMenu();
                    User u = new User();
                    u= userManagerNeo4J.getUserFromName(listUsers.get(indexUser-1))
                    //Metti funzioni che recupera l'oggetto dato il  nome
                   // vum.showMenu(listUsers.get(indexUser-1));
                }

            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");
                this.showMenu();
            }
        }

        case 0:
        default: {
            System.out.println("Wrong command !!!");
            this.showMenu();
            }

        }

    }




}
