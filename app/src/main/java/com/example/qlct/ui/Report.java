package com.example.qlct.ui;

import static com.github.mikephil.charting.utils.ColorTemplate.*;

import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ViewFlipper;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.animation.Easing;
import com.example.qlct.Database.DataBaseHelper;
import com.example.qlct.R;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report extends Fragment {

    private PieChart pieChartIncome;
    private PieChart pieChartExpense;
    private DataBaseHelper dbHelper;
    private ViewFlipper viewFlipper;
    private RadioButton rbIncome;
    private RadioButton rbExpense;
    private ImageButton btnPrev;
    private ImageButton btnNext;

    List<PieEntry> pieEntryList;


    public Report() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        pieChartExpense = view.findViewById(R.id.pieChartExpense);

        dbHelper = new DataBaseHelper(requireContext());

        setupPieChartExpenseAppearance();
        loadExpenseDataToPieChart();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DataBaseHelper(getContext());

        rbIncome = view.findViewById(R.id.rbIncome);
        rbExpense = view.findViewById(R.id.rbExpense);
        viewFlipper = view.findViewById(R.id.viewFlipper);

        pieChartIncome = view.findViewById(R.id.pieChartIncome);
        btnPrev = view.findViewById(R.id.btnPrev);
        btnNext = view.findViewById(R.id.btnNext);


        rbIncome.setClickable(false);
        rbIncome.setFocusable(false);
        rbExpense.setClickable(false);
        rbExpense.setFocusable(false);

        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);


        btnPrev.setOnClickListener(v -> {
            viewFlipper.showPrevious();
            updateRadioButton();
        });

        btnNext.setOnClickListener(v -> {
            viewFlipper.showNext();
            updateRadioButton();
        });
        updateRadioButton();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void updateRadioButton() {
        if (viewFlipper.getDisplayedChild() == 0) {
            rbIncome.setChecked(true);
            rbExpense.setChecked(false);
        } else {
            rbIncome.setChecked(false);
            rbExpense.setChecked(true);
        }
    }

    private void setupPieChartExpenseAppearance() {
        pieChartExpense.setDrawHoleEnabled(true);
        pieChartExpense.setHoleRadius(60f);
        pieChartExpense.setTransparentCircleRadius(45f);
        pieChartExpense.setUsePercentValues(true);
        pieChartExpense.setDrawEntryLabels(false);
        pieChartExpense.setEntryLabelColor(Color.BLACK);
        pieChartExpense.getLegend().setEnabled(false);
        pieChartExpense.getDescription().setEnabled(false);
        pieChartExpense.animateY(1000, Easing.EaseInOutCubic);
    }

    private void setupPieChartIncomeAppearance() {
        pieChartExpense.setDrawHoleEnabled(true);
        pieChartExpense.setHoleRadius(60f);
        pieChartExpense.setTransparentCircleRadius(45f);
        pieChartExpense.setUsePercentValues(true);
        pieChartExpense.setDrawEntryLabels(false);
        pieChartExpense.setEntryLabelColor(Color.BLACK);
        pieChartExpense.getDescription().setEnabled(false);
        pieChartExpense.getLegend().setEnabled(false);
        pieChartExpense.animateY(1000, Easing.EaseInOutCubic);
    }


    private void loadExpenseDataToPieChart() {
        try {
            List<PieEntry> entries = dbHelper.getExpenseDataForPieChart();
            float totalExpense = dbHelper.getTotalExpenseAmount();

            if (entries == null || entries.isEmpty()) {
                Log.e("PieChart", "Không có dữ liệu để hiển thị!");
                pieChartExpense.clear(); // Xoá biểu đồ nếu không có dữ liệu
                pieChartExpense.invalidate();
                return;
            }

            PieDataSet dataSet = new PieDataSet(entries, "Categories");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setValueTextSize(14f);
            dataSet.setValueTextColor(Color.WHITE);

            PieData pieData = new PieData(dataSet);
            pieChartExpense.setData(pieData);

            pieChartExpense.setCenterText("$" + totalExpense);

            pieChartExpense.invalidate();
        } catch (Exception e) {
            Log.e("PieChart", "Lỗi khi load dữ liệu biểu đồ: " + e.getMessage());
        }
    }

    private void loadIncomeDataToPieChart() {
        try {
            List<PieEntry> entries = dbHelper.getIncomeDataForPieChart();
            float totalIncome = dbHelper.getTotalIncomeAmount();

            if (entries == null || entries.isEmpty()) {
                Log.e("PieChart", "Không có dữ liệu để hiển thị!");
                pieChartIncome.clear();
                pieChartIncome.invalidate();
                return;
            }

            PieDataSet dataSet = new PieDataSet(entries, "Categories");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setValueTextSize(14f);
            dataSet.setValueTextColor(Color.WHITE);

            PieData pieData = new PieData(dataSet);
            pieChartIncome.setData(pieData);
            pieChartIncome.invalidate();

            pieChartExpense.setCenterText("$" + totalIncome);

        } catch (Exception e) {
            Log.e("PieChart", "Lỗi khi load dữ liệu biểu đồ: " + e.getMessage());
        }
    }
}
