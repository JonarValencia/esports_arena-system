package com.esports.model;

public class Transaction {
    private int    id;
    private int    reservationId;
    private String transactionId;
    private String userName;
    private String userEmail;
    private String seatNumber;
    private String eventName;
    private String paymentMethod;
    private double amount;
    private double discount;
    private double vatAmount;
    private double finalAmount;
    private String status;      // "SUCCESS" or "FAILED"
    private String createdAt;

    public int    getId()            { return id; }
    public int    getReservationId() { return reservationId; }
    public String getTransactionId() { return transactionId; }
    public String getUserName()      { return userName; }
    public String getUserEmail()     { return userEmail; }
    public String getSeatNumber()    { return seatNumber; }
    public String getEventName()     { return eventName; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getAmount()        { return amount; }
    public double getDiscount()      { return discount; }
    public double getVatAmount()     { return vatAmount; }
    public double getFinalAmount()   { return finalAmount; }
    public String getStatus()        { return status; }
    public String getCreatedAt()     { return createdAt; }

    public void setId(int id)                    { this.id = id; }
    public void setReservationId(int r)          { this.reservationId = r; }
    public void setTransactionId(String t)       { this.transactionId = t; }
    public void setUserName(String u)            { this.userName = u; }
    public void setUserEmail(String e)           { this.userEmail = e; }
    public void setSeatNumber(String s)          { this.seatNumber = s; }
    public void setEventName(String e)           { this.eventName = e; }
    public void setPaymentMethod(String p)       { this.paymentMethod = p; }
    public void setAmount(double a)              { this.amount = a; }
    public void setDiscount(double d)            { this.discount = d; }
    public void setVatAmount(double v)           { this.vatAmount = v; }
    public void setFinalAmount(double f)         { this.finalAmount = f; }
    public void setStatus(String s)              { this.status = s; }
    public void setCreatedAt(String c)           { this.createdAt = c; }

    public void printDetails() {
        String statusTag = status.equals("SUCCESS") ? "[SUCCESS]" : "[FAILED]";
        System.out.printf(
            "  Transaction ID : %s  %s%n" +
            "  Name           : %s (%s)%n" +
            "  Event          : %s%n" +
            "  Seat           : %s%n" +
            "  Payment Method : %s%n" +
            "  Base Price     : PHP %,.2f%n" +
            "  Discount       : PHP %,.2f%n" +
            "  VAT (12%%)     : PHP %,.2f%n" +
            "  Final Amount   : PHP %,.2f%n" +
            "  Date           : %s%n",
            transactionId, statusTag,
            userName, userEmail,
            eventName, seatNumber,
            paymentMethod,
            amount, discount, vatAmount, finalAmount,
            createdAt
        );
    }
}
