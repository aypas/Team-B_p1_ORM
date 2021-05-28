package com.revature.util.crud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: Jbialon
 * Date: 5/15/2021
 * Time: 5:00 PM
 * Description: {Insert Description}
 */
public class ReadBasedQueries {

    Connection conn;

    public ReadBasedQueries(Connection conn){ this.conn = conn; }

    public ResultSet buildLogin(String tableName, Object[][] loginInfo) throws SQLException {

        String query = "select * from " + tableName + " where " + loginInfo[0][0] + " = ? and " + loginInfo[1][0] + " = ?;";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, loginInfo[0][1]);
        pstmt.setObject(2, loginInfo[1][1]);

        System.out.println(pstmt);

        ResultSet rs = pstmt.executeQuery();

        return rs;

    }

    /*public ResultSet buildLoginByEmail(String tableName, Object[][] loginInfo) throws SQLException {

        String query = "select " + loginInfo[0][0] + " from " + tableName + " where " + loginInfo[0][0] + " = ? and " + loginInfo[1][0] + " = ?;";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, loginInfo[0][1]);
        pstmt.setObject(2, loginInfo[1][1]);

        System.out.println(pstmt);

        ResultSet rs = pstmt.executeQuery();

        return rs;

    }*/


    public ResultSet buildSelectAllByPK(String tableName, Object[] pkInfo) throws SQLException {

        String query = "select * from " + tableName + " where " + pkInfo[0] + " = ?;";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, pkInfo[1]);

        System.out.println(pstmt);

        ResultSet rs = pstmt.executeQuery();

        return rs;

    }

    public ResultSet buildSelectAllByFK(String tableName, Object[] fkInfo) throws SQLException {

        String query = "select * from " + tableName + " where " + fkInfo[0] + " = ?;";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, fkInfo[1]);

        System.out.println(pstmt);

        ResultSet rs = pstmt.executeQuery();

        return rs;

    }

    public ResultSet buildGetDecryptedPgEncryptedPass(String tableName, Object[][] loginInfo) throws SQLException {

        String query = "select * from " + tableName + " where " + loginInfo[0][0] + " = ? and " + loginInfo[1][0] + " = crypt(?, ?)";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, loginInfo[0][1]);
        pstmt.setObject(2, loginInfo[1][1]);
        pstmt.setObject(3, buildGetPgEncryptedPass(tableName, loginInfo));

        System.out.println(pstmt);

        ResultSet rs = pstmt.executeQuery();

        return rs;

    }

    private String buildGetPgEncryptedPass(String tableName, Object[][] loginInfo) throws SQLException {

        String encryptedPass = null;

        String query = "select " + loginInfo[1][0] + " from " + tableName + " where " + loginInfo[0][0] + " = ?;";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, loginInfo[0][1]);

        System.out.println(pstmt);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            encryptedPass = rs.getString("password");
        }

        return encryptedPass;

    }

}
