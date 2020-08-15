package com.example.findreal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    private static final String TAG = "NewsActivity";

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
        NewYorkTimesApiClass nyTimesAPI = new NewYorkTimesApiClass();

        String title = articleData.getNewsTitle();
        String url = articleData.getUrlStr();
        String thumbnailUrl = articleData.getThumbnailUrlStr();

        Bitmap thumbnailBitmap = nyTimesAPI.getBitmapFromURL(thumbnailUrl);
        Drawable thumbnailDrawable = new BitmapDrawable(getResources(), thumbnailBitmap);

        Log.d(TAG, "Image width: " + thumbnailDrawable.getIntrinsicWidth());
        Log.d(TAG, "Image height: " + thumbnailDrawable.getIntrinsicHeight());

        ImageView imageView = (ImageView) findViewById(R.id.news_main_picture);
        imageView.setImageDrawable(thumbnailDrawable);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView titleView = (TextView) findViewById(R.id.article_title);
        titleView.setText(title);

        TextView contentView = (TextView) findViewById(R.id.article_content);
        TextView authorView  = (TextView) findViewById(R.id.article_author);
        TextView dateView    = (TextView) findViewById(R.id.article_date);

        List<String> resultLoadContent = new ArrayList<>();
        try {
            resultLoadContent = nyTimesAPI.loadContent(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        contentView.setText(resultLoadContent.get(0));
        authorView.setText(resultLoadContent.get(1));
        dateView.setText(resultLoadContent.get(2));

    }
}
