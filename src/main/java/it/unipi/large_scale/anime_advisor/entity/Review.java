package it.unipi.large_scale.anime_advisor.entity;

import java.time.LocalDate;
import java.util.Date;

public final class Review {

    private int id;
    private String profile;
    private String anime_title;
    private String title;       //title review
    private String text;         //text of the review
    private int    score;        //user score
    private LocalDate last_update;   //date upload review

    //SET METHODS
    public void setId(int id){
        this.id = id;
    }
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
    public void setScore(int score){
        this.score=score;
    }
    public  void setLast_update(LocalDate d){
        this.last_update=d;
    }

    //GET METHODS
    public int getId(){
        return this.id;
    }
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
    public int getScore() {
        return score;
    }
    public LocalDate getLast_update(){return last_update;}

    public String toString(){
        String string=(
                "Id : " +this.id +"\n" +
                "User profile: "+this.profile+"\n"+
                "Title review"+this.title+"\n"+
                "Anime name: " +this.anime_title +"\n"+
                "Review: \n" +this.text +"\n"+
                "User score: " +this.score +"\n"+
                "Last Update:" + this.last_update.toString()
                );
        return string;
    }


}
