package com.example.findreal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ImageButton backButton = (ImageButton) findViewById(R.id.backbtn_news);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        String receivedURL = intent.getExtras().getString("url");

        Toast.makeText(this, receivedURL, Toast.LENGTH_LONG).show();
    }
}
