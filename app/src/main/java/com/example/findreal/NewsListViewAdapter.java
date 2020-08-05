package com.example.findreal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;

public class NewsListViewAdapter extends RecyclerView.Adapter<NewsListViewAdapter.NewsListViewHolder>{
    private ArrayList<NewsListViewItem> newsListViewItems = new ArrayList<NewsListViewItem>();

    class NewsListViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView title;

        NewsListViewHolder(View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.news_thumbnail);
            title = itemView.findViewById(R.id.news_title);
        }

        void onBind(NewsListViewItem item) {
            thumbnail.setImageDrawable(item.getThumbnail());
            title.setText(item.getNewsTitle());

        }
    }

    public NewsListViewAdapter() {

    }

    @NonNull
    @Override
    public NewsListViewAdapter.NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_listview_item, parent, false);
        return new NewsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListViewAdapter.NewsListViewHolder holder, int position) {
        holder.onBind(newsListViewItems.get(position));
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return newsListViewItems.size();
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
