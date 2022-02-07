package it.unipi.large_scale.anime_advisor.application;

import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;

public class ProfileUserMenu {
    public void showMenu(){
        System.out.println("To do");
        System.out.println("TO DO: VISUALIZE PROFILE");
        System.out.println(GREEN+"**************************************"+RESET);
        System.out.println(GREEN+"PROFILE"+RESET);
        System.out.println(user.toString());
        System.out.println(GREEN+"**************************************"+RESET);
        System.out.println("What would you like to do?");
        System.out.println("Digit:");
        System.out.println("0) Go Back To Home Page");
        System.out.println("1) Modify your profile");
        // vedere anime  e utenti seguiti
        // vedere le recenioni lasciate
        System.out.println(GREEN+"**************************************"+RESET);
        System.out.println("Write your command here:");

    }
}
