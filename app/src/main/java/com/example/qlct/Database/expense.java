package com.example.qlct.Database;

public class expense {
    private int id;
    private String category;
    private float amount;  // Thay đổi kiểu từ double thành float
    private String date;

    // Constructor
    public expense(int id, String category, float amount, String date) {  // Sửa kiểu ở đây
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    // Getter và Setter
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

    public float getAmount() {  // Sửa kiểu dữ liệu ở đây
        return amount;
    }

    public void setAmount(float amount) {  // Sửa kiểu dữ liệu ở đây
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
