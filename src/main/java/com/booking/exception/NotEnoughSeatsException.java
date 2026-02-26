package com.booking.exception;

public class NotEnoughSeatsException extends Exception{
    public NotEnoughSeatsException(String message){
        super(message);
    }
}
