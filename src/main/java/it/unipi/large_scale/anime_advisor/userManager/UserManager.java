package it.unipi.large_scale.anime_advisor.userManager;

import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.exceptions.DuplicateUserException;

public interface UserManager {

    boolean createUser(User u) throws DuplicateUserException;
    void readUser(User u);
    void updateUser(User u);
    boolean deleteUser(User u);
}
