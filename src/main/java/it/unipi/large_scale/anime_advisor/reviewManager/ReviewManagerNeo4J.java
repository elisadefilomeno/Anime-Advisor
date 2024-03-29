package it.unipi.large_scale.anime_advisor.reviewManager;

import it.unipi.large_scale.anime_advisor.animeManager.AnimeManagerNeo4J;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.Anime;
import it.unipi.large_scale.anime_advisor.entity.Review;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.value.DateValue;
import org.neo4j.driver.internal.value.DateValue.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static it.unipi.large_scale.anime_advisor.entity.Review.stringToDate;
import static org.neo4j.driver.Values.parameters;

public class ReviewManagerNeo4J{
    private final DbManagerNeo4J dbNeo4J;

    public ReviewManagerNeo4J(DbManagerNeo4J dbNeo4J) {
        this.dbNeo4J = dbNeo4J;
    }
    //Ok
    public void createReview(Review r,Anime a,User u) {
        if(checkIfPresent(r.getTitle())){
            System.out.println("Invalid title review !");
            return;
        }

        LocalDate date = LocalDate.now();

        try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MERGE (r:Review {text: $text,title:$title,last_update:$data})",
                        parameters(
                                "text", r.getText(),
                                "data",date,
                                "title",r.getTitle()
                        )
                );
                return null;
            });
            System.out.println("Review inserted correctly\n");

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to create node due to an error");
        }
        //Aggiungo arco User-Review
        try (Session session = dbNeo4J.getDriver().session()) {
            session.run(
                    "MATCH (r:Review{ title: $titleR}), (a:User{username: $user})" +
                            " MERGE (a)-[:WRITE]->(r)",
                    parameters("titleR", r.getTitle(),
                            "user",u.getUsername())
            );
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Error");
        }
        //Aggiungo arco Review-Anime
        try (Session session = dbNeo4J.getDriver().session()) {
            session.run(
                    "MATCH (r:Review{ title: $titleR}), (a:Anime{title: $title})" +
                            " MERGE (r)-[:REFERRED_TO]->(a)",
                    parameters("titleR", r.getTitle(),
                            "title",a.getAnime_name())
            );
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Error");
        }

    }

    //ok
    public void readReview(Review review) {

        if(!checkIfPresent(review.getTitle())){
            System.out.println("Review not found !");
            return;
        }
        try(Session session= dbNeo4J.getDriver().session()){
            Review rev;
            rev = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (r:Review{title:$title}) RETURN r.title,r.text,r.last_update",
                        parameters(
                                "title", review.getTitle()
                        )
                );
                Review read_review= new Review();
                if (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();

                    String title = r.get("r.title").asString();
                    String text = r.get("r.text").asString();
                    DateValue dat = (DateValue) r.get("r.last_update");
                    LocalDate aaa= dat.asLocalDate();


                    read_review.setTitle(title);
                    read_review.setText(text);
                    read_review.setLast_update(aaa);


                }
                return read_review;
            });
            System.out.println("Review info:");
            System.out.println(rev.toString());


        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to read review");
        }



    }

    //ok
    public void updateTitleReview(Review rev,String new_title) {

        if(!checkIfPresent(rev.getTitle())){
            System.out.println("Review not found !");
            return;
        }
        LocalDate date = LocalDate.now();

        try(Session session= dbNeo4J.getDriver().session()){

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ( "MATCH (r:Review) WHERE r.title = $title " +
                                "SET r = { text:$text, last_update:$data ,title:$new_title}",
                        parameters(
                                "title", rev.getTitle(),
                                "text",rev.getText(),
                                "data",date,
                                "new_title", new_title
                        )
                );
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to set the review's title");
        }
        System.out.println("Title review correctly updated");


    }
    //ok
    public void updateTextReview(String title,String new_revText) {

        if(!checkIfPresent(title)){
            System.out.println("Review not found !");
            return;
        }

        LocalDate date = LocalDate.now();
        try(Session session= dbNeo4J.getDriver().session()){

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ( "MATCH (r:Review{title:$title}) " +
                                "SET r = { text:$text, last_update:$data ,title:$title} ",
                        parameters(
                                "title", title,
                                "text", new_revText,
                                "data",date
                        )
                );
                return null;
            } );

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to set the review's text");
        }
        System.out.println("Text review correctly updated");


    }

    public void deleteReview(String title_rev) {

        if(!checkIfPresent(title_rev)){
            System.out.println("Review not found !");
            return;
        }
        try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (r:Review{title:$titleR}) DETACH DELETE r",
                        parameters(
                                "titleR", title_rev

                        )
                );
                return null;
            });

        }catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println("Review correctly deleted");
    }

    public ArrayList<Review> list_Review(Anime a){
        ArrayList<Review> list ;

        try(Session session= dbNeo4J.getDriver().session()) {

            list = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (r:Review) -[f:REFERRED_TO]-> (a:Anime {title:$title}) "+
                                " RETURN r.title,r.text,r.last_update LIMIT 10",
                        parameters(
                                "title", a.getAnime_name()
                        )
                );
                ArrayList<Review> listRev = new ArrayList<>();
                while (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();

                    Review reviewFound = new Review();
                    reviewFound.setText(r.get("r.text").asString());
                    reviewFound.setTitle(r.get("r.title").asString());

                    DateValue dat = (DateValue) r.get("r.last_update");
                    LocalDate dataFound = dat.asLocalDate();
                    reviewFound.setLast_update(dataFound);

                    listRev.add(reviewFound);
                }
                return listRev;
            });

        }
        return list;

    }

    public boolean checkIfPresent(String title_rev){
        if(title_rev==null){
            System.out.println("Title review not inserted");
            return false;
        }
        int rev_count=0;
        try(Session session= dbNeo4J.getDriver().session()){

            rev_count = session.readTransaction(tx -> {
                Result result = tx.run( "MATCH (r:Review) WHERE r.title=$title RETURN count(r) as revCount",
                        parameters(
                                "title", title_rev
                        )
                );
                if(result.hasNext()){
                    org.neo4j.driver.Record r = result.next();
                    return (r.get("revCount").asInt());
                }
                else
                {
                    System.out.println("No present !");
                    return null;
                }

            });
            return (rev_count>0);
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("There is a problem");
        }

        return (rev_count>0);
    }

    public ArrayList<Review> list_ReviewFound(Anime a){
        ArrayList<Review> list ;

        try(Session session= dbNeo4J.getDriver().session()) {

            list = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (r:Review) -[f:REFERRED_TO]-> (a:Anime {title:$title}) "+
                                " RETURN r.title,r.text,r.last_update LIMIT 10",
                        parameters(
                                "title", a.getAnime_name()
                        )
                );
                ArrayList<Review> listRev = new ArrayList<>();
                while (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();

                    Review reviewFound = new Review();
                    reviewFound.setText(r.get("r.text").asString());
                    reviewFound.setTitle(r.get("r.title").asString());

                    DateValue dat = (DateValue) r.get("r.last_update");
                    LocalDate dataFound = dat.asLocalDate();
                    reviewFound.setLast_update(dataFound);

                    listRev.add(reviewFound);
                }
                return listRev;
            });

        }
        return list;

    }
    public ArrayList<Review> listLatestReviewByAnime(Anime a){

        ArrayList<Review> list ;

        try(Session session= dbNeo4J.getDriver().session()) {

            list = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (r:Review) -[f:REFERRED_TO]-> (a:Anime {title:$title}) "+
                                " RETURN r.title,r.text,r.last_update ORDER BY r.last_update DESC LIMIT 10",
                        parameters(
                                "title", a.getAnime_name()
                        )
                );
                ArrayList<Review> listRev = new ArrayList<>();
                while (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();

                    Review reviewFound = new Review();
                    reviewFound.setText(r.get("r.text").asString());
                    reviewFound.setTitle(r.get("r.title").asString());

                    DateValue dat = (DateValue) r.get("r.last_update");
                    LocalDate dataFound = dat.asLocalDate();
                    reviewFound.setLast_update(dataFound);

                    listRev.add(reviewFound);
                }
                return listRev;
            });

        }
        return list;

    }
    public ArrayList<Review> filterReviewByKeyWord(Anime a,String keyword){

        ArrayList<Review> list ;

        try(Session session= dbNeo4J.getDriver().session()) {

            list = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (r:Review) -[f:REFERRED_TO]-> (a:Anime {title:$title}) "+
                                " WHERE r.title CONTAINS $keyword RETURN r.title,r.text,r.last_update ORDER BY r.last_update DESC LIMIT 10",
                        parameters(
                                "title", a.getAnime_name(),
                                "keyword",keyword
                        )
                );
                ArrayList<Review> listRev = new ArrayList<>();
                while (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();

                    Review reviewFound = new Review();
                    reviewFound.setText(r.get("r.text").asString());
                    reviewFound.setTitle(r.get("r.title").asString());

                    DateValue dat = (DateValue) r.get("r.last_update");
                    LocalDate dataFound = dat.asLocalDate();
                    reviewFound.setLast_update(dataFound);

                    listRev.add(reviewFound);
                }
                return listRev;
            });

        }
        return list;

    }
    public Review getReviewByTitle(String titleReview){
        Review reviewFound ;
        try(Session session= dbNeo4J.getDriver().session()) {

            reviewFound = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (r:Review{title:$title}) RETURN "+
                                "r.title,r.text,r.last_update",
                        parameters(
                                "title",titleReview
                        )
                );
                Review rev= new Review();
                if (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();


                    rev.setText(r.get("r.text").asString());
                    rev.setTitle(r.get("r.title").asString());

                    DateValue dat = (DateValue) r.get("r.last_update");
                    LocalDate dataFound = dat.asLocalDate();
                    rev.setLast_update(dataFound);



                    return rev;
                }
                else{
                    System.out.println("Review not found");
                    return null;
                }

            });

        }
        return reviewFound;


    }

    public ArrayList<Review> getlistReviewByUser(User u){

        ArrayList<Review> list ;

        try(Session session= dbNeo4J.getDriver().session()) {

            list = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User{username:$user})-[f:WRITE]->(r:Review) "+
                                " RETURN r.title,r.text,r.last_update ",
                        parameters(
                                "user", u.getUsername()
                        )
                );
                ArrayList<Review> listRev = new ArrayList<>();
                while (result.hasNext()) {
                    org.neo4j.driver.Record r = result.next();

                    Review reviewFound = new Review();
                    reviewFound.setText(r.get("r.text").asString());
                    reviewFound.setTitle(r.get("r.title").asString());

                    DateValue dat = (DateValue) r.get("r.last_update");
                    LocalDate dataFound = dat.asLocalDate();
                    reviewFound.setLast_update(dataFound);

                    listRev.add(reviewFound);
                }
                return listRev;
            });

        }
        return list;


    }
}
