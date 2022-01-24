package test.entity.review;

import it.unipi.large_scale.anime_advisor.entity.Review;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class ReviewTest extends TestCase {

    @Test
    public static void test_toString() {
        Review rew = new Review();
        rew.setUser_id("11");
        rew.setProfile("Nick");
        rew.setScore(10);
        rew.setAnime_id("111");
        rew.setText("questa è la mia recensione...");

        String expected="Id User: 11\n" +
                "User profile: Nick\n" +
                "Anime id: 111\n" +
                "Review: \n" +
                "questa è la mia recensione...\n" +
                "User score: 10\n";
        Assert.assertEquals(rew.toString(),expected);
    }

}