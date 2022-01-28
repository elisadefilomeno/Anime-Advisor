package test.entity.anime;

import it.unipi.large_scale.anime_advisor.entity.Anime;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;


public class AnimeTest extends TestCase {
    @Test
    public static void test_toString(){
        Anime anime=new Anime();
    anime.setAnime_name("Cowboy Bebop");
    anime.setEpisodes(23);
    String[] genre={"Action","Sci-fi"};
    anime.setGenre(genre);
    String[] licensor={"Sunrise"};
    anime.setLicensor(licensor);
    anime.setMembers(344);
    anime.setPremiered(21);
    String[] prod={"Bandai"};
    anime.setProducer(prod);
    anime.setScored(9);
    anime.setScoredby(2333);
    anime.setSource("Original");
    String[] stud={"asd","dsa"};
    anime.setStudio(stud);
    anime.setType("Original");

    String expected=
            "Name: Cowboy Bebop\n" +
            "Premiered: 21\n" +
            "Genre: Action Sci-fi \n" +
            "Type: Original\n" +
            "Episodes: 23\n" +
            "Producer: Bandai \n" +
            "Licensor: Sunrise \n" +
            "Studio: \n" +
            "asd dsa \n" +
            "Source: Original\n" +
            "Score: 9.0\n" +
            "Scored By: 2333\n" +
            "Total members: 344\n";
        Assert.assertEquals(anime.toString(),expected);
    }
}