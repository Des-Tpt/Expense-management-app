package com.example.qlct.Database;

import android.content.Context;
import java.util.ArrayList;

public class UserDataBase {
    private static final ArrayList<User> users = new ArrayList<>();

    public static void addUser(Context context, String email, String password, String username) {
        User newUser = new User(context, email, password, username);
        users.add(newUser);
    }

    public static User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
}
