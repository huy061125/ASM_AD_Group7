package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.EditExpensesActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.ExpensesModel;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<ExpensesModel> expenses;
    private WeakReference<Context> contextRef;

    public ExpenseAdapter(Context context, List<ExpensesModel> expenses) {
        this.contextRef = new WeakReference<>(context);
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_expenses, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpensesModel expense = expenses.get(position);
        Context context = contextRef.get();
        if (context == null) return;

        // Định dạng số tiền theo USD, ký hiệu sau
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
        String formattedAmount = currencyFormat.format(expense.getAmount());
        formattedAmount = formattedAmount.replace("$", "").trim() + "$";

        holder.tvExpenseDescription.setText("Description: " + expense.getDescription());
        holder.tvExpenseAmount.setText("Amount: -" + formattedAmount);
        holder.tvExpenseDate.setText("Date: " + expense.getDate());
        holder.tvExpenseCategory.setText("Category: " + expense.getCategory());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditExpensesActivity.class);
            intent.putExtra("expense_id", expense.getId());
            intent.putExtra("description", expense.getDescription());
            intent.putExtra("amount", expense.getAmount());
            intent.putExtra("date", expense.getDate());
            intent.putExtra("category", expense.getCategory());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvExpenseDescription, tvExpenseAmount, tvExpenseDate, tvExpenseCategory;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExpenseDescription = itemView.findViewById(R.id.tvExpenseDescription);
            tvExpenseAmount = itemView.findViewById(R.id.tvExpenseAmount);
            tvExpenseDate = itemView.findViewById(R.id.tvExpenseDate);
            tvExpenseCategory = itemView.findViewById(R.id.tvExpenseCategory);
        }
    }

    public void updateData(List<ExpensesModel> newExpenses) {
        this.expenses = newExpenses;
        notifyDataSetChanged();
    }


}
