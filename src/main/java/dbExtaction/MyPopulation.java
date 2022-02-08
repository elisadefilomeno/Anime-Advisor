package dbExtaction;

import it.unipi.large_scale.anime_advisor.entity.Anime;

import it.unipi.large_scale.anime_advisor.entity.Review;
import it.unipi.large_scale.anime_advisor.entity.User;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.internal.value.LocalDateTimeValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.neo4j.driver.Values.parameters;

public class MyPopulation implements AutoCloseable {

    private final Driver driver;
    private final String uri = "neo4j://localhost:7687";
    private final String user = "neo4j";
    private final String password = "admin";

    public MyPopulation() {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password)); //authentication without encryption
    }

    //Popolazione degli anime
   /* public String[] getAnimeNames(ArrayList<Anime> animes) {
        int size = animes.size();
        String[] anime_names = new String[size];
        int pos=0;
        for(Anime a: animes){
            anime_names[pos]=a.getAnime_name();
            pos+=1;
        }
        return anime_names;
    }*/
    public ArrayList<Anime> loadAnimes(String file_path) throws FileNotFoundException {
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
                    System.out.println("total anime nodes: "+ Integer.toString(i));
                }
                if(i== anime_list.size()){
                    System.out.println("Finito Anime");
                }
            }
        }
    }

    //Population Users
    public ArrayList<User> loadUsers(String file_path) throws FileNotFoundException {
        ArrayList<User> users = new ArrayList<User>();
        boolean first = true;
        String csvRows;
        Scanner usersSc = new Scanner(new File(file_path));
        //parsing a CSV file into the constructor of Scanner class
        usersSc.useDelimiter(";");

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
    public void addUsersToGraph(ArrayList<User> user_list){
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
            while(fields.length<6){
                csvRows = reviewSc.nextLine();
                fields = csvRows.split(",");
            }

            String id_string = (fields[0].trim());
            String profile= (fields[1].trim());
            //String score = (fields[4].trim());
            String anime_name = (fields[2].trim());
            String title=(fields[3].trim());
            String text = (fields[4].trim());

            //Carico la data che inizialmente e' una stringa e la converto in LocalDate
            String last_update = (fields[5].trim());
            String [] parts =  last_update.split("-");
            LocalDate date = LocalDate.of(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));

            Review r = new Review();
            r.setId(Integer.parseInt(id_string));
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
    public void addReviewsToGraph(ArrayList<Review> review_list){
        try (Session session = driver.session()) {
            int i=0;
            System.out.println("PRINEE");
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

    }

    //Add relations
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

         /*       session.run(
                        "MATCH (r:Review) WHERE r.id = $r_id " + "MATCH (a:Anime) WHERE a.title = $title " +
                                "MERGE (r)-[:REFERRED_TO]->(a)",
                        parameters("r_id", r.getId(), "title", r.getAnime_title())
                );
                session.run(
                        "MATCH (r:Review) WHERE r.id = $r_id " + "MATCH (u:User) WHERE u.username = $username " +
                                "MERGE (u)-[:CREATED]->(r)",
                        parameters("r_id", r.getId(), "username", r.getProfile())
                );
*/



    //Popolazione
    @Override
    public void close() throws Exception {
        driver.close();
    }

    public static void main(String[] args) throws Exception{
        MyPopulation gp = new MyPopulation();
        //ArrayList<User> users = gp.loadUsers("C:\\Users\\onpep\\Desktop\\FilePerGrafo\\UserCompleto.csv");//Fatto
       // System.out.println("Total number of users: " + Integer.toString(users.size()));//Fatto
       // ArrayList<Anime> animes = gp.loadAnimes("C:\\Users\\onpep\\Desktop\\FilePerGrafo\\animeGraph.csv");
       // System.out.println("Total number of anime: " + Integer.toString(animes.size()));
        ArrayList<Review> reviews = gp.loadReviews("C:\\Users\\onpep\\Desktop\\FilePerGrafo\\reviewGraph.csv");
        System.out.println("Total number of reviews: " + Integer.toString(reviews.size()));

       // gp.addAnimeToGraph(animes);
        //System.out.println("FIniSH Anime");
        //gp.addUsersToGraph(users);//Fatto
       // System.out.println("finish User");//Fatto
        gp.addReviewsToGraph(reviews);
        System.out.println("FIniSH review");

     /*   int number_of_users = users.size();
        int n_follows_edges_users = number_of_users / 2;
        gp.createFollowsRelationshipBetweenUsers(number_of_users, n_follows_edges_users);
        String[] anime_names = gp.getAnimeNames(animes);
        int n_follows_edges_user_anime = number_of_users*5;
        gp.createFollowsRelationshipUserAnime(number_of_users, n_follows_edges_user_anime, anime_names);
*/


    }


}



