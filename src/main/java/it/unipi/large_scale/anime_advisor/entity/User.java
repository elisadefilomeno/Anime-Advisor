package it.unipi.large_scale.anime_advisor.entity;

import java.time.LocalDate;

public class User {
    private String username;
    private String password;
    private String gender;
    private boolean logged_in;  // default = false
    private boolean is_admin;  // default = false

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public User(String username, String password, String gender, boolean logged_in, boolean is_admin) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.logged_in = logged_in;
        this.is_admin = is_admin;
    }

    //SET METHODS
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }
    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    //GET METHODS
    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
    public String getGender() {
        return this.gender;
    }

    public boolean getLogged_in() {
        return this.logged_in;
    }
    public boolean getIs_admin() {
        return is_admin;
    }


    //OTHERS METHODS
    @Override
    public String toString() {

        StringBuilder string = new StringBuilder();
        string.append(
                "Username: " + this.username + "\n" +
                        "Password: " + this.password + "\n" +
                        "Gender: " + this.gender + "\n"

        );
        string.append("Logged_in: "+String.valueOf(this.logged_in)+ "\n" +
                "Is_admin: "+ String.valueOf(this.is_admin)
        );

        return string.toString();
    }

    public void copy(User u) {
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.gender = u.getGender();
        this.logged_in = u.getLogged_in();
        this.is_admin = u.getIs_admin();
    }
}
