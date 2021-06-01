package com.revature.util.crud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: Jbialon
 * Date: 5/15/2021
 * Time: 5:00 PM
 * Description: Builds and sends delete based queries
 */
public class DeleteBasedQueries {

    Connection conn;

    public DeleteBasedQueries(Connection conn){ this.conn = conn; }

    /**
     *
     * Description: Deletes a record from a database by primary key
     *
     * @param tableName
     * @param pkInfo
     * @return
     * @throws SQLException
     */
    public boolean buildDeleteByPK(String tableName, Object[] pkInfo) throws SQLException {

        String query = "delete from " + tableName + " where " + pkInfo[0] + " = ?;";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, pkInfo[1]);

        System.out.println(pstmt);

        int deletedRows = pstmt.executeUpdate();

        if (deletedRows != 0) {
            return true;
        }

        if (pstmt != null) pstmt.close();
        //if (conn != null) conn.close();

        return false;

    }

}
