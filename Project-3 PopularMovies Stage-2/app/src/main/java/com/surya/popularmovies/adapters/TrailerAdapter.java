package com.surya.popularmovies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.surya.popularmovies.R;
import com.surya.popularmovies.models.Trailer;

import java.util.List;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {


    private Context mContext;
    private List<Trailer> trailerList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.trailer_image);
            name = view.findViewById(R.id.trailer_name);
        }
    }


    public TrailerAdapter(Context mContext, List<Trailer> trailerList) {
        this.mContext = mContext;
        this.trailerList = trailerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_cardview, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Trailer trailer = trailerList.get(position);
        holder.name.setText(trailer.getName());
        final String key= trailer.getKey();
        Glide.with(mContext).load(trailer.getThumbnail()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedSingle(key);
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedSingle(key);
            }
        });

    }

    public void clickedSingle(String key){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(key));
        intent.setPackage("com.google.android.youtube");
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }




}
