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

import com.example.qlct.Database.IncomeModel;  // Sử dụng IncomeModel
import com.example.qlct.R;

import java.util.List;

public class ListIncomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private IncomeListAdapter incomeListAdapter;  // Thay adapter thành IncomeListAdapter
    private List<IncomeModel> incomeList;  // Thay ExpenseModel thành IncomeModel
    private DataBaseHelper dbHelper;

    public ListIncomeFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewIncome); // ID RecyclerView trong XML
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DataBaseHelper(getContext());
        incomeList = dbHelper.getListIncomes();  // Sử dụng hàm getListIncomes()

        incomeListAdapter = new IncomeListAdapter(incomeList, getContext());  // Sử dụng IncomeListAdapter
        recyclerView.setAdapter(incomeListAdapter);

        View checkAllButton = view.findViewById(R.id.checkBoxAll);  // Nút Check All
        checkAllButton.setOnClickListener(v -> {
            incomeListAdapter.toggleCheckAll();
        });

        // Bắt sự kiện nút "Delete" từ toolbar
        View deleteButton = view.findViewById(R.id.btnDelete);  // Nút Delete
        deleteButton.setOnClickListener(v -> {
            incomeListAdapter.deleteChecked();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income_list, container, false);  // Layout cho income list
    }
}
