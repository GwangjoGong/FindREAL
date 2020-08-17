package com.example.findreal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import com.bumptech.glide.*;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyPageActivity extends AppCompatActivity {
    public static String email;
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        ImageButton backButton = (ImageButton) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (email == null) {
            Intent userdata = getIntent();
            email = userdata.getStringExtra("token");
            if (email == null) {
                Toast.makeText(this, "Invalid Access", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        TextView user_email = (TextView) findViewById(R.id.email_text);
        TextView user_name = (TextView) findViewById(R.id.name_text);
        TextView user_service = (TextView) findViewById(R.id.services_text);
        ImageView profile_image = (ImageView) findViewById(R.id.profile_image);

        String url = "http://3.35.41.92:3000/profile";
        String[] result = sendPost(email, url);

        String name = result[0];
        String service = result[1];

        user_email.setText(email);
        user_name.setText(name);
        user_service.setText(service);
        Glide.with(this).load(result[2]).into(profile_image);
    }

    public static String[] sendPost(final String email, final String url) {
        final String[] result = new String[3];
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("email", email);

        final byte[] singInDataBytes = parseParameter(data);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL requestUrl = new URL(url);
                    StringBuffer res = new StringBuffer();

                    HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(singInDataBytes.length));
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.write(singInDataBytes);

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    int status = conn.getResponseCode();
                    if (status != 200) {
                        throw new IOException("Post failed");
                    } else {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            res.append(inputLine);
                        }
                        in.close();
                    }

                    conn.disconnect();

                    JSONObject response = new JSONObject(res.toString());
                    JSONObject user = response.getJSONObject("user");
                    String name = user.getString("name");
                    String service = user.getString("service");
                    String profile = user.getString("profile");

                    result[0] = name;
                    result[1] = service;
                    result[2] = profile;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        while (thread.isAlive()){}

        return result;
    }

    public static byte[] parseParameter(Map<String, Object> params) {
        StringBuilder postData = new StringBuilder();
        byte[] postDataBytes = null;
        try {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append("=");
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            postDataBytes = postData.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return postDataBytes;
    }
}