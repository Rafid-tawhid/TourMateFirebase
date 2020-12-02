package com.example.tourmatefirebase.models;

public class TourModel {

    private String id;
    private String name;
    private String destination;
    private double budget;
    private String formatedDate;
    private long timeSnap;
    private int month;
    private int year;
    private boolean iscompleted=false;

    public TourModel() {
    }

    public TourModel(String id, String name, String destination, double budget, String formatedDate, long timeSnap, int month, int year) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.budget = budget;
        this.formatedDate = formatedDate;
        this.timeSnap = timeSnap;
        this.month = month;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getFormatedDate() {
        return formatedDate;
    }

    public void setFormatedDate(String formatedDate) {
        this.formatedDate = formatedDate;
    }

    public long getTimeSnap() {
        return timeSnap;
    }

    public void setTimeSnap(long timeSnap) {
        this.timeSnap = timeSnap;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "TourModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", destination='" + destination + '\'' +
                ", budget=" + budget +
                ", formatedDate='" + formatedDate + '\'' +
                ", timeSnap=" + timeSnap +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
