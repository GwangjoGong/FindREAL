package com.example.findreal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class DoneListAdapter extends BaseAdapter {
    List<DoneRequest> doneRequests = new ArrayList<DoneRequest>();
    Context context;

    public DoneListAdapter(Context context) {this.context = context;}

    public void add(DoneRequest dr){
        this.doneRequests.add(dr);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return doneRequests.size();
    }

    @Override
    public Object getItem(int position) {
        return doneRequests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        DoneListViewHolder holder = new DoneListViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        DoneRequest dr = doneRequests.get(position);

        convertView = messageInflater.inflate(R.layout.completed_request, null);
        holder.imview_completed_request = convertView.findViewById(R.id.imview_completed_request);
        holder.tv_request_result = convertView.findViewById(R.id.tv_request_result);
        holder.imview_request_mark = convertView.findViewById(R.id.imview_request_mark);
        convertView.setTag(holder);

        byte[] decodedString = Base64.decode(dr.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
        holder.imview_completed_request.setImageBitmap(decodedByte);

        double real = dr.getReal();
        double fake = dr.getFake();
        String error = dr.getError();

        if(error != "null"){
            holder.tv_request_result.setText("Unanalyzable");
            holder.imview_request_mark.setVisibility(View.INVISIBLE);
        }
        else if(real-fake >= 0.25){
            holder.tv_request_result.setText("Highly Trustful");
            holder.imview_request_mark.setImageResource(R.drawable.request_good);
        }else if(real-fake >= 0.0){
            holder.tv_request_result.setText("Slightly Trustful");
            holder.imview_request_mark.setImageResource(R.drawable.request_good);
        }else if(real-fake >= -0.25){
            holder.tv_request_result.setText("Slightly Suspicious");
            holder.imview_request_mark.setImageResource(R.drawable.request_bad);
        }else{
            holder.tv_request_result.setText("Highly Suspicious");
            holder.imview_request_mark.setImageResource(R.drawable.request_bad);
        }

        return convertView;
    }
}

class DoneListViewHolder {
    public ImageView imview_completed_request;
    public TextView tv_request_result;
    public ImageView imview_request_mark;
}

