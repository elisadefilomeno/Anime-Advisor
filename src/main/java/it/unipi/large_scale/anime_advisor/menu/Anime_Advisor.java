package it.unipi.large_scale.anime_advisor.menu;

import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;


import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.menu.ConsoleColors.RED;
import static it.unipi.large_scale.anime_advisor.menu.ConsoleColors.BLUE;
import static it.unipi.large_scale.anime_advisor.menu.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.menu.ConsoleColors.RESET;

public class Anime_Advisor {
    static private DbManagerNeo4J dbNeo4J;
    static private UserManagerNeo4J userManagerNeo4J;

    public static void main(String argd[]) throws Exception {
        try{
            // connections to databases
            dbNeo4J = new DbManagerNeo4J();
            userManagerNeo4J = new UserManagerNeo4J();

        } catch (Exception e) {
            System.out.println("Error");
            return;
        }
        int value_case =0;
        System.out.println(BLUE+"WELCOME IN ANIME ADVISOR!"+RESET);
        do{
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println(GREEN+"MENU"+RESET);
            System.out.println("What would you like to do?");
            System.out.println("Select:");
            System.out.println("1) Log in");
            System.out.println("2) Sign up");
            System.out.println("0) Exit");
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println("Write your command here:");

            Scanner sc =new Scanner(System.in);

            try{
                value_case = Integer.valueOf(sc.nextLine());
            }
            catch(Exception e){
                System.out.println("ATTENTION! Wrong command");
                value_case=4;
            }
            if(value_case == 1) {

                System.out.println("TO DO: Log in page");
                // call login function
                User user = userManagerNeo4J.logIn();
                continue;
            }

            else if(value_case==2){
                System.out.println("TO DO: Sign up page");
                continue;
            }
        }

        while(value_case!=0);
        System.out.println(BLUE+"GOODBYE"+RESET);

        System.out.println(BLUE+"COME BACK SOON! :)"+RESET);
        dbNeo4J.closeNeo4J();
    }
}
