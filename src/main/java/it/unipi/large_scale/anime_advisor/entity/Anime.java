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
        String string;
        string=(
            "Anime_id: " + this.anime_id + "\n" +
            "Name: " +this.anime_name +  "\n" +
            "Premiered: " +this.premiered + "\n" +
            "Genre: "
        );
        for(String i:this.genre){
          string.concat(i +" ");
        }
        string.concat("\n");
      string.concat("" +
                "Type: "+this.type+"\n"+
                "Episodes: "+this.episodes+"\n"
        );
        for(String i:this.producer){
           string.concat(i+" ");
        }
        string.concat("\n");
        for(String i:this.licensor){
            string.concat(i+" ");
        }
        string.concat("\n");
        for(String i:this.studio){
           string.concat(i+" ");
        }
        string.concat("\n");
       string.concat(
                "Source: "+ this.source +"\n" +
                "Score: " +this.scored + "\n" +
                "Scored By: " +this.scoredby +"\n" +
                "Total members: "+this.members +"\n"
        );
       return string;




    }

}
