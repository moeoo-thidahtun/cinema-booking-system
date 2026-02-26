package com.booking.service;

import com.booking.exception.NotEnoughSeatsException;
import com.booking.model.Booking;
import com.booking.model.Movie;
import com.booking.model.Seat;
import com.booking.model.SeatingMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * handle booking logic
 */
public class BookingService {
    private final Map<String, Booking> bookingMap = new HashMap<>();
    private int bookingCounter=1;

    public Booking createBooking(Movie movie, SeatingMap seatingMap, List<Seat> selectedSeats) throws NotEnoughSeatsException {
        boolean notAvailable = selectedSeats.stream().anyMatch(seat -> !seat.isAvailable());
        if (notAvailable) {
            throw new NotEnoughSeatsException("Not enough seat");
        }

        seatingMap.bookSeats(selectedSeats); // Mark these seats as booked
        String bookingId = generateBookingId();
        Booking booking = new Booking(movie, selectedSeats, bookingId);
        bookingMap.put(bookingId, booking);
        return booking;
    }


    private String generateBookingId() {
        return String.format("MOE%04d", bookingCounter++);
    }

    public Booking getBookingById(String bookingId){
        return bookingMap.get(bookingId);
    }
}
