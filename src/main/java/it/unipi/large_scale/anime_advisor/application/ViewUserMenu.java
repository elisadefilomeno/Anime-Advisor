package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;

import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;

//1) vede info user,
// 2) vuoi seguire l'utente?
// 3) vuoi unfolloware l'utente?
// 4 e 5) if user è admin mostra l'opzione di eliminare l'utente o promuoverlo ad admin

public class ViewUserMenu {
    UserManagerNeo4J userManagerNeo4J;

    public void showMenu(User u){
        if(user==u){
            System.out.println("View your personal profile");
            PersonalProfileUserMenu personalProfileUserMenu = new PersonalProfileUserMenu();
            personalProfileUserMenu.showMenu();
        }
        userManagerNeo4J = new UserManagerNeo4J(dbNeo4J);
        System.out.println("TO DO: )View this user written reviews, liked anime, followed users");
        System.out.println(GREEN+"**************************************"+RESET);
        System.out.println(GREEN+"USER PROFILE:"+RESET);
        StringBuilder string = new StringBuilder();
        string.append("Username: ").append(u.getUsername()).append("\n")
                .append("Gender: ").append(u.getGender()).append("\n");
        if(u.getIs_admin())
            string.append("Status: Admin\n");
        else
            string.append("Status: Not an Admin\n");
        System.out.println(string);

        System.out.println(GREEN+"**************************************"+RESET);
        System.out.println("What would you like to do?");
        System.out.println("Digit:");
        System.out.println("0) Go Back To Home Page");
        System.out.println("1) Follow this user");
        System.out.println("2) Unfollow this user");
        if(user.getIs_admin()){
            System.out.println("3) Promote this user to Admin");
            System.out.println("4) Retrocede this Admin to Registered User");
            System.out.println("5) Delete this user");
        }

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
            case 1 -> {userManagerNeo4J.followUser(user.getUsername(),u.getUsername());
                this.showMenu(u);
            }
            case 2 -> {userManagerNeo4J.unfollowUser(user.getUsername(), u.getUsername());
                this.showMenu(u);
            }
            case 3 -> {
                if(!user.getIs_admin()){
                    System.out.println("ATTENTION! Wrong number");
                    this.showMenu(u);
                }
                if(u.getIs_admin()){
                    System.out.println("This user is already an Admin!");
                    this.showMenu(u);
                }
                u = userManagerNeo4J.promoteToAdmin(u);
                this.showMenu(u);
            }
            case 4 -> {
                if(!user.getIs_admin()){
                    System.out.println("ATTENTION! Wrong number");
                    this.showMenu(u);
                }
                if(!u.getIs_admin()){
                    System.out.println("This user wasn't an Admin!");
                    this.showMenu(u);
                }
                u=userManagerNeo4J.retrocedeAdmin(u);
                this.showMenu(u);
            }
            case 5 -> {
                if(!user.getIs_admin()){
                    System.out.println("ATTENTION! Wrong number");
                    this.showMenu(u);
                }
                userManagerNeo4J.deleteUser(u);
                Registered_Home_page rgh = new Registered_Home_page();
                rgh.showMenu();
            }
            case 0 -> {Registered_Home_page registered_home_page = new Registered_Home_page();
                registered_home_page.showMenu();
            }
            default -> System.out.println("ATTENTION! Wrong command");
        }




    }
}
