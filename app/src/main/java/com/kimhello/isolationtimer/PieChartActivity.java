package com.kimhello.isolationtimer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PieChartActivity extends AppCompatActivity {
    PieChart pieChart;
    ArrayList<Integer> mSeriesTime; // 각 계열의 값
    ArrayList<String> mSeriesTitle; // 각 계열의 타이틀
    private static ArrayList<Integer> COLORS; //각 계열의 색상

    TextView textView;

    DBHelpter dbHelpter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        dbHelpter = DBHelpter.getInstance(getApplicationContext());
        pieChart = findViewById(R.id.pieChart);
        textView = (TextView)findViewById(R.id.tv_chart);

        if (dbHelpter.getAll()!=null) {mSeriesTitle = dbHelpter.getAll();}  //타이틀
        if (dbHelpter.getAllTimes()!=null) { mSeriesTime = dbHelpter.getAllTimes();} // 시간

        if(mSeriesTitle.size()==0) textView.setVisibility(View.VISIBLE);

        ArrayList<PieEntry> datavalue = new ArrayList<>();

        for(int i=0; i<mSeriesTitle.size(); i++) {
            datavalue.add(new PieEntry(mSeriesTime.get(i),mSeriesTitle.get(i) ));
        }

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        Description description = new Description();
        description.setText("활동 통계");
        description.setTextSize(15);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EaseInOutCubic);


        PieDataSet dataSet = new PieDataSet(datavalue, "Activities");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
    }

}
