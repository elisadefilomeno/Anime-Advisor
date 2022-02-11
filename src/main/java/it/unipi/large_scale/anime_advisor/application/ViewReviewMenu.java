package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.Review;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.reviewManager.ReviewManagerNeo4J;

import java.util.ArrayList;
import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.Main.anime_collection;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;

public class ViewReviewMenu {
    public void showReviewMenu(ArrayList<Review> list, Anime a) {
        if (checkIsAdmin(user) && user!=null) {
            System.out.println(GREEN + "**************************************" + RESET);

            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println("1) Read one reviews");
            System.out.println("2) Read information about the author");
            System.out.println("3) Delete review ");
            System.out.println("0) Exit");
            System.out.println(GREEN + "**************************************" + RESET);

            System.out.println("Write your command here:");
            Scanner sc = new Scanner(System.in);
            int value_case = -1;

            try {
                value_case = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");
                this.showReviewMenu(list, a);
            }

            switch (value_case) {
                case 1: {
                    ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                    int numberTitle = -1;


                    int count = 0;
                    for (Review r : list) {
                        count++;
                        System.out.println(GREEN + count + ")" + RESET + "  " + r.getTitle());
                    }
                    System.out.println(GREEN + "**************************************" + RESET);

                    System.out.println("Select the review that you want read");
                    try {
                        numberTitle = Integer.parseInt(sc.nextLine());
                        int maxIndex = 0;
                        if (list.size() < 11) {
                            maxIndex = list.size();
                        } else {
                            maxIndex = 10;
                        }
                        if (numberTitle > maxIndex || numberTitle <=0) {
                            System.out.println("ATTENTION! Wrong command");
                            this.showReviewMenu(list, a);
                        }
                    } catch (Exception e) {
                        System.out.println("ATTENTION! Wrong command");
                        this.showReviewMenu(list, a);
                    }

                    //-1 perche' in input valori partono da 1
                    System.out.println(list.get(numberTitle - 1).getText());
                    this.showReviewMenu(list, a);
                }

                case 2: {
                    System.out.println("Prendere funzione di Eli");
                }
                case 3 : {
                    ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                    System.out.println("Deleting Review ...");
                    int count=0;
                    for (Review r : list) {
                        count++;
                        System.out.println(GREEN + count + ")" + RESET + "  " + r.getTitle());
                    }
                    int numberTitle=-1;
                    try {
                        System.out.println("Choose the review that you want delete");

                        numberTitle = Integer.parseInt(sc.nextLine());
                        int maxIndex = 0;
                        if (list.size() < 11) {
                            maxIndex = list.size();
                        } else {
                            maxIndex = 10;
                        }
                        if (numberTitle > maxIndex || numberTitle <=0) {
                            System.out.println("ATTENTION! HahWrong command");
                            this.showReviewMenu(list, a);
                        }
                        else{
                            System.out.println("Deletion successfully !");
                            rm.deleteReview(list.get(numberTitle-1).getTitle());
                            list.remove(numberTitle-1);
                            BrowseReviewsMenu brm = new BrowseReviewsMenu();
                            brm.showMenu(a);

                        }
                    } catch (Exception e) {
                        System.out.println("ATTENTION! Wrong command");
                        this.showReviewMenu(list, a);
                    }


                }




                case 0: {
                    BrowseReviewsMenu brm = new BrowseReviewsMenu();
                    brm.showMenu(a);
                }
                default:
                    System.out.println("Attention ! Wrong command");
            }
        }
        else{
            System.out.println(GREEN + "**************************************" + RESET);

            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println("1) Read one reviews");
            System.out.println("2) Read information about the author");
            System.out.println("0) Exit");
            System.out.println(GREEN + "**************************************" + RESET);

            System.out.println("Write your command here:");
            Scanner sc = new Scanner(System.in);
            int value_case = -1;

            try {
                value_case = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");
                this.showReviewMenu(list, a);
            }

            switch (value_case) {
                case 1: {
                    ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                    int numberTitle = -1;


                    int count = 0;
                    for (Review r : list) {
                        count++;
                        System.out.println(GREEN + count + ")" + RESET + "  " + r.getTitle());
                    }
                    System.out.println(GREEN + "**************************************" + RESET);

                    System.out.println("Select the review that you want read");
                    try {
                        numberTitle = Integer.parseInt(sc.nextLine());
                        int maxIndex = 0;
                        if (list.size() < 11) {
                            maxIndex = list.size();
                        } else {
                            maxIndex = 10;
                        }
                        if (numberTitle > maxIndex || numberTitle <= 0) {
                            System.out.println("ATTENTION! Wrong command");
                            this.showReviewMenu(list, a);
                        }
                    } catch (Exception e) {
                        System.out.println("ATTENTION! Wrong command");
                        this.showReviewMenu(list, a);
                    }

                    //-1 perche' in input valori partono da 1
                    System.out.println(list.get(numberTitle - 1).getText());
                    this.showReviewMenu(list, a);
                }

                case 2: {
                    System.out.println("Prendere funzione di Eli");
                }
                case 0: {
                    BrowseReviewsMenu brm = new BrowseReviewsMenu();
                    brm.showMenu(a);
                }
                default:
                    System.out.println("Attention ! Wrong command");
            }
        }

        //1) vede info review, autore,...
        // 2) vuoi leggere il testo?
        // 3) if user è admin mostra l'opzione di eliminare la review
    }
    public boolean checkIsAdmin(User user){
        if(user==null){
            return false;
        }
        return user.getIs_admin();
    }
}
