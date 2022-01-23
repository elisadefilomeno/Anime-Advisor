package it.unipi.large_scale.anime_advisor.entity;

import javax.sql.rowset.serial.SerialException;

public final class Anime {
    private String anime_id;
    private String anime_name;
    private String premiered;
    private String[] genre; //list of genres
    private String type;    //if movie or serie type
    private int episodes;
    private String[] producer;
    private String[] licensor;
    private String[] studio;
    private String source; //original or comes from a manga book
    private int scored; //total score obtained
    private int scoredby; //number of users that voted
    private int members; //number of followers

    //SET METHODS
    public void setAnime_id(String anime_id){
        this.anime_id=anime_id;
    }
    public void setAnime_name(String anime_name){
        this.anime_name=anime_name;
    }
    public void setPremiered(String premiered){
        this.premiered=premiered;
    }
    public void setGenre(String[] genres){
        this.genre=genres;
    }
    public void setType(String type){
        this.type=type;
    }
    public void setEpisodes(int episodes){
        this.episodes=episodes;
    }
    public void setProducer(String[] producer){
        this.producer=producer;
    }
    public void setLicensor(String[] licensor){
        this.licensor= licensor;
    }
    public void setStudio(String[] studio){
        this.studio=studio;
    }
    public void setSource(String source){
        this.source=source;
    }
    public void setScored(int scored){
        this.scored=scored;
    }
    public void setScoredby(int scoredby){
        this.scoredby=scoredby;
    }
    public void setMembers(int members){
        this.members=members;
    }
    //GET METHODS
    public String getAnime_id(){
        return this.anime_id;
    }
    public String getAnime_name(){
        return this.anime_name;
    }
    public String getPremiered(){
        return this.premiered;
    }
    public String[] getGenre(){
        return this.genre;
    }
    public String getType(){
        return this.type;
    }
    public int getEpisodes(){
        return this.episodes;
    }
    public String[] getProducer(){
        return this.producer;
    }
    public String[] getLicensor(){
        return this.licensor;
    }
    public String[] getStudio(){
        return this.studio;
    }
    public String getSource(){
        return this.source;
    }
    public int getScored(){
        return this.scored;
    }
    public int getScoredby(){
        return this.scoredby;
    }
    public int getMembers(){
        return this.members;
    }
    public String toString(){
        StringBuilder string= new StringBuilder();
        string.append(
            "Anime_id: " + this.anime_id + "\n" +
            "Name: " +this.anime_name +  "\n" +
            "Premiered: " +this.premiered + "\n" +
            "Genre: "
        );
        for(String i:this.genre){
          string.append(i +" ");
        }
        string.append("\n");
      String str=("" +
                "Type: "+this.type+"\n"+
                "Episodes: "+this.episodes+"\n"
        );
      string.append(str);
      string.append("" +
              "Producer: ");
        for(String i:this.producer){
           string.append(i+" ");
        }
        string.append("\n");
        string.append("" +
                "Licensor: ");
        for(String i:this.licensor){
            string.append(i+" ");
        }
        string.append("" +
                "Studio: ");
        string.append("\n");
        for(String i:this.studio){
           string.append(i+" ");
        }
        string.append("\n");
       String str2=(
                "Source: "+ this.source +"\n" +
                "Score: " +this.scored + "\n" +
                "Scored By: " +this.scoredby +"\n" +
                "Total members: "+this.members +"\n"
        );
       string.append(str2);
       return string.toString();

    }

}
