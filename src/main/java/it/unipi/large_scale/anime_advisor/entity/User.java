package it.unipi.large_scale.anime_advisor.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class User {
    private String username;
    private String password;
    private String gender;
    private Date birthday;
    private int[] favorites_anime;
    private ArrayList<Integer> favorites_users;
    private Boolean logged_in;
    private Boolean is_admin;

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
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public void setFavorites_anime(int[] favorites_anime) {
        this.favorites_anime = favorites_anime;
    }
    public void setFavorites_users(ArrayList<Integer> favorites_users) {
        this.favorites_users = favorites_users;
    }
    public void setLogged_in(Boolean logged_in) {
        this.logged_in = logged_in;
    }
    public void setIs_admin(Boolean is_admin) {
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
    public Date getBirthday() {
        return this.birthday;
    }
    public int[] getFavorites_anime() {
        return this.favorites_anime;
    }
    public ArrayList<Integer> getFavorites_users() {
        return this.favorites_users;
    }
    public Boolean getLogged_in() {
        return this.logged_in;
    }
    public Boolean getIs_admin() {
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
        String format = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(format);
        String birthdayAsString = df.format(this.birthday);
        String str = ("" +
                "Birthday: " + birthdayAsString + "\n"
        );
        string.append(str);
        string.append("" +
                "Favorites Animes : ");
        for (int i : this.favorites_anime)
            string.append(i + " ");
        string.append("\n");

        string.append("" +
                "Favorite Users : ");
        ListIterator<Integer> it = this.favorites_users.listIterator(this.favorites_users.size());
        while(it.hasPrevious())
            string.append(" "+ Integer.toString(it.previous()));
        string.append("\n");

        string.append(""+
                "Logged_in"+String.valueOf(this.logged_in)
        );

        return string.toString();
    }

    public void copy(User u) {
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.gender = u.getGender();
        this.birthday=u.getBirthday();
        this.favorites_anime=u.getFavorites_anime();
        this.favorites_users = u.getFavorites_users();
        this.logged_in = u.getLogged_in();
        if(this instanceof Admin) {
            this.setIs_admin(Boolean.TRUE);
        }
        else{
            this.setIs_admin(Boolean.FALSE);
        }
    }
}
