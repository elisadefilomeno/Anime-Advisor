package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.entity.User;

import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.*;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Main.userManagerNeo4J;


public class Interface {
    protected static User user;
    private Registered_Home_page registered_home_page = new Registered_Home_page();

    public void showMenu(){
        int value_case =0;
        System.out.println(BLUE+"WELCOME IN ANIME ADVISOR!"+RESET);
        do{
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println(GREEN+"MENU"+RESET);
            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println("1) Log in");
            System.out.println("2) Sign up");
            System.out.println("3) Browse Anime");
            System.out.println("0) Exit");
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println("Write your command here:");

            Scanner sc =new Scanner(System.in);

            try{
                value_case = Integer.parseInt(sc.nextLine());
            }
            catch(Exception e){
                System.out.println("ATTENTION! Wrong command");
                value_case=4;
            }
            if(value_case == 1) {
                // call login function
                User logged_user = userManagerNeo4J.logIn();
                if(logged_user == null){
                    System.out.println("Login failed");
                    continue;
                }
                else{
                    user = logged_user;
                    System.out.println(GREEN+"**************************************"+RESET);
                    System.out.println(GREEN+"WELCOME BACK "+ user.getUsername()+"!"+RESET);
                    registered_home_page.showMenu();
                }

                continue;
            }

            else if(value_case==2){
                userManagerNeo4J.signUp();  // change function name to SignUp
                System.out.println("fine signUp");
                continue;
            }
            else if(value_case==3){

                System.out.println("TO DO: Browse Anime");
                continue;
            }
        }

        while(value_case!=0);
        if(user!=null){
            userManagerNeo4J.logOut(user);
        }

        System.out.println(BLUE+"GOODBYE"+RESET);

        System.out.println(BLUE+"COME BACK SOON! :)"+RESET);
    }
}
