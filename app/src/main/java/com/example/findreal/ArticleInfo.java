package com.example.findreal;

import android.graphics.drawable.Drawable;

public class ArticleInfo {
    private Drawable thumbnailDrawable;
    private String titleStr;
    private String urlStr;

    public Drawable getThumbnailBitmap() {
        return thumbnailDrawable;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public String getUrlStr() { return urlStr; }

    public void setThumbnailDrawable(Drawable thumbnailDrawable) {
        this.thumbnailDrawable = thumbnailDrawable;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public void setUrlStr(String urlStr) { this.urlStr = urlStr; }
}
