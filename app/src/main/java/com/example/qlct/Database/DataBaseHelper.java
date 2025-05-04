package com.example.qlct.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Log;
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
                    "date TEXT NOT NULL);";

    //Bảng Income:
    private static final String TABLE_INCOMES = "incomes";

    private static final String CREATE_TABLE_INCOMES =
            "CREATE TABLE " + TABLE_INCOMES + " ("
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

        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_USERNAME + ","+ COLUMN_EMAIL +" FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            String Username = cursor.getString(1);
            String Email = cursor.getString(2);
            user = new User(userId, Username, Email);
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
        db.execSQL("INSERT INTO expenses (category, amount, note, date) VALUES ('Essential', 50.0, 'Mua đồ ăn', '2025-04-01')");
        db.execSQL("INSERT INTO expenses (category, amount, note, date) VALUES ('Leisure', 100.0, 'Đi du lịch Hà Nội', '2025-04-01')");
        db.execSQL("INSERT INTO expenses (category, amount, note, date) VALUES ('Investment', 100.0, 'Mua cổ phần công ty A', '2025-04-02')");
        db.execSQL("INSERT INTO expenses (category, amount, note, date) VALUES ('Unexpected', 10.0, 'Tiền chữa bệnh trĩ', '2025-04-02')");

        db.execSQL("INSERT INTO incomes (category, amount, note, date) VALUES ('Salary', 10.0, 'Lương tháng tư', '2025-04-01')");
        db.execSQL("INSERT INTO incomes (category, amount, note, date) VALUES ('Side Income', 30.0, 'Tiền lãi tiết kiệm', '2025-04-01')");
        db.execSQL("INSERT INTO incomes (category, amount, note, date) VALUES ('Investment Profit', 20.0, 'Tiền bán cổ phần', '2025-04-02')");
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

    public List<ExpenseModel> getListExpenses() {
        List<ExpenseModel> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES, null);

        int idIndex = cursor.getColumnIndex("id");
        int categoryIndex = cursor.getColumnIndex("category");
        int amountIndex = cursor.getColumnIndex("amount");
        int noteIndex = cursor.getColumnIndex("note");
        int dateIndex = cursor.getColumnIndex("date");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(idIndex);
                String cat = cursor.getString(categoryIndex);
                double amount = cursor.getDouble(amountIndex);
                String note = cursor.getString(noteIndex);
                String date = cursor.getString(dateIndex);

                expenses.add(new ExpenseModel(id, cat, amount, note, date, false));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenses;
    }

    public List<IncomeModel> getListIncomes() {
        List<IncomeModel> incomes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INCOMES, null);

        int idIndex = cursor.getColumnIndex("id");
        int categoryIndex = cursor.getColumnIndex("category");
        int amountIndex = cursor.getColumnIndex("amount");
        int noteIndex = cursor.getColumnIndex("note");
        int dateIndex = cursor.getColumnIndex("date");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(idIndex);
                String cat = cursor.getString(categoryIndex);
                double amount = cursor.getDouble(amountIndex);
                String note = cursor.getString(noteIndex);
                String date = cursor.getString(dateIndex);

                incomes.add(new IncomeModel(id, cat, amount, note, date, false));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return incomes;
    }

    public void deleteExpenseById(int id) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            int rowsDeleted = db.delete("expenses", "id = ?", new String[]{String.valueOf(id)});
            if (rowsDeleted == 0) {
                Log.d("Database", "No expense found with ID " + id);
            }
        } catch (Exception e) {
            Log.e("Database", "Error while deleting expense", e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
    public void deleteIncomeById(int id) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            int rowsDeleted = db.delete("incomes", "id = ?", new String[]{String.valueOf(id)});
            if (rowsDeleted == 0) {
                Log.d("Database", "No incomes found with ID " + id);
            }
        } catch (Exception e) {
            Log.e("Database", "Error while deleting incomes", e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
    public void insertExpense(ExpenseModel expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, expense.getCategory());
        values.put(COLUMN_AMOUNT, expense.getAmount());
        values.put(COLUMN_NOTE, expense.getNote());
        values.put(COLUMN_DATE, expense.getDate());

        db.insert(TABLE_EXPENSES, null, values);

        db.close();
    }

    public void insertIncome(IncomeModel income) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, income.getCategory());
        values.put(COLUMN_AMOUNT, income.getAmount());
        values.put(COLUMN_NOTE, income.getNote());
        values.put(COLUMN_DATE, income.getDate());

        db.insert(TABLE_INCOMES, null, values);

        db.close();
    }

    public List<CategorySum> getExpenseSummaryByCategory() {
        List<CategorySum> summaryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT category, SUM(amount) as totalAmount FROM expenses GROUP BY category";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                float totalAmount = cursor.getFloat(cursor.getColumnIndexOrThrow("totalAmount"));
                summaryList.add(new CategorySum(category, totalAmount));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return summaryList;
    }

    public List<CategorySum> getIncomeSummaryByCategory() {
        List<CategorySum> summaryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT category, SUM(amount) as totalAmount FROM incomes GROUP BY category";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                float totalAmount = cursor.getFloat(cursor.getColumnIndexOrThrow("totalAmount"));
                summaryList.add(new CategorySum(category, totalAmount));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return summaryList;
    }

    public void updateIncome(IncomeModel income) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("note", income.getNote());
        values.put("date", income.getDate());
        values.put("category", income.getCategory());
        values.put("amount", income.getAmount());

        db.update("incomes", values, "id = ?", new String[]{String.valueOf(income.getId())});
        db.close();
    }

    public void updateExpense(ExpenseModel expense){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("note", expense.getNote());
        values.put("date", expense.getDate());
        values.put("category", expense.getCategory());
        values.put("amount", expense.getAmount());

        db.update("expenses", values, "id = ?", new String[]{String.valueOf(expense.getId())});
        db.close();
    }

}


