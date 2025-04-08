package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.BudgetDB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {

    private EditText expenseDescription, expenseAmount, expenseDate;
    private Spinner categorySpinner;
    private Button addExpenseButton, cancelExpenseButton;
    private BudgetDB budgetDB;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        expenseDescription = findViewById(R.id.expense_name);
        expenseAmount = findViewById(R.id.expense_amount);
        expenseDate = findViewById(R.id.expense_date);
        categorySpinner = findViewById(R.id.category_spinner);
        addExpenseButton = findViewById(R.id.add_expense_button);
        cancelExpenseButton = findViewById(R.id.cancel_expense_button);

        cancelExpenseButton.setOnClickListener(v -> finish());

        budgetDB = new BudgetDB(this);

        setupCategorySpinner();
        expenseDate.setOnClickListener(v -> showDatePicker());
        addExpenseButton.setOnClickListener(v -> addExpense());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
            expenseDate.setText(formattedDate);
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void setupCategorySpinner() {
        int userId = getCurrentUserId();
        if (userId <= 0) userId = 1;

        // Gọi qua instance thay vì static
        List<String> categories = budgetDB.getBudgetCategories(userId);
        if (categories == null || categories.isEmpty()) {
            categories = new ArrayList<>();
        }

        categories.add(0, "Select category");

        // Dùng layout custom để chỉnh màu chữ
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void addExpense() {
        String description = expenseDescription.getText().toString().trim();
        String amountStr = expenseAmount.getText().toString().trim();
        String date = expenseDate.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        expenseDescription.setError(null);
        expenseAmount.setError(null);
        expenseDate.setError(null);

        boolean hasError = false;
        if (description.isEmpty()) {
            expenseDescription.setError("Required!");
            hasError = true;
        }
        if (amountStr.isEmpty()) {
            expenseAmount.setError("Required!");
            hasError = true;
        }
        if (date.isEmpty()) {
            expenseDate.setError("Required!");
            hasError = true;
        } else if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            expenseDate.setError("Invalid date format (YYYY-MM-DD)!");
            hasError = true;
        }
        if ("Select category".equals(category)) {
            Toast.makeText(this, "Please select a valid category!", Toast.LENGTH_SHORT).show();
            hasError = true;
        }

        if (hasError) return;

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                expenseAmount.setError("Amount must be greater than 0!");
                return;
            }
        } catch (NumberFormatException e) {
            expenseAmount.setError("Invalid amount format!");
            return;
        }

        int userId = getCurrentUserId();
        if (userId <= 0) userId = 1;

        long result = budgetDB.addExpense(userId, description, category, amount, date);

        if (result != -1) {

            Toast.makeText(this, "Expense added successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Error adding expense!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        expenseDescription.setText("");
        expenseAmount.setText("");
        expenseDate.setText("");
        categorySpinner.setSelection(0);
    }

    private int getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getInt("userId", -1);
    }

}
