package com.revature.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: Jbialon
 * Date: 5/15/2021
 * Time: 11:04 AM
 * Description: Throws an exception if the required annotation is not found
 */
public class AnnotationNotFound extends RuntimeException{

    public AnnotationNotFound(String message) {
        super(message);
    }

}
