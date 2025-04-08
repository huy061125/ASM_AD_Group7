package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.example.myapplication.model.BudgetModel;
import com.example.myapplication.model.ExpensesModel;

import java.util.ArrayList;
import java.util.List;

public class BudgetDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "campus_expenses";
    public static final String DB_TABLE = "budgets";
    public static final String EXPENSE_TABLE = "expenses";
    public static final int DB_VERSION = 5; // Cập nhật version mới

    // Budget table columns
    public static final String ID_COL = "id";
    public static final String USER_ID_COL = "user_id";
    public static final String CATEGORY_COL = "category";
    public static final String AMOUNT_COL = "amount";
    public static final String MONTH_YEAR_COL = "month_year";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    // Expenses table columns
    public static final String EXPENSE_ID_COL = "id";
    public static final String EXPENSE_USER_ID_COL = "user_id";
    public static final String EXPENSE_CATEGORY_COL = "category";
    public static final String EXPENSE_AMOUNT_COL = "amount";
    public static final String EXPENSE_DATE_COL = "date";
    public static final String EXPENSE_CREATED_AT = "created_at";
    public static final String EXPENSE_DESCRIPTION = "description";

    public BudgetDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        // Tạo bảng budgets
        String budgetQuery = "CREATE TABLE IF NOT EXISTS " + DB_TABLE + " ( "
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_ID_COL + " INTEGER NOT NULL, "
                + CATEGORY_COL + " TEXT NOT NULL, "
                + AMOUNT_COL + " REAL NOT NULL, "
                + MONTH_YEAR_COL + " TEXT NOT NULL, "
                + CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + "UNIQUE(" + USER_ID_COL + ", " + CATEGORY_COL + ", " + MONTH_YEAR_COL + ")"
                + ")";
        db.execSQL(budgetQuery);

        // Tạo bảng expenses
        String expenseQuery = "CREATE TABLE IF NOT EXISTS " + EXPENSE_TABLE + " ( "
                + EXPENSE_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EXPENSE_USER_ID_COL + " INTEGER NOT NULL, "
                + EXPENSE_CATEGORY_COL + " TEXT NOT NULL, "
                + EXPENSE_DESCRIPTION + " TEXT NOT NULL, "
                + EXPENSE_AMOUNT_COL + " REAL NOT NULL, "
                + EXPENSE_DATE_COL + " TEXT NOT NULL, "
                + EXPENSE_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP "
                + ")";
        db.execSQL(expenseQuery);
    }

    //2 phương thức này sử lý Update SQL_lite tránh bị lỗi sv
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }
    private boolean tableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{tableName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Thêm kiểm tra bảng trước khi thao tác
    private SQLiteDatabase getCheckedDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!tableExists(db, DB_TABLE) || !tableExists(db, EXPENSE_TABLE)) {
            createTables(db);
        }
        return db;
    }

    // Thêm khoản chi tiêu
    public long addExpense(int userId, String description, String category, double amount, String date) {
        SQLiteDatabase db = getCheckedDatabase();
        ContentValues values = new ContentValues();
        values.put(EXPENSE_USER_ID_COL, userId);
        values.put(EXPENSE_DESCRIPTION, description);
        values.put(EXPENSE_CATEGORY_COL, category);
        values.put(EXPENSE_AMOUNT_COL, amount);
        values.put(EXPENSE_DATE_COL, date);

        long insert = db.insert(EXPENSE_TABLE, null, values);
        db.close();
        return insert;
    }

    // Lấy toàn bộ khoản Expenses vs hiển thị
    public List<ExpensesModel> getAllExpenses(int userId) {
        List<ExpensesModel> expensesList = new ArrayList<>();
        SQLiteDatabase db = getCheckedDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EXPENSE_TABLE + " WHERE user_id = ?",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(EXPENSE_ID_COL));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(EXPENSE_CATEGORY_COL));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(EXPENSE_AMOUNT_COL));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(EXPENSE_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(EXPENSE_DATE_COL));

                expensesList.add(new ExpensesModel(id, userId, description, category, amount, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expensesList;
    }

    // Lấy tất cả Budget vs hiển thị
    public List<BudgetModel> getAllBudget(int userId) {
        List<BudgetModel> budgetList = new ArrayList<>();
        SQLiteDatabase db = getCheckedDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TABLE + " WHERE " + USER_ID_COL + " = ?",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COL));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(AMOUNT_COL));
                String monthYear = cursor.getString(cursor.getColumnIndexOrThrow(MONTH_YEAR_COL));

                budgetList.add(new BudgetModel(id, userId, category, amount, monthYear));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return budgetList;
    }

    // Thêm budget vs create budget
    public long setBudget(int userId, String category, double amount, String budgetMonth) {
        SQLiteDatabase db = getCheckedDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID_COL, userId);
        values.put(CATEGORY_COL, category);
        values.put(AMOUNT_COL, amount);
        values.put(MONTH_YEAR_COL, budgetMonth);

        long insert = db.insertWithOnConflict(DB_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return insert;
    }

    // Cập nhật ngân sách vs edit budget
    public boolean updateBudget(int budgetId, String category, double amount, String date) {
        SQLiteDatabase db = getCheckedDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COL, category);
        values.put(AMOUNT_COL, amount);
        values.put(MONTH_YEAR_COL, date);
        values.put(UPDATED_AT, "CURRENT_TIMESTAMP");

        int rowsAffected = db.update(DB_TABLE, values, ID_COL + " = ?", new String[]{String.valueOf(budgetId)});
        db.close();
        return rowsAffected > 0;
    }

    ////Delete Budget trong cơ sở dữ liệu Budget
    public boolean deleteBudget(int budgetId) {
        SQLiteDatabase db = getCheckedDatabase();
        int deletedRows = db.delete(DB_TABLE, ID_COL + "=?", new String[]{String.valueOf(budgetId)});
        db.close();
        return deletedRows > 0;
    }

    //Update thông tin của Expenses
    public boolean updateExpense(int expenseId, String newDescription, double newAmount, String newDate, String newCategory) {
        SQLiteDatabase db = getCheckedDatabase();
        ContentValues values = new ContentValues();
        values.put(EXPENSE_DESCRIPTION, newDescription);
        values.put(EXPENSE_AMOUNT_COL, newAmount);
        values.put(EXPENSE_DATE_COL, newDate);
        values.put(EXPENSE_CATEGORY_COL, newCategory);

        int rowsAffected = db.update(EXPENSE_TABLE, values, EXPENSE_ID_COL + " = ?", new String[]{String.valueOf(expenseId)});
        db.close();
        return rowsAffected > 0;
    }

    //Delete một bản ghi Expenses trong cơ sở dữ liệu Expense SQLite dựa trên ID của nó.
    public boolean deleteExpense(int expenseId) {
        SQLiteDatabase db = getCheckedDatabase();
        int deletedRows = db.delete(EXPENSE_TABLE, EXPENSE_ID_COL + "=?", new String[]{String.valueOf(expenseId)});
        db.close();
        return deletedRows > 0;
    }

    //Lấy tổng Budget  và Expenses vs tính tổng tác động lên Total khi thêm sửa xóa ở Budger vs Expenses
    public double getTotalBudget(int userId) {
        double totalBudget = 0;
        double totalExpenses = 0;
        SQLiteDatabase db = getCheckedDatabase();

        // Lấy tổng Budget
        Cursor budgetCursor = db.rawQuery("SELECT SUM(amount) FROM " + DB_TABLE + " WHERE user_id = ?",
                new String[]{String.valueOf(userId)});
        if (budgetCursor.moveToFirst()) {
            totalBudget = budgetCursor.getDouble(0);
        }
        budgetCursor.close();

        // Lấy tổng Expenses
        Cursor expenseCursor = db.rawQuery("SELECT SUM(amount) FROM " + EXPENSE_TABLE + " WHERE user_id = ?",
                new String[]{String.valueOf(userId)});
        if (expenseCursor.moveToFirst()) {
            totalExpenses = expenseCursor.getDouble(0);
        }
        expenseCursor.close();

        db.close();

        // Trả về ngân sách còn lại (total Budget - Total Expenses)
        return totalBudget - totalExpenses;
    }

    //lấy dữ liệu hiển thị Categories
    public List<String> getBudgetCategories(int userId) {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = getCheckedDatabase();

        String query = "SELECT DISTINCT category FROM " + DB_TABLE + " WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                categories.add(cursor.getString(0));
            }
            cursor.close();
        }
        db.close();
        return categories;
    }




}