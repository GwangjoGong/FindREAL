package com.example.findreal;

import android.graphics.Bitmap;

public class ArticleInfo {
    private Bitmap thumbnailBitmap;
    private String titleStr;
    private String urlStr;
    private String thumbnailUrlStr;

    public Bitmap getThumbnailBitmap() {
        return this.thumbnailBitmap;
    }

    public String getTitleStr() {
        return this.titleStr;
    }

    public String getUrlStr() { return this.urlStr; }

    public String getThumbnailUrlStr() { return this.thumbnailUrlStr; }

    public void setThumbnailBitmap(Bitmap thumbnailBitmap) {
        this.thumbnailBitmap = thumbnailBitmap;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public void setUrlStr(String urlStr) { this.urlStr = urlStr; }

    public void setThumbnailUrlStr(String thumbnailUrlStr) { this.thumbnailUrlStr = thumbnailUrlStr; }
}
