package com.surya.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SingleMovieActivity extends AppCompatActivity {

    ImageView poster, thumbnail;
    TextView name, rating, date, overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);


        name=findViewById(R.id.name_single);
        rating=findViewById(R.id.rating_single);
        date=findViewById(R.id.release_date_single);
        overview=findViewById(R.id.overview);
        poster=findViewById(R.id.poster_view);
        thumbnail=findViewById(R.id.thumbnail);

        String[] movie=getIntent().getStringArrayExtra("moviedetails");
        name.setText(movie[0]);
        rating.setText( movie[3]);
        overview.setText(movie[2]);
        date.setText(movie[5]);
        String firstImage=getString(R.string.base_poster_url)+"w500/"+movie[4];
        String secondImage=getString(R.string.base_poster_url)+"w200/"+movie[1];

        Glide.with(this).load(firstImage).into(poster);
        Glide.with(this).load(secondImage).into(thumbnail);

    }
}
