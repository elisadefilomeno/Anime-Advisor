package it.unipi.large_scale.anime_advisor.dbManager;

import org.neo4j.driver.*;


public class DbManagerNeo4J implements DbManager {
    private static Driver driver;
    private final String uri = "neo4j://localhost:7687"; // original database
//    private final String uri = "bolt://localhost:7687"; // personal new connection to test function
    private final String user = "neo4j";
    private final String password = "admin";

    public DbManagerNeo4J() {
        try {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password)); //authentication without encryption
        }catch (Exception e){
            System.out.println("An error occurred while opening connection with Neo4j");
        }

    }

    public void closeNeo4J() throws Exception {
        try{
            driver.close();
        }catch (Exception e){
            System.out.println("An error occurred while closing connection with Neo4j");
        }
    }

    public Driver getDriver() {
        return driver;
    }
}

