package com.booking.model;

public class Movie {
    private String title;
    private int rows;
    private int seatsPerRow;

    public Movie(String title, int rows, int seatsPerRow){
        this.title = title;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }
}
