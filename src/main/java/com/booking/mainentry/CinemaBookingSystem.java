package com.booking.mainentry;

import com.booking.exception.NotEnoughSeatsException;
import com.booking.model.Booking;
import com.booking.model.Movie;
import com.booking.model.Seat;
import com.booking.model.SeatingMap;
import com.booking.service.BookingService;

import java.util.List;
import java.util.Scanner;

/**
 * main entry point of the system to handle user interaction
 */
public class CinemaBookingSystem {
    private Scanner scanner;
    private BookingService bookingService;
    private Movie movie;
    private SeatingMap seatingMap;

    public CinemaBookingSystem(){
        scanner=new Scanner(System.in);
        bookingService=new BookingService();
    }

    public CinemaBookingSystem(BookingService bookingService, Movie movie, SeatingMap seatingMap){
        this.bookingService=bookingService;
        this.movie=movie;
        this.seatingMap=seatingMap;
        this.scanner=new Scanner(System.in);
    }

    public void start(){
        setupCinema();
        showMainMenu();
    }

    private void setupCinema(){
        System.out.println("Please define movie title and seating map in [Title] [Row] [SeatsPerRow] format:");
        String userInput = scanner.nextLine();
        String[] parts = userInput.split(" ");
        if(parts.length!=3){
            System.out.println("Invalid input format. Example: Inception 8 10");
            setupCinema();
            return;
        }

        try{
            String title = parts[0];
            int rows = Integer.parseInt(parts[1]);
            int seatsPerRow = Integer.parseInt(parts[2]);
            if(rows<1 || rows>26 || seatsPerRow<1 || seatsPerRow>50){
                System.out.println("Invalid row or seat number. Rows: 1-26, SeatsPerRow: 1-50");
                setupCinema();
                return;
            }

            this.movie = new Movie(title, rows, seatsPerRow);
            this.seatingMap = new SeatingMap(rows, seatsPerRow);

        }catch (NumberFormatException e){
            System.out.println("Invalid number format. Please enter numbers for rows and seats");
            setupCinema();
        }
    }

    void showMainMenu(){
        boolean showMenu = true;

        while(showMenu){
            System.out.println("\nWelcome to MOE Cinema");
            System.out.println("[1] Book tickets for "+movie.getTitle()+" ["+getAvailableSeats()+" seats available]");
            System.out.println("[2] Check bookings");
            System.out.println("[3] Exit");
            System.out.print("Please enter your selection: ");

            String choice = scanner.nextLine();
            switch (choice){
                case "1":
                    bookTickets();
                    break;
                case "2":
                    checkBooking();
                    break;
                case "3":
                    System.out.println("Thank you for using MOE cinema system. Bye!");
                    showMenu=false;
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    private void bookTickets() {
        System.out.print("Enter number of tickets to book, enter blank to go back to main menu: ");
        String input = scanner.nextLine();

        if (input.isEmpty()) {
            return;
        }

        try {
            int numOfTickets = Integer.parseInt(input);
            int availableSeats = getAvailableSeats();

            if (numOfTickets < 1) {
                System.out.println("Please enter a valid number of tickets.");
                return;
            }

            if (numOfTickets > availableSeats) {
                System.out.println("Sorry, there are only " + availableSeats + " seats available.");
                return;
            }

            // Preview default seats (not yet booked)
            List<Seat> selectedSeats = seatingMap.setDefaultSelectedSeats(numOfTickets);
            if (selectedSeats.size() < numOfTickets) {
                throw new NotEnoughSeatsException("Not enough seats available.");
            }

            System.out.println("\nSuccessfully reserved " + numOfTickets + " " + movie.getTitle() + " tickets.");
            System.out.println("Selected seats:\n");
            seatingMap.displaySeating(selectedSeats);
            // handle confirm or not
            handleSeatSelection(selectedSeats, numOfTickets);

        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number format. Please enter a valid number.");
        } catch (NotEnoughSeatsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleSeatSelection(List<Seat> selectedSeats, int numOfTickets) throws NotEnoughSeatsException {
        boolean isConfirmed = false;
        // Ask user to confirm or change selection
        while (!isConfirmed) {
            System.out.print("\nEnter blank to accept seat selection, or enter new seating position: ");
            String inputPosition = scanner.nextLine();

            if (inputPosition.isEmpty()) {
                // Confirm booking
                Booking booking = bookingService.createBooking(movie, seatingMap, selectedSeats);
                System.out.println("Booking ID: " + booking.getBookingId() + " confirmed.");
                isConfirmed=true;
            } else if (isValidSeatPosition(inputPosition)) {
                try {
                    List<Seat> newSelection = seatingMap.setUserSelectedSeats(numOfTickets, inputPosition);
                    if (newSelection.size() < numOfTickets) {
                        System.out.println("Not enough seats available from " + inputPosition + ". Try again.");
                    } else {
                        selectedSeats = newSelection;
                        System.out.println("\nUpdated seat selection:");
                        seatingMap.displaySeating(selectedSeats);
                    }
                } catch (Exception e) {
                    System.out.println("Could not select seats: " + e.getMessage());
                }
            } else {
                System.out.println("Invalid seat position. Please try again.");
            }
        }
    }

    private boolean isValidSeatPosition(String startPosition) {
        if (startPosition == null || startPosition.length() < 2) {
            return false;
        }
        try {
            char row = Character.toUpperCase(startPosition.charAt(0)); // Normalize to uppercase
            int col = Integer.parseInt(startPosition.substring(1)) - 1; // Convert to 0-based index

            return seatingMap.hasRow(row) && seatingMap.isValidColumn(col);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return false;
        }
    }

    private void checkBooking() {
        System.out.print("Enter booking id or enter blank to go back to main menu:");
        String bookingId=scanner.nextLine();
        if(bookingId.isEmpty()){
            return;
        }

        Booking booking = bookingService.getBookingById(bookingId);
        if(booking==null){
            System.out.println("Booking not found");
        }else{
            System.out.println("\nBooking ID: "+booking.getBookingId());
            System.out.println("Selected seats:\n\n");
            seatingMap.displaySeating(booking.getBookedSeats());
        }
    }

    private int getAvailableSeats() {
        int availableSeats = 0;
        for(char row='A'; row< 'A'+seatingMap.getRows();row++){
            for(Seat seat:seatingMap.getRowSeats(row)){
                if(seat.isAvailable()){
                    availableSeats++;
                }
            }
        }
        return availableSeats;
    }

    public static void main(String[] args){
        new CinemaBookingSystem().start();
    }
}
