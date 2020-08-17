package com.example.findreal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddRequestActivity extends AppCompatActivity {

    private int RC_PICK_IMAGE = 1;
    private int RC_PERMISSION = 2;

    private ImageButton imbtn_upload;
    private ImageButton imbtn_request;
    private ImageView imview_processing;
    private Bitmap request_image;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        findViewById(R.id.imbtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RequestListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imbtn_upload = findViewById(R.id.imbtn_upload);
        imbtn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        request_image = null;

        imview_processing = findViewById(R.id.imview_processing);

        imbtn_request = findViewById(R.id.imbtn_request);
        imbtn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imbtn_request.setVisibility(View.GONE);
                imview_processing.setVisibility(View.VISIBLE);
                makeRequest();
                imbtn_request.setVisibility(View.VISIBLE);
                imview_processing.setVisibility(View.GONE);

            }
        });
    }

    private void pickImage(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "분석할 사진을 선택해주세요."), RC_PICK_IMAGE);
        }else{
            requestPermission();
        }
    }

    private void requestPermission(){
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        ActivityCompat.requestPermissions(
                this,
                permissions,
                RC_PERMISSION
        );
    }

    private void makeRequest(){
        if(request_image == null){
            Toast.makeText(this, "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show();
        }else{

            String b = toBase64(request_image);
            String url = "http://3.35.41.92:3000/new_request";

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("image", b);
            data.put("email", MainActivity.email);

            Log.i("Email", MainActivity.email);


            byte[] imageDataBytes = LoginActivity.parseParameter(data);
            String result = LoginActivity.sendPost(imageDataBytes, url);

            Log.i("Result", result);

            try{
                JSONObject resultObj = new JSONObject(result);

                if(resultObj.getBoolean("success")){
                    Intent intent = new Intent(getApplicationContext(), AddRequestSuccessActivity.class);
                    intent.putExtra("image", toBase64(request_image));
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, "요청에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                Log.e("Error", e.toString());
            }
        }
    }

    private String toBase64(Bitmap bitmap) {
        int quality = 100;
        int threshold = 100000;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        int divQual = (int) Math.ceil(byteArray.length / threshold);

        Log.i("DIV", "nom : "+divQual);


        byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, Math.round(quality/divQual), byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();


        Log.i("Bytes", "size : "+byteArray.length);
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == RC_PICK_IMAGE){
                if(data == null){
                    Toast.makeText(this, "이미지 선택에 실패했습니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                try{
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    Log.i("InputStream Size","Size " + is.available());

                    Bitmap bm = BitmapFactory.decodeStream(is);
                    imbtn_upload.setImageBitmap(bm);
                    request_image = bm;
                }catch (Exception e){
                    Log.d("Error", e.toString());
                }
            }
        }else{
            Toast.makeText(this, "이미지 선택에 실패했습니다.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == RC_PERMISSION){
            for(int i = 0; i < permissions.length; i++){
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    if(grantResult == PackageManager.PERMISSION_GRANTED){
                        pickImage();
                    }else {
                        requestPermission();
                    }
                }
            }
        }

    }

}
