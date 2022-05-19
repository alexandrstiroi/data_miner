package org.shtiroy.data_miner.datemd.exception;

public class ParseExcelError extends Exception{
    private String message;

    public ParseExcelError(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message + " | " + super.getMessage();
    }
}
