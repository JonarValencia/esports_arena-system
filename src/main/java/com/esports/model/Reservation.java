
package com.esports.model;

public class Reservation {
    private int    id;
    private int    seatId;
    private int    eventId;
    private String userName;
    private String userEmail;
    private String reservedAt;

    // Joined display fields
    private String seatNumber;
    private String eventName;
    private String eventDate;
    private String eventTime;

    public Reservation() {}

    public int    getId()         { return id; }
    public int    getSeatId()     { return seatId; }
    public int    getEventId()    { return eventId; }
    public String getUserName()   { return userName; }
    public String getUserEmail()  { return userEmail; }
    public String getReservedAt() { return reservedAt; }
    public String getSeatNumber() { return seatNumber; }
    public String getEventName()  { return eventName; }
    public String getEventDate()  { return eventDate; }
    public String getEventTime()  { return eventTime; }

    public void setId(int id)                   { this.id = id; }
    public void setSeatId(int seatId)           { this.seatId = seatId; }
    public void setEventId(int eventId)         { this.eventId = eventId; }
    public void setUserName(String userName)    { this.userName = userName; }
    public void setUserEmail(String userEmail)  { this.userEmail = userEmail; }
    public void setReservedAt(String r)         { this.reservedAt = r; }
    public void setSeatNumber(String s)         { this.seatNumber = s; }
    public void setEventName(String e)          { this.eventName = e; }
    public void setEventDate(String d)          { this.eventDate = d; }
    public void setEventTime(String t)          { this.eventTime = t; }

    public void printDetails() {
        System.out.printf(
            "  Reservation ID : %d%n" +
            "  Name           : %s%n" +
            "  Email          : %s%n" +
            "  Event          : %s (%s %s)%n" +
            "  Seat           : %s%n" +
            "  Reserved At    : %s%n",
            id, userName, userEmail,
            eventName, eventDate, eventTime,
            seatNumber, reservedAt
        );
    }
}