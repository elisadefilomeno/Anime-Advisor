package it.unipi.large_scale.anime_advisor.userManager;

import it.unipi.large_scale.anime_advisor.entity.User;

public interface UserManager {

    void createUser(User u);
    void readUser(User u);
    void updateUser(User u);
    boolean deleteUser(User u);
}
