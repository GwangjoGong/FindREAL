package com.example.findreal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class RequestListActivity extends AppCompatActivity {

    private RecyclerView doneList;
    private RecyclerView progressList;

    private DoneListAdapter doneListAdapter;
    private ProgressListAdapter progressListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        progressList = findViewById(R.id.list_ongoing);

        LinearLayoutManager progressLayoutManager = new LinearLayoutManager(this);
        progressList.setLayoutManager(progressLayoutManager);

        progressListAdapter = new ProgressListAdapter();
        progressList.setAdapter(progressListAdapter);


        doneList = findViewById(R.id.list_completed);

        LinearLayoutManager doneLayoutManager = new LinearLayoutManager(this);
        doneList.setLayoutManager(doneLayoutManager);

        doneListAdapter = new DoneListAdapter();
        doneListAdapter.setOnItemClickListener(new DoneListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                DoneListItem item = (DoneListItem) doneListAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), RequestResultActivity.class);
                intent.putExtra("real", item.getReal());
                intent.putExtra("fake", item.getFake());
                intent.putExtra("error", item.getError());
                intent.putExtra("image", item.getImage());
                startActivity(intent);
            }
        });

        doneList.setAdapter(doneListAdapter);

        findViewById(R.id.imbtn_add_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddRequestActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.imbtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getRequests();
    }

    private void getRequests(){
        String url = "http://3.35.41.92:3000/requests";

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("email", MainActivity.email);

        byte[] requestDataBytes = LoginActivity.parseParameter(data);
        String result = LoginActivity.sendPost(requestDataBytes, url);


        try{
            JSONObject resultObj = new JSONObject(result);
            JSONArray done = resultObj.getJSONArray("done");
            JSONArray progress = resultObj.getJSONArray("progress");

            if(done.length() == 0){
                findViewById(R.id.layout_header_completed).setVisibility(View.GONE);
                doneList.setVisibility(View.GONE);
            }else{
                findViewById(R.id.layout_header_completed).setVisibility(View.VISIBLE);
                doneList.setVisibility(View.VISIBLE);
            }

            for(int i = done.length() - 1; i >= 0; i--){
                JSONObject dit = done.getJSONObject(i);
                doneListAdapter.addItem(dit.getString("image"), dit.getDouble("real"), dit.getDouble("fake"), dit.getString("error"));
            }

            if(progress.length() == 0){
                progressList.setVisibility(View.GONE);
            }else{
                progressList.setVisibility(View.VISIBLE);
            }

            for(int i = 0; i < progress.length(); i++){
                JSONObject pit = progress.getJSONObject(i);
                progressListAdapter.addItem(pit.getString("image"),pit.getString("created"), pit.getString("service"));
            }


        }catch (JSONException e){
            Log.e("Error", e.toString());
        }
    }
}
