package com.example.findreal;

import android.graphics.Bitmap;

public class ArticleInfo {
    private Bitmap thumbnailBitmap;
    private String titleStr;

    public Bitmap getThumbnailBitmap() {
        return thumbnailBitmap;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setThumbnailBitmap(Bitmap thumbnailBitmap) {
        this.thumbnailBitmap = thumbnailBitmap;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }
}
