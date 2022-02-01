package dbExtaction;

import it.unipi.large_scale.anime_advisor.dbManager.DbManager;
import it.unipi.large_scale.anime_advisor.entity.*;
import java.io. * ;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.time.*;

import org.neo4j.driver.*;
import static org.neo4j.driver.Values.parameters;


// load data and populate graph
public class GraphPopulator implements DbManager, AutoCloseable{
    private final Driver driver;
    private final String uri = "neo4j://localhost:7687";
    private final String user = "neo4j";
    private final String password = "admin";

    public GraphPopulator() {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password)); //authentication without encryption
    }

    @Override
    public void close() {
        driver.close();
    }

    public String[] getAnimeNames(ArrayList<Anime> animes) {
        int size = animes.size();
        String[] anime_names = new String[size];
        int pos=0;
        for(Anime a: animes){
            anime_names[pos]=a.getAnime_name();
            pos+=1;
        }
        return anime_names;
    }

    public ArrayList<Anime> loadAnimes(String file_path) throws FileNotFoundException{
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
    public ArrayList<Review> loadReviews(String file_path) throws FileNotFoundException{
        ArrayList<Review> reviews = new ArrayList<Review>();
        boolean first = true;
        String csvRows;
        Scanner reviewSc = new Scanner(new File(file_path));
        reviewSc.useDelimiter("/n");
        int i =0;
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
                fields = csvRows.split(",");
            }
            String id_string = (fields[0].trim());
            String profile= (fields[1].trim());
            String score = (fields[4].trim());
            String anime_name = (fields[3].trim());
            String text = (fields[5].trim());
            Review r = new Review();
            r.setId(Integer.parseInt(id_string));
            r.setProfile(profile);
            r.setScore(Integer.parseInt(score));
            r.setAnime_title(anime_name);
            r.setText(text);
            reviews.add(r);
        }
        reviewSc.close();
        return reviews;
    }
    public ArrayList<User> loadUsers(String file_path) throws FileNotFoundException {
        ArrayList<User> users = new ArrayList<User>();
        boolean first = true;
        String csvRows;
        Scanner usersSc = new Scanner(new File(file_path));
        //parsing a CSV file into the constructor of Scanner class
        usersSc.useDelimiter(";");
        int i =0;
        while (usersSc.hasNext()) {
            i = i+1;
            csvRows = usersSc.nextLine();
            // raplace all text between []
            csvRows = csvRows.replaceAll("\\[.*?\\]", "");
            if (first) {
                first = false;
                continue;
            }
            String[] fields = csvRows.split(",");
            String gender= (fields[2].trim());
            String username = (fields[3].trim());
            LocalDate start = LocalDate.of(1940, Month.JANUARY, 1);
            LocalDate end = LocalDate.of(2004, Month.DECEMBER, 31);
            long days = ChronoUnit.DAYS.between(start, end);
            LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + i));

            User u = new User();
            u.setGender(gender);
            u.setUsername(username);
            u.setPassword(Integer.toString(i));
            u.setBirthday(randomDate);
            u.setIs_admin(false);
            u.setLogged_in(false);
            users.add(u);
        }
        usersSc.close();
        return users;
    }

    public void addAnimeToGraph(ArrayList<Anime> anime_list){
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
                    System.out.println("total anime nodes:"+ Integer.toString(i));
                }
            }
        }
    }
    public void addUsersToGraph(ArrayList<User> user_list){
        try (Session session = driver.session()) {
            int i=0;
            for(User u: user_list) {
                i+=1;
                session.run("MERGE (u:User {username: $username, birthday: $birthday, password: $password, " +
                                "gender: $gender, logged_in: $logged_in, is_admin: $is_admin})",
                        parameters(
                                "username", u.getUsername(),
                                "birthday", u.getBirthday(),
                                "password", u.getPassword(),
                                "gender", u.getGender(),
                                "logged_in", u.getLogged_in(),
                                "is_admin", u.getIs_admin()
                        )
                );
                if ((i%1000)==0){
                    System.out.println("total user nodes:"+ Integer.toString(i));
                }
            }
        }
    }
    public void addReviewsWithRelationshipsToGraph(ArrayList<Review> review_list){
        try (Session session = driver.session()) {
            int i=0;
            for(Review r: review_list) {
                i+=1;
                session.run("MERGE (r:Review {text: $text, id: $id})",
                        parameters(
                                "text", r.getText(),
                                "id", r.getId()
                        )
                );
                session.run(
                        "MATCH (r:Review) WHERE r.id = $r_id " + "MATCH (a:Anime) WHERE a.title = $title " +
                                "MERGE (r)-[:REFERRED_TO]->(a)",
                        parameters("r_id", r.getId(), "title", r.getAnime_title())
                );
                session.run(
                        "MATCH (r:Review) WHERE r.id = $r_id " + "MATCH (u:User) WHERE u.username = $username " +
                                "MERGE (u)-[:CREATED]->(r)",
                        parameters("r_id", r.getId(), "username", r.getProfile())
                );
                if ((i%1000)==0){
                    System.out.println("total review nodes:"+ Integer.toString(i));
                }
            }
        }

    }

    public void createFollowsRelationshipUserAnime(int number_of_users, int number_of_edges, String[] anime_names){
        Random rand = new Random();
        for(int i =0; i<=number_of_edges; i++){
            int random_user_psw = rand.nextInt(number_of_users);
            int random_anime_name_pos = rand.nextInt(anime_names.length);
            String random_anime_name = anime_names[random_anime_name_pos];
            try (Session session = driver.session()){
                session.run(
                        "MATCH (u:User) WHERE u.password = $user " + "MATCH (a:Anime) WHERE a.name = $anime " +
                                "MERGE (u)-[:FOLLOWS]->(a)",
                        parameters("user", Integer.toString(random_user_psw), "anime", random_anime_name)
                );
            }

        }
    }
    public void createFollowsRelationshipBetweenUsers(int number_of_users, int number_of_edges){
        Random rand = new Random();
        for(int i =0; i<=number_of_edges; i++){
            int random_psw1 = rand.nextInt(number_of_users);
            int random_psw2 = rand.nextInt(number_of_users);
            while (random_psw2 == random_psw1){
                random_psw2=rand.nextInt(number_of_users);
            }
            try (Session session = driver.session()){
                session.run(
                        "MATCH (u1:User) WHERE u1.password = $user1 " + "MATCH (u2:User) WHERE u2.password = $user2 " +
                                "MERGE (u1)-[:FOLLOWS]->(u2)",
                        parameters("user1", Integer.toString(random_psw1), "user2", Integer.toString(random_psw2))
                );
            }
        }
    }


    public static void main(String[] args) throws Exception{
        GraphPopulator gp = new GraphPopulator();
        ArrayList<User> users = gp.loadUsers("C:\\Users\\Elisa\\Desktop\\large scale\\AnimeDb\\users.csv");
        System.out.println("Total number of users: " + Integer.toString(users.size()));
        ArrayList<Anime> animes = gp.loadAnimes("C:\\Users\\Elisa\\Desktop\\large scale\\AnimeDb\\animeNeo4j.csv");
        System.out.println("Total number of anime: " + Integer.toString(animes.size()));
        ArrayList<Review> reviews = gp.loadReviews("C:\\Users\\Elisa\\Desktop\\large scale\\AnimeDb\\reviewsGraph.csv");
        System.out.println("Total number of reviews: " + Integer.toString(reviews.size()));

        gp.addAnimeToGraph(animes);
        gp.addUsersToGraph(users);
        gp.addReviewsWithRelationshipsToGraph(reviews);
        int number_of_users = users.size();
        int n_follows_edges_users = number_of_users / 4;
        gp.createFollowsRelationshipBetweenUsers(number_of_users, n_follows_edges_users);
        String[] anime_names = gp.getAnimeNames(animes);
        int n_follows_edges_user_anime = number_of_users;
        gp.createFollowsRelationshipUserAnime(number_of_users, n_follows_edges_user_anime, anime_names);

    }


}
