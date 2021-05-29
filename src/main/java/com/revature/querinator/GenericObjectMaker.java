package com.revature.querinator;

import com.revature.util.annotation.Column;
import com.revature.util.annotation.Primary;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GenericObjectMaker {

    public GenericObjectMaker() {

    }

    /**
     *
     * Description: Builds and returns a single object.
     *
     * @param clazz
     * @param objInfo
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */

    public Object buildObject(Class clazz, List<Map<String, Object>> objInfo) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {

        Object newObj = clazz.getDeclaredConstructor().newInstance();

        // POJO Class's fields
        Field[] classFields = clazz.getDeclaredFields();

        // Field level annotations
        Annotation[] fieldAnno;

        int indexCount = 0;

        while (indexCount < objInfo.size()) {

            Map currObjInfo = objInfo.get(indexCount);

            // Start looping through the class fields
            for (Field field : classFields) {

                // Grab the current fields annotations
                fieldAnno = field.getAnnotations();

                // Loop through the annotations in the previous step
                for (Annotation ano : fieldAnno) {

                    if (ano instanceof Primary) {

                        field.setAccessible(true);
                        field.set(newObj, currObjInfo.get(field.getAnnotation(Primary.class).name()));
                        field.setAccessible(false);

                    }

                    if (ano instanceof Column) {

                        field.setAccessible(true);
                        field.set(newObj, currObjInfo.get(field.getAnnotation(Column.class).name()));
                        field.setAccessible(false);

                    }

                }

            }

            indexCount++;

        }

        return newObj;
    }

    /**
     *
     * Description: Builds and returns multiple objects in a Deque.
     *
     * @param clazz
     * @param objInfo
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public List<Object> buildObjects(Class clazz, List<Map<String, Object>> objInfo) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {

        List<Object> returnVal = new ArrayList<>();

        Object newObj = clazz.getDeclaredConstructor().newInstance();

        // POJO Class's fields
        Field[] classFields = clazz.getDeclaredFields();

        // Field level annotations
        Annotation[] fieldAnno;

        int indexCount = 0;

        while (indexCount < objInfo.size()) {

            newObj = clazz.getDeclaredConstructor().newInstance();

            Map currObjInfo = objInfo.get(indexCount);

            // Start looping through the class fields
            for (Field field : classFields) {

                // Grab the current fields annotations
                fieldAnno = field.getAnnotations();

                // Loop through the annotations in the previous step
                for (Annotation ano : fieldAnno) {

                    if (ano instanceof Primary) {

                        field.setAccessible(true);
                        field.set(newObj, currObjInfo.get(field.getAnnotation(Primary.class).name()));
                        field.setAccessible(false);

                    }

                    if (ano instanceof Column) {

                        field.setAccessible(true);
                        field.set(newObj, currObjInfo.get(field.getAnnotation(Column.class).name()));
                        field.setAccessible(false);

                    }

                }

            }

            returnVal.add(newObj);

            indexCount++;

        }

        return returnVal;
    }

}
