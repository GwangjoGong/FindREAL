package com.example.findreal;

import android.graphics.drawable.Drawable;

public class NewsListViewItem {
    private Drawable thumbnailDrawable;
    private String newsTitleStr;
    private String urlStr;

    public void setThumbnail(Drawable thumbnail) {
        this.thumbnailDrawable = thumbnail;
    }

    public void setNewsTitle(String newsTitle) { this.newsTitleStr = newsTitle; }

    public void setUrlStr(String url) { this.urlStr = url; }

    public Drawable getThumbnail() {
        return this.thumbnailDrawable;
    }

    public String getNewsTitle() {
        return this.newsTitleStr;
    }

    public String getUrlStr() { return this.urlStr; }
}
