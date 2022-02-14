package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.entity.User;

import java.util.HashMap;
import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.*;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Main.userManagerNeo4J;
import it.unipi.large_scale.anime_advisor.application.BrowseAnimeMenu;


public class Interface {
    public static User user=null;
    private Registered_Home_page registered_home_page = new Registered_Home_page();
    private BrowseAnimeMenu browse_anime_menu= new BrowseAnimeMenu();

    public void showMenu(){
        int value_case =0;
        System.out.println(BLUE+"WELCOME IN ANIME ADVISOR!"+RESET);
        do{
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println(GREEN+"MENU"+RESET);
            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println(GREEN+"1) "+RESET+"Log in");
            System.out.println(GREEN+"2) "+RESET+"Sign up");
            System.out.println(GREEN+"3) "+RESET+"Browse Anime");
            System.out.println(GREEN+"0) "+RESET+"Exit");
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println("Write your command here:");

            Scanner sc =new Scanner(System.in);

            try{
                value_case = Integer.parseInt(sc.nextLine());
            }
            catch(Exception e){
                System.out.println("ATTENTION! Wrong command");
                value_case=-1;
            }
            if(value_case == 1) {
                // call login function
                User logged_user = userManagerNeo4J.logIn();
                if(logged_user == null){
                    System.out.println("Login failed");

                }
                else{
                    user = logged_user;
                    System.out.println(GREEN+"**************************************"+RESET);
                    System.out.println(GREEN+"WELCOME BACK "+ user.getUsername()+"!"+RESET);
                    registered_home_page.showMenu();

                }


            }

            else if(value_case==2){
                userManagerNeo4J.signUp();  // change function name to SignUp
                System.out.println("End signUp");

            }
            else if(value_case==3){
                browse_anime_menu.showMenu();

            }
        }

        while(value_case!=0);

        if(user!=null){
            userManagerNeo4J.logOut(user);
        }

        System.out.println(BLUE+"GOODBYE"+RESET);

        System.out.println(BLUE+"COME BACK SOON! :)"+RESET);
    }

    public void printResults(HashMap<Integer,String> map){
        int elements=map.size();
        int temp=0;
        int answ=0;
        Scanner sc=new Scanner(System.in);
        for(int i=0;i<elements;i++){
            answ=0;
            System.out.println(GREEN+(i+1)+") "+RESET+map.get(i+1));
            temp++;
            if(temp==10) {
                while (answ != 1 && answ != 2) {
                    try {
                        System.out.println("Do you want to see more results?\n1) YES 2) NO");
                        answ = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ATTENTION! Wrong command\n");
                        continue;
                    }
                }
                if (answ == 1) {
                    temp = 0;
                }
                if (answ == 2)
                    break;

            }
        }
        return;
    }



}
