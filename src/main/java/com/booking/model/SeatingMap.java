package com.booking.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * manage seating arrangement
 */
public class SeatingMap {
    private Map<Character, List<Seat>> seats;
    private int rows;
    private int seatsPerRow;

    public SeatingMap(int rows, int seatsPerRow){
        this.rows= rows;
        this.seatsPerRow=seatsPerRow;
        this.seats= new TreeMap<>(Collections.reverseOrder());
        initializeSeats();
    }

    private void initializeSeats() {
        for(int i=0;i<rows;i++){
            char rowName = (char) ('A'+i);
            List<Seat> rowSeats = new ArrayList<>();
            for(int j=1;j<=seatsPerRow;j++){
                rowSeats.add(new Seat(rowName, j));
            }
            seats.put(rowName, rowSeats);
        }
    }

    /**
     * default seat selection
     * - Start from the furthest row from the screen
     * - Start from the middle-most possible col
     * - when there are not enough seats, overflow to the next row
     */
    public List<Seat> setDefaultSelectedSeats(int numOfTickets){
        List<Seat> selectedSeats = new ArrayList<>();

        List<Character> sortedRows = new ArrayList<>(seats.keySet());
        Collections.sort(sortedRows);
        for(char row:sortedRows){
            List<Seat> rowSeats = seats.get(row);

            int mid = seatsPerRow/2;  //seatsPerRow-1/2 ?
            int left= mid-1;
            int right = mid;

            while(selectedSeats.size()<numOfTickets){
                if(left >= 0 && rowSeats.get(left).isAvailable()){
                    selectedSeats.add(rowSeats.get(left));
                }
                if(right<seatsPerRow && selectedSeats.size()<numOfTickets && rowSeats.get(right).isAvailable()){
                    selectedSeats.add(rowSeats.get(right));
                }
                left--;
                right++;

                if(left<0 && right>=seatsPerRow){
                    break;
                }
            }

            if(selectedSeats.size() == numOfTickets){
                break;
            }
        }

        return selectedSeats;
    }

    /**
     * user selected seats
     * - Start from the specified position all the way to the right
     * - when there are not enough seats, overflow to the next row
     */
    public List<Seat> setUserSelectedSeats(int numOfTickets, String startPosition){
        List<Seat> selectedSeats = new ArrayList<>();

        char startRow = Character.toUpperCase(startPosition.charAt(0));
        int startCol = Integer.parseInt(startPosition.substring(1)) - 1;
        List<Character> rowOrder = new ArrayList<>(seats.keySet());
        Collections.sort(rowOrder);

        int startRowIndex = rowOrder.indexOf(startRow);
        if (startRowIndex == -1) return selectedSeats;

        for (int i = startRowIndex; i < rowOrder.size() && selectedSeats.size() < numOfTickets; i++) {
            char row = rowOrder.get(i);
            List<Seat> rowSeats = seats.get(row);

            int colStart = (i==startRowIndex) ? startCol : 1;

            for (int j = colStart;j < seatsPerRow && selectedSeats.size() < numOfTickets; j++) {
                Seat seat = rowSeats.get(j);
                if (seat.isAvailable()) {
                    selectedSeats.add(seat);
                }
            }
        }

        return selectedSeats;
    }

    public void bookSeats(List<Seat> selectedSeats){
        selectedSeats.forEach(Seat::bookSeat);
    }

    public void displaySeating(List<Seat> currentSelection){
        List<Character> sortedRows = new ArrayList<>(seats.keySet());
        sortedRows.sort(Collections.reverseOrder()); // Display from H (closest) to A (furthest)

        System.out.println("\n\t\t\t S C R E E N");
        System.out.println("_______________________________________________________");

        for (char row : sortedRows) {
            System.out.print(row + "  ");
            for (Seat seat : seats.get(row)) {
                if (!seat.isAvailable()) {
                    if (currentSelection != null && currentSelection.contains(seat)) {
                        System.out.print("O   "); // (edge case)?
                    } else {
                        System.out.print("#   "); // confirmed booking
                    }
                } else if (currentSelection != null && currentSelection.contains(seat)) {
                    System.out.print("O   "); // currently selected
                } else {
                    System.out.print(".   "); // available
                }
            }
            System.out.println();
        }

        System.out.print("    ");
        for (int i = 1; i <= seatsPerRow; i++) {
            System.out.printf("%-4d", i);
        }
        System.out.println();
    }

    public int getRows() {
        return rows;
    }

    public List<Seat> getRowSeats(char row){
        return seats.get(row);
    }

    public boolean hasRow(char row){
        return seats.containsKey(row);
    }

    public boolean isValidColumn(int col){
        return col>=0 && col<seatsPerRow;
    }

}
