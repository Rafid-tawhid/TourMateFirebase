package com.example.tourmatefirebase.models;

public class ExpensesModel {
    public String expId;
    public String tourId;
    public String titel;
    public Double amount;
    public long timeSnap;

    public ExpensesModel() {
    }

    public ExpensesModel(String expId, String tourId, String titel, Double amount, long timeSnap) {
        this.expId = expId;
        this.tourId = tourId;
        this.titel = titel;
        this.amount = amount;
        this.timeSnap = timeSnap;
    }
}
