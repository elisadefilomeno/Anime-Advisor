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
            System.out.println("Review node inserted correctly\n");

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
                            " MERGE (a)-[:REFERRED_TO]->(r)",
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

    public void deleteReview(String title_rev,String title_anime,String profile) {

        if(!checkIfPresent(title_rev)){
            System.out.println("Review not found !");
            return;
        }
        try(Session session= dbNeo4J.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (r:Review{title:$titleR}),(a:Anime{title:$titleA}),"+
                                "(u:User{username:$user})DETACH DELETE r",
                        parameters(
                                "titleR", title_rev,
                                "titleA",title_anime,
                                "user",profile
                        )
                );
                return null;
            });

        }catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println("ESCO");
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
        System.out.println(rev_count>0);
        return (rev_count>0);
    }

   /* public static void main(String[] args) throws Exception {

        DbManagerNeo4J db = new DbManagerNeo4J();
        ReviewManagerNeo4J rm = new ReviewManagerNeo4J(db);
        UserManagerNeo4J um = new UserManagerNeo4J(db);
        AnimeManagerNeo4J am = new AnimeManagerNeo4J(db);
        User u = new User();
        u.setUsername("Peppe");
        u.setGender("male");
        u.setPassword("1234");
        u.setLogged_in(false);
        u.setIs_admin(false);
        um.createUser(u);

        Anime a = new Anime ();
        a.setAnime_name("OnePiece");
        am.createAnime("OnePiece");

        um.followAnime("Peppe","OnePiece");

        Review r= new Review();
        r.setTitle("Commento");
        LocalDate al=LocalDate.of(1999,1,1);
        r.setLast_update(al);
        r.setText("Belisssiimoooo");

        rm.createReview(r,a,u);
        rm.deleteReview("Commento",a.getAnime_name(),u.getUsername());



        db.closeNeo4J();

    }
*/
}
