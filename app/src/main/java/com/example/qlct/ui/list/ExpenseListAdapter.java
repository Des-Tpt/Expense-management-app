package com.example.qlct.ui.list;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlct.Database.DataBaseHelper;
import com.example.qlct.Database.ExpenseModel;
import com.example.qlct.Database.IncomeModel;
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
            showEditPopup(v.getContext(), expense);
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
    public boolean hasAnyChecked() {
        for (ExpenseModel expense : expenseList) {
            if (expense.isChecked()) return true;
        }
        return false;
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
    private void showEditPopup(Context context, ExpenseModel expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_expense, null);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText edtNote = dialogView.findViewById(R.id.edtNote);
        EditText edtMonth = dialogView.findViewById(R.id.edtMonth);
        EditText edtAmount = dialogView.findViewById(R.id.edtAmount);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        Button btnXacNhan = dialogView.findViewById(R.id.btnThem);
        Button btnHuy = dialogView.findViewById(R.id.btnHuy);

        edtNote.setText(expense.getNote());
        edtMonth.setText(expense.getDate());
        edtAmount.setText(String.valueOf(expense.getAmount()));

        String[] categories = context.getResources().getStringArray(R.array.categories);

        String[] displayCategories = {"Chi tiêu thiết yếu", "Giải trí", "Đầu tư", "Chi tiêu không đoán trước"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, displayCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);


        int selectedIndex = 0;
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(expense.getCategory())) {
                selectedIndex = i;
                break;
            }
        }

        spinnerCategory.setSelection(selectedIndex);

        btnHuy.setOnClickListener(view -> dialog.dismiss());


        btnXacNhan.setOnClickListener(view -> {
            String note = edtNote.getText().toString().trim();
            String date = edtMonth.getText().toString().trim();
            String categoryVi = spinnerCategory.getSelectedItem().toString();
            String amountStr = edtAmount.getText().toString().trim();

            if (note.isEmpty() || date.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(context, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);

            String categoryEn;
            switch (categoryVi) {
                case "Chi tiêu thiết yếu":
                    categoryEn = "Essential";
                    break;
                case "Giải trí":
                    categoryEn = "Leisure";
                    break;
                case "Đầu tư":
                    categoryEn = "Investment";
                    break;
                case "Chi tiêu không đoán trước":
                    categoryEn = "Unexpected";
                    break;
                default:
                    categoryEn = "Null";
            }

            expense.setNote(note);
            expense.setDate(date);
            expense.setAmount(amount);
            expense.setCategory(categoryEn);

            dbHelper.updateExpense(expense);

            expenseList.clear();
            expenseList.addAll(dbHelper.getListExpenses());
            notifyDataSetChanged();

            dialog.dismiss();
        });

        dialog.show();
    }
}