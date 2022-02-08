//package it.unipi.large_scale.anime_advisor.application;
//
//import it.unipi.large_scale.anime_advisor.entity.User;
//
//import java.util.Scanner;
//
//import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
//import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
//import static it.unipi.large_scale.anime_advisor.application.Anime_Advisor.user;
//
//public class UserInterface {
//    public void show_home_page(){
//        System.out.println(GREEN+"**************************************"+RESET);
//        System.out.println(GREEN+"HOME PAGE"+RESET);
//        System.out.println("What would you like to do?");
//        System.out.println("Digit:");
//        System.out.println("1) Browse Anime");
//        System.out.println("2) Browse Users");
//        System.out.println("3) Visualize your profile");
//        System.out.println("0) Go Back");
//        System.out.println(GREEN+"**************************************"+RESET);
//        System.out.println("Write your command here:");
//
//        Scanner sc =new Scanner(System.in);
//        int value_case;
//        try{
//            value_case = Integer.parseInt(sc.nextLine());
//        }
//        catch(Exception e){
//            System.out.println("ATTENTION! Wrong command");
//            return;
//        }
//        switch (value_case) {
//            case 1 -> this.browseAnimeMenu();
//            case 2 -> this.browseUsersMenu();
//            case 3 -> this.visualizeProfileMenu();
//            case 0 -> {}
//            default -> System.out.println("ATTENTION! Wrong command");
//        }
//
//    }
//
//    private void visualizeProfileMenu() {
//        System.out.println("TO DO: VISUALIZE PROFILE");
//        System.out.println(GREEN+"**************************************"+RESET);
//        System.out.println(GREEN+"PROFILE"+RESET);
//        System.out.println(user.toString());
//        System.out.println(GREEN+"**************************************"+RESET);
//        System.out.println("What would you like to do?");
//        System.out.println("Digit:");
//        System.out.println("0) Go Back To Home Page");
//        System.out.println("1) Modify your profile");
//        System.out.println(GREEN+"**************************************"+RESET);
//        System.out.println("Write your command here:");
//
//    }
//
//
//    private void browseUsersMenu() {
//        System.out.println("TO DO: BROWSE USERS");
//    }
//
//    private void browseAnimeMenu() {
//        System.out.println("TO DO: BROWSE ANIME");
//
//    }
//
//
//    public void show_admin_home_page(User u) {
//        // can see analytics and modify databases
//    }
//}
