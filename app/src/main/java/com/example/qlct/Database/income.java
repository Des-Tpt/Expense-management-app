package com.example.qlct.Database;

public class income {
    private int id;
    private String category;
    private double amount;
    private String note;
    private String date;

    public income(int id, String category, double amount, String note, String date) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
    }

    public int getId() { return id; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getNote() { return note; }
    public String getDate() { return date; }

}
