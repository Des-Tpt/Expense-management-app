package com.example.qlct.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qlct.Database.DataBaseHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlct.Database.ExpenseModel;
import com.example.qlct.R;

import java.util.List;

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

        recyclerView = view.findViewById(R.id.recyclerViewIncome); // ID RecyclerView trong XML
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DataBaseHelper(getContext());
        expenseList = dbHelper.getListExpenses();

        expenseListAdapter = new ExpenseListAdapter(expenseList, getContext());
        recyclerView.setAdapter(expenseListAdapter);

        View checkAllButton = view.findViewById(R.id.checkBoxAll);
        checkAllButton.setOnClickListener(v -> {
            expenseListAdapter.toggleCheckAll();
        });

        // Bắt sự kiện nút "Delete" từ toolbar
        View deleteButton = view.findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(v -> {
            expenseListAdapter.deleteChecked();
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense_list, container, false);
    }
}

