package com.example.qlct.fragment.list;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
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

        holder.tvPrice.setText("$" + income.getAmount());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(income.isChecked());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> income.setChecked(isChecked));

        holder.btnEdit.setOnClickListener(v -> {
            showEditPopup(v.getContext(), income);
        });
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
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

    public boolean hasAnyChecked() {
        for (IncomeModel income : incomeList) {
            if (income.isChecked()) return true;
        }
        return false;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void toggleCheckAll() {
        isAllChecked = !isAllChecked;
        for (IncomeModel item : incomeList) {
            item.setChecked(isAllChecked);
        }
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteChecked() {
        List<IncomeModel> toDelete = new ArrayList<>();

        for (IncomeModel income : incomeList) {
            if (income.isChecked()) {
                toDelete.add(income);
            }
        }

        for (IncomeModel income : toDelete) {
            dbHelper.deleteIncomeById(income.getId());
        }

        incomeList.clear();
        incomeList.addAll(dbHelper.getListIncomes());

        notifyDataSetChanged();
    }

    private void showEditPopup(Context context, IncomeModel income) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_income, null);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText edtNote = dialogView.findViewById(R.id.edtNote);
        EditText edtMonth = dialogView.findViewById(R.id.edtMonth);
        EditText edtAmount = dialogView.findViewById(R.id.edtAmount);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        Button btnXacNhan = dialogView.findViewById(R.id.btnThem);
        Button btnHuy = dialogView.findViewById(R.id.btnHuy);

        edtNote.setText(income.getNote());
        edtMonth.setText(income.getDate());
        edtAmount.setText(String.valueOf(income.getAmount()));

        String[] categories = context.getResources().getStringArray(R.array.categories);

        String[] displayCategories = {"Lương", "Thu nhập phụ", "Lợi nhuận đầu tư"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, displayCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);


        int selectedIndex = 0;
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(income.getCategory())) {
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
                case "Lương":
                    categoryEn = "Salary";
                    break;
                case "Thu nhập phụ":
                    categoryEn = "Side Income";
                    break;
                case "Lợi nhuận đầu tư":
                    categoryEn = "Investment Profit";
                    break;
                default:
                    categoryEn = "Null";
            }

            income.setNote(note);
            income.setDate(date);
            income.setAmount(amount);
            income.setCategory(categoryEn);

            dbHelper.updateIncome(income);

            incomeList.clear();
            incomeList.addAll(dbHelper.getListIncomes());
            notifyDataSetChanged();

            dialog.dismiss();
        });

        dialog.show();
    }

}
