package it.unipi.large_scale.anime_advisor.entity;

public final class Review {

    private int user_id;
    private String profile;
    private int anime_id;
    private String text;         //text of the review
    private int    score;        //user score

    //SET METHODS
    public void setUser_id(int user_id){
        this.user_id = user_id;
    }
    public void setProfile(String profile){
        this.profile=profile;
    }
    public void setAnime_id(int anime_id){
        this.anime_id = anime_id;
    }
    public void setText(String text){
        this.text=text;
    }
    public void setScore(int score){
        this.score=score;
    }

    //GET METHODS
    public int getUser_id(){
        return this.user_id;
    }
    public String getProfile(){
        return this.profile;
    }
    public int getAnime_id(){
        return this.anime_id;
    }
    public String getText(){
        return this.text;
    }
    public String toString(){
        String string=(
                "Id User: " +this.user_id +"\n" +
                "User profile: "+this.profile+"\n"+
                "Anime id: " +this.anime_id +"\n"+
                "Review: \n" +this.text +"\n"+
                "User score: " +this.score +"\n"
        );
        return string;
    }


}
