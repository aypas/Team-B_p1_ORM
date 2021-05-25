package com.revature.util.querinator;

import com.revature.util.annotation.Column;
import com.revature.util.annotation.Primary;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenericObjectMaker {

    public GenericObjectMaker() {

    }

    public Object buildObject(Class clazz, ResultSet rs) throws InstantiationException, IllegalAccessException, SQLException {

        Object newObj = clazz.newInstance();

        // POJO Class's fields
        Field[] classFields = clazz.getDeclaredFields();

        // Field level annotations
        Annotation[] fieldAnno;

        while (rs.next()) {

            // Start looping through the class fields
            for (Field field : classFields) {

                // Grab the current fields annotations
                fieldAnno = field.getAnnotations();

                // Loop through the annotations in the previous step
                for (Annotation ano : fieldAnno) {

                    if (ano instanceof Primary) {

                        field.setAccessible(true);
                        field.set(newObj, rs.getObject(field.getAnnotation(Primary.class).name()));
                        field.setAccessible(false);

                    }

                    if (ano instanceof Column) {

                        field.setAccessible(true);
                        field.set(newObj, rs.getObject(field.getAnnotation(Column.class).name()));
                        field.setAccessible(false);

                    }

                }

            }

        }

        return newObj;
    }

}
