package it.unipi.large_scale.anime_advisor.entity;


import java.util.ArrayList;
import java.util.Date;

public final class Admin extends User{

    public Admin(User user) {
        super.copy(user);
    }

    public Admin promoteUserToAdmin(User u){
        if (u instanceof Admin){
            System.out.println("The user is already an admin");
            return (Admin) u;
        }
        else {
            System.out.println("Promoted user to admin");
            Admin a = new Admin(u);
            return a;
        }
    }
}
