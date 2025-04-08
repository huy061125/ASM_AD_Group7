package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.BudgetDB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditExpensesActivity extends AppCompatActivity {

    private EditText edtDescription, edtAmount, edtDate;
    private Spinner spinnerCategory;
    private Button btnSave, btnCancel;
    private ImageView btnDelete;

    private int expenseId;
    private BudgetDB db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add_expenses);

        // Initialize UI elements
        edtDescription = findViewById(R.id.description_edit_expenses);
        edtAmount = findViewById(R.id.edt_edit_expenses_amount);
        edtDate = findViewById(R.id.expenses_edit_date);
        spinnerCategory = findViewById(R.id.spinner_edit_expenses);
        btnSave = findViewById(R.id.add_edit_expenses_button);
        btnCancel = findViewById(R.id.cancel_edit_expense_button);
        btnDelete = findViewById(R.id.delete_expenses);

        db = new BudgetDB(this);

        setupCategorySpinner();

        // Prevent manual input in the date field
        edtDate.setFocusable(false);
        edtDate.setInputType(InputType.TYPE_NULL);
        edtDate.setOnClickListener(v -> showDatePickerDialog());

        // Receive data from Intent
        Intent intent = getIntent();
        expenseId = intent.getIntExtra("expense_id", -1);
        if (expenseId == -1) {
            Toast.makeText(this, "Invalid expense ID!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String description = intent.getStringExtra("description");
        double amount = intent.getDoubleExtra("amount", 0);
        String date = intent.getStringExtra("date");
        String category = intent.getStringExtra("category");

        // Populate fields with data
        edtDescription.setText(description);
        edtAmount.setText(String.valueOf(amount));
        edtDate.setText(date);

        if (category != null) {
            int categoryPosition = ((ArrayAdapter<String>) spinnerCategory.getAdapter()).getPosition(category);
            spinnerCategory.setSelection(categoryPosition);
        }

        // Set button click listeners
        btnSave.setOnClickListener(v -> updateExpense());
        btnDelete.setOnClickListener(v -> confirmDeleteExpense());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void setupCategorySpinner() {
        int userId = getCurrentUserId();
        if (userId <= 0) userId = 1;

        // Gọi qua instance thay vì static
        List<String> categories = db.getBudgetCategories(userId);
        if (categories == null || categories.isEmpty()) {
            categories = new ArrayList<>();
        }

        categories.add(0, "Select category");

        // Dùng layout custom để chỉnh màu chữ
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }
    private int getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getInt("userId", -1);
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            edtDate.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void updateExpense() {
        String newDescription = edtDescription.getText().toString().trim();
        String newAmountStr = edtAmount.getText().toString().trim();
        String newDate = edtDate.getText().toString().trim();
        String newCategory = spinnerCategory.getSelectedItem().toString();

        if (newDescription.isEmpty() || newAmountStr.isEmpty() || newDate.isEmpty() || "Select category".equals(newCategory)) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        double newAmount;
        try {
            newAmount = Double.parseDouble(newAmountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = db.updateExpense(expenseId, newDescription, newAmount, newDate, newCategory);

        if (success) {
            Toast.makeText(this, "Expense updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDeleteExpense() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Yes", (dialog, which) -> deleteExpense())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteExpense() {
        boolean deleted = db.deleteExpense(expenseId);

        if (deleted) {
            Toast.makeText(this, "Expense deleted!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete!", Toast.LENGTH_SHORT).show();
        }
    }
}
