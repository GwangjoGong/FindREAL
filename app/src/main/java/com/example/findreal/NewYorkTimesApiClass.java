package com.example.findreal;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

import com.squareup.picasso.*;


public class NewYorkTimesApiClass {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<ArticleInfo> loadArticleInfo() throws IOException {
        URL url = new URL("https://api.nytimes.com/svc/search/v2/articlesearch.json?" +
                "begin_date=20180101" +
                "&end_date=20200801" +
                "&q=deepfake" +
                "&api-key=8rbAjzkDbCUjngtsT76jQi6kNiW0yNSR");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        List<ArticleInfo> articleInfoList = new ArrayList<ArticleInfo>();

        try (BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            JSONObject jObject = new JSONObject(r.lines().collect(Collectors.joining("\n")));
            JSONObject responseObject = jObject.getJSONObject("response");
            JSONArray docsArray = responseObject.getJSONArray("docs");

            // Crawl 3 articles from New York Times and return with title and thumbnail
            for (int i = 0; i < 3; i++)
            {
                JSONObject article = docsArray.getJSONObject(i);
                ArticleInfo tempInfo = new ArticleInfo();

                String urlArticle = article.getString("web_url");
                JSONObject headlineObject = article.getJSONObject("headline");
                String titleArticle = headlineObject.getString("main");

                tempInfo.setTitleStr(titleArticle);
                tempInfo.setUrlStr(urlArticle);

                // Codes to be implemented more
                String thumbnailUrl = "https://...";
                ImageView tempThumbnail = this.downloadImageFromURL(thumbnailUrl);
                Drawable thumbnailArticle = tempThumbnail.getDrawable();
                //

                tempInfo.setThumbnailDrawable(thumbnailArticle);

                articleInfoList.add(tempInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    public ImageView downloadImageFromURL(String url) {
        ImageView imageView = null;
        Picasso.get().load(url).into(imageView);
        return imageView;
    }

}
