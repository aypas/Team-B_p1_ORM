package com.revature.util.annotationhelper;

import com.revature.exceptions.InvalidInput;
import com.revature.util.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayDeque;

/**
 * Created by IntelliJ IDEA.
 * User: Jbialon
 * Date: 5/21/2021
 * Time: 8:02 PM
 * Description: {Insert Description}
 */
public class AnnotationGetters {

    public AnnotationGetters() {};

    /**
     *
     * @param obj
     * @return tableName
     *
     */
    public String getTableName(Object obj) {

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
    public ArrayDeque<String> getColumnNames(Object obj) {

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
    public ArrayDeque<Object> getValues(Object obj) throws IllegalAccessException {

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
    public Object[] getPrimaryKey(Object obj) throws IllegalAccessException, InvalidInput {

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
