package com.surya.popularmovies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.surya.popularmovies.R;
import com.surya.popularmovies.models.Review;

import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {


    private Context mContext;
    private List<Review> reviewList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView author, content;

        MyViewHolder(View view) {
            super(view);
            author = view.findViewById(R.id.review_author);
            content = view.findViewById(R.id.review_content);
        }
    }


    public ReviewAdapter(Context mContext, List<Review> movieList) {
        this.mContext = mContext;
        this.reviewList = movieList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_cardview, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Review review = reviewList.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }




}
