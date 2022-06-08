package com.ndr.socialasteroids.infra.error.exception;

public class DataNotFoundException extends Exception {

    public DataNotFoundException(){
        super("Data not found. operation can't go on.");
    }
    
}
