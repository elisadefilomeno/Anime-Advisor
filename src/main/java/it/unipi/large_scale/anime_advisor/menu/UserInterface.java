package it.unipi.large_scale.anime_advisor.menu;

import it.unipi.large_scale.anime_advisor.entity.User;

import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.menu.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.menu.ConsoleColors.RESET;

public class UserInterface {
    public void show_home_page(User u){
        System.out.println(GREEN+"**************************************"+RESET);
        System.out.println(GREEN+"HOME PAGE"+RESET);
        System.out.println("What would you like to do?");
        System.out.println("Digit:");
        System.out.println("1 to Browse Anime");
        System.out.println("2 to Browse Users");
        System.out.println("2 to Visualize your profile");
        System.out.println("0 to Go Back");
        System.out.println(GREEN+"**************************************"+RESET);
        System.out.println("Write your command here:");

        Scanner sc =new Scanner(System.in);
        int value_case =0;
        try{
            value_case = Integer.valueOf(sc.nextLine());
        }
        catch(Exception e){
            System.out.println("ATTENTION! Wrong command");
            return;
        }
        switch(value_case) {
            case 1:{
                break;
            }


            case 2:
            {
                break;
            }



            default:
                return;
        }

    }


    public void show_admin_home_page(User u) {
        // can see analytics and modify databases
    }
}
