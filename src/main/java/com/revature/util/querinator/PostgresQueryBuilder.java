package com.revature.util.querinator;

import com.revature.exceptions.AnnotationNotFound;
import com.revature.exceptions.InvalidInput;
import com.revature.util.annotation.*;
import com.revature.util.querinator.crud.CreateBasedQueries;
import com.revature.util.querinator.crud.DeleteBasedQueries;
import com.revature.util.querinator.crud.ReadBasedQueries;
import com.revature.util.querinator.crud.UpdateBasedQueries;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * @param queryType
     * @return query
     * @throws IllegalAccessException
     * @throws InvalidInput
     * @throws AnnotationNotFound
     */

    public boolean buildQuery(T obj, String queryType) throws IllegalAccessException, InvalidInput, AnnotationNotFound, SQLException {

        // TODO: Maybe turn this into an ENUM?
        // Set of valid queryType entries
        Set<String> validQueryTypes = Stream.of("insert", "update", "select_by_pk", "login_username", "login_email", "delete")
                                            .collect(Collectors.toCollection(HashSet::new));

        // Ensures a good entry for query type
        if (!validQueryTypes.contains(queryType)) { throw new InvalidInput("Bad query type value!"); }

        // Ensures the pojo is supposed to be persisted to a table
        if (!obj.getClass().isAnnotationPresent(Entity.class)) { throw new AnnotationNotFound("Entity annotation not found!!"); }

        // Holds the table name related to our POJO
        String tableName = getTableName(obj);

        // Get the queries column names
        ArrayDeque<String> queryColumns = getColumnNames(obj);

        // Get the queries values
        ArrayDeque<Object> queryValues = getValues(obj);

        // Primary key info [0] will be the pk column name [1] will be the key itself
        Object[] pkInfo = getPrimaryKey(obj);

        /*
            Will be needed for login functions within switch
            Email or Username column will be [0][0] and their values will be [0][1]
            Password column will be [1][0] and the value will be [1][1]
         */
        Object[][] loginInfo;

        // Query Builders
        CreateBasedQueries createGenerator;
        ReadBasedQueries readGenerator;
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



            case "select_by_pk":

                readGenerator = new ReadBasedQueries(conn);

                query = readGenerator.buildSelectAllByPK(tableName, pkInfo);

                break;


            case "login_username":

                readGenerator = new ReadBasedQueries(conn);

                loginInfo = getLoginInfoByUsername(obj);

                query = readGenerator.buildLoginByUsername(tableName, loginInfo);

                break;

            case "login_email":

                readGenerator = new ReadBasedQueries(conn);

                loginInfo = getLoginInfoByEmail(obj);

                query = readGenerator.buildLoginByEmail(tableName, loginInfo);

                break;

            case "delete":

                deleteGenerator = new DeleteBasedQueries(conn);

                query = deleteGenerator.buildDeleteByPK(tableName, pkInfo);

                break;

        }

        return query;

    }

    /**
     *
     * @param obj
     * @return tableName
     *
     */
    private String getTableName(Object obj) {

        // Get the objects class
        Class clazz = obj.getClass();

        // Return value
        String tableName = "";

        // POJO Class level annotations
        Annotation[] classAnnotations = clazz.getAnnotations();

        // Loop through our class level annotations
        for(Annotation ano : classAnnotations) {

            // If there is a table annotation...
            if (ano instanceof Table) {
                Table table = (Table) clazz.getAnnotation(ano.annotationType());

                // Set our table name variable to the annotation table_name value
                tableName = table.table_name();

            }

        }

        return tableName;
    }

    /**
     *
     * @param obj
     * @return ArrayDeque of column names
     */
    private ArrayDeque<String> getColumnNames(Object obj) {

        // Get the objects class
        Class clazz = obj.getClass();

        // POJO Class's fields
        Field[] classFields = clazz.getDeclaredFields();

        // Return value
        ArrayDeque<String> queryColumns = new ArrayDeque<>();

        // Field level annotations
        Annotation[] fieldAnno;

        // Start looping through the class fields
        for(Field field : classFields) {

            // Grab the current fields annotations
            fieldAnno = field.getAnnotations();

            // Loop through the annotations in the previous step
            for (Annotation ano : fieldAnno) {

                // Check for the Column annotation
                if (ano instanceof Column) {

                    Column column = (Column) field.getAnnotation(ano.annotationType());

                    // If so add the column name to our column deque
                    queryColumns.add(column.name());

                }
            }
        }

        return queryColumns;

    }

    /**
     *
     * @param obj
     * @return ArrayDeque of mixed type values
     * @throws IllegalAccessException
     */
    private ArrayDeque<Object> getValues(Object obj) throws IllegalAccessException {

        // Get the objects class
        Class clazz = obj.getClass();

        // POJO Class's fields
        Field[] classFields = clazz.getDeclaredFields();

        // This will hold out values part of the query
        ArrayDeque fieldHolder = new ArrayDeque();

        // Field level annotations
        Annotation[] fieldAnno;

        // Start looping through the class fields
        for(Field field : classFields) {

            // Grab the current fields annotations
            fieldAnno = field.getAnnotations();

            // Loop through the annotations in the previous step
            for (Annotation ano : fieldAnno) {

                // Check for the Column annotation
                if (ano instanceof Column) {

                    // Allow this script to grab the private fields
                    field.setAccessible(true);

                    //...Otherwise just add it to the ArrayDeque as is
                    fieldHolder.add(field.get(obj));

                    // Set the private field back to inaccessible
                    field.setAccessible(false);
                }
            }
        }

        return fieldHolder;

    }

    /**
     *
     * @param obj
     * @return int (primary key)
     * @throws IllegalAccessException
     */
    private Object[] getPrimaryKey(Object obj) throws IllegalAccessException, InvalidInput {

        // Get the objects class
        Class clazz = obj.getClass();

        // POJO Class's fields
        Field[] classFields = clazz.getDeclaredFields();

        // Annotation holder
        Annotation[] fieldAnno;

        // Return values
        int primaryKey = -1;
        String primaryColumnName = "";
        Object[] returnArray = new Object[2];

        for (Field field : classFields) {

            // Grab the current fields annotations
            fieldAnno = field.getAnnotations();

            // Loop through the annotations in the previous step
            for (Annotation ano : fieldAnno) {

                // Check for the Column annotation
                if (ano instanceof Primary) {

                    Primary pkAnno = (Primary) field.getAnnotation(ano.annotationType());

                    primaryColumnName = pkAnno.name();

                    // Allow this script to grab the private fields
                    field.setAccessible(true);

                    if (field.get(obj) != null) {

                        primaryKey = (int) field.get(obj);

                    } else { throw new InvalidInput("Primary key cannot be null!"); }

                    // Stop others from grabbing the private field
                    field.setAccessible(false);

                }
            }
        }

        if (primaryKey == -1) { throw new InvalidInput("Primary key is non-existent!"); }

        returnArray[0] = primaryColumnName;
        returnArray[1] = primaryKey;


        return returnArray;

    }

    public Object[][] getLoginInfoByUsername(Object obj) throws IllegalAccessException {

        Object[][] returnArray = new String[3][2];

        // Get the objects class
        Class clazz = obj.getClass();

        // POJO Class's fields
        Field[] classFields = clazz.getDeclaredFields();

        // Field level annotations
        Annotation[] fieldAnno;

        // Start looping through the class fields
        for(Field field : classFields) {

            // Grab the current fields annotations
            fieldAnno = field.getAnnotations();

            // Loop through the annotations in the previous step
            for (Annotation ano : fieldAnno) {

                // Check for the Username annotation
                if (ano instanceof Username) {

                    // Allow this script to grab the private fields
                    field.setAccessible(true);


                    ///...And add to the return array
                    returnArray[0][0] = field.getAnnotation(Column.class).name();
                    returnArray[0][1] = field.get(obj);


                    // Set the private field back to inaccessible
                    field.setAccessible(false);
                }

                // Check for the Username annotation
                if (ano instanceof Password) {

                    // Allow this script to grab the private fields
                    field.setAccessible(true);


                    // add to the return array
                    returnArray[1][0] = field.getAnnotation(Column.class).name();
                    returnArray[1][1] = field.get(obj);


                    // Set the private field back to inaccessible
                    field.setAccessible(false);
                }
            }
        }

        return returnArray;

    }

    public Object[][] getLoginInfoByEmail(Object obj) throws IllegalAccessException {

        Object[][] returnArray = new String[3][2];

        // Get the objects class
        Class clazz = obj.getClass();

        // POJO Class's fields
        Field[] classFields = clazz.getDeclaredFields();

        // Field level annotations
        Annotation[] fieldAnno;

        // Start looping through the class fields
        for(Field field : classFields) {

            // Grab the current fields annotations
            fieldAnno = field.getAnnotations();

            // Loop through the annotations in the previous step
            for (Annotation ano : fieldAnno) {

                // Check for the Username annotation
                if (ano instanceof Email) {

                    // Allow this script to grab the private fields
                    field.setAccessible(true);

                    // Add to the return array
                    returnArray[0][0] = field.getAnnotation(Column.class).name();
                    returnArray[0][1] = field.get(obj);


                    // Set the private field back to inaccessible
                    field.setAccessible(false);
                }

                // Check for the Username annotation
                if (ano instanceof Password) {

                    // Allow this script to grab the private fields
                    field.setAccessible(true);

                    // Add to the return array
                    returnArray[1][0] = field.getAnnotation(Column.class).name();
                    returnArray[1][1] = field.get(obj);

                    // Set the private field back to inaccessible
                    field.setAccessible(false);
                }
            }
        }

        return returnArray;

    }

}
