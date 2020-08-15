package com.example.findreal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RequestResultActivity extends AppCompatActivity {

    private TextView tv_request_result;
    private ImageView imview_image;
    private ImageView imview_request_mark;
    private TextView tv_real;
    private TextView tv_fake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tv_request_result = findViewById(R.id.tv_result);
        imview_image = findViewById(R.id.imview_image);
        imview_request_mark = findViewById(R.id.imview_result_mark);
        tv_real = findViewById(R.id.tv_real);
        tv_fake = findViewById(R.id.tv_fake);

        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        double real = intent.getDoubleExtra("real",0);
        double fake = intent.getDoubleExtra("fake",0);
        String error = intent.getStringExtra("error");

        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
        imview_image.setImageBitmap(decodedByte);

        Log.i("ERROR", "?" + (error == null));

        if(real == -1.0){
            tv_request_result.setText("Unanalyzable");
            imview_request_mark.setVisibility(View.INVISIBLE);
            tv_real.setText(error);
            tv_fake.setVisibility(View.INVISIBLE);
        }else if(real-fake >= 0.25){
            tv_request_result.setText("Highly Trustful");
            imview_request_mark.setImageResource(R.drawable.request_good);
            tv_real.setText("Real : "+round(real)+"%");
            tv_fake.setText("Fake : "+round(fake)+"%");
        }else if(real-fake >= 0.0){
            tv_request_result.setText("Slightly Trustful");
            imview_request_mark.setImageResource(R.drawable.request_good);
            tv_real.setText("Real : "+round(real)+"%");
            tv_fake.setText("Fake : "+round(fake)+"%");
        }else if(real-fake >= -0.25){
            tv_request_result.setText("Slightly Suspicious");
            imview_request_mark.setImageResource(R.drawable.request_bad);
            tv_real.setText("Real : "+round(real)+"%");
            tv_fake.setText("Fake : "+round(fake)+"%");
        }else{
            tv_request_result.setText("Highly Suspicious");
            imview_request_mark.setImageResource(R.drawable.request_bad);
            tv_real.setText("Real : "+round(real)+"%");
            tv_fake.setText("Fake : "+round(fake)+"%");
        }

        findViewById(R.id.imbtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private double round(double d){
        double val = d*10000;
        val = Math.round(val);
        val = val /10000;
        return val * 100;
    }
}
