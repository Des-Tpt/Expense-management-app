package com.example.qlct.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.animation.Easing;
import com.example.qlct.Database.DataBaseHelper;
import com.example.qlct.R;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;

public class Report extends Fragment {

    private PieChart pieChartIncome;
    private PieChart pieChartExpense;
    private DataBaseHelper dbHelper;
    private ViewFlipper viewFlipper;
    private RadioButton rbIncome;
    private RadioButton rbExpense;
    private ImageButton btnPrev;
    private ImageButton btnNext;


    public Report() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        pieChartExpense = view.findViewById(R.id.pieChartExpense);
        pieChartIncome = view.findViewById(R.id.pieChartIncome);

        dbHelper = new DataBaseHelper(requireContext());

        setupPieChartIncomeAppearance();
        loadIncomeDataToPieChart();

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
        pieChartExpense.setCenterTextSize(20f);
        pieChartExpense.setCenterTextColor(Color.parseColor("#644BAC"));
        pieChartExpense.animateY(1000, Easing.EaseInOutCubic);
    }

    private void setupPieChartIncomeAppearance() {
        pieChartIncome.setDrawHoleEnabled(true);
        pieChartIncome.setHoleRadius(60f);
        pieChartIncome.setTransparentCircleRadius(45f);
        pieChartIncome.setUsePercentValues(true);
        pieChartIncome.setDrawEntryLabels(false);
        pieChartIncome.setEntryLabelColor(Color.BLACK);
        pieChartIncome.getLegend().setEnabled(false);
        pieChartIncome.getDescription().setEnabled(false);
        pieChartIncome.setCenterTextSize(20f);
        pieChartIncome.setCenterTextColor(Color.parseColor("#644BAC"));
        pieChartIncome.animateY(1000, Easing.EaseInOutCubic);
    }

    private void loadExpenseDataToPieChart() {
        try {
            Pair<List<PieEntry>, Float> result = dbHelper.getExpenseDataAndTotal();
            List<PieEntry> entries = result.first;
            float totalExpense = result.second;

            if (entries.isEmpty()) {
                Log.e("PieChart", "Không có dữ liệu để hiển thị!");
                pieChartExpense.clear();
                pieChartExpense.invalidate();
                return;
            }

            Log.d("PieChart", "Entries size: " + entries.size());

            PieDataSet dataSet = new PieDataSet(entries, "Categories");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setValueTextSize(14f);
            dataSet.setValueTextColor(Color.WHITE);

            PieData pieData = new PieData(dataSet);
            pieChartExpense.setData(pieData);

            SpannableString centerText = new SpannableString("$" + totalExpense);
            centerText.setSpan(new StyleSpan(Typeface.BOLD), 0, centerText.length(), 0); // In đậm
            centerText.setSpan(new ForegroundColorSpan(Color.parseColor("#644BAC")), 0, centerText.length(), 0); // Đổi màu
            centerText.setSpan(new RelativeSizeSpan(1.5f), 0, centerText.length(), 0); // Tăng kích thước 1.5 lần

            pieChartExpense.setCenterText(centerText);


            pieChartExpense.notifyDataSetChanged();
            pieChartExpense.invalidate();

        } catch (Exception e) {
            Log.e("PieChart", "Lỗi khi load dữ liệu biểu đồ: " + e.getMessage());
        }
    }

    private void loadIncomeDataToPieChart() {
        try {
            Pair<List<PieEntry>, Float> result = dbHelper.getIncomeDataAndTotal();
            List<PieEntry> entries = result.first;
            float totalIncome = result.second;

            if (entries.isEmpty()) {
                Log.e("PieChart", "Không có dữ liệu để hiển thị!");
                pieChartIncome.clear();
                pieChartIncome.invalidate();
                return;
            }

            Log.d("PieChart", "Entries size: " + entries.size());

            PieDataSet dataSet = new PieDataSet(entries, "Categories");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setValueTextSize(14f);
            dataSet.setValueTextColor(Color.WHITE);

            PieData pieData = new PieData(dataSet);
            pieChartIncome.setData(pieData);

            SpannableString centerText = new SpannableString("$" + totalIncome);
            centerText.setSpan(new StyleSpan(Typeface.BOLD), 0, centerText.length(), 0); // In đậm
            centerText.setSpan(new ForegroundColorSpan(Color.parseColor("#644BAC")), 0, centerText.length(), 0); // Đổi màu
            centerText.setSpan(new RelativeSizeSpan(1.5f), 0, centerText.length(), 0); // Tăng kích thước 1.5 lần

            pieChartIncome.setCenterText(centerText);


            pieChartIncome.notifyDataSetChanged();
            pieChartIncome.invalidate();

        } catch (Exception e) {
            Log.e("PieChart", "Lỗi khi load dữ liệu biểu đồ: " + e.getMessage());
        }
    }
}
