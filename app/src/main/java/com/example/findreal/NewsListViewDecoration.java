package com.example.findreal;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class NewsListViewDecoration extends RecyclerView.ItemDecoration {
    private final int divHeight;

    public NewsListViewDecoration(int divHeight) {
        this.divHeight = divHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = divHeight;
    }
}
