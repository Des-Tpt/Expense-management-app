package com.example.qlct.Database;

public class User {
    private int id;
    private String email;
    private String username;

    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }
    public String getUsername()
    {
        return username;
    }
    public String getEmail() {
        return email;
    }

}
