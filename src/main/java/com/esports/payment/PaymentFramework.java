package com.esports.payment;

abstract class PaymentFramework {
    protected String name;
    protected String transactionID;
    protected double amount;
    protected double discount;
    protected boolean hasValidPaymentMethod;
    protected double creditBalance;


    public PaymentFramework(String name, String transactionID, double amount,
                            boolean hasValidPaymentMethod, double discount,
                            double creditBalance) {
        this.name              = name;
        this.transactionID     = transactionID;
        this.amount            = amount;
        this.discount          = discount;
        this.hasValidPaymentMethod = hasValidPaymentMethod;
        this.creditBalance     = creditBalance;
    }

    public boolean validatePayment(double finalAmount) {
        return hasValidPaymentMethod &&
               creditBalance >= finalAmount;
    }

    public double applyVATInclusiveTax() {
        double vatAmount = amount * 12 / 112;
        System.out.println("VAT Amount: " + vatAmount);
        return vatAmount;
    }

    public double applyDiscount() {
        double discountedAmount = amount - discount;
        System.out.println("Discount Applied: " + discount);
        return discountedAmount;
    }

    public void finalizeTransaction(double finalAmount) {
        creditBalance -= finalAmount;
        System.out.println("Transaction Finalized!");
        System.out.println("Final Amount: " + finalAmount);
        System.out.println("Remaining Balance: " + creditBalance);
    }


    public void processInvoice() {
        System.out.println("=== PROCESSING INVOICE ===");

        double finalAmount = applyDiscount();
        applyVATInclusiveTax();

        System.out.println("Final Amount: " + finalAmount);

        if (validatePayment(finalAmount)) {
            finalizeTransaction(finalAmount);
        } else {
            System.out.println(
                "Payment Failed: Invalid Payment Method or Insufficient Credit!"
            );
        }
    }
}