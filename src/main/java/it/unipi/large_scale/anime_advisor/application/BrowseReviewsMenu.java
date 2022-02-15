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
        int check=1;
        while(check==1) {
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println(GREEN + "BROWSE REVIEWS PAGE" + RESET);
            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println(GREEN+"1) "+RESET+"Show all the reviews");
            System.out.println(GREEN+"2) "+RESET+"View latest reviews");
            System.out.println(GREEN+"3) "+RESET+"Write a review");
            System.out.println(GREEN+"4) "+RESET+"Find a review by keyword");
            if(user.getIs_admin())
                System.out.println(GREEN+"5) "+RESET+"Delete a review");

            System.out.println(GREEN+"0) "+RESET+"Exit");
            System.out.println(GREEN + "**************************************" + RESET);

            System.out.println("Write your command here:");
            Scanner sc = new Scanner(System.in);
            int value_case = -1;

            try {
                value_case = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");

            }
            switch (value_case) {
                case 1: {//Show all the reviews

                    ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                    ArrayList<Review> list = new ArrayList<>();
                    list = rm.list_ReviewFound(a);
                    int count = 0;
                    for (Review r : list) {
                        count++;
                        System.out.println(GREEN + count + ")" + RESET + "  " + r.getTitle());
                    }
                    ViewReviewMenu viewReviewMenu = new ViewReviewMenu();
                    viewReviewMenu.showReviewMenu(list, a);
                   // check=-1;
                    break;

                }

                case 2: {//View latest reviews
                    ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                    ArrayList<Review> list = new ArrayList<>();
                    list = rm.listLatestReviewByAnime(a);
                    int count = 0;
                    for (Review r : list) {
                        count++;
                        System.out.println(GREEN + count + ")" + RESET + "  " + r.getTitle() + WHITE + "   " + r.getLast_update() + RESET);
                    }
                    ViewReviewMenu viewReviewMenu = new ViewReviewMenu();
                    viewReviewMenu.showReviewMenu(list, a);
                  //  check=-1;
                    break;
                }

                case 3: {//Write a review
                    ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                    String titleReview, textReview;

                    System.out.println("Insert the title of your review : ");
                    titleReview = sc.nextLine();
                    if (rm.checkIfPresent(titleReview)) {
                        System.out.println("Change title review ");
                        break;
                    }
                    System.out.println("Write your review : ");
                    textReview = sc.nextLine();

                    Review newReview = new Review();

                    newReview.setTitle(titleReview);
                    newReview.setText(textReview);

                    rm.createReview(newReview, a, user);

                    break;

                }

                case 4: {
                    ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                    ArrayList<Review> list = new ArrayList<>();
                    String keyWord;

                    System.out.println("Insert the keyword : ");
                    keyWord = sc.nextLine();

                    list = rm.filterReviewByKeyWord(a, keyWord);
                    int count = 0;

                    for (Review r : list) {
                        count++;
                        System.out.println(GREEN + count + ")" + RESET + "  " + r.getTitle() + WHITE + "   " + r.getLast_update() + RESET);
                    }
                    if (!list.isEmpty()) {
                        ViewReviewMenu vrm = new ViewReviewMenu();
                        vrm.showReviewMenu(list, a);
                        break;
                    } else {
                        System.out.println("Not found result !");
                        break;
                    }

                }
                case 5 :{

                    ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                    ArrayList<Review> list = new ArrayList<>();
                    list = rm.list_ReviewFound(a);
                    int count = 0;
                    for (Review r : list) {
                        count++;
                        System.out.println(GREEN + count + ")" + RESET + "  " + r.getTitle());
                    }

                    int check3=1;
                    while(check3==1) {
                        System.out.println("Do you want delete one review ?");
                        System.out.println(GREEN+"1) "+RESET+"Yes");
                        System.out.println(GREEN+"2) "+RESET+"No");
                        System.out.println("Digit: ");
                        value_case = 0;
                        try {
                            value_case = Integer.parseInt(sc.nextLine());
                        } catch (Exception e) {
                            System.out.println("ATTENTION! Wrong command");
                        }
                        switch (value_case) {
                            case 1 -> {
                                int indexAnime=-1;
                                try {
                                    System.out.println("Select the review that you want delete: ");
                                    indexAnime = Integer.parseInt(sc.nextLine());
                                } catch (Exception e) {
                                    System.out.println("ATTENTION! Wrong command");
                                }
                                if(indexAnime<1||indexAnime>list.size()){
                                    System.out.println("ATTENTION! Wrong command");
                                    break;
                                }
                                else {
                                    rm.deleteReview(list.get(indexAnime-1).getTitle());
                                    check3=-1;
                                    break;
                                }
                            }
                            case 2 ->{

                                check3=-1;
                                break;
                            }
                            default -> {
                                System.out.println("ATTENTION! Wrong command");
                                break;
                            }

                        }
                        if(check3==-1)
                            return;
                    }
                    break;
                }

                case 0: {

                    check=-1;
                    break;
                }

                default: {
                    System.out.println("Wrong command !!!");
                    break;
                }


            }
            if(check==-1)
                return;
        }
    }
}
