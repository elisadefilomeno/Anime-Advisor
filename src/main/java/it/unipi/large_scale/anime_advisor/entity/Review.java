package it.unipi.large_scale.anime_advisor.entity;

public final class Review {
    private String id_user;
    private String profile;
    private String id_anime;
    private String text;         //text of the review
    private int    score;        //user score

    //SET METHODS
    public void setId_user(String id_user){
        this.id_user=id_user;
    }
    public void setProfile(String profile){
        this.profile=profile;
    }
    public void setId_anime(String id_anime){
        this.id_anime=id_anime;
    }
    public void setText(String text){
        this.text=text;
    }
    public void setScore(int score){
        this.score=score;
    }
    //GET METHODS
    public String getId_user(){
        return this.id_user;
    }
    public String getProfile(){
        return this.profile;
    }
    public String getId_anime(){
        return this.id_anime;
    }
    public String getText(){
        return this.text;
    }
    public String toString(){
        String string=(
                "Id User: " +this.id_user +"\n" +
                "User profile: "+this.profile+"\n"+
                "Anime id: " +this.id_anime+"\n"+
                "Review: \n" +this.text +"\n"+
                "User score: " +this.score +"\n"
        );
        return string;
    }


}
