package com.example.qlct.ui.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlct.Database.DataBaseHelper;
import com.example.qlct.Database.ExpenseModel;
import com.example.qlct.R;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ViewHolder> {
    private List<ExpenseModel> expenseList;
    private DataBaseHelper dbHelper;
    private boolean isAllChecked = false;

    public ExpenseListAdapter(List<ExpenseModel> expenseList, Context context) {
        this.expenseList = expenseList;
        this.dbHelper = new DataBaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpenseModel expense = expenseList.get(position);
        holder.tvTitle.setText(expense.getNote());
        switch (expense.getCategory()){
            case "Essential":
                holder.tvCategory.setText("Chi tiêu thiết yếu");
                break;
            case "Leisure":
                holder.tvCategory.setText("Giải trí");
                break;
            case "Investment":
                holder.tvCategory.setText("Đầu tư");
                break;
            case "Unexpected":
                holder.tvCategory.setText("Chi tiêu không đoán trước");
                break;
            default:
                holder.tvCategory.setText("Trống");
                break;
        }
        holder.tvPrice.setText("$" + expense.getAmount());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(expense.isChecked());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> expense.setChecked(isChecked));

        holder.btnEdit.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox, checkAll;
        TextView tvTitle, tvCategory, tvPrice;
        ImageView btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            checkAll = itemView.findViewById(R.id.checkBoxAll);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void toggleCheckAll() {
        isAllChecked = !isAllChecked;
        for (ExpenseModel item : expenseList) {
            item.setChecked(isAllChecked);
        }
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteChecked() {
        List<ExpenseModel> toDelete = new ArrayList<>();

        for (ExpenseModel expense : expenseList) {
            if (expense.isChecked()) {
                toDelete.add(expense);
            }
        }

        for (ExpenseModel expense : toDelete) {
            dbHelper.deleteExpenseById(expense.getId());
        }

        expenseList.clear();
        expenseList.addAll(dbHelper.getListExpenses());

        notifyDataSetChanged();
    }
}