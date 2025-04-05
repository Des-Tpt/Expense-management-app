package com.example.qlct.Database;

public class ExpenseModel {
    private int id;
    private String category;
    private double amount;
    private String note;  // Gán từ "note"
    private String date;
    private boolean isChecked;

    public ExpenseModel(int id, String category, double amount, String note, String date, boolean isChecked) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.note = note;
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
}
