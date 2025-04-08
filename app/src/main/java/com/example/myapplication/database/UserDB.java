package com.example.myapplication.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myapplication.model.UserModel;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "campus_expenses";
    public static final String DB_TABLE = "users";
    public static final int DB_VERSION = 2;  // Tăng phiên bản để tránh lỗi downgrade

    // Định nghĩa các cột trong bảng users
    public static final String ID_COL = "id";
    public static final String USERNAME_COL = "username";
    public static final String PASSWORD_COL = "password";
    public static final String EMAIL_COL = "email";
    public static final String PHONE_COL = "phone";
    public static final String ROLE_ID_COL = "role_id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String DELETED_AT = "deleted_at";

    public UserDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        String query = "CREATE TABLE " + DB_TABLE + " ( "
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USERNAME_COL + " VARCHAR(60) NOT NULL, "
                + PASSWORD_COL + " VARCHAR(230) NOT NULL, "
                + EMAIL_COL + " VARCHAR(65) NOT NULL, "
                + PHONE_COL + " VARCHAR(30) NOT NULL, "  // Sửa lỗi VAR_CHAR thành VARCHAR
                + ROLE_ID_COL + " INTEGER, "
                + CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + DELETED_AT + " DATETIME )";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public long insertUserToDatabase(String username, String password, String email, String phone) {
        // Lấy thời gian hiện tại theo định dạng yyyy-MM-dd HH:mm:ss
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String datenow = dtf.format(ZonedDateTime.now());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME_COL, username);
        values.put(PASSWORD_COL, password);
        values.put(EMAIL_COL, email);
        values.put(PHONE_COL, phone);
        values.put(ROLE_ID_COL, 0);
        values.put(CREATED_AT, datenow);
        values.put(UPDATED_AT, datenow);

        long insert = db.insert(DB_TABLE, null, values);
        db.close();
        return insert;
    }

    public boolean checkUsernameExists(String username) {
        boolean exists = false;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] cols = { ID_COL, USERNAME_COL, EMAIL_COL, PHONE_COL, ROLE_ID_COL };
            String condition = USERNAME_COL + " =? ";
            String[] params = { username };
            Cursor cursor = db.query(DB_TABLE, cols, condition, params, null, null, null);
            if (cursor.getCount() > 0) {
                exists = true;
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return exists;
    }

    @SuppressLint("Range")
    public UserModel getInfoUser(String username, String data, int type) {
        UserModel user = new UserModel();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] cols = { ID_COL, USERNAME_COL, EMAIL_COL, PHONE_COL, ROLE_ID_COL };

            String[] params = { username, data };
            String condition = (type == 0)
                    ? (USERNAME_COL + " =? AND " + PASSWORD_COL + " =? ")
                    : (USERNAME_COL + " =? AND " + EMAIL_COL + " =? ");

            Cursor cursor = db.query(DB_TABLE, cols, condition, params, null, null, null);
            if (cursor.moveToFirst()) {
                user.setId(cursor.getInt(cursor.getColumnIndex(ID_COL)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME_COL)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL_COL)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(PHONE_COL)));
                user.setRoleID(cursor.getInt(cursor.getColumnIndex(ROLE_ID_COL)));
            }
            cursor.close();
            db.close();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public int updateAccountPassword(int idAccount, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PASSWORD_COL, newPassword);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            values.put(UPDATED_AT, getCurrentTime());  // Cập nhật thời gian
        }

        String condition = ID_COL + " =? ";
        String[] params = { String.valueOf(idAccount) };
        int update = db.update(DB_TABLE, values, condition, params);
        db.close();
        return update;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dtf.format(ZonedDateTime.now());
    }
}
