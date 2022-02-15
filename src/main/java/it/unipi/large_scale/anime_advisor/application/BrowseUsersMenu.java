package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.Review;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.reviewManager.ReviewManagerNeo4J;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.*;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;
import static it.unipi.large_scale.anime_advisor.application.Interface.user;
import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;
import static it.unipi.large_scale.anime_advisor.application.Main.userManagerNeo4J;


public class BrowseUsersMenu {
    public void showMenu() {

        int check=1;
        while(check==1) {
            System.out.println(GREEN + "**************************************" + RESET);
            System.out.println(GREEN + "BROWSE USER PAGE" + RESET);
            System.out.println("What would you like to do?");
            System.out.println("Digit:");
            System.out.println(GREEN+"1) "+RESET+"Find User by username");
            System.out.println(GREEN+"2) "+RESET+"Show your profile");
            System.out.println(GREEN+"3) "+RESET+"View most active users");
            System.out.println(GREEN+"4) "+RESET+"View suggested users to follow");
            System.out.println(GREEN+"0) "+RESET+"Go Back To Home Page");
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
                    System.out.println("Insert the username :");
                    String userToFind = sc.nextLine();
                    if (userToFind == null || userToFind.isEmpty()) {
                        System.out.println("Insert the name !");
                        break;
                    }

                    ArrayList<User> listUsers = new ArrayList<>();
                    listUsers = userManagerNeo4J.findUserByKeyWord(user.getUsername(), userToFind);
                    if (listUsers.size() == 0) {
                        System.out.println("No users found !");
                        break;
                    }

                    int count = 0;
                    for (User u : listUsers) {
                        count++;
                        System.out.println(GREEN + count + ")" + RESET + "  " + u.getUsername());
                    }
                    System.out.println(GREEN + "Choose the profile that you want visualize (click 0 to come back)" + RESET);
                    int indexUser = 0;
                    try {
                        indexUser = Integer.parseInt(sc.nextLine());

                        int maxIndex = listUsers.size();

                        if (indexUser > maxIndex || indexUser < 0) {
                            System.out.println("ATTENTION! Wrong command");
                            break;

                        }

                        else {
                            ViewUserMenu vum = new ViewUserMenu();
                            vum.showMenu(listUsers.get(indexUser - 1));

                            break;
                        }

                    } catch (Exception e) {
                        System.out.println("ATTENTION! Wrong command");
                        break;
                    }


                }

                case 2: {
                    System.out.println("View your personal profile");
                    PersonalProfileUserMenu personalProfileUserMenu = new PersonalProfileUserMenu();
                    personalProfileUserMenu.showMenu();
                    break;
                }

                case 3: {
                    System.out.println("Show most active users ");
                    ArrayList<String> listNameUsers = userManagerNeo4J.viewMostActiveUsers();
                    int count = 0;
                    for (String s : listNameUsers) {
                        count++;
                        System.out.println(GREEN + count + ") " + RESET + s);
                    }
                    System.out.println(GREEN+"Choose the profile that you want visualize (click 0 to come back)"+RESET);
                    int indexUser = 0;
                    try {
                        indexUser = Integer.parseInt(sc.nextLine());

                        int maxIndex = 0;
                        if (listNameUsers.size() < 11) {
                            maxIndex = listNameUsers.size();
                        } else {
                            maxIndex = 10;
                        }

                        if (indexUser > maxIndex || indexUser < 0) {
                            System.out.println("ATTENTION! Wrong command");
                            break;

                        }
                        else {

                            ViewUserMenu vum = new ViewUserMenu();
                            User u = new User();
                            u = userManagerNeo4J.getUserFromName(listNameUsers.get(indexUser - 1));
                            vum.showMenu(u);
                            break;
                        }

                    } catch (Exception e) {
                        System.out.println("ATTENTION! Wrong command");
                        break;
                    }
                }

                case 4: {
                    this.viewSuggestedUsers();
                    break;
                }

                case 0: {

                    check=0;
                    break;
                }

                default: {
                    System.out.println("Wrong command !!!");
                    break;
                }


            }

            if(check==0)
                return;
        }
    }

    private void viewSuggestedUsers() {
        UserManagerNeo4J userManagerNeo4J = new UserManagerNeo4J(dbNeo4J);
        List<String> suggested_users = userManagerNeo4J.getNSuggestedUsers(user.getUsername(), 10);
        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("Suggested Users:");
        HashMap<Integer, String> user_map_to_access_users = new HashMap<>();
        Interface anInterface = new Interface();
        int key = 0;
        if (!suggested_users.isEmpty()) {
            for (String user : suggested_users) {
                key++;
                user_map_to_access_users.put(key, user);
            }
            anInterface.printResults(user_map_to_access_users);
        } else {
            System.out.println("You don't have any suggested user!");
            //this.showMenu();
        }

        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("What would you like to do?");
        System.out.println("Digit:");
        System.out.println("0) Go Back");
        System.out.println("1) View specific User info");

        System.out.println(GREEN + "**************************************" + RESET);
        System.out.println("Write your command here:");
        Scanner sc = new Scanner(System.in);
        int value_case = 0;
        try {
            value_case = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("ATTENTION! Wrong command");
            this.showMenu();
        }
        int check = 1;
        while (check == 1) {
            switch (value_case) {
                case 1 -> {
                    System.out.println("Insert the number of the user you want to visit: ");
                    int user_number = 0;
                    try {
                        user_number = Integer.parseInt(sc.nextLine());
                    } catch (Exception e) {
                        System.out.println("ATTENTION! Wrong command");
                    }
                    if (!user_map_to_access_users.containsKey(user_number)) {
                        System.out.println("ATTENTION! Wrong number");
                    }
                    User u = new User();
                    u.setUsername(user_map_to_access_users.get(user_number));
                    ViewUserMenu viewUserMenu = new ViewUserMenu();
                    viewUserMenu.showMenu(u);
                    check=-1;
                    break;
                }
                case 0 -> {
                    check=-1;
                    break;
                }
                default -> {
                    System.out.println("ATTENTION! Wrong command");
                    break;
                }
            }
            if(check==-1)
                return;
        }
    }
}
