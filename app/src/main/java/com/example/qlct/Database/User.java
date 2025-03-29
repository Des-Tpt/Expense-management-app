package com.example.qlct.Database;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_ID_COUNTER = "idCounter";

    private static int idCounter = 1;
    private String id;
    private String email;
    private String password;
    private String username;

    public User(Context context, String email, String password, String username) {
        this.id = String.valueOf(getNextId(context));
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }


    private static int getNextId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        idCounter = prefs.getInt(KEY_ID_COUNTER, 1);
        saveIdCounter(context, idCounter + 1);
        return idCounter;
    }

    private static void saveIdCounter(Context context, int newCounter) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_ID_COUNTER, newCounter);
        editor.apply();
    }
}
