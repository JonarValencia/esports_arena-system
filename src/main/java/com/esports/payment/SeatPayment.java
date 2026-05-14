package com.esports.payment;

public class SeatPayment extends PaymentFramework {

    private final String paymentMethod;
    private final String seatNumber;
    private final String eventName;

    private double capturedVAT;
    private double capturedFinal;
    private boolean paymentSuccess;

    public SeatPayment(String name, String transactionID, double amount,
                       boolean hasValidPaymentMethod, double discount,
                       double creditBalance, String paymentMethod,
                       String seatNumber, String eventName) {
        super(name, transactionID, amount, hasValidPaymentMethod, discount, creditBalance);
        this.paymentMethod = paymentMethod;
        this.seatNumber    = seatNumber;
        this.eventName     = eventName;
    }

    @Override
    public void processInvoice() {
        capturedFinal  = amount - discount;              
        capturedVAT    = capturedFinal * 12.0 / 112.0; 
        paymentSuccess = validatePayment(capturedFinal);
        super.processInvoice();                         
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public String getPaymentMethod()  { return paymentMethod; }
    public String getSeatNumber()     { return seatNumber; }
    public String getEventName()      { return eventName; }
    public double getCapturedVAT()    { return capturedVAT; }
    public double getCapturedFinal()  { return capturedFinal; }
    public boolean isPaymentSuccess() { return paymentSuccess; }
    public String getTransactionID()  { return transactionID; }
    public String getName()           { return name; }
    public double getAmount()         { return amount; }
    public double getDiscount()       { return discount; }
    public double getCreditBalance()  { return creditBalance; }
}