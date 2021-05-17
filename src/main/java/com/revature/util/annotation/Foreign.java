package com.revature.util.annotation;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: James Bialon
 * Date: 5/15/2021
 * Time: 9:09 AM
 * Description: Annotation letting the compiler know this field will go to a database column as a foreign key
 */


@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Foreign {
    String name() default "";
}
