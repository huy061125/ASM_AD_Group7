package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.BudgetDB;

import java.util.Calendar;

public class EditBudgetActivity extends AppCompatActivity {
    private EditText edtAmount, edtDate;
    private Spinner spinnerCategory;
    private Button btnEdit, btnCancel;
    private ImageView Edit_delete_budget;
    private BudgetDB budgetDB;
    private int budgetId;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bubget);

        budgetDB = new BudgetDB(this);

        spinnerCategory = findViewById(R.id.spinner_edit_Category);
        edtAmount = findViewById(R.id.edt_Budget_edit_Amount);
        edtDate = findViewById(R.id.budget_edit_date);
        btnEdit = findViewById(R.id.add_edit_Budget_button);
        btnCancel = findViewById(R.id.cancel_edit_expense_button);
        Edit_delete_budget = findViewById(R.id.Edit_delete_budget);  // Nút xóa


        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        budgetId = intent.getIntExtra("budget_id", -1);
        String category = intent.getStringExtra("category");
        double amount = intent.getDoubleExtra("amount", 0);
        String date = intent.getStringExtra("month_year");

        setupCategorySpinner();

        if (amount > 0) {
            edtAmount.setText(String.valueOf(amount));
        }
        if (date != null) {
            edtDate.setText(date);
        }
        setSpinnerSelection(spinnerCategory, category);

        // Xử lý chọn ngày
        edtDate.setOnClickListener(v -> showDatePicker());

        // Xử lý nút Lưu
        btnEdit.setOnClickListener(v -> updateBudget());

        // Xử lý nút Hủy
        btnCancel.setOnClickListener(v -> finish());

        // Xử lý nút Xóa
        Edit_delete_budget.setOnClickListener(v -> confirmDeleteBudget());
    }

    private void setupCategorySpinner() {
        String[] categories = {"Select category", "Food", "Entertainment", "Education", "Transport", "Health"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Dùng layout mặc định cho dropdown
        spinnerCategory.setAdapter(adapter);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                    edtDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void updateBudget() {
        String category = spinnerCategory.getSelectedItem().toString();
        String amountText = edtAmount.getText().toString().trim();
        String date = edtDate.getText().toString().trim();

        if (amountText.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount format!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = budgetDB.updateBudget(budgetId, category, amount, date);

        if (isUpdated) {
            Toast.makeText(this, "Budget Updated!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setSpinnerSelection(Spinner spinner, String category) {
        if (category == null) return;
        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(category)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    // Hàm xác nhận xóa budget
    private void confirmDeleteBudget() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Budget")
                .setMessage("Are you sure you want to delete this budget?")
                .setPositiveButton("Yes", (dialog, which) -> deleteBudget())
                .setNegativeButton("No", null)
                .show();
    }

    // Hàm xóa budget khỏi database
    private void deleteBudget() {
        boolean isDeleted = budgetDB.deleteBudget(budgetId);

        if (isDeleted) {
            Toast.makeText(this, "Budget Deleted!", Toast.LENGTH_SHORT).show();
            finish(); // Quay về màn hình trước
        } else {
            Toast.makeText(this, "Delete Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
