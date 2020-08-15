package com.example.findreal;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;

public class NewsListViewAdapter extends RecyclerView.Adapter<NewsListViewAdapter.NewsListViewHolder>{
    private ArrayList<NewsListViewItem> newsListViewItems = new ArrayList<NewsListViewItem>();

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    class NewsListViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView title;

        NewsListViewHolder(View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.news_thumbnail);
            title = itemView.findViewById(R.id.news_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if(mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }

        void onBind(NewsListViewItem item) {
            thumbnail.setImageDrawable(item.getThumbnail());
            thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

    public void addItem(Drawable thumbnail, String title, String url, String thumbnailUrl) {
        NewsListViewItem item = new NewsListViewItem();

        item.setThumbnail(thumbnail);
        item.setNewsTitle(title);
        item.setUrlStr(url);
        item.setThumbnailUrlStr(thumbnailUrl);

        newsListViewItems.add(item);
    }
}
