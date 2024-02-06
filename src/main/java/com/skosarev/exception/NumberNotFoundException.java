package com.skosarev.exception;

public class NumberNotFoundException extends Exception {

    public NumberNotFoundException(int from, int to) {
        super("The number was not found in the interval [" + from + ", " + to + ").");
    }
}
