package com.revature.util.querinator.crud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayDeque;

/**
 * Created by IntelliJ IDEA.
 * User: Jbialon
 * Date: 5/15/2021
 * Time: 4:59 PM
 * Description: {Insert Description}
 */
public class CreateBasedQueries {

    Connection conn;
    public CreateBasedQueries(Connection conn){ this.conn = conn; }

    /**
     *
     * @param tableName
     * @param queryColumns
     * @param queryValues
     * @return Insert query
     */
    public boolean buildInsertQueryString(String tableName, ArrayDeque<String> queryColumns, ArrayDeque<Object> queryValues) throws SQLException {

        String preparedParams = "";
        int paramCounter = 1;
        String lastQueryColumn = queryColumns.peekLast();

        // Return value
        String query = "insert into " + tableName + "(";

        // While we still have column data in our deque...
        while (!queryColumns.isEmpty()) {

            // If it's not the last item in the deque
            if (!queryColumns.peek().equals(lastQueryColumn)) {

                // Add it to our query followed by a comma
                query = query + queryColumns.poll() + ", ";

            } else {

                // If it is the last item add it to the query but close it off and start the values portion of our query
                query = query + queryColumns.poll() + ") values(";
            }

        }

        for (Object value : queryValues) {
            preparedParams = preparedParams + "?, ";
        }

        preparedParams = preparedParams.substring(0, preparedParams.length() - 2);


        // insert into users(firstname, lastname) values(?, ?);
        query = query + preparedParams + ");";

        PreparedStatement pstmt = conn.prepareStatement(query);

        // While our arrayDeque of values is not empty...
        while (!queryValues.isEmpty()) {

            pstmt.setObject(paramCounter, queryValues.poll());

            paramCounter++;

        }

        System.out.println(pstmt);

        pstmt.executeUpdate();

        return true;

    }


}
