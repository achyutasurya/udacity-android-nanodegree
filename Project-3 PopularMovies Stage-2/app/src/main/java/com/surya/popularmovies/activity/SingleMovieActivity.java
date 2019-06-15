package com.surya.popularmovies.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteConstraintException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.surya.popularmovies.R;
import com.surya.popularmovies.adapters.ReviewAdapter;
import com.surya.popularmovies.adapters.TrailerAdapter;
import com.surya.popularmovies.db.FavViewModel;
import com.surya.popularmovies.db.FavoriteDB;
import com.surya.popularmovies.db.Favorites;
import com.surya.popularmovies.models.Review;
import com.surya.popularmovies.models.Trailer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SingleMovieActivity extends AppCompatActivity {

    ImageView poster, thumbnail;
    TextView name, rating, date, overview, noReviewTextView, noInternetReview, noTrailerTextView, noInternetTrailer;

    public RecyclerView reviewRecyclerView, trailerRecyclerView;
    public ReviewAdapter reviewAadapter;
    public TrailerAdapter trailerAdapter;
    public List<Review> reviewList;
    public List<Trailer> trailerList;
    boolean isMovieInFav = false;

    String nameT, idT, overviewT, dateT, posterPathT, ratingT, posT;

    FavoriteDB favoriteDatabase;
    FavViewModel favViewModel;

    public void addFav(View view){
        Favorites favorites = new Favorites();

        if (!isMovieInFav) {
            try {

                favorites.setId(idT);
                favorites.setDate(dateT);
                favorites.setDes(overviewT);
                favorites.setName(nameT);
                favorites.setPoster(posterPathT);
                favorites.setThumbnail(posT);
                favorites.setRating(ratingT);
                favViewModel.addfav(favorites);
                isMovieInFav = true;
                Toast.makeText(this, "Added to Favorite",Toast.LENGTH_SHORT).show();
                ImageView b = findViewById(R.id.facbutton);
                b.setBackgroundResource(R.drawable.in);


            } catch (SQLiteConstraintException e) {

                Toast.makeText(SingleMovieActivity.this, "the movie is already added as Favorite", Toast.LENGTH_SHORT).show();

            }

        } else {
            favorites.setId(idT);
            favViewModel.removefav(favorites);
            Toast.makeText(SingleMovieActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            isMovieInFav = false;
            Toast.makeText(this, "Removed from Favorite",Toast.LENGTH_SHORT).show();
            ImageView b = findViewById(R.id.facbutton);
            b.setBackgroundResource(R.drawable.out);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        favoriteDatabase = Room.databaseBuilder(this, FavoriteDB.class, "Favorites").allowMainThreadQueries().build();


        favViewModel = ViewModelProviders.of(this).get(FavViewModel.class);


        name = findViewById(R.id.name_single);
        rating = findViewById(R.id.rating_single);
        date = findViewById(R.id.release_date_single);
        overview = findViewById(R.id.overview);
        poster = findViewById(R.id.poster_view);
        thumbnail = findViewById(R.id.thumbnail);
        noReviewTextView = findViewById(R.id.no_review);
        noInternetReview = findViewById(R.id.no_internet_review);
        noInternetTrailer = findViewById(R.id.no_internet_trailer);
        noTrailerTextView = findViewById(R.id.no_trailer);

        String[] movie = getIntent().getStringArrayExtra("moviedetails");
        nameT = movie[0];
        ratingT = movie[3];
        overviewT = movie[2];
        dateT = movie[5];
        idT = movie[6];
        posterPathT = movie[4];
        posT = movie[1];

        String test;
        test = favoriteDatabase.favDao().readid(idT);

        if (test != null) {
            ImageView b = findViewById(R.id.facbutton);
            b.setBackgroundResource(R.drawable.in);
            isMovieInFav = true;
        }


        name.setText(nameT);
        rating.setText(ratingT);
        overview.setText(overviewT);
        date.setText(dateT);


        String firstImage = getString(R.string.base_poster_url) + "w500/" + posterPathT;
        String secondImage = getString(R.string.base_poster_url) + "w200/" + posT;

        Glide.with(this).load(firstImage).into(poster);
        Glide.with(this).load(secondImage).into(thumbnail);

        checkNow();
    }


    @SuppressLint("StaticFieldLeak")
    class TrailerTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            trailerRecyclerView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                Scanner s = new Scanner(inputStream);
                s.useDelimiter("\\A");
                if (s.hasNext()) {
                    return s.next();
                } else {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            trailerRecyclerView.setVisibility(View.VISIBLE);
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.optJSONArray("results");

                    if (jsonArray.length() == 0) {
                        noTrailerTextView.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject reviewinfo = jsonArray.optJSONObject(i);
                        String key = reviewinfo.optString("key");
                        String thumbnail = "https://img.youtube.com/vi/" + key + "/0.jpg";
                        key = "https://www.youtube.com/watch?v=" + key;
                        String name = reviewinfo.optString("name");
                        trailerList.add(new Trailer(name, key, thumbnail));
                    }
                    trailerAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(SingleMovieActivity.this, "No Trailer", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @SuppressLint("StaticFieldLeak")
    class ReviewTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reviewRecyclerView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                Scanner s = new Scanner(inputStream);
                s.useDelimiter("\\A");
                if (s.hasNext()) {
                    return s.next();
                } else {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            reviewRecyclerView.setVisibility(View.VISIBLE);
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.optJSONArray("results");

                    if (jsonArray.length() == 0) {
                        noReviewTextView.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject reviewinfo = jsonArray.optJSONObject(i);
                        String author = reviewinfo.optString("author");
                        String content = reviewinfo.optString("content");
                        reviewList.add(new Review(author, content));
                    }
                    reviewAadapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(SingleMovieActivity.this, "No Reviews", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void checkNow() {
        if (!isNetworkAvailable()) {
            new AlertDialog.Builder(this)
                    .setTitle("No Internet")
                    .setMessage("Please check your Internet Connection?")
                    .setPositiveButton("Check Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkNow();
                        }
                    })
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            noInternetReview.setVisibility(View.VISIBLE);
                            noInternetTrailer.setVisibility(View.VISIBLE);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            reviewRecyclerView = findViewById(R.id.review_recyclerview);
            reviewList = new ArrayList<>();
            reviewAadapter = new ReviewAdapter(this, reviewList);
            RecyclerView.LayoutManager mReviewLayoutManager = new GridLayoutManager(this, 1);
            reviewRecyclerView.setLayoutManager(mReviewLayoutManager);
            reviewRecyclerView.setAdapter(reviewAadapter);


            int spanCount = 2;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                spanCount = 4;
            }

            trailerRecyclerView = findViewById(R.id.trailer_recyclerview);
            trailerList = new ArrayList<>();
            trailerAdapter = new TrailerAdapter(this, trailerList);
            RecyclerView.LayoutManager mTrailerLayoutManager = new GridLayoutManager(this, spanCount);
            trailerRecyclerView.setLayoutManager(mTrailerLayoutManager);
            trailerRecyclerView.setAdapter(trailerAdapter);

            String reviewurl = getString(R.string.baseurl) + idT + getString(R.string.reviews) + getString(R.string.api_key);
            String trailersurl = getString(R.string.baseurl) + idT + getString(R.string.trailers) + getString(R.string.api_key);
            new ReviewTask().execute(reviewurl);
            new TrailerTask().execute(trailersurl);
        }
    }
}
