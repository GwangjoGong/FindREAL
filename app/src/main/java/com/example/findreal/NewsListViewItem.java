package com.example.findreal;

import android.graphics.drawable.Drawable;

public class NewsListViewItem {
    private Drawable thumbnailDrawable;
    private String newsTitleStr;

    public void setThumbnail(Drawable thumbnail) {
        thumbnailDrawable = thumbnail;
    }

    public void setNewsTitle(String newsTitle) {
        newsTitleStr = newsTitle;
    }

    public Drawable getThumbnail() {
        return this.thumbnailDrawable;
    }

    public String getNewsTitle() {
        return this.newsTitleStr;
    }
}
