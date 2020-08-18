package com.example.findreal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProgressListAdapter extends RecyclerView.Adapter<ProgressListAdapter.ProgressListViewHolder> {
    List<ProgressListItem> progressRequests = new ArrayList<ProgressListItem>();

    public ProgressListAdapter() {}

    class ProgressListViewHolder extends RecyclerView.ViewHolder {
        public ImageView imview_ongoing_request;
        public TextView tv_request_timestamp;
        public TextView tv_request_service_name;

        ProgressListViewHolder(View itemView){
            super(itemView);

            imview_ongoing_request = itemView.findViewById(R.id.imview_ongoing_request);
            tv_request_timestamp = itemView.findViewById(R.id.tv_request_timestamp);
            tv_request_service_name = itemView.findViewById(R.id.tv_request_service_name);
        }

        void onBind(ProgressListItem pr){
            byte[] decodedString = Base64.decode(pr.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
            imview_ongoing_request.setImageBitmap(decodedByte);

            tv_request_timestamp.setText(pr.getTimestamp());

            switch (pr.getService()){
                case "beginner":
                    tv_request_service_name.setText("Beginner Service");
                    break;
                case "standard":
                    tv_request_service_name.setText("Standard Service");
                    break;
                case "professional":
                    tv_request_service_name.setText("Professional Service");
                    break;
            }
        }
    }

    @NonNull
    @Override
    public ProgressListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_request, parent, false);
        return new ProgressListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressListViewHolder holder, int position){
        holder.onBind(progressRequests.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return progressRequests.size();
    }

    //@Override
    public Object getItem(int position) {
        return progressRequests.get(position);
    }

    public void addItem(String image_base64, String timestamp, String service){
        ProgressListItem item = new ProgressListItem();

        item.setImage(image_base64);
        item.setTimestamp(timestamp);
        item.setService(service);

        progressRequests.add(item);
    }

}

