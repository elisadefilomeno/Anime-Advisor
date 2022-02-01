package it.unipi.large_scale.anime_advisor.entity;

public final class Review {

    private int id;
    private String profile;
    private String anime_title;
    private String text;         //text of the review
    private int    score;        //user score

    //SET METHODS
    public void setId(int id){
        this.id = id;
    }
    public void setProfile(String profile){
        this.profile=profile;
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

    //GET METHODS
    public int getId(){
        return this.id;
    }
    public String getProfile(){
        return this.profile;
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
    public String toString(){
        String string=(
                "Id : " +this.id +"\n" +
                "User profile: "+this.profile+"\n"+
                "Anime name: " +this.anime_title +"\n"+
                "Review: \n" +this.text +"\n"+
                "User score: " +this.score +"\n"
        );
        return string;
    }


}
