package com.example.findreal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProgressListAdapter extends BaseAdapter {
    List<ProgressRequest> progressRequests = new ArrayList<ProgressRequest>();
    Context context;

    public ProgressListAdapter(Context context) {this.context = context;}

    public void add(ProgressRequest pr){
        this.progressRequests.add(pr);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return progressRequests.size();
    }

    @Override
    public Object getItem(int position) {
        return progressRequests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        ProgressListViewHolder holder = new ProgressListViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ProgressRequest pr = progressRequests.get(position);

        convertView = messageInflater.inflate(R.layout.ongoing_request, null);
        holder.imview_ongoing_request = convertView.findViewById(R.id.imview_ongoing_request);
        holder.tv_request_timestamp = convertView.findViewById(R.id.tv_request_timestamp);
        holder.tv_request_service_name = convertView.findViewById(R.id.tv_request_service_name);
        convertView.setTag(holder);

        byte[] decodedString = Base64.decode(pr.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
        holder.imview_ongoing_request.setImageBitmap(decodedByte);

        holder.tv_request_timestamp.setText(pr.getTimestamp());

        switch (pr.getService()){
            case "beginner":
                holder.tv_request_service_name.setText("Beginner Service");
                break;
            case "standard":
                holder.tv_request_service_name.setText("Standard Service");
                break;
            case "professional":
                holder.tv_request_service_name.setText("Professional Service");
                break;
        }

        return convertView;
    }
}

class ProgressListViewHolder {
    public ImageView imview_ongoing_request;
    public TextView tv_request_timestamp;
    public TextView tv_request_service_name;
}

