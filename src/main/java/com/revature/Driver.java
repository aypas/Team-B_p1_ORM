package com.revature;

import com.revature.models.Person;
import com.revature.util.factory.ConnectionFactory;
import com.revature.util.querinator.PostgresQueryBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: James Bialon
 * Date: 5/14/2021
 * Time: 5:21 PM
 * Description: The main method to kick everything off.
 */

public class Driver {

    public static void main(String[] args) {

        final PostgresQueryBuilder qBuild = new PostgresQueryBuilder(ConnectionFactory.getInstance().getConnection());

        Person testPerson = new Person("Test", "Person", 25, "01-18-1996", "test_person", "test.person@test.org", "password");

        try {

            // Insert Test
            System.out.println(qBuild.buildQuery(testPerson, "insert"));

            // Select Test
            System.out.println(qBuild.buildQuery(testPerson, "select_by_pk"));

            // Delete Test
            System.out.println(qBuild.buildQuery(testPerson, "delete"));

            // Update Test
            System.out.println(qBuild.buildQuery(testPerson, "update"));

            // Username Login Test
            System.out.println(qBuild.buildQuery(testPerson, "login_username"));

            // Email Login Test
            System.out.println(qBuild.buildQuery(testPerson, "login_email"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


}
