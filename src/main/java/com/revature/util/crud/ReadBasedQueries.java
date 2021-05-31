package com.revature.util.crud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jbialon
 * Date: 5/15/2021
 * Time: 5:00 PM
 * Description: Builds and sends of select based queries to a database
 */
public class ReadBasedQueries {

    Connection conn;

    ResultSetMapper rsMapper = new ResultSetMapper();

    public ReadBasedQueries(Connection conn){ this.conn = conn; }

    /**
     *
     * Description: Builds and sends a query that will send back a users info to be used after a successful login
     *              authentication attempt
     *
     * @param tableName
     * @param loginInfo
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> buildLogin(String tableName, Object[][] loginInfo) throws SQLException {

        String query = "select * from " + tableName + " where " + loginInfo[0][0] + " = ? and " + loginInfo[1][0] + " = ?;";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, loginInfo[0][1]);
        pstmt.setObject(2, loginInfo[1][1]);

        System.out.println(pstmt);

        ResultSet rs = pstmt.executeQuery();

        List<Map<String, Object>> returnVal = rsMapper.mapResultSet(rs);

        //if (rs != null) rs.close();
        //if (pstmt != null) pstmt.close();
        //if (conn != null) conn.close();

        return returnVal;

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

    /**
     *
     * Description: Grabs a users information used for authentication purposes
     *
     * @param tableName
     * @param loginInfo
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> buildSelectUsernameOrEmail(String tableName, Object[][] loginInfo) throws SQLException {

        String query = "select " + loginInfo[0][0] + " from " + tableName + " where " + loginInfo[0][0] + " = ?;";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, loginInfo[1][1]);

        System.out.println(pstmt);

        ResultSet rs = pstmt.executeQuery();

        List<Map<String, Object>> returnVal = rsMapper.mapResultSet(rs);

        //if (rs != null) rs.close();
        //if (pstmt != null) pstmt.close();
        //if (conn != null) conn.close();

        return returnVal;

    }

    /**
     *
     * Description: Selects a records information by primary key
     *
     * @param tableName
     * @param pkInfo
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> buildSelectAllByPK(String tableName, Object[] pkInfo) throws SQLException {

        String query = "select * from " + tableName + " where " + pkInfo[0] + " = ?;";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, pkInfo[1]);

        System.out.println(pstmt);

        ResultSet rs = pstmt.executeQuery();

        List<Map<String, Object>> returnVal = rsMapper.mapResultSet(rs);

        //if (rs != null) rs.close();
        //if (pstmt != null) pstmt.close();
        //if (conn != null) conn.close();

        return returnVal;

    }

    /**
     *
     * Description: Selects a records information by foreign key
     *
     * @param tableName
     * @param fkInfo
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> buildSelectAllByFK(String tableName, Object[] fkInfo) throws SQLException {

        String query = "select * from " + tableName + " where " + fkInfo[0] + " = ?;";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, fkInfo[1]);

        System.out.println(pstmt);

        ResultSet rs = pstmt.executeQuery();

        List<Map<String, Object>> returnVal = rsMapper.mapResultSet(rs);

        //if (rs != null) rs.close();
        //if (pstmt != null) pstmt.close();
        //if (conn != null) conn.close();

        return returnVal;

    }

    /**
     *
     * Description: Decrypts a password encrypted by PgCrypt
     *
     * @param tableName
     * @param loginInfo
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> buildGetDecryptedPgEncryptedPass(String tableName, Object[][] loginInfo) throws SQLException {

        String query = "select * from " + tableName + " where " + loginInfo[0][0] + " = ? and " + loginInfo[1][0] + " = crypt(?, ?)";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, loginInfo[0][1]);
        pstmt.setObject(2, loginInfo[1][1]);
        pstmt.setObject(3, buildGetPgEncryptedPass(tableName, loginInfo));

        System.out.println(pstmt);

        ResultSet rs = pstmt.executeQuery();

        List<Map<String, Object>> returnVal = rsMapper.mapResultSet(rs);

        //if (rs != null) rs.close();
        //if (pstmt != null) pstmt.close();
        //if (conn != null) conn.close();

        return returnVal;

    }

    /**
     *
     * Description: Gets an encrypted password for authentication purposes
     *
     * @param tableName
     * @param loginInfo
     * @return
     * @throws SQLException
     */
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

        //if (rs != null) rs.close();
        //if (pstmt != null) pstmt.close();
        //if (conn != null) conn.close();

        return encryptedPass;

    }

    /**
     *
     * Description: Gets all records from a desired table
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> selectAll(String tableName) throws SQLException {

        String query = "select * from " + tableName + ";";

        PreparedStatement pstmt = conn.prepareStatement(query);

        System.out.println(pstmt);

        ResultSet rs = pstmt.executeQuery();

        List<Map<String, Object>> returnVal = rsMapper.mapResultSet(rs);

        //if (rs != null) rs.close();
        //if (pstmt != null) pstmt.close();
        //if (conn != null) conn.close();

        return returnVal;
    }

}
