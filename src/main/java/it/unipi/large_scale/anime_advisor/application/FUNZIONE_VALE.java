package it.unipi.large_scale.anime_advisor.application;
import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static it.unipi.large_scale.anime_advisor.application.Main.dbNeo4J;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.GREEN;
import static it.unipi.large_scale.anime_advisor.application.ConsoleColors.RESET;

public class FUNZIONE_VALE {
        private AnimeManagerNeo4J animeManagerNeo4J;

        public void viewTop10MostFollowedAnime(){ // vale
            // TO DO
            animeManagerNeo4J = new AnimeManagerNeo4J(dbNeo4J);
           /* Map<String, Integer> top10_anime= animeManagerNeo4J.getTop10MostFollowedAnime();
            int i =0;
            Map<Integer, String> user_map_to_access_anime = new HashMap<>();
            System.out.println(GREEN+"**************************************"+RESET);
            System.out.println("Which anime would you like to see?");
            System.out.println("Digit:");

            for (String key : top10_anime.keySet()) {
                i++;
                user_map_to_access_anime.put(i,key);
                System.out.println(Integer.toString(i)+") Anime: "+ key+ ", " +
                        "number of followers: "+ top10_anime.get(key).toString());
            }
            System.out.println(GREEN+"**************************************"+RESET);

            System.out.println("Write your command here:");
            Scanner sc =new Scanner(System.in);
            int anime_key;
            try{
                anime_key = Integer.parseInt(sc.nextLine());
            }
            catch(Exception e){
                // TO DO
                System.out.println("ATTENTION! Wrong command");
                //this.showMenu();
                return;
            }
            if(!user_map_to_access_anime.containsKey(anime_key)){
                // TO DO
                System.out.println("Wrong number digited");
                //this.showMenu();
                return;
            }
            String anime_title = user_map_to_access_anime.get(anime_key);
            System.out.println(GREEN+"**************************************"+RESET);

            System.out.println("You have choosen:");
            Anime anime = new Anime(anime_title);
            System.out.println(anime.getAnime_name());
*/
        }
}
