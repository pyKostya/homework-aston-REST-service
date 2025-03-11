package com.pykost.exception;

public class DAOException extends RuntimeException {
    public DAOException(Throwable throwable){
        super(throwable);
    }
}
