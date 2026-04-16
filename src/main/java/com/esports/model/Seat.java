package com.esports.model;

public class Seat {
    private int     id;
    private int     eventId;
    private String  seatNumber;
    private boolean reserved;

    public Seat() {}

    public Seat(int id, int eventId, String seatNumber, boolean reserved) {
        this.id         = id;
        this.eventId    = eventId;
        this.seatNumber = seatNumber;
        this.reserved   = reserved;
    }

    public int     getId()         { return id; }
    public int     getEventId()    { return eventId; }
    public String  getSeatNumber() { return seatNumber; }
    public boolean isReserved()    { return reserved; }

    public void setId(int id)                   { this.id = id; }
    public void setEventId(int eventId)         { this.eventId = eventId; }
    public void setSeatNumber(String seatNumber){ this.seatNumber = seatNumber; }
    public void setReserved(boolean reserved)   { this.reserved = reserved; }

    @Override
    public String toString() {
        String status = reserved ? "  [RESERVED]" : "  [AVAILABLE]";
        return String.format("Seat %-5s %s", seatNumber, status);
    }
}