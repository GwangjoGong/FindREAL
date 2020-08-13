package com.example.findreal;

import android.graphics.Bitmap;

public class ArticleInfo {
    private Bitmap thumbnailBitmap;
    private String titleStr;
    private String urlStr;

    public Bitmap getThumbnailBitmap() {
        return thumbnailBitmap;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public String getUrlStr() { return urlStr; }

    public void setThumbnailBitmap(Bitmap thumbnailBitmap) {
        this.thumbnailBitmap = thumbnailBitmap;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public void setUrlStr(String urlStr) { this.urlStr = urlStr; }
}
