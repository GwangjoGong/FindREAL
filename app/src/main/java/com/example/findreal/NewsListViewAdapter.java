package com.example.findreal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;

public class NewsListViewAdapter {
    private ArrayList<NewsListViewItem> newsListViewItems = new ArrayList<NewsListViewItem>();

    public NewsListViewAdapter() {

    }

    //@Override
    public int getCount() {
        return newsListViewItems.size();
    }

    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.news_listview_item, parent, false);
        }

        ImageView thumbnailImageView = (ImageView) convertView.findViewById(R.id.news_thumbnail);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.news_title);

        NewsListViewItem newsListViewItem = newsListViewItems.get(position);

        thumbnailImageView.setImageDrawable(newsListViewItem.getThumbnail());
        titleTextView.setText(newsListViewItem.getNewsTitle());

        return convertView;
    }

    //@Override
    public long getItemId(int position) {
        return position;
    }

    //@Override
    public Object getItem(int position) {
        return newsListViewItems.get(position);
    }

    public void addItem(Drawable thumbnail, String title) {
        NewsListViewItem item = new NewsListViewItem();

        item.setThumbnail(thumbnail);
        item.setNewsTitle(title);

        newsListViewItems.add(item);
    }
}
