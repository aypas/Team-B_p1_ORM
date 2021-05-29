package com.revature.querinator;

import com.revature.exceptions.AnnotationNotFound;
import com.revature.exceptions.InvalidInput;
import com.revature.util.annotation.*;
import com.revature.util.annotationhelper.AnnotationGetters;
import com.revature.util.crud.CreateBasedQueries;
import com.revature.util.crud.DeleteBasedQueries;
import com.revature.util.crud.ReadBasedQueries;
import com.revature.util.crud.UpdateBasedQueries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: James Bialon
 * Date: 5/14/2021
 * Time: 5:33 PM
 * Description: Dynamically build queries to interact with a postgres database
 */
public class PostgresQueryBuilder<T> {

    Connection conn;

    public PostgresQueryBuilder(Connection conn){ this.conn = conn; };

    /**
     *
     * Description: Wraps everything nicely into a switch for multiple query types.
     *
     * @param obj
     * @return query
     * @throws IllegalAccessException
     * @throws InvalidInput
     * @throws AnnotationNotFound
     */

    public boolean insert (T obj) throws IllegalAccessException, AnnotationNotFound, SQLException {
        return sendQuery(obj, "insert");
    }

    public int insertAndGetId (T obj) throws IllegalAccessException, AnnotationNotFound, SQLException {

        if (!obj.getClass().isAnnotationPresent(Entity.class)) { throw new AnnotationNotFound("Entity annotation not found!!"); }

        AnnotationGetters annoGetter = new AnnotationGetters();

        // Holds the table name related to our POJO
        String tableName = annoGetter.getTableName(obj);

        // Get the queries column names
        ArrayDeque<String> queryColumns = annoGetter.getColumnNames(obj);

        // Get the queries values
        ArrayDeque<Object> queryValues = annoGetter.getValues(obj);

        // Primary key info [0] will be the pk column name [1] will be the key itself
        Object[] pkInfo = annoGetter.getPrimaryKey(obj);

        // Query Builders
        CreateBasedQueries createGenerator;

        // The return value
        int val = -1;


        createGenerator = new CreateBasedQueries(conn);

        val = createGenerator.buildInsertQueryString(tableName, queryColumns, queryValues, pkInfo);


        return val;

    }

    public boolean update (T obj) throws IllegalAccessException, AnnotationNotFound, SQLException {
        return sendQuery(obj, "update");
    }

    public boolean delete (T obj) throws IllegalAccessException, AnnotationNotFound, SQLException {
        return sendQuery(obj, "delete");
    }

    public List<Map<String, Object>> selectByPrimaryKey (T obj) throws IllegalAccessException, AnnotationNotFound, SQLException {
        return buildQuery(obj, "select_by_pk");
    }

    public List<Map<String, Object>> loginByUsername (T obj) throws IllegalAccessException, AnnotationNotFound, SQLException {
        return buildQuery(obj, "login_username");
    }

    public List<Map<String, Object>> loginByEmail (T obj) throws IllegalAccessException, AnnotationNotFound, SQLException {
        return buildQuery(obj, "login_email");
    }

    public String getEmail (T obj) throws IllegalAccessException, AnnotationNotFound, SQLException {

        AnnotationGetters annoGetter = new AnnotationGetters();

        List<Map<String, Object>> mapList = buildQuery(obj, "select_email");

        if (mapList.isEmpty()) {
            return null;
        } else {
            Map<String, Object> userInfo = mapList.get(0);
            return (String) userInfo.get(annoGetter.getLoginInfoByEmail(obj)[0][1]);
        }
    }

    public String getUsername (T obj) throws IllegalAccessException, AnnotationNotFound, SQLException {

        AnnotationGetters annoGetter = new AnnotationGetters();

        List<Map<String, Object>> mapList = buildQuery(obj, "select_email");

        if (mapList.isEmpty()) {
            return null;
        } else {
            Map<String, Object> userInfo = mapList.get(0);
            return (String) userInfo.get(annoGetter.getLoginInfoByUsername(obj)[0][1]);
        }
    }

    public List<Map<String, Object>> loginByEmailPgCrypt (T obj) throws IllegalAccessException, AnnotationNotFound, SQLException {
        return buildQuery(obj, "pgCrypt_login_email");
    }

    public List<Map<String, Object>> loginByUsernamePgCrypt (T obj) throws IllegalAccessException, AnnotationNotFound, SQLException {
        return buildQuery(obj, "pgCrypt_login_username");
    }

    /**
     *
     * @param obj
     * @param fkInfo [0] = foreign key column name [1] = value
     * @return
     * @throws IllegalAccessException
     * @throws AnnotationNotFound
     * @throws SQLException
     */
    public List<Map<String, Object>> getObjectByForeignKey (T obj, Object[] fkInfo) throws IllegalAccessException, AnnotationNotFound, SQLException {

        ReadBasedQueries selectMaker = new ReadBasedQueries(conn);

        AnnotationGetters annoGetter = new AnnotationGetters();

        // Holds the table name related to our POJO
        String tableName = annoGetter.getTableName(obj);

        return selectMaker.buildSelectAllByFK(tableName, fkInfo);
    }

    public boolean sendQuery(T obj, String queryType) throws IllegalAccessException, InvalidInput, AnnotationNotFound, SQLException {

        // TODO: Maybe turn this into an ENUM?
        // Set of valid queryType entries
        //Set<String> validQueryTypes = Stream.of("insert", "update", "delete")
        //                                    .collect(Collectors.toCollection(HashSet::new));

        // Ensures a good entry for query type
        //if (!validQueryTypes.contains(queryType)) { throw new InvalidInput("Bad query type value!"); }

        // Ensures the pojo is supposed to be persisted to a table
        if (!obj.getClass().isAnnotationPresent(Entity.class)) { throw new AnnotationNotFound("Entity annotation not found!!"); }

        AnnotationGetters annoGetter = new AnnotationGetters();

        // Holds the table name related to our POJO
        String tableName = annoGetter.getTableName(obj);

        // Get the queries column names
        ArrayDeque<String> queryColumns = annoGetter.getColumnNames(obj);

        // Get the queries values
        ArrayDeque<Object> queryValues = annoGetter.getValues(obj);

        // Primary key info [0] will be the pk column name [1] will be the key itself
        Object[] pkInfo = annoGetter.getPrimaryKey(obj);

        // Query Builders
        CreateBasedQueries createGenerator;
        UpdateBasedQueries updateGenerator;
        DeleteBasedQueries deleteGenerator;

        // The return value
        boolean query = false;

        switch (queryType) {

            case "insert":

                createGenerator = new CreateBasedQueries(conn);

                // We have our dynamic/generic query :)
                query = createGenerator.buildInsertQueryString(tableName, queryColumns, queryValues);
                break;

            case "update":

                updateGenerator = new UpdateBasedQueries(conn);

                query = updateGenerator.buildUpdateQueryString(tableName, pkInfo, queryColumns, queryValues);
                break;

            case "delete":

                deleteGenerator = new DeleteBasedQueries(conn);

                query = deleteGenerator.buildDeleteByPK(tableName, pkInfo);

                break;

        }

        return query;

    }

    public List<Map<String, Object>> buildQuery(T obj, String queryType) throws IllegalAccessException, InvalidInput, AnnotationNotFound, SQLException {

        // TODO: Maybe turn this into an ENUM?
        // Set of valid queryType entries
        //Set<String> validQueryTypes = Stream.of("select_by_pk", "login_username", "login_email", "pgCrypt_login_email", "pgCrypt_login_username")
        //        .collect(Collectors.toCollection(HashSet::new));

        // Ensures a good entry for query type
        //if (!validQueryTypes.contains(queryType)) { throw new InvalidInput("Bad query type value!"); }

        // Ensures the pojo is supposed to be persisted to a table
        if (!obj.getClass().isAnnotationPresent(Entity.class)) { throw new AnnotationNotFound("Entity annotation not found!!"); }

        AnnotationGetters annoGetter = new AnnotationGetters();

        // Holds the table name related to our POJO
        String tableName = annoGetter.getTableName(obj);

        // Primary key info [0] will be the pk column name [1] will be the key itself
        Object[] pkInfo = annoGetter.getPrimaryKey(obj);

        /*
            Will be needed for login functions within switch
            Email or Username column will be [0][0] and their values will be [0][1]
            Password column will be [1][0] and the value will be [1][1]
         */
        Object[][] loginInfo;

        // Query Builders
        ReadBasedQueries readGenerator;

        // The return value
        List<Map<String, Object>> returnVal = null;

        switch (queryType) {

            case "select_email":

                readGenerator = new ReadBasedQueries(conn);

                loginInfo = annoGetter.getLoginInfoByEmail(obj);

                returnVal = readGenerator.buildSelectUsernameOrEmail(tableName, loginInfo);

                break;

            case "select_username":

                readGenerator = new ReadBasedQueries(conn);

                loginInfo = annoGetter.getLoginInfoByUsername(obj);

                returnVal = readGenerator.buildSelectUsernameOrEmail(tableName, loginInfo);

                break;

            case "select_by_pk":

                readGenerator = new ReadBasedQueries(conn);

                returnVal = readGenerator.buildSelectAllByPK(tableName, pkInfo);

                break;


            case "login_username":

                readGenerator = new ReadBasedQueries(conn);

                loginInfo = annoGetter.getLoginInfoByUsername(obj);

                returnVal = readGenerator.buildLogin(tableName, loginInfo);

                break;

            case "login_email":

                readGenerator = new ReadBasedQueries(conn);

                loginInfo = annoGetter.getLoginInfoByEmail(obj);

                returnVal = readGenerator.buildLogin(tableName, loginInfo);

                break;

            case "pgCrypt_login_email":

                readGenerator = new ReadBasedQueries(conn);

                loginInfo = annoGetter.getLoginInfoByEmail(obj);

                returnVal = readGenerator.buildGetDecryptedPgEncryptedPass(tableName, loginInfo);

                break;

            case "pgCrypt_login_username":

                readGenerator = new ReadBasedQueries(conn);

                loginInfo = annoGetter.getLoginInfoByUsername(obj);

                returnVal = readGenerator.buildGetDecryptedPgEncryptedPass(tableName, loginInfo);

                break;
        }

        return returnVal;

    }

    public List<Map<String, Object>> selectAllFromTable (T obj) throws IllegalAccessException, AnnotationNotFound, SQLException {

        AnnotationGetters annoGetter = new AnnotationGetters();

        ReadBasedQueries queryMaker = new ReadBasedQueries(conn);

        String tableName = annoGetter.getTableName(obj);

        return queryMaker.selectAll(tableName);
    }

}
