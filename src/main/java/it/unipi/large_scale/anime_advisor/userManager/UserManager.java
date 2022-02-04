package it.unipi.large_scale.anime_advisor.userManager;

import it.unipi.large_scale.anime_advisor.entity.User;

public interface UserManager {

    void createUser(User u) throws  Exception;
    void getUserByUsername(String username);
    void updateUser(User u);
    void deleteUser(User u);
}
