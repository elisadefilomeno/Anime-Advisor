package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.Review;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.reviewManager.ReviewManagerNeo4J;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;


public class PersonalProfileUserMenu {
    private UserManagerNeo4J userManagerNeo4J;
    private ReviewManagerNeo4J reviewManagerNeo4J;
    private Interface anInterface;

    private ViewAnimeMenu viewAnimeMenu;
    private ViewUserMenu viewUserMenu;
    private Registered_Home_page registered_home_page;
    
    
    public void showMenu() {
        userManagerNeo4J = new UserManagerNeo4J(dbNeo4J);
        int check = 1;
        while (check == 1) {
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println(GREEN + "YOUR PROFILE:" + RESET);
            StringBuilder string = new StringBuilder();

            if (user.getIs_admin())
                string.append("Status: Admin\n");
            else
                string.append("Status: Normal User\n");

            string.append("Username: ").append(user.getUsername()).append("\n")
                    .append("Password: ").append(user.getPassword()).append("\n")
                    .append("Gender: ").append(user.getGender());

            System.out.println(string);

            System.out.println("Reviews Written: " + userManagerNeo4J.getNumberReviews(user.getUsername()));
            System.out.println("Numbers Follows: " + userManagerNeo4J.getNumberUserFollow(user.getUsername()));
            System.out.println("Numbers Follower: " + userManagerNeo4J.getNumberFollowers(user.getUsername()));

            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println(GREEN + "What would you like to do?" + RESET);
            System.out.println("Digit:");
            System.out.println(GREEN+"0) "+RESET+"Go Back To Home Page");
            System.out.println(GREEN+"1) "+RESET+"Modify your profile");
            System.out.println(GREEN+"2) "+RESET+"View all the anime you like");
            System.out.println(GREEN+"3) "+RESET+"View all the users you are following");
            System.out.println(GREEN+"4) "+RESET+"View all your followers");
            System.out.println(GREEN+"5) "+RESET+"View all the reviews you have written");

            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println("Write your command here:");
            Scanner sc = new Scanner(System.in);
            int value_case;
            try {
                value_case = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");
                //return;
                break;
            }
            switch (value_case) {
                case 1 -> {
                    modifyProfile();
                    break;
                }
                case 2 -> {
                    viewLikedAnime();
                    break;
                }
                case 3 -> {
                    viewFollowedUsers();
                    break;
                }
                case 4 -> {
                    viewFollowers();
                    break;
                }
                case 5 -> {
                    viewWrittenReviews();
                    break;
                }
                case 0 -> {
                    /*this.registered_home_page = new Registered_Home_page();
                    registered_home_page.showMenu();*/
                    check=-1;
                    break;
                }
                default -> {
                    System.out.println("ATTENTION! Wrong command");
                    //this.showMenu();
                    break;
                }

            }
            if(check == 0)
                return;
        }
    }

    private void viewFollowers() {

        Set<String> followers = userManagerNeo4J.getFollowers(user);
        int check=1;
        while(check==1) {
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println("Your followers:");
            HashMap<Integer, String> user_map_to_access_users = new HashMap<>();
            anInterface = new Interface();
            int key = 0;
            if (!followers.isEmpty()) {
                for (String user : followers) {
                    key++;
                    user_map_to_access_users.put(key, user);
                }
                anInterface.printResults(user_map_to_access_users);
            } else {
                System.out.println("You don't have any followers");
                //this.showMenu();
                check=-1;
                break;
            }

            this.viewSelectUserMenu(user_map_to_access_users);
            check=-1;


            if(check==-1)
                return;
        }
    }

    private void viewFollowedUsers() {
        Set<String> followed_users = userManagerNeo4J.getFollowedUsers(user);
        int check=1;
        while(check==1) {
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println("Followed Users:");
            HashMap<Integer, String> user_map_to_access_users = new HashMap<>();
            anInterface = new Interface();
            int key = 0;
            if (!followed_users.isEmpty()) {
                for (String user : followed_users) {
                    key++;
                    user_map_to_access_users.put(key, user);
                }
                anInterface.printResults(user_map_to_access_users);
            } else {
                System.out.println("You don't follow any user");
                //this.showMenu();
                check=-1;
                break;
            }

            this.viewSelectUserMenu(user_map_to_access_users);
            check=-1;
            if(check==-1)
                return;
        }
    }

    private void viewSelectUserMenu(HashMap<Integer, String> user_map_to_access_users){
        int check=1;
        while(check==1) {
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println(GREEN + "What would you like to do?" + RESET);
            System.out.println("Digit:");
            System.out.println(GREEN + "0) " + RESET + "Go Back to your profile");
            System.out.println(GREEN + "1) " + RESET + "View specific User info");

            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println("Write your command here:");
            Scanner sc = new Scanner(System.in);
            int value_case = 0;
            try {
                value_case = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");
                //this.showMenu();
            }
            switch (value_case) {
                case 1 -> {
                    System.out.println("Insert the number of the user you want to visit: ");
                    int user_number = 0;
                    try {
                        user_number = Integer.parseInt(sc.nextLine());
                    } catch (Exception e) {
                        System.out.println("ATTENTION! Wrong command");
                        // this.showMenu();
                        break;
                    }
                    if (!user_map_to_access_users.containsKey(user_number)) {
                        System.out.println("ATTENTION! Wrong number");
                        //this.showMenu();
                        break;
                    }
                    User u = new User();
                    u.setUsername(user_map_to_access_users.get(user_number));
                    viewUserMenu = new ViewUserMenu();
                    viewUserMenu.showMenu(u);
                    check=-1;
                    break;

                }
                case 0 -> {
                    //this.showMenu();
                    check=-1;
                    break;
                }

                default -> {
                    System.out.println("ATTENTION! Wrong command");
                    //this.showMenu();
                    break;
                }
            }
            if(check==-1)
                return;
        }
    }

    private void viewWrittenReviews() {
        Set<Review> written_reviews = userManagerNeo4J.getWrittenReviews(user);
        int check=1;
        while(check==1) {
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println("Written Reviews:");
            HashMap<Integer, String> user_map_to_access_reviews = new HashMap<>();
            anInterface = new Interface();
            int key = 0;
            if (!written_reviews.isEmpty()) {
                for (Review review : written_reviews) {
                    key++;
                    user_map_to_access_reviews.put(key, review.getTitle());
                }
                anInterface.printResults(user_map_to_access_reviews);
            } else {
                System.out.println("You didn't write any review yet");
                check=-1;
                break;
            }

            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println("0) Go Back to your profile");
            System.out.println("1) View specific Review info");
            System.out.println("2) Modify one review");
            System.out.println("3) Delete one review");

            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println("Write your command here:");
            Scanner sc = new Scanner(System.in);
            int value_case = 0;
            try {
                value_case = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");
                //this.showMenu();
            }
            switch (value_case) {
                case 1 -> {
                    System.out.println("Insert the number of the review you want to see: ");
                    int review_number = 0;
                    try {
                        review_number = Integer.parseInt(sc.nextLine());
                    } catch (Exception e) {
                        System.out.println("ATTENTION! Wrong command");
                        //this.viewWrittenReviews();
                        break;
                    }
                    if (!user_map_to_access_reviews.containsKey(review_number)) {
                        System.out.println("ATTENTION! Wrong number");
                        //this.viewWrittenReviews();
                        break;
                    }
                    reviewManagerNeo4J = new ReviewManagerNeo4J(dbNeo4J);
                    Review selected_review = reviewManagerNeo4J.getReviewByTitle(user_map_to_access_reviews.get(review_number));
                    ViewReviewMenu viewReviewMenu = new ViewReviewMenu();
                    viewReviewMenu.showSingleReview(selected_review);
                    check=-1;
                    break;
                }
                case 0 -> {
                    check=-1;
                    break;
                }

                case 2 -> {
                    System.out.println("Insert the number of the review you want modify: ");
                    int review_number = 0;
                    try {
                        review_number = Integer.parseInt(sc.nextLine());
                    } catch (Exception e) {
                        System.out.println("ATTENTION! Wrong command");
                       // this.viewWrittenReviews();
                        break;
                    }
                    if (!user_map_to_access_reviews.containsKey(review_number)) {
                        System.out.println("ATTENTION! Wrong number");
                        //this.showMenu();
                        break;
                    }
                    reviewManagerNeo4J = new ReviewManagerNeo4J(dbNeo4J);
                    Review selected_review = reviewManagerNeo4J.getReviewByTitle(user_map_to_access_reviews.get(review_number));
                    int check2=1;
                    while(check2==1) {
                        System.out.println("What do you want modify ?");
                        System.out.println(GREEN+"1) "+RESET+"Title");
                        System.out.println(GREEN+"2) "+RESET+"Text");
                        System.out.println(GREEN+"0) "+RESET+"Go Back");
                        System.out.println("Digit: ");
                        value_case = 0;
                        try {
                            value_case = Integer.parseInt(sc.nextLine());
                        } catch (Exception e) {
                            System.out.println("ATTENTION! Wrong command");
                            //this.viewWrittenReviews();
                        }
                        switch (value_case) {
                            case 1 -> {
                                Review revToUpdate = reviewManagerNeo4J.getReviewByTitle(user_map_to_access_reviews.get(review_number));
                                System.out.println(GREEN+"Last title: "+RESET + revToUpdate.getTitle());
                                System.out.println(GREEN+"Insert the new title:"+RESET);
                                String newTitle = sc.nextLine();

                                if (newTitle.isEmpty() || newTitle == null) {
                                    System.out.println("ATTENTION! Wrong command");
                                    //this.showMenu();
                                    break;
                                }
                                reviewManagerNeo4J.updateTitleReview(revToUpdate, newTitle);
                                //this.viewWrittenReviews();
                                check2=-1;
                                return;
                            }
                            case 2 -> {
                                Review revToUpdateText = reviewManagerNeo4J.getReviewByTitle(user_map_to_access_reviews.get(review_number));
                                System.out.println(GREEN+"Last text:\n "+RESET + revToUpdateText.getText());
                                System.out.println(GREEN+"Insert the new text:"+RESET);
                                String newText = sc.nextLine();

                                if (newText.isEmpty() || newText == null) {
                                    System.out.println("ATTENTION! Wrong command");
                                    //this.showMenu();
                                    break;
                                }

                                reviewManagerNeo4J.updateTextReview(revToUpdateText.getTitle(), newText);
                               // this.viewWrittenReviews();
                                check2=-1;
                                return;

                            }
                            case 0 -> {
                                check2=-1;
                                break;
                            }
                            default -> {
                                System.out.println("ATTENTION! Wrong number");
                                //this.showMenu();
                                break;
                            }

                        }
                        if(check2==-1)
                            break;
                    }
                    check=-1;
                    break;
                }
                case 3 -> {
                    System.out.println("Insert the number of the review you want delete: ");
                    int review_number = 0;
                    try {
                        review_number = Integer.parseInt(sc.nextLine());
                    } catch (Exception e) {
                        System.out.println("ATTENTION! Wrong command");
                        //this.viewWrittenReviews();
                        break;
                    }
                    if (!user_map_to_access_reviews.containsKey(review_number)) {
                        System.out.println("ATTENTION! Wrong number");
                        //this.showMenu();
                        break;
                    }
                    reviewManagerNeo4J = new ReviewManagerNeo4J(dbNeo4J);
                    Review selected_review = reviewManagerNeo4J.getReviewByTitle(user_map_to_access_reviews.get(review_number));
                    int check2=1;
                    while(check2==1) {
                        System.out.println("Are you sure ?");
                        System.out.println(GREEN+"1) "+"Yes");
                        System.out.println(GREEN+"2) "+"No");
                        System.out.println("Digit: ");
                        value_case = 0;
                        try {
                            value_case = Integer.parseInt(sc.nextLine());
                        } catch (Exception e) {
                            System.out.println("ATTENTION! Wrong command");
                            //this.viewWrittenReviews();
                        }
                        switch (value_case) {
                            case 1 -> {
                                Review selected_reviewToDelete = reviewManagerNeo4J.getReviewByTitle(user_map_to_access_reviews.get(review_number));
                                reviewManagerNeo4J.deleteReview(selected_reviewToDelete.getTitle());
                                //this.viewWrittenReviews();
                                check2=-1;
                                return;
                            }
                            case 2 ->{
                              //  this.viewWrittenReviews();
                                check2=-1;
                                break;
                            }
                            default -> {
                                System.out.println("ATTENTION! Wrong command");
                                //this.viewWrittenReviews();
                                break;
                            }

                        }
                        if(check2==-1)
                            return;
                    }
                }

                default -> {
                    System.out.println("ATTENTION! Wrong command");
                    //this.showMenu();
                    break;
                }

            }
            if(check==-1)
                return;
        }
    }

    private void viewLikedAnime () {
            Set<String> followed_anime = userManagerNeo4J.getLikedAnime(user);
            int check=1;
            while(check==1) {
                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println("Liked Anime:");
                HashMap<Integer, String> user_map_to_access_anime = new HashMap<>();
                anInterface = new Interface();
                int key = 0;
                if (!followed_anime.isEmpty()) {
                    for (String anime : followed_anime) {
                        key++;
                        user_map_to_access_anime.put(key, anime);
                    }
                    anInterface.printResults(user_map_to_access_anime);
                } else {
                    System.out.println("You don't like any anime");
                    check=-1;
                    break;
                }

                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println("What would you like to do?");
                System.out.println("Digit:");
                System.out.println(GREEN + "0) " + RESET + "Go Back to your profile");
                System.out.println(GREEN + "1) " + RESET + "View specific Anime info");

                System.out.println(GREEN + "**************************************" + RESET);
                System.out.println("Write your command here:");
                Scanner sc = new Scanner(System.in);
                int value_case = 0;
                try {
                    value_case = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("ATTENTION! Wrong command");
                    //this.showMenu();
                    break;
                }
                switch (value_case) {
                    case 1 -> {
                        System.out.println("Insert the number of the anime you want to visit: ");
                        int anime_number = 0;
                        try {
                            anime_number = Integer.parseInt(sc.nextLine());
                        } catch (Exception e) {
                            System.out.println("ATTENTION! Wrong command");
                            //this.showMenu();
                            break;
                        }
                        if (!user_map_to_access_anime.containsKey(anime_number)) {
                            System.out.println("ATTENTION! Wrong number");
                            //this.showMenu();
                            break;
                        }
                        Anime anime = new Anime();
                        anime.setAnime_name(user_map_to_access_anime.get(anime_number));
                        viewAnimeMenu = new ViewAnimeMenu();
                        viewAnimeMenu.showMenu(anime);
                        check=-1;
                        break;
                    }
                    case 0 ->{
                        //this.showMenu();
                        check=-1;
                        break;
                    }
                    default -> {
                        System.out.println("ATTENTION! Wrong command");
                        //this.showMenu();
                        break;
                    }
                }
                if(check==-1)
                    return;
            }
        }


    private void modifyProfile () {
        int check=1;
        while (check == 1) {
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println("What would you like to modify?");
            System.out.println("Digit:");
            System.out.println(GREEN + "0)" + RESET + " Go Back");
            System.out.println(GREEN + "1)" + RESET + " Modify your password");
            System.out.println(GREEN + "2)" + RESET + " Modify your gender");
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println("Write your command here:");
            Scanner sc = new Scanner(System.in);
            int value_case;
            try {
                value_case = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("ATTENTION! Wrong command");
                value_case = -2;
            }
            switch (value_case) {

                case 1 -> {
                    System.out.println(GREEN + "**************************************" + RESET);
                    System.out.println("Write your new password here:");
                    String new_password = sc.nextLine();
                    User updated_user = userManagerNeo4J.updateUser(
                            new User(user.getUsername(), new_password, user.getGender(),
                                    user.getLogged_in(), user.getIs_admin()));
                    if (!(updated_user == null)) {
                        user = updated_user;
                    }
                    //showMenu();
                    break;
                }
                case 2 -> {
                    System.out.println(GREEN + "**************************************" + RESET);
                    System.out.println("Choose your new gender:");
                    System.out.println("1) Female");
                    System.out.println("2) Male");
                    System.out.println("3) Other");
                    System.out.println("4) I prefer not to specify");
                    System.out.println(GREEN + "**************************************" + RESET);
                    System.out.println("Write your command here:");
                    String new_gender = user.getGender();

                    try {
                        int gender_case = Integer.parseInt(sc.nextLine());
                        switch ((gender_case)) {
                            case 1 -> {
                                new_gender = "Female";
                            }
                            case 2 -> {
                                new_gender = "Male";
                            }
                            case 3 -> {
                                new_gender = "Other";
                            }
                            case 4 -> {
                                new_gender = "Not specified";
                            }
                            default -> {
                                System.out.println("Invalid option!");
                                // showMenu();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("ATTENTION! Wrong command");
                        // showMenu();
                        break;
                    }

                    User updated_user = userManagerNeo4J.updateUser(
                            new User(user.getUsername(), user.getPassword(), new_gender,
                                    user.getLogged_in(), user.getIs_admin()));
                    if (!(updated_user == null)) {
                        user = updated_user;
                    }
                    //showMenu();
                    break;
                }
                case 0 -> {
                    //showMenu();
                    check=-1;
                    break;
                }
                default -> {
                    System.out.println("ATTENTION! Wrong command");
                    //showMenu();
                    break;
                }

            }
            if(check==-1)
                return;
        }
    }
}
