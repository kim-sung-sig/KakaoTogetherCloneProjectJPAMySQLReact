package com.example.kakao.global.exception;

public class FileStorageException extends Exception{

    public FileStorageException(String msg){
        super(msg);
    }

    public FileStorageException(String msg, Throwable cause){
        super(msg, cause);
    }

}
