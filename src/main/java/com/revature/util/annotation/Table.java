package com.revature.util.annotation;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: James Bialon
 * Date: 5/14/2021
 * Time: 5:10 PM
 * Description: This annotation gives us information u the database table the POJO belongs in
 */

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String table_name() default "";
}
