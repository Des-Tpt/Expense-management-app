package com.example.qlct.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlct.Database.income;
import com.example.qlct.R;

import java.util.List;

public class CategoryIncomeAdapter extends RecyclerView.Adapter<CategoryIncomeAdapter.CategoryViewHolder> {

    private Context context;
    private List<income> incomeList;

    public CategoryIncomeAdapter(Context context, List<income> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        income currentIncome = incomeList.get(position);
        String vietnameseCategory = getCategoryNameInVietnamese(currentIncome.getCategory());

        holder.txtCategoryName.setText(vietnameseCategory);
        holder.txtCategoryAmount.setText("$" + currentIncome.getAmount());

        int color = getCategoryColor(currentIncome.getCategory());
        holder.categoryColor.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return incomeList != null ? incomeList.size() : 0;
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
            case "Salary":
                return Color.rgb(76, 175, 80); // Green
            case "Side Income":
                return Color.parseColor("#644BAC"); // Blue
            case "Investment Profit":
                return Color.rgb(255, 193, 7); // Amber
            default:
                return Color.DKGRAY;
        }
    }

    private String getCategoryNameInVietnamese(String category) {
        switch (category) {
            case "Salary":
                return "Lương chính";
            case "Side Income":
                return "Thu nhập phụ";
            case "Investment Profit":
                return "Lợi nhuận đầu tư";
            default:
                return "Khác";
        }
    }
}
