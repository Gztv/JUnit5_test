package org.solution.junit.examples.exceptions;

public class InsufficientMoneyException extends RuntimeException{

    public InsufficientMoneyException(String message) {
        super(message);
    }
}
