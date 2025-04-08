package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.EditBudgetActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.BudgetModel;
import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {
    private List<BudgetModel> budgetList;
    private Context context;

    public BudgetAdapter(Context context, List<BudgetModel> budgetList) {
        this.context = context;
        this.budgetList = budgetList;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bubget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        BudgetModel budget = budgetList.get(position);
        if (budget == null) return;

        holder.category.setText("Category: " + budget.getCategory());
        holder.amount.setText("Amount: +" + budget.getAmount() + "$");
        holder.monthYear.setText("Date: " + budget.getMonthYear());

        // In dữ liệu ra Log để kiểm tra lỗi
        Log.d("DEBUG", "Item Clicked - ID: " + budget.getId());
        Log.d("DEBUG", "Category: " + budget.getCategory());
        Log.d("DEBUG", "Amount: " + budget.getAmount());
        Log.d("DEBUG", "Date: " + budget.getMonthYear());

        holder.itemView.setOnClickListener(v -> {
            if (context != null) {
                Intent intent = new Intent(context, EditBudgetActivity.class);
                intent.putExtra("budget_id", budget.getId());
                intent.putExtra("amount", budget.getAmount());
                intent.putExtra("month_year", budget.getMonthYear());
                intent.putExtra("category", budget.getCategory());

                // Kiểm tra intent có null không
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Error: EditBudgetActivity not found!", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", "EditBudgetActivity not found!");
                }
            } else {
                Toast.makeText(v.getContext(), "Error: Context is null", Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "Context is null!");
            }
        });
    }

    @Override
    public int getItemCount() {
        return budgetList != null ? budgetList.size() : 0;
    }

    public void updateData(List<BudgetModel> newBudgets) {
        if (newBudgets == null) return;
        this.budgetList.clear();
        this.budgetList.addAll(newBudgets);
        notifyDataSetChanged();
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView category, amount, monthYear;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.budget_category);
            amount = itemView.findViewById(R.id.budget_amount);
            monthYear = itemView.findViewById(R.id.budget_month_year);
        }
    }

}
