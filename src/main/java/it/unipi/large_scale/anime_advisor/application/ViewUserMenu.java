package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.Review;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.reviewManager.ReviewManagerNeo4J;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;

import java.util.ArrayList;
import java.util.HashMap;
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
        else {
            int check = 1;

            while (check == 1) {
                userManagerNeo4J = new UserManagerNeo4J(dbNeo4J);

                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println(GREEN + "USER PROFILE:" + RESET);
                StringBuilder string = new StringBuilder();
                string.append("Username: ").append(u.getUsername()).append("\n")
                        .append("Gender: ").append(u.getGender()).append("\n");
                if (u.getIs_admin())
                    string.append("Status: Admin");
                else
                    string.append("Status: Normal User");
                System.out.println(string);

                System.out.println("Reviews Written: " + userManagerNeo4J.getNumberReviews(u.getUsername()));
                System.out.println("Numbers Follows: " + userManagerNeo4J.getNumberUserFollow(u.getUsername()));
                System.out.println("Numbers Follower: " + userManagerNeo4J.getNumberFollowers(u.getUsername()));


                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println("What would you like to do?");
                System.out.println("Digit:");
                System.out.println(GREEN+"0) "+RESET+"Go Back To Home Page");
                System.out.println(GREEN+"1) "+RESET+"Follow this user");
                System.out.println(GREEN+"2) "+RESET+"Unfollow this user");
                System.out.println(GREEN+"3) "+RESET+"Show list anime liked");
                System.out.println(GREEN+"4) "+RESET+"Show list review written");

                if (user.getIs_admin()) {
                    System.out.println(GREEN+"5) "+RESET+"Promote this user to Admin");
                    System.out.println(GREEN+"6) "+RESET+"Retrocede this Admin to Registered User");
                    System.out.println(GREEN+"7) "+RESET+"Delete this user");
                }

                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println("Write your command here:");
                Scanner sc = new Scanner(System.in);
                int value_case;
                try {
                    value_case = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("ATTENTION! Wrong command");
                    break;
                }
                switch (value_case) {
                    case 1 -> {//follow this user
                        userManagerNeo4J.followUser(user.getUsername(), u.getUsername());
                        break;
                    }
                    case 2 -> {//UNFollow this user
                        userManagerNeo4J.unfollowUser(user.getUsername(), u.getUsername());
                        break;
                    }
                    case 3 ->{ //Show anime list

                        ArrayList<Anime> listAnime = new ArrayList<>();
                        listAnime = userManagerNeo4J.getAnimeFromUser(u);
                        if(listAnime.size()==0){
                            System.out.println("No anime are followed");

                        }
                        else{
                            int count =0;
                            for(Anime a : listAnime){
                                count++;
                                System.out.println(GREEN+count+") "+RESET+a.getAnime_name());
                            }
                            seePageAnime(listAnime);


                        }
                        break;
                    }
                    case 4->{

                        ArrayList<Review> listReview = new ArrayList<>();
                        ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);


                        listReview = rm.getlistReviewByUser(u);

                        if( listReview.size()==0){
                            System.out.println("No review has been written");

                        }
                        else{
                            int count =0;
                            for(Review r : listReview){
                                count++;
                                System.out.println(GREEN+count+") "+RESET+r.getTitle());
                            }
                            seeReviewPage(listReview);

                        }
                        break;
                    }
                    case 5 -> {//Promote this user to Admin
                        if (!user.getIs_admin()) {
                            System.out.println("ATTENTION! Wrong number");
                          //  this.showMenu(u);
                            break;
                        }
                        if (u.getIs_admin()) {
                            System.out.println("This user is already an Admin!");
                            //this.showMenu(u);
                            break;
                        }
                        u = userManagerNeo4J.promoteToAdmin(u);
                       // this.showMenu(u);
                        break;
                    }
                    case 6 -> {//Retrocede this Admin to Registered User
                        if (!user.getIs_admin()) {
                            System.out.println("ATTENTION! Wrong number");
                            //this.showMenu(u);
                            break;
                        }
                        if (!u.getIs_admin()) {
                            System.out.println("This user wasn't an Admin!");
                          //  this.showMenu(u);
                            break;
                        }
                        u = userManagerNeo4J.retrocedeAdmin(u);
                        //this.showMenu(u);
                        break;
                    }
                    case 7 -> { //Delete this use
                        if (!user.getIs_admin()) {
                            System.out.println("ATTENTION! Wrong number");
                            //this.showMenu(u);
                            break;
                        }
                        userManagerNeo4J.deleteUser(u);
                      /*  Registered_Home_page rgh = new Registered_Home_page();
                        rgh.showMenu();*/
                        //qui dovrei settare la check
                        break;
                    }
                    case 0 -> {//Go Back To Home Page
                        check=-1;
                        break;
                    }
                    default -> {
                        System.out.println("ATTENTION1! Wrong command");
                    }
                }
                if(check==-1)
                    return;


            }
        }
    }

    public void seePageAnime(ArrayList<Anime> list){
        if(list.isEmpty()){
            System.out.println("No anime is followed");
            return;
        }
        else {
            int value_case = -1;
            int check = 1;
            while (check == 1) {
                System.out.println("Do you want see one of this anime?\n" + GREEN + "1)" + RESET + "Yes  " + GREEN + "2)" + RESET + "No");
                Scanner sc = new Scanner(System.in);
                try {
                    value_case = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("ATTENTION! Wrong command");

                }
                switch (value_case) {
                    case 1: {
                        int indexAnime=-1;
                        System.out.println("Insert the anime's number:");
                        try {
                            indexAnime = Integer.parseInt(sc.nextLine());
                        } catch (Exception e) {
                            System.out.println("ATTENTION! Wrong command");
                            break;

                        }
                        int max_index=list.size();
                        if(indexAnime>max_index ||indexAnime<1){
                            System.out.println("ATTENTION! Wrong command");
                            break;
                        }
                        else{
                            ViewAnimeMenu vam=new ViewAnimeMenu();
                            vam.showMenu(list.get(indexAnime-1));
                            check=-1;
                            break;
                        }


                    }
                    case 2: {
                        check=-1;
                        break;

                    }
                    default:{
                        System.out.println("Attention!!! WRONG COMMAND!");
                        break;
                    }

                }
                if(check==-1)
                    return;
            }
        }
    }

    public void seeReviewPage(ArrayList<Review> list){
        if(list.isEmpty()){
            System.out.println("No Review is followed");
            return;
        }
        else {
            int value_case = -1;
            int check = 1;
            while (check == 1) {
                System.out.println("Do you want see one of this anime?\n" + GREEN + "1)" + RESET + "Yes  " + GREEN + "2)" + RESET + "No");
                Scanner sc = new Scanner(System.in);
                try {
                    value_case = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("ATTENTION! Wrong command");

                }
                switch (value_case) {
                    case 1: {
                        int indexTitle=-1;
                        System.out.println("Choose review:");
                        try {
                            indexTitle = Integer.parseInt(sc.nextLine());
                        } catch (Exception e) {
                            System.out.println("ATTENTION! Wrong command");
                            break;

                        }
                        int max_index=list.size();
                        if(indexTitle>max_index ||indexTitle<1){
                            System.out.println("ATTENTION! Wrong command");
                            break;
                        }
                        else{
                            ViewReviewMenu vrm = new ViewReviewMenu();
                            vrm.showSingleReview(list.get(indexTitle-1));
                            check=-1;
                            break;
                        }


                    }
                    case 2: {
                        check=-1;
                        break;

                    }
                    default:{
                        System.out.println("Attention!!! WRONG COMMAND!");
                        break;
                    }

                }
                if(check==-1)
                    return;
            }
        }
    }
}
