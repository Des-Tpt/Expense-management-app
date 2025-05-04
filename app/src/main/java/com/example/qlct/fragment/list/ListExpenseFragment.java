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

import com.example.qlct.Database.ExpenseModel;
import com.example.qlct.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class ListExpenseFragment extends Fragment {
    private RecyclerView recyclerView;
    private ExpenseListAdapter expenseListAdapter;
    private List<ExpenseModel> expenseList;
    private DataBaseHelper dbHelper;


    public ListExpenseFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewExpense); // ID RecyclerView trong XML
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DataBaseHelper(getContext());
        expenseList = dbHelper.getListExpenses();

        expenseListAdapter = new ExpenseListAdapter(expenseList, getContext());
        recyclerView.setAdapter(expenseListAdapter);

        View checkAllButton = view.findViewById(R.id.checkBoxAll);
        checkAllButton.setOnClickListener(v -> {
            expenseListAdapter.toggleCheckAll();
        });

        FloatingActionButton fab = view.findViewById(R.id.fabAddExpense);
        fab.setOnClickListener(v -> {
            showAddDialog();
        });

        View deleteButton = view.findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(v -> {
            if (!expenseListAdapter.hasAnyChecked()) {
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
            expenseListAdapter.deleteChecked();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showAddDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_expense, null);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        EditText edtNote = dialogView.findViewById(R.id.edtNote);
        EditText edtAmount = dialogView.findViewById(R.id.edtAmount);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        Button btnAdd = dialogView.findViewById(R.id.btnThem);
        Button btnCancel = dialogView.findViewById(R.id.btnHuy);
        EditText edtMonth = dialogView.findViewById(R.id.edtMonth);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                new String[]{"Chi tiêu thiết yếu", "Giải trí", "Đầu tư", "Chi tiêu không đoán trước"});
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
                case "Chi tiêu thiết yếu":
                    category = "Essential";
                    break;
                case "Giải trí":
                    category = "Leisure";
                    break;
                case "Đầu tư":
                    category = "Investment";
                    break;
                case "Chi tiêu không đoán trước":
                    category = "Unexpected";
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

            // Tạo model và lưu vào CSDL
            ExpenseModel newExpense = new ExpenseModel(0, category, amount, note, date,false);

            dbHelper.insertExpense(newExpense);

            // Cập nhật lại danh sách
            expenseList.clear();
            expenseList.addAll(dbHelper.getListExpenses());
            expenseListAdapter.notifyDataSetChanged();

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
        return inflater.inflate(R.layout.fragment_expense_list, container, false);
    }
}

