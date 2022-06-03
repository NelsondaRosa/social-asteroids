package com.ndr.socialasteroids.domain.exceptions;

public class DataNotFoundException extends Exception {

    public DataNotFoundException(){
        super("Data not found. operation can't go on.");
    }
    
}
