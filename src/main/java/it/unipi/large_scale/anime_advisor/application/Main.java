package it.unipi.large_scale.anime_advisor.application;

import it.unipi.large_scale.anime_advisor.dbManager.DbManagerNeo4J;
import it.unipi.large_scale.anime_advisor.entity.User;
import it.unipi.large_scale.anime_advisor.userManager.UserManagerNeo4J;

public class Main {
    protected static Interface anInterface = new Interface();
    protected static DbManagerNeo4J dbNeo4J;
    protected static UserManagerNeo4J userManagerNeo4J;


    public static void main(String argd[]) throws Exception {
        try{
            // connections to databases
            dbNeo4J = new DbManagerNeo4J();
            userManagerNeo4J =new UserManagerNeo4J(dbNeo4J);

        } catch (Exception e) {
            System.out.println("Error");
            return;
        }
        anInterface.showMenu();

        dbNeo4J.closeNeo4J();
    }
}
