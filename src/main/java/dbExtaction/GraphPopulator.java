package dbExtaction;

import it.unipi.large_scale.anime_advisor.dbManager.DbManager;
import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
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

    public GraphPopulator() throws Exception {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password)); //authentication without encryption
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public String[] getAnimeNames(ArrayList<Anime> animes) throws FileNotFoundException{
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
        Scanner usersSc = new Scanner(new File(file_path));
        usersSc.useDelimiter("\n");
        int i =0;
        while (usersSc.hasNext() && i <= 12) {
            i = i+1;
            csvRows = usersSc.nextLine();
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

//    public ArrayList<Review> loadReview(String file_path) throws FileNotFoundException{
//        ArrayList<Review> reviews = new ArrayList<Review>();
//        boolean first = true;
//        String csvRows;
//        Scanner usersSc = new Scanner(new File(file_path));
//        usersSc.useDelimiter("/n");
//        int i =0;
//        while (usersSc.hasNext() && i <= 5) {
//            System.out.println("iteration: "+ Integer.toString(i));
//            i = i+1;
//            csvRows = usersSc.nextLine();
//            System.out.println(csvRows);
//            if (first) {
//                first = false;
//                continue;
//            }
//
////            String[] fields = csvRows.split(",");
//
//        }
//        return reviews;
//    }

    public ArrayList<User> loadUsers(String file_path) throws FileNotFoundException {
        ArrayList<User> users = new ArrayList<User>();
        boolean first = true;
        String csvRows;
        Scanner usersSc = new Scanner(new File(file_path));
        //parsing a CSV file into the constructor of Scanner class
        usersSc.useDelimiter(";");
        int i =0;
        while (usersSc.hasNext() && i <= 12) {
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
        try (Session session = driver.session()) {
            for(Anime a: anime_list) {
                session.run("MERGE (a:Anime {name: $name})",
                        parameters(
                                "name", a.getAnime_name()
                        )
                );
            }
        }
    }
    public void addUsersToGraph(ArrayList<User> user_list){
        try (Session session = driver.session()) {
            for(User u: user_list) {
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
            }
        }
    }

    public void createFollowsRelationshipUserAnime(int number_of_users, int number_of_edges, String[] anime_names){
        Random rand = new Random();
        for(int i =0; i<=number_of_edges; i++){
            int random_user_psw = rand.nextInt(number_of_users);
            System.out.println(random_user_psw);
            int random_anime_name_pos = rand.nextInt(anime_names.length);
            String random_anime_name = anime_names[random_anime_name_pos];
            System.out.println(random_anime_name);
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
//        ArrayList<Review> reviews = gp.loadReview("C:\\Users\\Elisa\\Desktop\\large scale\\AnimeDb\\reviews.csv");

        try ( DbManagerNeo4J neo4j = new DbManagerNeo4J() )
        {
            ArrayList<Anime> animes = gp.loadAnimes("C:\\Users\\Elisa\\Desktop\\large scale\\AnimeDb\\animeNeo4j.csv");
            gp.addAnimeToGraph(animes);
            ArrayList<User> users = gp.loadUsers("C:\\Users\\Elisa\\Desktop\\large scale\\AnimeDb\\users.csv");
            gp.addUsersToGraph(users);
            int number_of_users = users.size();
            int n_follows_edges_users = number_of_users/3;
            gp.createFollowsRelationshipBetweenUsers(number_of_users, n_follows_edges_users);
            String[] anime_names = gp.getAnimeNames(animes);
            int n_follows_edges_user_anime = number_of_users;
            gp.createFollowsRelationshipUserAnime(number_of_users,n_follows_edges_user_anime,anime_names);

        }
    }





}
