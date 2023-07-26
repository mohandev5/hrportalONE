package com.task.hrPortalOne.exception;

public class ServerException extends RuntimeException{
    public ServerException(String message, String exMessage) {
        super(message);
    }
}
