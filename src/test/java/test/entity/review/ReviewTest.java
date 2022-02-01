package test.entity.review;

import it.unipi.large_scale.anime_advisor.entity.Review;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class ReviewTest extends TestCase {

    @Test
    public void test_toString() {
        Review rew = new Review();
        rew.setId(11);
        rew.setProfile("Nick");
        rew.setScore(10);
        rew.setAnime_title("Naruto");
        rew.setText("questa è la mia recensione...");

        String expected= """
                Id : 11
                User profile: Nick
                Anime name: Naruto
                Review:\s
                questa è la mia recensione...
                User score: 10
                """;
        Assert.assertEquals(rew.toString(),expected);
    }

}