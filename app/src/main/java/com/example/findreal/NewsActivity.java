package com.example.findreal;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

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
        NewsListViewItem articleData = (NewsListViewItem) intent.getSerializableExtra("articleData");

        Drawable thumbnailDrawable = articleData.getThumbnail();
        String title = articleData.getNewsTitle();
        String url = articleData.getUrlStr();

        ImageView imageView = (ImageView) findViewById(R.id.news_main_picture);
        imageView.setImageDrawable(thumbnailDrawable);

        TextView titleView = (TextView) findViewById(R.id.article_title);
        titleView.setText(title);

        NewYorkTimesApiClass nyTimesAPI = new NewYorkTimesApiClass();

        TextView contentView = (TextView) findViewById(R.id.article_content);
        String content = null;
        try {
            content = nyTimesAPI.loadContent(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentView.setText(content);

        Toast.makeText(this, url, Toast.LENGTH_LONG).show();
    }
}
