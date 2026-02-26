package com.booking.model;

import java.util.List;

public class Booking {
    private String bookingId;
    private Movie movie;
    private List<Seat> bookedSeats;

    public Booking(Movie movie, List<Seat> seats, String bookingId) {
        this.movie = movie;
        this.bookedSeats = seats;
        this.bookingId = bookingId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public List<Seat> getBookedSeats() {
        return bookedSeats;
    }

    public Movie getMovie() {
        return movie;
    }

}
