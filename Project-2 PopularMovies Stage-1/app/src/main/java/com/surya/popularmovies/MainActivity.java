package com.surya.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> moviesList;
    private ProgressBar progressBar;
    public static boolean isPopular = true, isTopRated = false;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {

        Log.i("SuryaTeja out", isTopRated + "  " + isPopular);
        if (!isNetworkAvailable()) {
            new AlertDialog.Builder(this)
                    .setTitle("No Internet")
                    .setMessage("Please check your Internet Connection?")
                    .setPositiveButton("Check Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            onResume();
                        }
                    })
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        super.onResume();
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
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void checkNow() {
        if (isNetworkAvailable()) {
            int spanCount = 2;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                spanCount = 4;
            }
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, spanCount);
            recyclerView.setLayoutManager(mLayoutManager);
            Resources r = getResources();
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics())), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            String movieurl;
            if (isPopular) {
                movieurl = getString(R.string.movie_main_url) + getString(R.string.api_key);
                setTitle("Popular Movies");
            } else {

                movieurl = getString(R.string.top_rate_url) + getString(R.string.api_key);
                setTitle("Top Rated Movies");
            }
            new MoviesTask().execute(movieurl);
        } else {
            alertDialogMain();
        }


        Log.i("SuryaTeja checknow", isTopRated + "  " + isPopular);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        moviesList = new ArrayList<>();
        adapter = new MovieAdapter(this, moviesList);


        checkNow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void topRatedMenu() {
        if (isNetworkAvailable()) {
            moviesList.clear();
            isTopRated = true;
            isPopular = false;

            setTitle("Top Rated Movies");
            String movieurl = getString(R.string.top_rate_url) + getString(R.string.api_key);
            new MoviesTask().execute(movieurl);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("No Internet")
                    .setMessage("Please check your Internet Connection?")
                    .setPositiveButton("Check Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            topRatedMenu();
                        }
                    })
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        Log.i("SuryaTeja topRatedMenu", isTopRated + "  " + isPopular);
    }

    public void popularMenu() {
        if (isNetworkAvailable()) {
            moviesList.clear();
            isPopular = true;
            setTitle("Popular Movies");
            isTopRated = false;
            String movieurl = getString(R.string.popular_url) + "&" + getString(R.string.api_key);
            new MoviesTask().execute(movieurl);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("No Internet")
                    .setMessage("Please check your Internet Connection?")
                    .setPositiveButton("Check Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            popularMenu();
                        }
                    })
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        Log.i("SuryaTeja popularMenu", isTopRated + "  " + isPopular);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toprated_movie_url:

                topRatedMenu();
                break;
            case R.id.popular_movie_url:
                popularMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                urlCon.connect();
                InputStream inStr = urlCon.getInputStream();
                Scanner s = new Scanner(inStr);
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
                        Double rating = movieinfo.getDouble("vote_average");
                        String thumnail = movieinfo.getString("backdrop_path");
                        String relesedate = movieinfo.getString("release_date");
                        Log.i("MoviesName", movietitle);
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
}
