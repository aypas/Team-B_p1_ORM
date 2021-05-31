package com.revature.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: James Bialon
 * Date: 5/15/2021
 * Time: 11:04 AM
 * Description: Throws an exception if bad input is given
 */

public class InvalidInput extends RuntimeException {

    public InvalidInput(String message) {
        super(message);
    }

}
