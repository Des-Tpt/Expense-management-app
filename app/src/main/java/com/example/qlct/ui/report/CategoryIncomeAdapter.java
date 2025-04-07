package com.example.qlct.ui.report;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlct.Database.CategorySum;
import com.example.qlct.Database.income;
import com.example.qlct.R;

import java.util.List;

public class CategoryIncomeAdapter extends RecyclerView.Adapter<CategoryIncomeAdapter.CategoryViewHolder> {

    private Context context;
    private List<CategorySum> summaryList;
    public CategoryIncomeAdapter(Context context, List<CategorySum> summaryList) {
        this.context = context;
        this.summaryList = summaryList;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategorySum currentSummary = summaryList.get(position);
        String vietnameseCategory = getCategoryNameInVietnamese(currentSummary.getCategory());

        holder.txtCategoryName.setText(vietnameseCategory);
        holder.txtCategoryAmount.setText("$" + currentSummary.getTotalAmount());

        int color = getCategoryColor(currentSummary.getCategory());
        holder.categoryColor.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return summaryList != null ? summaryList.size() : 0;
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
                return "Lương";
            case "Side Income":
                return "Thu nhập phụ";
            case "Investment Profit":
                return "Lợi nhuận đầu tư";
            default:
                return "Khác";
        }
    }
}
