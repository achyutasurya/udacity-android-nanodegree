package com.surya.dailynews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.surya.dailynews.R;
import com.surya.dailynews.model.NewsModel;
import com.surya.dailynews.ui.SingleNewsActivity;

import java.util.ArrayList;

public class NewsApadter extends RecyclerView.Adapter<NewsApadter.NewsViewHolder> {
    private Context context;
    private ArrayList<NewsModel> newsList;

    public NewsApadter(Context context, ArrayList<NewsModel> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.news_cardview, viewGroup, false);
        return new NewsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder newsViewHolder, int i) {
        String title = newsList.get(i).getTitle();
        newsViewHolder.titleTV.setText(title);

        String source = "Source: " + newsList.get(i).getSourceName();
        newsViewHolder.sourceTV.setText(source);

        String image = newsList.get(i).getImageLink();
        Glide.with(context).load(image)
                .apply(new RequestOptions().fitCenter().override(600, 200))
                .error(R.drawable.imagenotfound)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        newsViewHolder.imageView.setVisibility(View.VISIBLE);
                        newsViewHolder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        newsViewHolder.progressBar.setVisibility(View.GONE);
                        newsViewHolder.imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(newsViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV, sourceTV;
        ImageView imageView;
        ProgressBar progressBar;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.news_title_tv);
            sourceTV = itemView.findViewById(R.id.new_source_tv);
            imageView = itemView.findViewById(R.id.new_imageview);
            progressBar = itemView.findViewById(R.id.new_progressbar);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClicked();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClicked();
                }
            });
        }

        void onClicked(){

            int position = getAdapterPosition();
            Intent intent = new Intent(context, SingleNewsActivity.class);
            intent.putExtra("id", newsList.get(position).getSourceId());
            intent.putExtra("source", newsList.get(position).getSourceId());
            intent.putExtra("author", newsList.get(position).getAuthor());
            intent.putExtra("url", newsList.get(position).getUrl());
            intent.putExtra("title", newsList.get(position).getTitle());
            intent.putExtra("image", newsList.get(position).getImageLink());
            intent.putExtra("desc", newsList.get(position).getDescription());
            intent.putExtra("content", newsList.get(position).getContent());
            intent.putExtra("published", newsList.get(position).getPublishedAt());
            context.startActivity(intent);
        }
    }
}






