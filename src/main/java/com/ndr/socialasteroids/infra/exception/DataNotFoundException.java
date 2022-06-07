package com.ndr.socialasteroids.infra.exception;

public class DataNotFoundException extends Exception {

    public DataNotFoundException(){
        super("Data not found. operation can't go on.");
    }
    
}
