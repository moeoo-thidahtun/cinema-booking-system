package com.booking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SeatingMapTest {
    private SeatingMap seatingMap;

    @BeforeEach
    void setup(){
        seatingMap = new SeatingMap(2, 5);
    }

    @Test
    public void testSetDefaultSelectedSeats(){
        List<Seat> seats = seatingMap.setDefaultSelectedSeats(3);
        assertEquals(3, seats.size());
        assertTrue(seats.stream().allMatch(Seat::isAvailable));

        //skip first
        List<Seat> firstBooking = seatingMap.setDefaultSelectedSeats(1);
        seatingMap.bookSeats(firstBooking);
        List<Seat> secondBooking = seatingMap.setDefaultSelectedSeats(2);
        assertEquals(2, secondBooking.size());
        assertTrue(secondBooking.stream().noneMatch(firstBooking::contains));

        //overflow to next row
        seatingMap.bookSeats(seatingMap.setDefaultSelectedSeats(5));
        List<Seat> overflow = seatingMap.setDefaultSelectedSeats(2);
        assertEquals(2, overflow.size());
        assertEquals('B', overflow.get(1).getRow());
    }

    @Test
    public void testSetUserSelectedSeats(){
        List<Seat> seats = seatingMap.setUserSelectedSeats(2,"A03");
        assertEquals(2, seats.size());
        assertEquals('A', seats.getFirst().getRow());
        assertEquals(3, seats.getFirst().getNumber());

        //overflow to next row
        seatingMap.bookSeats(seatingMap.setUserSelectedSeats(4, "A02"));
        List<Seat> nextBookedSeats = seatingMap.setUserSelectedSeats(3, "A03");
        assertEquals(3, nextBookedSeats.size());
        long aRowCount=nextBookedSeats.stream().filter(s->s.getRow()=='A').count();
        long bRowCount=nextBookedSeats.stream().filter(s->s.getRow()=='B').count();
        assertTrue(aRowCount==0 && bRowCount==3);
    }

    @Test
    public void testBookSeats(){
        List<Seat> selectedSeats = seatingMap.setDefaultSelectedSeats(2);
        seatingMap.bookSeats(selectedSeats);
        selectedSeats.forEach(s-> assertFalse(s.isAvailable()));
    }

}