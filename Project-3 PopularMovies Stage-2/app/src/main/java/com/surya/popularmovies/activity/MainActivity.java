package com.surya.popularmovies.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import com.surya.popularmovies.R;
import com.surya.popularmovies.adapters.MovieAdapter;
import com.surya.popularmovies.db.FavViewModel;
import com.surya.popularmovies.db.Favorites;
import com.surya.popularmovies.models.Movie;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public MovieAdapter adapter;
    static List<Movie> moviesList;
    private ProgressBar progressBar;
    boolean isLastFavorite = false;
    public String scrollPosition = "position";
    Bundle savedInstanceState;
    GridLayoutManager mLayoutManager;
    static boolean isFavoriteClicked = false, isTopRatedClicked = false, isPopularClicked = false;

    @Override
    protected void onStart() {


        if (isFavoriteClicked) {
            favoriteCon();
        }
        super.onStart();
    }


    public void setRecyclerView() {
        int spanCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4;
        }

        moviesList = new ArrayList<>();
        adapter = new MovieAdapter(this, moviesList);
        mLayoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(mLayoutManager);
        Resources r = getResources();
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics())), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void setRecyclerViewTwo() {
        int spanCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4;
        }

        adapter = new MovieAdapter(this, moviesList);
        mLayoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(mLayoutManager);
        Resources r = getResources();
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics())), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(scrollPosition, mLayoutManager.findFirstCompletelyVisibleItemPosition());
        SharedPreferences.Editor editor = getSharedPreferences("Surya", MODE_PRIVATE).edit();
        editor.putInt("position",  mLayoutManager.findFirstCompletelyVisibleItemPosition());
        editor.apply();
    }

    public void checkNow() {
        if (!isNetworkAvailable()) {
            alertDialogMain();
        } else {
            if (savedInstanceState!=null && savedInstanceState.containsKey("position")) {
                setRecyclerViewTwo();
                mLayoutManager.scrollToPosition(savedInstanceState.getInt("position"));
                adapter.notifyDataSetChanged();
            } else {

                if (isFavoriteClicked) {
                    setRecyclerViewTwo();
                    favoriteCon();
                } else if (isTopRatedClicked || isPopularClicked) {
                    setRecyclerViewTwo();
                    if (isTopRatedClicked) {
                        setTitle("Top Rated Movies");
                    } else {
                        setTitle("Popular Movies");
                    }
                    recyclerView.setAdapter(new MovieAdapter(MainActivity.this, moviesList));
                    adapter.notifyDataSetChanged();
                } else {
                    setRecyclerView();
                    String movieurl = getString(R.string.movie_main_url) + getString(R.string.api_key);
                    isPopularClicked = true;
                    new MoviesTask().execute(movieurl);
                }
            }

        }
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.savedInstanceState = savedInstanceState;

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);

        checkNow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String movieurl;
        switch (item.getItemId()) {
            case R.id.toprated_movie_url:
                moviesList.clear();
                movieurl = getString(R.string.top_rate_url) + getString(R.string.api_key);
                if (isNetworkAvailable()) {
                    new MoviesTask().execute(movieurl);
                    setTitle("Top Rated Movies");
                } else {
                    if (isLastFavorite) {
                        alertDialog();
                        isLastFavorite = false;
                    } else {
                        alertDialogMain();
                    }
                }
                isTopRatedClicked = true;
                isFavoriteClicked = false;
                isPopularClicked = false;
                break;
            case R.id.popular_movie_url:
                moviesList.clear();
                movieurl = getString(R.string.popular_url) + "&" + getString(R.string.api_key);
                if (isNetworkAvailable()) {
                    new MoviesTask().execute(movieurl);
                    setTitle("Popular Movies");
                } else {
                    if (isLastFavorite) {
                        alertDialog();
                        isLastFavorite = false;
                    } else {
                        alertDialogMain();
                    }
                }
                isTopRatedClicked = false;
                isPopularClicked = true;
                isFavoriteClicked = false;
                break;

            case R.id.favorite_movie_url:
                isTopRatedClicked = false;
                isPopularClicked = false;
                isFavoriteClicked = true;
                favoriteCon();

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    static FavViewModel favViewModel;

    static List<Favorites> favoritesList;

    public void favoriteCon() {
        isLastFavorite = true;
        moviesList.clear();
        final int[] x = new int[1];
        favViewModel = ViewModelProviders.of(this).get(FavViewModel.class);
        favViewModel.getGetAllFav().observe(this, new Observer<List<Favorites>>() {
            @Override
            public void onChanged(@Nullable List<Favorites> favorites) {
                favoritesList = favorites;
                assert favorites != null;
                x[0] = favorites.size();
                if (isFavoriteClicked) {
                    favoriteCon2(x);
                }
            }
        });
    }

    public void favoriteCon2(int[] x) {

        moviesList.clear();
        if (x[0] == 0) {
            Toast.makeText(this, "No Favorite to Show", Toast.LENGTH_LONG).show();
        }

        setTitle("Favorite Movies");
        for (int i = 0; i < x[0]; i++) {
            moviesList.add(new Movie(favoritesList.get(i).getDate(), favoritesList.get(i).getThumbnail(), favoritesList.get(i).getDes(),
                    favoritesList.get(i).getName(), favoritesList.get(i).getId(), 0, 0, favoritesList.get(i).getRating(),
                    favoritesList.get(i).getPoster()));
        }
        recyclerView.getRecycledViewPool().clear();
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    class MoviesTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
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
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject movieinfo = jsonArray.getJSONObject(i);
                        String id = movieinfo.getString("id");
                        String movietitle = movieinfo.getString("title");
                        String path = movieinfo.getString("poster_path");
                        String overview = movieinfo.getString("overview");
                        String rating = String.valueOf(movieinfo.getDouble("vote_average"));
                        String thumnail = movieinfo.getString("backdrop_path");
                        String relesedate = movieinfo.getString("release_date");
                        moviesList.add(new Movie(relesedate, path, overview, movietitle, id, 0, 0, rating, thumnail));
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(MainActivity.this, "No Movies", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }


    public void alertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet")
                .setMessage("Please check your Internet Connection?")
                .setPositiveButton("Check Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checkNow();
                    }
                })
                .setNegativeButton("Close", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void alertDialogMain() {
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
                        finish();
                    }
                })
                .setNeutralButton("Favorites", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setRecyclerView();
                        isTopRatedClicked = false;
                        isPopularClicked = false;
                        isFavoriteClicked = true;
                        favoriteCon();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
