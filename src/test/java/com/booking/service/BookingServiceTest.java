package com.booking.service;

import com.booking.exception.NotEnoughSeatsException;
import com.booking.model.Booking;
import com.booking.model.Movie;
import com.booking.model.Seat;
import com.booking.model.SeatingMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingServiceTest {
    private BookingService bookingService;
    private SeatingMap seatingMap;
    private Movie movie;

    @BeforeEach
    public void setup(){
        bookingService=new BookingService();
        seatingMap = new SeatingMap(2, 5);
        movie = new Movie("BeautifulMind",2,5);
    }

    @Test
    public void testCreateBookingDefaultSelection() throws NotEnoughSeatsException {
        List<Seat> selectedSeats = seatingMap.setDefaultSelectedSeats(3);
        Booking booking = bookingService.createBooking(movie, seatingMap, selectedSeats);
        assertNotNull(booking);
        assertEquals(3, booking.getBookedSeats().size());
        assertTrue(booking.getBookedSeats().stream().noneMatch(Seat::isAvailable));
    }

    @Test
    public void testCreateBookingUserSelection() throws NotEnoughSeatsException {
        List<Seat> selectedSeats = seatingMap.setUserSelectedSeats(2, "A03");
        Booking booking = bookingService.createBooking(movie, seatingMap, selectedSeats);
        assertNotNull(booking);
        List<Seat> seats=booking.getBookedSeats();
        assertEquals(2, seats.size());
        assertEquals('A', seats.getFirst().getRow());
        assertEquals(3, seats.getFirst().getNumber());
    }

    @Test
    public void testOverBooking() throws NotEnoughSeatsException {
        List<Seat> selectedSeats = seatingMap.setDefaultSelectedSeats(10);
        bookingService.createBooking(movie, seatingMap, selectedSeats);
        assertThrows(NotEnoughSeatsException.class, () -> {
            bookingService.createBooking(movie, seatingMap, selectedSeats);
        });
    }

    @Test
    public void getBookingById() throws NotEnoughSeatsException {
        List<Seat> selectedSeats = seatingMap.setDefaultSelectedSeats(2);
        Booking booking = bookingService.createBooking(movie, seatingMap, selectedSeats);
        Booking testBooking = bookingService.getBookingById(booking.getBookingId());
        assertNotNull(testBooking);
        assertEquals(booking.getBookingId(), testBooking.getBookingId());
    }

}