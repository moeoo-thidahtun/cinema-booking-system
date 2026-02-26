package com.booking.mainentry;

import com.booking.model.Movie;
import com.booking.model.SeatingMap;
import com.booking.service.BookingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CinemaBookingSystemTest {
    private InputStream iStream;
    private PrintStream oStream;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setupStreams() {
        iStream = System.in;
        oStream = System.out;
        testOut = new ByteArrayOutputStream();
    }

    @AfterEach
    public void restoreStreams() {
        System.setIn(iStream);
        System.setOut(oStream);
    }

    private void setSimulatedIO(String simulatedInput) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        System.setIn(testIn);
        System.setOut(new PrintStream(testOut));
    }

    @Test
    public void testSuccessfulBooking(){
        setSimulatedIO("1\n2\n\n3\n");

        BookingService bookingService = new BookingService();
        SeatingMap seatingMap = new SeatingMap(2, 5);
        Movie movie = new Movie("BeautifulMind", 2, 5);
        CinemaBookingSystem cinemaBookingSystem = new CinemaBookingSystem(bookingService, movie, seatingMap);
        cinemaBookingSystem.showMainMenu();

        String output = testOut.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Booking ID"));
        assertTrue(output.contains("Successfully reserved"));
        assertTrue(output.contains("Welcome to MOE Cinema"));
    }

    @Test
    public void testOverBooking(){
        setSimulatedIO("1\n11\n3\n");

        BookingService bookingService = new BookingService();
        SeatingMap seatingMap = new SeatingMap(2, 5);
        Movie movie = new Movie("BeautifulMind", 2, 5);
        CinemaBookingSystem cinemaBookingSystem = new CinemaBookingSystem(bookingService, movie, seatingMap);
        cinemaBookingSystem.showMainMenu();

        String output = testOut.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Sorry, there are only 10 seats available."));
        assertTrue(output.contains("Thank you for using MOE cinema system"));
    }

    @Test
    public void testUserSelection(){
        setSimulatedIO("1\n2\nB03\n\n3\n");

        BookingService bookingService = new BookingService();
        SeatingMap seatingMap = new SeatingMap(2, 5);
        Movie movie = new Movie("BeautifulMind", 2, 5);
        CinemaBookingSystem cinemaBookingSystem = new CinemaBookingSystem(bookingService, movie, seatingMap);
        cinemaBookingSystem.showMainMenu();

        String output = testOut.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Successfully reserved"));
        assertTrue(output.contains("Booking ID"));
        assertTrue(output.contains("Selected seats:"));
    }

}