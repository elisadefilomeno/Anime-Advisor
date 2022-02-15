package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.Review;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.reviewManager.ReviewManagerNeo4J;

import java.util.ArrayList;
import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.Main.*;

public class ViewReviewMenu {
    public void showReviewMenu(ArrayList<Review> list, Anime a) {
        if (checkIsAdmin(user) && user!=null) {
            int check = 1;
            while (check == 1) {
                System.out.println(GREEN + "**************************************" + RESET);

                System.out.println("What would you like to do?");
                System.out.println("Digit:");
                System.out.println(GREEN+"1) "+RESET+"Read one reviews");
                System.out.println(GREEN+"2) "+RESET+"Read information about the author");
                System.out.println(GREEN+"3) "+RESET+"Delete review ");
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
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("ATTENTION! Wrong command");
                            break;
                        }

                        //-1 perche' in input valori partono da 1
                        System.out.println(list.get(numberTitle - 1).getText());
                        check=-1;
                        break;
                    }

                    case 2: {
                        ViewUserMenu vum = new ViewUserMenu();
                        User uFound = new User();
                        uFound= userManagerNeo4J.getUserFromReview(list.get(0).getTitle());
                        vum.showMenu(uFound);
                        break;
                    }
                    case 3: {
                        ReviewManagerNeo4J rm = new ReviewManagerNeo4J(dbNeo4J);
                        System.out.println("Deleting Review ...");
                        int count = 0;
                        for (Review r : list) {
                            count++;
                            System.out.println(GREEN + count + ")" + RESET + "  " + r.getTitle());
                        }
                        int numberTitle = -1;
                        try {
                            System.out.println("Choose the review that you want delete");

                            numberTitle = Integer.parseInt(sc.nextLine());
                            int maxIndex = 0;
                            if (list.size() < 11) {
                                maxIndex = list.size();
                            } else {
                                maxIndex = 10;
                            }
                            if (numberTitle > maxIndex || numberTitle <= 0) {
                                System.out.println("ATTENTION! Wrong command");
                            }
                            else {
                                System.out.println("Deletion successfully !");
                                rm.deleteReview(list.get(numberTitle - 1).getTitle());
                                list.remove(numberTitle - 1);

                                break;

                            }
                        } catch (Exception e) {
                            System.out.println("ATTENTION! Wrong command");
                            break;
                        }

                    }

                    case 0: {
                        check = -1;
                        break;
                    }
                    default:
                        System.out.println("Attention ! Wrong command");
                        break;
                }

            }
            if(check==-1)
                return;
        }
        else {
            int check = 1;
            while (check == 1) {
                System.out.println(GREEN + "**************************************" + RESET);

                System.out.println("What would you like to do?");
                System.out.println("Digit:");
                System.out.println(GREEN+"1) "+RESET+"Read one reviews");
                System.out.println(GREEN+"2) "+RESET+"Read information about the author");
                System.out.println(GREEN+"0) "+RESET+"Go back");
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
                    case 1: {//Read one reviews
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
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("ATTENTION! Wrong command");
                            break;
                        }

                        //-1 perche' in input valori partono da 1
                        System.out.println(list.get(numberTitle - 1).getText());
                        break;
                    }

                    case 2: {
                        ViewUserMenu vum = new ViewUserMenu();
                        User uFound = new User();
                        int numberTitle=-1;
                        System.out.println(GREEN+"Choose which review you want see :"+RESET);
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
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("ATTENTION! Wrong command");
                            break;
                        }
                        System.out.println(list.get(numberTitle-1).getTitle());
                        uFound= userManagerNeo4J.getUserFromReview(list.get(numberTitle-1).getTitle());
                        System.out.println("OK TROVATO");
                        System.out.println(" "+uFound.getUsername()+" "+uFound.getGender());
                        vum.showMenu(uFound);
                        break;
                    }
                    case 0: {
                        check=-1;
                        break;
                    }
                    default: {
                        System.out.println("Attention ! Wrong command");
                        break;
                    }
                }
                if(check==-1)
                    return;

            }
        }

    }
    public boolean checkIsAdmin(User user){
        if(user==null){
            return false;
        }
        return user.getIs_admin();
    }
    public void showSingleReview(Review r) {

        int check = 1;
        while (check == 1) {
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println(GREEN + "INFORMATION REVIEW" + RESET);
            System.out.println("Title Review: " + r.getTitle());
            Anime a = new Anime();
            AnimeManagerNeo4J amn = new AnimeManagerNeo4J(dbNeo4J);
            a = amn.getAnimeFromReview(r);
            System.out.println("Title Anime: " + a.getAnime_name());
            System.out.println("Text: ");
            System.out.println(r.getText());
            System.out.println(GREEN + "**************************************" + RESET);

            System.out.println("Choose one option :");
            int value_case = -1;
            Scanner sc = new Scanner(System.in);

            System.out.println(GREEN + "1) " + RESET + "Come back to your profile");
            System.out.println(GREEN + "2) " + RESET + "See other reviews of this anime");

            try {
                value_case = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");
            }

            switch (value_case) {
                case 1: {
                    check=-1;
                    break;

                }

                case 2: {
                    BrowseReviewsMenu brm = new BrowseReviewsMenu();
                    brm.showMenu(a);
                    check=-1;
                    break;
                }

                default:
                    System.out.println("Attention ! Wrong command");
                    break;
            }
            if(check==-1)
                return;


        }
    }
}
