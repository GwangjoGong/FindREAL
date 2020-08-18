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

public class DoneListAdapter extends RecyclerView.Adapter<DoneListAdapter.DoneListViewHolder> {
    List<DoneListItem> doneRequests = new ArrayList<DoneListItem>();

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(DoneListAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    class DoneListViewHolder extends RecyclerView.ViewHolder {
        public ImageView imview_completed_request;
        public TextView tv_request_result;
        public ImageView imview_request_mark;


        DoneListViewHolder(View itemView) {
            super(itemView);

            imview_completed_request = itemView.findViewById(R.id.imview_completed_request);
            tv_request_result = itemView.findViewById(R.id.tv_request_result);
            imview_request_mark = itemView.findViewById(R.id.imview_request_mark);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }

        void onBind(DoneListItem item) {
            byte[] decodedString = Base64.decode(item.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imview_completed_request.setImageBitmap(decodedByte);

            double real = item.getReal();
            double fake = item.getFake();
            String error = item.getError();

            if (error != "null") {
                tv_request_result.setText("Unanalyzable");
                imview_request_mark.setVisibility(View.INVISIBLE);
            } else if (real - fake >= 0.25) {
                tv_request_result.setText("Highly Trustful");
                imview_request_mark.setImageResource(R.drawable.request_good);
            } else if (real - fake >= 0.0) {
                tv_request_result.setText("Slightly Trustful");
                imview_request_mark.setImageResource(R.drawable.request_good);
            } else if (real - fake >= -0.25) {
                tv_request_result.setText("Slightly Suspicious");
                imview_request_mark.setImageResource(R.drawable.request_bad);
            } else {
                tv_request_result.setText("Highly Suspicious");
                imview_request_mark.setImageResource(R.drawable.request_bad);
            }
        }


    }

    public DoneListAdapter() {
    }


    @NonNull
    @Override
    public DoneListAdapter.DoneListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_request, parent, false);
        return new DoneListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoneListAdapter.DoneListViewHolder holder, int position) {
        holder.onBind(doneRequests.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return doneRequests.size();
    }

    //@Override
    public Object getItem(int position) {
        return doneRequests.get(position);
    }

    public void addItem(String image, double real, double fake, String error){
        DoneListItem item = new DoneListItem();

        item.setImage(image);
        item.setReal(real);
        item.setFake(fake);
        item.setError(error);

        doneRequests.add(item);
    }
}
