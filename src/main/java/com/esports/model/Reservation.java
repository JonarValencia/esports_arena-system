shibal
aint.racist
Idle

This is the start of the #files channel. 
shibal — 2/19/2026 2:22 PM
@everyone 
package TASK3;

import java.util.List;

public class Main {
    public static void main(String[] args) {

main.txt
2 KB
package TASK3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

repository.txt
2 KB
package TASK3;

public class Student {
    private int studId, age, yearLevel;
    private String firstName, middleName, lastName, gender, course, email, address, phoneNumber;

student.txt
2 KB
shibal — 2/19/2026 2:49 PM
Attachment file type: unknown
Student.db
16.00 KB
shibal — 2/26/2026 10:53 PM
<dependencies>
        <dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.45.1.0</version>
</dependency>
Xiao Lie [FT],  — 2/27/2026 12:05 AM
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    private final String URL = "jdbc:sqlite:Student.db";

New repo.txt
3 KB
@shibal  ito ilagay mo sa repo
Xiao Lie [FT],  — 2/27/2026 12:23 AM
Wag mo pla toh lagayy
Xiao Lie [FT],  — 4/8/2026 10:40 PM
package com.esports.ui;

import com.esports.dao.*;
import com.esports.model.*;
import com.esports.util.ConsoleUtil;
import java.sql.SQLException;

AdminMenu.java
6 KB
package com.esports.ui;

import com.esports.dao.*;
import com.esports.model.*;
import com.esports.util.ConsoleUtil;

UserMenu.java
11 KB
package com.esports.model;

public class Event {
    private int    id;
    private String name;
    private String gameTitle;

Event.java
2 KB
package com.esports.model;

public class Reservation {
    private int    id;
    private int    seatId;
    private int    eventId;

Reservation.java
3 KB
package com.esports.model;

public class Seat {
    private int     id;
    private int     eventId;
    private String  seatNumber;

Seat.java
2 KB
Xiao Lie [FT],  — 4/8/2026 11:20 PM
public abstract class Hardware {
    private int id;
    private String brand;
    protected int spec;

    public Hardware(int id, String brand, int spec) {

Hardware.java
1 KB
public class Laptop extends Hardware {
    public Laptop(int id, String brand, int spec) {
        super(id, brand, spec);
    }

    @Override

Laptop.java
1 KB
public class Phone extends Hardware {
    public Phone(int id, String brand, int spec) {
        super(id, brand, spec);
    }

    @Override

Phone.java
1 KB
Xiao Lie [FT],  — 8:57 PM
abstract class Hardware {
    protected String brand;
    protected int spec;

    public Hardware(String brand, int spec) {
        this.brand = brand;

Hardware.java
1 KB
class Laptop extends Hardware {
    public Laptop(String brand, int spec) { super(brand, spec); }
    @Override
    public String getFormattedSpec() { return spec + "GB RAM"; }
}
Laptop.java
1 KB
class Phone extends Hardware {
    public Phone(String brand, int spec) { super(brand, spec); }
    @Override
    public String getFormattedSpec() { return spec + " Megapixels"; }
}
Phone.java
1 KB
﻿
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