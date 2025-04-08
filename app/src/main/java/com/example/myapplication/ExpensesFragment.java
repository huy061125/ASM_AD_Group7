package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapplication.Adapter.ExpenseAdapter;
import com.example.myapplication.database.BudgetDB;
import com.example.myapplication.model.ExpensesModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ExpensesFragment extends Fragment {

    private RecyclerView recyclerViewExpenses;
    private ExpenseAdapter expenseAdapter;
    private BudgetDB budgetDB;

    public ExpensesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        // Tìm FloatingActionButton để mở màn hình thêm chi tiêu
        FloatingActionButton btnAddExpense = view.findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ExpenseActivity.class);
            startActivity(intent);
        });

        // Khởi tạo RecyclerView
        recyclerViewExpenses = view.findViewById(R.id.recyclerViewExpenses);
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Khởi tạo database
        budgetDB = new BudgetDB(requireContext());

        // Tải dữ liệu
        loadExpenses();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExpenses(); // Cập nhật danh sách khi quay lại Fragment
    }

    private void loadExpenses() {
        List<ExpensesModel> expenses = budgetDB.getAllExpenses(1); // Giả định userId = 1
        if (expenseAdapter == null) {
            expenseAdapter = new ExpenseAdapter(requireContext(), expenses);
            recyclerViewExpenses.setAdapter(expenseAdapter);
        } else {
            expenseAdapter.updateData(expenses);
        }
    }
}