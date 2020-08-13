package com.example.findreal;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewYorkTimesApiClass extends AppCompatActivity {
    private Bitmap result;
    private static final String TAG = "MainActivity";

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<ArticleInfo> loadArticleInfo() throws IOException {
        URL url = new URL("https://api.nytimes.com/svc/search/v2/articlesearch.json?" +
                "begin_date=20180101" +
                "&end_date=20200801" +
                "&q=deepfake" +
                "&api-key=8rbAjzkDbCUjngtsT76jQi6kNiW0yNSR");

        Log.d(TAG, "request api");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        List<ArticleInfo> articleInfoList = new ArrayList<>();

        try (BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            JSONObject jObject = new JSONObject(r.lines().collect(Collectors.joining("\n")));
            JSONObject responseObject = jObject.getJSONObject("response");
            JSONArray docsArray = responseObject.getJSONArray("docs");

            // Crawl 3 articles from New York Times
            for (int i = 0; i < 3; i++)
            {
                JSONObject article = docsArray.getJSONObject(i);
                ArticleInfo tempInfo = new ArticleInfo();

                String urlArticle = article.getString("web_url");
                JSONObject headlineObject = article.getJSONObject("headline");
                String titleArticle = headlineObject.getString("main");

                tempInfo.setTitleStr(titleArticle);
                tempInfo.setUrlStr(urlArticle);

                String thumbnailUrl = "https://nytimes.com/"; // base url

                JSONArray multimediaObject = article.getJSONArray("multimedia");
                JSONObject thumbnailObject = multimediaObject.getJSONObject(0);
                thumbnailUrl += thumbnailObject.getString("url"); // add image path

                GetBitmapFromURL downloadTask = new GetBitmapFromURL();
                downloadTask.execute(thumbnailUrl);

                Log.d(TAG, thumbnailUrl);

                Bitmap thumbnailArticle = result;

                tempInfo.setThumbnailBitmap(thumbnailArticle);

                articleInfoList.add(tempInfo);

                Log.d(TAG, Integer.toString(articleInfoList.size()));
            }
        } catch (JSONException e) {
            e.printStackTrace();

            // fill with empty information
            int count = articleInfoList.size();
            for (int i = count; i < 3; i++) {
                ArticleInfo emptyInfo = new ArticleInfo();

                Drawable tempThumbnail = getDrawable(R.drawable.no_image);

                emptyInfo.setThumbnailBitmap(((BitmapDrawable)tempThumbnail).getBitmap());
                emptyInfo.setTitleStr("Empty Title");
                emptyInfo.setUrlStr("empty");

                articleInfoList.add(i, emptyInfo);
            }
        }

        return articleInfoList;
    }

    public static String loadContent(String url) throws IOException {
        Document doc = Jsoup.parse(new URL(url).openStream(), "iso-8859-1", url);
        Elements paragraphs = doc.select("div[class=css-53u6y8]");
        String allContent = "";

        for (Element paragraph : paragraphs){
            String temp = paragraph.text();
            allContent = allContent.concat(temp);
        }

        return allContent;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public class GetBitmapFromURL extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmapFromURL(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            result = bitmap;
        }
    }
}
