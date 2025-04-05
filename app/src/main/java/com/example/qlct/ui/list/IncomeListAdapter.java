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
import com.example.qlct.Database.IncomeModel;
import com.example.qlct.R;

import java.util.ArrayList;
import java.util.List;

public class IncomeListAdapter extends RecyclerView.Adapter<IncomeListAdapter.ViewHolder> {
    private List<IncomeModel> incomeList;
    private DataBaseHelper dbHelper;
    private boolean isAllChecked = false;

    public IncomeListAdapter(List<IncomeModel> incomeList, Context context) {
        this.incomeList = incomeList;
        this.dbHelper = new DataBaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);  // Thay đổi thành item_income
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IncomeModel income = incomeList.get(position);
        holder.tvTitle.setText(income.getNote());

        switch (income.getCategory()){
            case "Salary":
                holder.tvCategory.setText("Lương");
                break;
            case "Side Income":
                holder.tvCategory.setText("Thu nhập phụ");
                break;
            case "Investment Profit":
                holder.tvCategory.setText("Lợi nhuận đầu tư");
                break;
            default:
                holder.tvCategory.setText("Trống");
                break;
        }

        holder.tvPrice.setText("$" + income.getAmount());  // Thay đổi thành income.getAmount()

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(income.isChecked());  // Thay đổi thành income.isChecked()
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> income.setChecked(isChecked));  // Thay đổi thành income.setChecked(isChecked)

        holder.btnEdit.setOnClickListener(v -> {
            // Xử lý sự kiện chỉnh sửa nếu cần
        });
    }

    @Override
    public int getItemCount() {
        return incomeList.size();  // Thay đổi thành incomeList.size()
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox, checkAll;
        TextView tvTitle, tvCategory, tvPrice;
        ImageView btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);  // Giữ nguyên, nếu không thay đổi
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            checkAll = itemView.findViewById(R.id.checkBoxAll);  // Giữ nguyên, nếu không thay đổi
            btnDelete = itemView.findViewById(R.id.btnDelete);  // Giữ nguyên, nếu không thay đổi
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void toggleCheckAll() {
        isAllChecked = !isAllChecked;
        for (IncomeModel item : incomeList) {  // Thay đổi thành IncomeModel
            item.setChecked(isAllChecked);
        }
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteChecked() {
        List<IncomeModel> toDelete = new ArrayList<>();  // Thay đổi thành IncomeModel

        for (IncomeModel income : incomeList) {  // Thay đổi thành IncomeModel
            if (income.isChecked()) {  // Thay đổi thành income.isChecked()
                toDelete.add(income);
            }
        }

        for (IncomeModel income : toDelete) {  // Thay đổi thành IncomeModel
            dbHelper.deleteIncomeById(income.getId());  // Phải có phương thức deleteIncomeById trong DataBaseHelper
        }

        incomeList.clear();
        incomeList.addAll(dbHelper.getListIncomes());  // Phải có phương thức getListIncomes trong DataBaseHelper

        notifyDataSetChanged();
    }
}
