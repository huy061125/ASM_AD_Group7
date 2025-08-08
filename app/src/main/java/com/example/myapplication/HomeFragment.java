package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.database.BudgetDB;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private PieChart pieChart;
    private BudgetDB budgetDB;
    private TextView tvSummaryValues;

    // A hardcoded user ID for demonstration.
    // In a real application, you should get this from a user session or login manager.
    private int currentUserId = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize database helper
        budgetDB = new BudgetDB(getContext());

        // Find views by their ID
        pieChart = view.findViewById(R.id.pieChart);
        tvSummaryValues = view.findViewById(R.id.tvSummaryValues);

        // Load the data and set up the chart
        setupPieChart();
        loadChartData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload data every time the fragment becomes visible to reflect changes
        loadChartData();
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true); // Make it a donut chart
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Summary");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        // Configure the legend
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadChartData() {
        // Fetch data from the database
        double totalBudget = budgetDB.getTotalSetBudget(currentUserId);
        double totalExpenses = budgetDB.getTotalExpenses(currentUserId);
        double remainingBudget = totalBudget - totalExpenses;

        // Format currency for display
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String formattedBudget = currencyFormat.format(totalBudget);
        String formattedExpenses = currencyFormat.format(totalExpenses);

        tvSummaryValues.setText(String.format("Budget: %s | Expenses: %s", formattedBudget, formattedExpenses));


        ArrayList<PieEntry> entries = new ArrayList<>();

        if (totalBudget <= 0) {
            // If no budget is set, display a message
            pieChart.setCenterText("No Budget Set");
            pieChart.setData(null); // Clear previous data
        } else {
            pieChart.setCenterText("Summary");
            // Ensure remaining budget is not negative for the chart
            if (remainingBudget < 0) {
                entries.add(new PieEntry((float) totalExpenses, "Expenses"));
                pieChart.setCenterText("Over Budget!");
            } else {
                entries.add(new PieEntry((float) totalExpenses, "Expenses"));
                entries.add(new PieEntry((float) remainingBudget, "Remaining"));
            }
        }

        if (entries.isEmpty()) {
            pieChart.invalidate();
            return;
        }

        // Define colors for the chart slices
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#f44336")); // Red for Expenses
        colors.add(Color.parseColor("#4caf50")); // Green for Remaining

        PieDataSet dataSet = new PieDataSet(entries, "Financial Summary");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate(); // Refresh the chart
    }
}