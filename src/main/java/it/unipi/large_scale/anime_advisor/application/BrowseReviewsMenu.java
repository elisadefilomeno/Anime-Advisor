package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.Review;
import it.unipi.large_scale.anime_advisor.reviewManager.ReviewManagerNeo4J;

import java.util.ArrayList;
import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.*;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;


public class BrowseReviewsMenu {

    public void showMenu(Anime a){
        // 1) filter review that contains in the text the input string
        // 2) view latest reviews
        // 3) if (user==admin) delete review
        // 4) go back to registered home page

        System.out.println(GREEN+"**************************************"+RESET);
        System.out.println(GREEN+"REVIEWS MENU"+RESET);
        System.out.println("What would you like to do?");
        System.out.println("Digit:");
        System.out.println("1) Show all the reviews");
        System.out.println("2) View latest reviews");
        System.out.println("3) Write a review");
        System.out.println("4) Find a review by keyword");
        System.out.println("0) Exit");
        System.out.println(GREEN+"**************************************"+RESET);

        System.out.println("Write your command here:");
        Scanner sc = new Scanner(System.in);
        int value_case = -1;

        try {
            value_case = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("ATTENTION! Wrong command");
            this.showMenu(a);
        }
        switch (value_case) {
            case 1:{

                ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                ArrayList<Review> list =  new ArrayList<>();
                list =rm.list_ReviewFound(a);
                int count=0;
                for(Review r: list){
                    count++;
                    System.out.println(GREEN+count+")"+RESET+"  "+r.getTitle());
                }
                ViewReviewMenu viewReviewMenu = new ViewReviewMenu ();
                viewReviewMenu.showReviewMenu(list,a);

            }

            case 2: {
                ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                ArrayList<Review> list =  new ArrayList<>();
                list =rm.listLatestReviewByAnime(a);
                int count=0;
                for(Review r: list){
                    count++;
                    System.out.println(GREEN+count+")"+RESET+"  "+r.getTitle() + WHITE+"   "+r.getLast_update()+RESET);
                }
                ViewReviewMenu viewReviewMenu = new ViewReviewMenu ();
                viewReviewMenu.showReviewMenu(list,a);



            }

            case 3:{
                ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                String titleReview,textReview;

                System.out.println("Insert the title of your review : ");
                titleReview= sc.nextLine();
                if(rm.checkIfPresent(titleReview)){
                    System.out.println("Change title review ");
                    this.showMenu(a);
                }
                System.out.println("Write your review : ");
                textReview=sc.nextLine();

                Review newReview = new Review();

                newReview.setTitle(titleReview);
                newReview.setText(textReview);

                rm.createReview(newReview,a,user);

                this.showMenu(a);

            }

            case 4:{
                ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                ArrayList<Review> list =  new ArrayList<>();
                String keyWord;

                System.out.println("Insert the keyword : ");
                keyWord=sc.nextLine();

                list=rm.filterReviewByKeyWord(a,keyWord);
                int count=-1;

                for (Review r:list){
                    count++;
                    System.out.println(GREEN+count+")"+RESET+"  "+r.getTitle() + WHITE+"   "+r.getLast_update()+RESET);
                }
                if(!list.isEmpty()){
                    ViewReviewMenu vrm = new ViewReviewMenu ();
                    vrm.showReviewMenu(list,a);
                }
                else
                {
                    System.out.println("Not found result !");
                    this.showMenu(a);
                }

            }



            case 0: {
                BrowseAnimeMenu browseAnimeMenu = new BrowseAnimeMenu();
                browseAnimeMenu.showMenu();
            }
            default:{
                System.out.println("Wrong command !!!");
                this.showMenu(a);
            }

        }

    }
}
