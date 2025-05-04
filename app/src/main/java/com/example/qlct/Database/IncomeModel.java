package com.example.qlct.Database;

public class IncomeModel {
    private int id;
    private String category;
    private double amount;
    private String note;
    private String date;
    private boolean isChecked;

    public IncomeModel(int id, String category, double amount, String name, String date, boolean isChecked) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.note = name;
        this.date = date;
        this.isChecked = isChecked;
    }

    public int getId() { return id; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getNote() { return note; }
    public String getDate() { return date; }
    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
