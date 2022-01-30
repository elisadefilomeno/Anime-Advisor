package it.unipi.large_scale.anime_advisor.dbManager;

import org.neo4j.driver.*;
import it.unipi.large_scale.anime_advisor.entity.*;
import java.util.*;
import static org.neo4j.driver.Values.parameters;


public class DbManagerNeo4J implements DbManager, AutoCloseable {
    private final Driver driver;
    private final String uri = "neo4j://localhost:7687";
    private final String user = "neo4j";
    private final String password = "admin";

    public DbManagerNeo4J() {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password)); //authentication without encryption
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }


    public static void main(String[] args) throws Exception {

    }
}

