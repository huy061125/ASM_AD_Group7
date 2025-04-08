package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.BudgetDB;

import java.util.Calendar;

public class AddBudgetActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText edtBudgetAmount, edtBudgetDate;
    private Button btnSaveBudget, btnCancelBudget;
    private BudgetDB budgetDB;
    private int userId = 1; // Giả định user ID, thực tế có thể lấy từ session hoặc intent
    private static final String TAG = "AddBudget";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        // Ánh xạ View
        spinnerCategory = findViewById(R.id.category_edit_budget_spinner);
        edtBudgetAmount = findViewById(R.id.edtBudgetAmount);
        edtBudgetDate = findViewById(R.id.edtBudgetDate);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);
        btnCancelBudget = findViewById(R.id.btnCancelBudget);

        // Khởi tạo database
        budgetDB = new BudgetDB(this);

        // Cấu hình Spinner
        setupCategorySpinner();

        // Xử lý chọn ngày
        edtBudgetDate.setOnClickListener(v -> showDatePicker());

        // Xử lý lưu ngân sách
        btnSaveBudget.setOnClickListener(v -> saveBudget());

        // Xử lý hủy
        btnCancelBudget.setOnClickListener(v -> finish());
    }

    // Cấu hình danh mục trong Spinner
    private void setupCategorySpinner() {
        String[] categories = {"Select category", "Food", "Entertainment", "Education", "Transport", "Health", "Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    // Hiển thị DatePickerDialog và định dạng ngày thành YYYY-MM-DD
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
            edtBudgetDate.setText(formattedDate);
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    // Kiểm tra đầu vào và lưu ngân sách vào database
    private void saveBudget() {
        String category = spinnerCategory.getSelectedItem().toString();
        String amountStr = edtBudgetAmount.getText().toString().trim();
        String date = edtBudgetDate.getText().toString().trim();
        boolean hasError = false;

        // Reset lỗi cũ
        edtBudgetAmount.setError(null);
        edtBudgetDate.setError(null);

        // Kiểm tra danh mục
        if (spinnerCategory.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a category!", Toast.LENGTH_SHORT).show();
            hasError = true;
        }

        // Kiểm tra số tiền nhập vào
        double amount = 0;
        if (amountStr.isEmpty()) {
            edtBudgetAmount.setError("Required!");
            hasError = true;
        } else {
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    edtBudgetAmount.setError("Amount must be greater than 0!");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                edtBudgetAmount.setError("Invalid format!");
                hasError = true;
            }
        }

        // Kiểm tra ngày nhập vào
        if (date.isEmpty()) {
            edtBudgetDate.setError("Required!");
            hasError = true;
        } else if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            edtBudgetDate.setError("Invalid date format (YYYY-MM-DD)!");
            hasError = true;
        }

        if (hasError) {
            return;
        }

        // Không cắt chuỗi nữa, giữ đầy đủ ngày tháng năm
        String fullDate = date;

        Log.d(TAG, "Saving: userId=" + userId + ", category=" + category + ", amount=" + amount + ", date=" + fullDate);

        long result = -1;
        try {
            result = budgetDB.setBudget(userId, category, amount, fullDate); // Truyền ngày đầy đủ
        } catch (SQLiteException e) {
            Log.e(TAG, "Database error: " + e.getMessage());
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Result: " + result);

        if (result != -1) {
            // ✅ Cập nhật totalBudget trong SharedPreferences sau khi setBudget thành công
            saveTotalBudgetToPreferences(amount);

            Toast.makeText(this, "Budget saved successfully!", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Budget already exists for this category and month!", Toast.LENGTH_SHORT).show();
        }
    }

    // ✅ Hàm cập nhật SharedPreferences
    private void saveTotalBudgetToPreferences(double amount) {
        SharedPreferences prefs = getSharedPreferences("BudgetPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("totalBudget", (float) amount); // Ghi đè lại total mới
        editor.apply();
    }
}
