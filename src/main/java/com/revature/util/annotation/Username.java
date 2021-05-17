package com.revature.util.annotation;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jbialon
 * Date: 5/15/2021
 * Time: 2:06 PM
 * Description: {Insert Description}
 */

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {
}
