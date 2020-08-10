package com.example.findreal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private View header;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide Navigation Bar - doesn't work well
        // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // initialize navigation menu
        initializeNavigation();

        // Piechart for requests
        initializePieChart();

        // initialize News list
        initializeNewsList();
    }

    public void initializeNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                Intent intent;

                switch (menuItem.getItemId())
                {
                    case R.id.menu_mypage:
                        intent = new Intent (MainActivity.this, MyPageActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_info:
                        intent = new Intent (MainActivity.this, InformationActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_services:
                        intent = new Intent (MainActivity.this, ServicesActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_settings:
                        intent = new Intent (MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
//                    case R.id.menu_logout:
//                        intent = new Intent (MainActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    public void initializePieChart() {
        PieChart requestPieChart = (PieChart)findViewById(R.id.requests_piechart);

        requestPieChart.setUsePercentValues(true);
        requestPieChart.getDescription().setEnabled(false);
        requestPieChart.getLegend().setEnabled(false);
        requestPieChart.setExtraOffsets(0,0,0,0);

        requestPieChart.setTouchEnabled(false);

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
    }

    public void initializeNewsList() {
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
