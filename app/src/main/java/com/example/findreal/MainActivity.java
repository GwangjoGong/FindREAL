package com.example.findreal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    public static String email;
    private View header;
    private DrawerLayout drawerLayout;

    private List<ArticleInfo> loadedArticleInfo = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onResume(){
        super.onResume();
        Log.i("resume","resume");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(email == null){
            Intent userdata = getIntent();
            email = userdata.getStringExtra("token");
            if(email == null){
                Toast.makeText(this, "Invalid Access", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        if(Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy((policy));
        }

        // initialize navigation menu
        try {
            initializeNavigation(email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Initialize requests
        LinearLayout requests = (LinearLayout) findViewById(R.id.requests);
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RequestListActivity.class);
                startActivity(intent);
            }
        });

        try {
            initializeRequests(email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // initialize News list
        try {
            initializeNewsList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeNavigation(final String email) throws JSONException {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        String url = "http://3.35.41.92:3000/profile";
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("email", email);

        byte[] signInDataBytes = LoginActivity.parseParameter(data);
        String result = LoginActivity.sendPost(signInDataBytes, url);

        JSONObject response = new JSONObject(result);
        JSONObject userObject = response.getJSONObject("user");
        String user = userObject.getString("name");
        String profileUrl = userObject.getString("profile");

        Bitmap profileBitmap = NewYorkTimesApiClass.getBitmapFromURL(profileUrl);

        ImageView userProfile = (ImageView) headerView.findViewById(R.id.profile);
        userProfile.setImageBitmap(profileBitmap);
        TextView userName = (TextView) headerView.findViewById(R.id.user_name);
        userName.setText(user);
        TextView userEmail = (TextView) headerView.findViewById(R.id.user_email);
        userEmail.setText(email);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(false);
                drawerLayout.closeDrawers();
                Intent intent;

                switch (menuItem.getItemId())
                {
                    case R.id.menu_mypage:
                        intent = new Intent (MainActivity.this, MyPageActivity.class);
                        intent.putExtra("token",email);
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
                    case R.id.menu_logout:
                        intent = new Intent (MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void initializeRequests(String email) throws JSONException {
        // Requests
        TextView ongoingRequests   = (TextView) findViewById(R.id.ongoing_requests);
        TextView completedRequests = (TextView) findViewById(R.id.completed_requests);

        String url = "http://3.35.41.92:3000/requests";
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("email", email);

        byte[] signInDataBytes = LoginActivity.parseParameter(data);
        String result = LoginActivity.sendPost(signInDataBytes, url);

        JSONObject response = new JSONObject(result);
        JSONArray doneArray = response.getJSONArray("done");
        JSONArray progressArray = response.getJSONArray("progress");

        int numOfOngoingRequests = progressArray.length();
        int numOfCompletedRequests = doneArray.length();

        if (numOfOngoingRequests <= 1){
            ongoingRequests.setText(numOfOngoingRequests + " Ongoing Request");
        } else {
            ongoingRequests.setText(numOfOngoingRequests + " Ongoing Requests");
        }

        if (numOfCompletedRequests <= 1){
            completedRequests.setText(numOfCompletedRequests + " Completed Request");
        } else {
            completedRequests.setText(numOfCompletedRequests + " Completed Requests");
        }


        // PieChart
        PieChart requestPieChart = (PieChart)findViewById(R.id.requests_piechart);
        // Piechart for requests

        requestPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RequestListActivity.class);
                startActivity(intent);
            }
        });

        requestPieChart.setUsePercentValues(true);
        requestPieChart.getDescription().setEnabled(false);
        requestPieChart.getLegend().setEnabled(false);
        requestPieChart.setExtraOffsets(0,0,0,0);

        requestPieChart.setTouchEnabled(false);

        requestPieChart.setDrawHoleEnabled(false);
        requestPieChart.setHoleColor(Color.WHITE);
        requestPieChart.setTransparentCircleRadius(20f);
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(numOfCompletedRequests,""));
        yValues.add(new PieEntry(numOfOngoingRequests,""));

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(0f);

        dataSet.setColors(new int[] {getResources().getColor(R.color.completed), getResources().getColor(R.color.ongoing)});

        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(0f);

        requestPieChart.setData(pieData);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initializeNewsList() throws IOException {
        final NewsListViewAdapter adapter;
        RecyclerView recyclerView = findViewById(R.id.news_listview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NewsListViewAdapter();
        adapter.setOnItemClickListener(new NewsListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                NewsListViewItem articleData = (NewsListViewItem) adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                intent.putExtra("articleData", articleData);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new NewsListViewDecoration(20)); // set border between news

        if(loadedArticleInfo.size() == 0){
            NewYorkTimesApiClass nyTimesAPI = new NewYorkTimesApiClass();
            loadedArticleInfo = nyTimesAPI.loadArticleInfo();
        }
        // add 3 article previews
        for (ArticleInfo articleInfo : loadedArticleInfo) {
            Drawable thumbnailDrawable = new BitmapDrawable(getResources(), articleInfo.getThumbnailBitmap());
            adapter.addItem(thumbnailDrawable, articleInfo.getTitleStr(), articleInfo.getUrlStr(), articleInfo.getThumbnailUrlStr());
        }

    }
}
