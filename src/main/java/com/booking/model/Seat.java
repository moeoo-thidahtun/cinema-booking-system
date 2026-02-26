package com.booking.model;

import java.util.Objects;

/**
 * represent a single seat in the hall
 */
public class Seat {
    private char row;
    private int number;

    private boolean isBooked;

    public Seat(char row, int number){
        this.row= row;
        this.number = number;
        isBooked = false;
    }

    public boolean isAvailable(){
        return !isBooked;
    }

    public void bookSeat(){
        this.isBooked = true;
    }

    public char getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }

    public boolean isBooked() {
        return isBooked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat seat)) return false;
        return row == seat.row && number == seat.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, number);
    }
}
