package com.surya.popularmovies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.surya.popularmovies.R;
import com.surya.popularmovies.activity.MainActivity;
import com.surya.popularmovies.activity.SingleMovieActivity;
import com.surya.popularmovies.models.Movie;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {


    private Context mContext;
    private List<Movie> movieList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, releaseDate;
        ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            thumbnail = view.findViewById(R.id.thumbnail);
            releaseDate = view.findViewById(R.id.release_date);
        }
    }


    public MovieAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_cardview, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Movie movie = movieList.get(position);
        holder.title.setText(movie.getName());
        holder.releaseDate.setText(movie.getReleaseDate());
        Glide.with(mContext).load("http://image.tmdb.org/t/p/w500/"+movie.getPosterPath()).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedSingle(position);
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedSingle(position);
            }
        });

        holder.releaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedSingle(position);
            }
        });

    }

    public void clickedSingle(int position){
        String[] movieDetails=new String[7];
        movieDetails[0]=movieList.get(position).getName();
        movieDetails[1]=movieList.get(position).getPosterPath();
        movieDetails[2]=movieList.get(position).getOverview();
        movieDetails[3]= String.valueOf(movieList.get(position).getRating());
        movieDetails[4]=movieList.get(position).getPos();
        movieDetails[5]=movieList.get(position).getReleaseDate();
        movieDetails[6]=movieList.get(position).getId();
        Intent i=new Intent(mContext, SingleMovieActivity.class);
        i.putExtra("moviedetails",movieDetails);
        mContext.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }




}
