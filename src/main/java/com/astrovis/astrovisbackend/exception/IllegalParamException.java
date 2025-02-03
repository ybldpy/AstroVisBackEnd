package com.astrovis.astrovisbackend.exception;

public class IllegalParamException extends RuntimeException{



    public IllegalParamException(){
        this("Illegal params");
    }
    public IllegalParamException(String msg){
        super(msg);
    }




}
