package it.unipi.large_scale.anime_advisor.entity;

import java.time.LocalDate;
import java.util.Date;
import java.lang.String;

public final class Review {

    private String profile;
    private String anime_title;
    private String title;       //title review
    private String text;         //text of the review
    private LocalDate last_update;   //date upload review

    //SET METHODS
    public void setProfile(String profile){
        this.profile=profile;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public void setAnime_title(String anime_title){
        this.anime_title = anime_title;
    }
    public void setText(String text){
        this.text=text;
    }
    public  void setLast_update(LocalDate d){
        this.last_update=d;
    }

    //GET METHODS

    public String getProfile(){
        return this.profile;
    }
    public String getTitle(){
        return this.title;
    }
    public String getAnime_title(){
        return this.anime_title;
    }
    public String getText(){
        return this.text;
    }
    public LocalDate getLast_update(){return last_update;}

    public String toString(){
        String string=(
                "Title review: "+this.title+"\n"+
                "Review: " +this.text +"\n"+
                "Last Update:" + this.last_update.toString()
                );
        return string;
    }

    public static LocalDate stringToDate(String s){
        String [] parts = new String [3];
        LocalDate data = LocalDate.of(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
        return data;
    }
}
