package com.example.qlct.Database;

public class CategorySum {
    private String category;
    private float totalAmount;

    public CategorySum(String category, float totalAmount) {
        this.category = category;
        this.totalAmount = totalAmount;
    }

    public String getCategory() {
        return category;
    }

    public float getTotalAmount() {
        return totalAmount;
    }
}


