package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Adapter.BudgetAdapter;
import com.example.myapplication.database.BudgetDB;
import com.example.myapplication.model.BudgetModel;
import java.util.List;

public class BudgetFragment extends Fragment {
    private RecyclerView recyclerView;
    private BudgetAdapter budgetAdapter;
    private BudgetDB budgetDB;
    private Button btnCreateBudget;
    private TextView txtTotalBudget;

    public BudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        btnCreateBudget = view.findViewById(R.id.btnCreateBudget);
        recyclerView = view.findViewById(R.id.recyclerViewBudget);
        txtTotalBudget = view.findViewById(R.id.txtTotalBudget);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        budgetDB = new BudgetDB(requireContext());

        btnCreateBudget.setOnClickListener(v -> openAddBudgetActivity());

        loadBudgets();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBudgets();
    }

    private void openAddBudgetActivity() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), AddBudgetActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Fragment is not attached to an Activity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBudgets() {
        List<BudgetModel> budgetList = budgetDB.getAllBudget(1);
        if (budgetList == null || budgetList.isEmpty()) {
            txtTotalBudget.setText("0 VND");
            return;
        }

        if (budgetAdapter == null) {
            budgetAdapter = new BudgetAdapter(requireContext(), budgetList);
            recyclerView.setAdapter(budgetAdapter);
        } else {
            budgetAdapter.updateData(budgetList);
        }

        updateTotalBudget();

    }

    private void updateTotalBudget() {

        double totalBudget = budgetDB.getTotalBudget(1);
        txtTotalBudget.setText(totalBudget + "$");

    }

}
