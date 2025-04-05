package com.example.qlct.ui.report;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlct.Database.expense;
import com.example.qlct.R;

import java.util.List;

public class CategoryExpenseAdapter extends RecyclerView.Adapter<CategoryExpenseAdapter.CategoryViewHolder> {

    private Context context;
    private List<expense> expenseList;

    public CategoryExpenseAdapter(Context context, List<expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public CategoryExpenseAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryExpenseAdapter.CategoryViewHolder holder, int position) {
        expense currentExpense = expenseList.get(position);
        String vietnameseCategory = getCategoryNameInVietnamese(currentExpense.getCategory());

        holder.txtCategoryName.setText(vietnameseCategory);
        holder.txtCategoryAmount.setText("$" + currentExpense.getAmount());

        int color = getCategoryColor(currentExpense.getCategory());
        holder.categoryColor.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return expenseList != null ? expenseList.size() : 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategoryName, txtCategoryAmount;
        View categoryColor;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryColor = itemView.findViewById(R.id.category_color);
            txtCategoryName = itemView.findViewById(R.id.txt_category_name);
            txtCategoryAmount = itemView.findViewById(R.id.txt_category_amount);
        }
    }
    private int getCategoryColor(String category) {
        switch (category) {
            case "Essential":
                return Color.GREEN;
            case "Leisure":
                return Color.RED;
            case "Investment":
                return Color.parseColor("#644BAC");
            case "Unexpected":
                return Color.GRAY;
            default:
                return Color.BLUE;
        }
    }
    private String getCategoryNameInVietnamese(String category) {
        switch (category) {
            case "Essential":
                return "Chi tiêu thiết yếu";
            case "Leisure":
                return "Giải trí";
            case "Investment":
                return "Đầu tư";
            case "Unexpected":
                return "Chi tiêu không đoán trước";
            default:
                return "Khác";
        }
    }
}
