package com.example.findreal;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class NewYorkTimesApiClass {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void loadArticle() throws IOException {
        URL url = new URL("https://api.nytimes.com/svc/search/v2/articlesearch.json?" +
                "begin_date=20180101" +
                "&end_date=20200801" +
                "&q=deepfake" +
                "&api-key=8rbAjzkDbCUjngtsT76jQi6kNiW0yNSR");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        try (BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            JSONObject jObject = new JSONObject(r.lines().collect(Collectors.joining("\n")));
            JSONObject responseObject = jObject.getJSONObject("response");
            JSONArray docsArray = responseObject.getJSONArray("docs");

            for (int i = 0; i < 5; i++)
            {
                JSONObject article = docsArray.getJSONObject(i);
                String url_article = article.getString("web_url");
                //System.out.println(url_article);

                JSONObject headlineObject = article.getJSONObject("headline");
                String title = headlineObject.getString("main");
                String snippet = article.getString("snippet");

                System.out.println(title);
                loadContent(url_article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void loadContent(String url) throws IOException {
        Document doc = Jsoup.parse(new URL(url).openStream(), "iso-8859-1", url);
        Elements paragraphs = doc.select("div[class=css-53u6y8]");

        for (Element paragraph : paragraphs){
            String temp = paragraph.text();
            //temp = temp.replace("???","");
            System.out.println(temp);
        }
        System.out.println("\n");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) throws IOException
    {
        loadArticle();
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap thumbnail = null;

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                thumbnail = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return thumbnail;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
