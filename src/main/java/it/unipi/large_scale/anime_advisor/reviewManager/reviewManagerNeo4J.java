package it.unipi.large_scale.anime_advisor.reviewManager;

import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.Review;
import it.unipi.large_scale.anime_advisor.entity.User;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.util.*;

import static org.neo4j.driver.Values.parameters;

public class ReviewManagerNeo4J{
    private final DbManagerNeo4J dbNeo4J;

    public ReviewManagerNeo4J(DbManagerNeo4J dbNeo4J) {
        this.dbNeo4J = dbNeo4J;
    }

    public void createReview(Review r) {

        try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MERGE (r:Review {text: $text})",
                        parameters(
                                "text", r.getText()
                        )
                );
                return null;
            });
            System.out.println("Review node inserted correctly\n");

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to create node due to an error");
        }

    }

    public void readReview(Review r) {

    }

    public void updateReview(Review r) {

    }

    public void deleteReview(Review r) {

    }

}
