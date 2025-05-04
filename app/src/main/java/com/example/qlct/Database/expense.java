package com.example.qlct.Database;

public class expense {
    private int id;
    private String category;
    private float amount;
    private String date;
    private String name;
    private boolean isChecked;

    public expense(int id, String category, float amount, String date) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
