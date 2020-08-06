package com.example.findreal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide Navigation Bar - doesn't work well
        // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Piechart for requests
        PieChart requestPieChart = (PieChart)findViewById(R.id.requests_piechart);

        requestPieChart.setUsePercentValues(true);
        requestPieChart.getDescription().setEnabled(false);
        requestPieChart.getLegend().setEnabled(false);
        requestPieChart.setExtraOffsets(0,0,0,0);

        requestPieChart.setDragDecelerationFrictionCoef(0.5f);

        requestPieChart.setDrawHoleEnabled(false);
        requestPieChart.setHoleColor(Color.WHITE);
        requestPieChart.setTransparentCircleRadius(20f);
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(2f,""));
        yValues.add(new PieEntry(1f,""));


        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(0f);

        dataSet.setColors(new int[] {getResources().getColor(R.color.completed), getResources().getColor(R.color.ongoing)});

        PieData data = new PieData((dataSet));
        data.setValueTextSize(0f);
//        data.setValueTextColor(Color.YELLOW);

        requestPieChart.setData(data);

        // Setting and showing News list
        NewsListViewAdapter adapter;
        RecyclerView recyclerView = findViewById(R.id.news_listview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NewsListViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new NewsListViewDecoration(20)); // set border between news

        // By modifying below part and using NYTimes API, automatically load article related to deepfake
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.no_image), "News Title 1");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.no_image), "News Title 2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.no_image), "News Title 3");
    }
}
