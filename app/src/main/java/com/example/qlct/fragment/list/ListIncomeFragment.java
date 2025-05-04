package com.example.qlct.fragment.list;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlct.Database.DataBaseHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlct.Database.IncomeModel;  // Sử dụng IncomeModel
import com.example.qlct.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;

import java.util.List;

public class ListIncomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private IncomeListAdapter incomeListAdapter;
    private List<IncomeModel> incomeList;
    private DataBaseHelper dbHelper;

    public ListIncomeFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewIncome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DataBaseHelper(getContext());
        incomeList = dbHelper.getListIncomes();

        incomeListAdapter = new IncomeListAdapter(incomeList, getContext());
        recyclerView.setAdapter(incomeListAdapter);

        View checkAllButton = view.findViewById(R.id.checkBoxAll);
        checkAllButton.setOnClickListener(v -> {
            incomeListAdapter.toggleCheckAll();
        });

        FloatingActionButton fab = view.findViewById(R.id.fabAddIncome);
        fab.setOnClickListener(v -> {
            showAddDialog();
        });

        View deleteButton = view.findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(v -> {
            if (!incomeListAdapter.hasAnyChecked()) {
                showWarningDialog();
            } else {
                showConfirmDialog();
            }
        });
    }

    private void showConfirmDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm_delete, null);
        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);
        TextView btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            incomeListAdapter.deleteChecked();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showAddDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_income, null);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edtNote = dialogView.findViewById(R.id.edtNote);
        EditText edtAmount = dialogView.findViewById(R.id.edtAmount);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        Button btnAdd = dialogView.findViewById(R.id.btnThem);
        Button btnCancel = dialogView.findViewById(R.id.btnHuy);
        EditText edtMonth = dialogView.findViewById(R.id.edtMonth);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                new String[]{"Lương", "Thu nhập phụ", "Lợi nhuận đầu tư"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnAdd.setOnClickListener(v -> {
            String note = edtNote.getText().toString().trim();
            String amountStr = edtAmount.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();

            switch (category) {
                case "Lương":
                    category = "Salary";
                    break;
                case "Thu nhập phụ":
                    category = "Side Income";
                    break;
                case "Lợi nhuận đầu tư":
                    category = "Investment Profit";
                    break;
                default:
                    category = "Unknown";
                    break;
            }

            String date = edtMonth.getText().toString().trim();

            if (note.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);

            IncomeModel newIncome = new IncomeModel(0, category, amount, note, date,false);

            dbHelper.insertIncome(newIncome);

            incomeList.clear();
            incomeList.addAll(dbHelper.getListIncomes());
            incomeListAdapter.notifyDataSetChanged();

            dialog.dismiss();
        });
        dialog.show();
    }

    private void showWarningDialog() {
        View warningView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_warning_select_item, null);
        final android.app.AlertDialog warningDialog = new android.app.AlertDialog.Builder(getContext())
                .setView(warningView)
                .setCancelable(true)
                .create();

        warningDialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(warningDialog::dismiss, 2000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income_list, container, false);  // Layout cho income list
    }
}
