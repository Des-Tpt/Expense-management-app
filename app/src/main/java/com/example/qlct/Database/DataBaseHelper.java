package com.example.qlct.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Pair;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "qlct.db";
    private static final int DATABASE_VERSION = 1;

    //Bảng user:
    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "username TEXT NOT NULL);";

    private static final String COLUMN_ID = "id";
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_USERNAME = "username";

    //Bảng expenses:

    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DATE = "date";

    private static final String CREATE_TABLE_EXPENSES =
            "CREATE TABLE expenses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "category TEXT CHECK(category IN ('Essential', 'Leisure', 'Investment', 'Unexpected')) NOT NULL, " +
                    "amount REAL NOT NULL, " +
                    "note TEXT, " +
                    "date TEXT NOT NULL)";

    //Bảng Income:
    private static final String TABLE_INCOMES = "incomes";

    private static final String CREATE_TABLE_INCOMES = "CREATE TABLE " + TABLE_INCOMES + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORY + " TEXT CHECK(" + COLUMN_CATEGORY + " IN ('Salary', 'Side Income', 'Investment Profit')), "
            + COLUMN_AMOUNT + " REAL, "
            + COLUMN_NOTE + " TEXT, "
            + COLUMN_DATE + " TEXT);";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS expenses");
        db.execSQL(CREATE_TABLE_INCOMES);
        db.execSQL(CREATE_TABLE_EXPENSES);
        db.execSQL(CREATE_USERS_TABLE);
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS expenses");
        db.execSQL("DROP TABLE IF EXISTS incomes");
        db.execSQL("DROP TABLE IF EXISTS expenses");
        onCreate(db);
    }



    public User checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_USERNAME + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            String username = cursor.getString(1);
            user = new User(userId, username);
        }

        cursor.close();
        db.close();
        return user;
    }
    public boolean addUser(String email, String password, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password);
        values.put("username", username);

        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    private void insertSampleData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO expenses (category, amount, note, date) VALUES ('Essential', 50.0, 'Lunch', '2025-04-01')");
        db.execSQL("INSERT INTO expenses (category, amount, note, date) VALUES ('Leisure', 20.0, 'Bus', '2025-04-01')");
        db.execSQL("INSERT INTO expenses (category, amount, note, date) VALUES ('Investment', 100.0, 'Clothes', '2025-04-02')");
        db.execSQL("INSERT INTO expenses (category, amount, note, date) VALUES ('Unexpected', 10.0, 'Clothes', '2025-04-02')");

        db.execSQL("INSERT INTO incomes (category, amount, note, date) VALUES ('Salary', 10.0, 'Lunch', '2025-04-01')");
        db.execSQL("INSERT INTO incomes (category, amount, note, date) VALUES ('Side Income', 30.0, 'Bus', '2025-04-01')");
        db.execSQL("INSERT INTO incomes (category, amount, note, date) VALUES ('Investment Profit', 20.0, 'Clothes', '2025-04-02')");
    }

    public Pair<List<PieEntry>, Float> getExpenseDataAndTotal() {
        List<PieEntry> entries = new ArrayList<>();
        float totalExpense = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT category, SUM(amount) FROM expenses GROUP BY category";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(0);
                float totalAmount = cursor.getFloat(1);
                entries.add(new PieEntry(totalAmount, category));
                totalExpense += totalAmount;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return new Pair<>(entries, totalExpense);
    }

    public Pair<List<PieEntry>, Float> getIncomeDataAndTotal() {
        List<PieEntry> entries = new ArrayList<>();
        float totalIncomes = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT category, SUM(amount) FROM incomes GROUP BY category";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(0);
                float totalAmount = cursor.getFloat(1);
                entries.add(new PieEntry(totalAmount, category));
                totalIncomes += totalAmount;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return new Pair<>(entries, totalIncomes);
    }
}

