package com.houssem.book.exception;

public class OperationNotPermitted extends RuntimeException {
    public OperationNotPermitted(String msg) {
        super(msg);
    }
}
