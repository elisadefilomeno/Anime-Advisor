package dbExtaction;

import com.mongodb.client.MongoCollection;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerMongoDB;
import it.unipi.large_scale.anime_advisor.entity.Anime;

import it.unipi.large_scale.anime_advisor.entity.Review;
import it.unipi.large_scale.anime_advisor.entity.User;
import org.bson.Document;
import org.neo4j.driver.*;
import org.neo4j.driver.internal.value.LocalDateTimeValue;
import it.unipi.large_scale.anime_advisor.application.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import it.unipi.large_scale.anime_advisor.animeManager.*;
import it.unipi.large_scale.anime_advisor.application.*;



import static org.neo4j.driver.Values.parameters;

public class MyPopulation implements AutoCloseable {

    private final Driver driver;
    private final String uri = "neo4j://localhost:7687";
    private final String user = "neo4j";
    private final String password = "admin";

    public MyPopulation() {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password)); //authentication without encryption
    }


    public ArrayList<Anime>  loadAnimes(String file_path) throws FileNotFoundException {
        ArrayList<Anime> animes = new ArrayList<Anime>();
        boolean first = true;
        String csvRows;
        Scanner animeSc = new Scanner(new File(file_path));
        animeSc.useDelimiter("\n");
        int i =0;
        while (animeSc.hasNext()) {
            i = i+1;
            csvRows = animeSc.nextLine();
            if (first) {
                first = false;
                continue;
            }
            String name = csvRows;
            Anime anime = new Anime();
            anime.setAnime_name(name);
            animes.add(anime);
        }
        return animes;
    }
    public void              addAnimeToGraph(ArrayList<Anime> anime_list){
        int i=0;
        try (Session session = driver.session()) {
            for(Anime a: anime_list) {
                i+=1;
                session.run("MERGE (a:Anime {title: $title})",
                        parameters(
                                "title", a.getAnime_name()
                        )
                );
                if ((i%1000)==0){
                    System.out.println("total anime nodes: "+ Integer.toString(i));
                }
                if(i== anime_list.size()){
                    System.out.println("Finito Anime");
                }
            }
        }
    }

    //Population Users
    public ArrayList<User>   loadUsers(String file_path) throws FileNotFoundException {
        ArrayList<User> users = new ArrayList<User>();
        boolean first = true;
        String csvRows;
        Scanner usersSc = new Scanner(new File(file_path));
        //parsing a CSV file into the constructor of Scanner class
        usersSc.useDelimiter(";");
    int count =0;

        while (usersSc.hasNext()) {

            csvRows = usersSc.nextLine();

            String[] fields = csvRows.split(",");
            String gender = (fields[0].trim());
            String username = (fields[1].trim());
            String password = (fields[2].trim());
            String logged_in = (fields[3].trim());
            String is_admin = (fields[4].trim());

            User u = new User();
            u.setGender(gender);
            u.setUsername(username);
            u.setPassword(password);
            u.setIs_admin(Boolean.parseBoolean(logged_in));
            u.setLogged_in(Boolean.parseBoolean(is_admin));
            users.add(u);
        }
        usersSc.close();
        return users;
    }
    public void              addUsersToGraph(ArrayList<User> user_list){
           try (Session session = driver.session()) {
            int i=0;
            for(User u: user_list) {
                i+=1;
                session.run("MERGE (u:User {gender: $gender, username: $username,  password: $password, " +
                                " logged_in: $logged_in, is_admin: $is_admin})",
                        parameters(
                                "gender", u.getGender(),
                                "username", u.getUsername(),
                                "password", u.getPassword(),
                                "logged_in", u.getLogged_in(),
                                "is_admin", u.getIs_admin()
                        )
                );
                if ((i%1000)==0){
                    System.out.println("total user nodes: "+ Integer.toString(i));
                }
            }
        }
    }

    //Population Reviews
    public ArrayList<Review> loadReviews(String file_path) throws FileNotFoundException{

        ArrayList<Review> reviews  = new ArrayList<Review>();
        String csvRows;
        boolean           first    = true;
        Scanner           reviewSc = new Scanner(new File(file_path));

        int i =0;

        reviewSc.useDelimiter("/n");

        while (reviewSc.hasNext()) {
            i = i+1;
            csvRows = reviewSc.nextLine();

            if (first) {
                first = false;
                continue;
            }

            String[] fields = csvRows.split(",");

            while(fields.length<5){

                csvRows = reviewSc.nextLine();
                fields  = csvRows.split(",");
            }

            String profile         = (fields[0].trim());
            String anime_name      = (fields[1].trim());
            String text            = (fields[2].trim());
            String title           = (fields[4].trim());


            //Carico la data che inizialmente e' una stringa e la converto in LocalDate
            String last_update  = (fields[3].trim());
            String [] parts     =  last_update.split("-");
            LocalDate date      = LocalDate.of(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));


            //Fill Review's attributes
            Review r = new Review();

            r.setProfile(profile);
            r.setTitle(title);
            r.setAnime_title(anime_name);
            r.setText(text);
            r.setLast_update(date);

            reviews.add(r);
        }

        reviewSc.close();
        return reviews;
    }
    public void              addReviewsToGraph(ArrayList<Review> review_list){
        try (Session session = driver.session()) {
            int i=0;

            for(Review r: review_list) {
                i+=1;
                session.run("MERGE (r:Review {text: $text,title: $title,last_update:$last_update})",
                        parameters(
                                "text", r.getText(),

                                "title", r.getTitle(),
                                "last_update",r.getLast_update()
                        )
                );
                if ((i%1000)==0){
                    System.out.println("total review nodes:"+ Integer.toString(i));
                }
                if(i==review_list.size()){
                    System.out.println("Finish upload reviews");
                }
            }
        }
        catch (Exception e){
            System.out.println("Error");
        }

    }

    //Add relations
    public void createFollowsRelationshipUserAnime (String[] user,ArrayList<Anime> anime,MongoCollection<Document> anime_collection){
        Random rand = new Random();
        int NFollows=0;
        int indexFollower=0;

        for(int i =0; i<anime.size(); i++){
            //Calcolo randomicamente il numero di followers totale per ogni anime
            NFollows = rand.nextInt(0,20);
            if(NFollows>0) {
                for (int j = 0; j < NFollows; j++) {
                    //Calcolo randomicamente l'utente che segue l'anime
                    indexFollower=rand.nextInt(0, user.length);

                    try (Session session = driver.session()) {
                        session.run(
                                "Match (u:User{username:$user}),(a:Anime{title:$anime}) MERGE (u)-[:LIKE]->(a)",
                                parameters("user", user[indexFollower], "anime", anime.get(i).getAnime_name())
                        );
                    }

                    catch (Exception e)
                    {
                        System.out.println("Error");
                    }

                }
            }

        }
    }

    public void createFollowsRelationshipBetweenUsers (String[] user ){

        Random rand = new Random();
        int maxNumberFollower=1;
        int randomValueUsers=0;
        int indexRandomUser=0;

        for(int i =0; i<user.length; i++){
            //Scelgo numero utenti seguiti in maniera casuale
            randomValueUsers=rand.nextInt(0,maxNumberFollower);
            if(randomValueUsers>0) {
                for (int j = 0; j < randomValueUsers; j++) {
                    //Scelgo indice utente seguito casualmente
                    indexRandomUser = rand.nextInt(0, user.length);
                    try (Session session = driver.session()) {
                        session.run(
                                "Match (u1:User{username:$user1}),(u2:User{username:$user2}) WHERE u1<>u2 merge (u1)-[:FOLLOWS]->(u2)",
                                parameters("user1", user[i],
                                        "user2", user[indexRandomUser])
                        );
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error");
                    }
                }
            }
        }
    }
    public void createRelationshipAnimeReviews(ArrayList<Review> review_list) {
        try (Session session = driver.session()) {

            //Associazione commento ->review legame presente nel csv review
            for (Review r : review_list) {

                session.run(
                        "MATCH (r:Review{ title: $titleR}), (a:Anime{title: $titleA})" +
                                " MERGE (r)-[:REFERRED_TO]->(a)",
                        parameters("titleR", r.getTitle(),
                                "titleA",r.getAnime_title())
                );

            }

        }
    }
    public void createRelationshipUserReviews(ArrayList<Review> review_list) {
        try (Session session = driver.session()) {

            //Associazione commento ->review legame presente nel csv review
            for (Review r : review_list) {

                session.run(
                        "MATCH (r:Review{ title: $titleR}), (a:User{username: $user})" +
                                " MERGE (a)-[:WRITE]->(r)",
                        parameters("titleR", r.getTitle(),
                                "user",r.getProfile())
                );

            }

        }
    }

    //Popolazione
    @Override
    public void close() throws Exception {
        driver.close();
    }

    public static void main(String[] args) throws Exception{
        MyPopulation gp = new MyPopulation();

        DbManagerMongoDB mongoM=new DbManagerMongoDB("mongodb://localhost:27017");
        mongoM.startMongo("Anime_Advisor");
        MongoCollection<Document> anime_collection= mongoM.getCollection("anime");

        ArrayList<User> users = gp.loadUsers("C:\\Users\\onpep\\Desktop\\FilePerGrafo\\UserCompleto.csv");
        System.out.println("Total number of users: " + Integer.toString(users.size()));

        ArrayList<Anime> animes = gp.loadAnimes("C:\\Users\\onpep\\Desktop\\FilePerGrafo\\animeGraph.csv");
        System.out.println("Total number of anime: " + Integer.toString(animes.size()));

        ArrayList<Review> reviews = gp.loadReviews("C:\\Users\\onpep\\Desktop\\FilePerGrafo\\reviewGraph.csv");
        System.out.println("Total number of reviews: " + Integer.toString(reviews.size()));
/*
        gp.addAnimeToGraph(animes);
        System.out.println("FIniSH Anime");

        gp.addUsersToGraph(users);
        System.out.println("FINITO user");

        gp.addReviewsToGraph(reviews);
        System.out.println("FINITO ADDING");
*/
        //Costruisco un vettore che contiene tutti i nomi degli utenti
        int countName=0;
        String[] allUsers = new String [users.size()];
        for(User u : users){
            allUsers[countName]=u.getUsername();
            countName++;
        }
        System.out.println("Start Relation");
      //  gp.createFollowsRelationshipBetweenUsers(allUsers);
        System.out.println("FINITO Follow");

    //    gp.createFollowsRelationshipUserAnime(allUsers,animes,anime_collection);
        System.out.println("Finito Like");

        gp.createRelationshipUserReviews(reviews);
        gp.createRelationshipAnimeReviews(reviews);
        System.out.println("FINISHHHHHH");


     /*   int number_of_users = users.size();
        int n_follows_edges_users = number_of_users / 2;
        gp.createFollowsRelationshipBetweenUsers(number_of_users, n_follows_edges_users);
        String[] anime_names = gp.getAnimeNames(animes);
        int n_follows_edges_user_anime = number_of_users*5;
        gp.createFollowsRelationshipUserAnime(number_of_users, n_follows_edges_user_anime, anime_names);
*/


    }


}



