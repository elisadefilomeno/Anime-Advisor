package it.unipi.large_scale.anime_advisor.userManager;

import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.exceptions.DuplicateUserException;

public interface UserManager {

    boolean createUser(User u) throws DuplicateUserException;
    User getUserByUsername(String username);
    boolean updateUser(User u);
    boolean deleteUser(User u);
}
