package com.surya.dailynews.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.surya.dailynews.R;
import com.surya.dailynews.adapter.NewsApadter;
import com.surya.dailynews.db.News;
import com.surya.dailynews.db.NewsViewModel;
import com.surya.dailynews.model.NewsModel;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    NewsApadter newsAdapter;
    public static ArrayList<NewsModel> newsList;
    private static RecyclerView newsRecyclerview;
    String tag = "SuryaTeja";
    RecyclerView.LayoutManager mLayoutManager;
    Bundle savedInstanceState;
    static NewsViewModel newsViewModel;
    public List<News> newDBList;
    ProgressBar progressBar;
    boolean isFavoriteClicked = false, isHead = false, isWall = false, isBitcoin = false, isTechCrunch = false;
    InterstitialAd mInterstitialAd;
    public static boolean isAddShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        admobADS();
        this.savedInstanceState = savedInstanceState;
        Log.d(tag, "In the onCreate() event");
        progressBar = findViewById(R.id.main_progressbar);
        setNavigation();
        newsSetRecyclerView();
    }


    public void newsSetRecyclerView() {

        newsRecyclerview = findViewById(R.id.news_recyclerview);
        if (savedInstanceState == null) {
            newsList = new ArrayList<>();
        }
        newsAdapter = new NewsApadter(this, newsList);
        mLayoutManager = new LinearLayoutManager(this);
        newsRecyclerview.setLayoutManager(mLayoutManager);
        newsRecyclerview.setAdapter(newsAdapter);
        if (savedInstanceState != null) {
            newsAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            newsRecyclerview.setVisibility(View.VISIBLE);
        } else {
            if (!isNetworkAvailable()) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.a9))
                        .setMessage(getString(R.string.a10))
                        .setPositiveButton(getString(R.string.a11), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                newsSetRecyclerView();
                            }
                        })
                        .setNegativeButton(getString(R.string.a12), null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {

                new NewsAsyncTask().execute(getString(R.string.top_bussiness_headlines));
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class NewsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            try {
                progressBar.setVisibility(View.VISIBLE);
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
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
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.i("SuryaTeja jsonobj", jsonObject.getString("status"));
                    JSONArray articlesObj = jsonObject.getJSONArray("articles");
                    newsList.clear();
                    int x = articlesObj.length() <= 20 ? articlesObj.length() : 20;
                    for (int i = 0; i < x; i++) {
                        JSONObject ingObject = articlesObj.getJSONObject(i);
                        JSONObject sourceObject = ingObject.getJSONObject("source");
                        String sourceId = sourceObject.getString("id");
                        String sourceName = sourceObject.getString("name");
                        Log.i("SuryaTeja jsonobj", sourceObject.getString("name"));
                        String author = ingObject.getString("author");
                        String title = ingObject.getString("title");
                        String description = ingObject.getString("description");
                        String url = ingObject.getString("url");
                        String imageLink = ingObject.getString("urlToImage");
                        String publishedAt = ingObject.getString("publishedAt");
                        String content = ingObject.getString("content");

                        newsList.add(new NewsModel(sourceId, sourceName, author, title, description, url, imageLink, publishedAt, content));
                    }
                    newsAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    newsRecyclerview.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        progressBar.setVisibility(View.VISIBLE);
        newsRecyclerview.setVisibility(View.GONE);
        isFavoriteClicked = false;
        isHead = false;
        isBitcoin = false;
        isTechCrunch = false;
        isWall = false;
        selectNav(id);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void selectNav(final int id) {


        if (!isNetworkAvailable()) {

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.a9))
                    .setMessage(getString(R.string.a10))
                    .setPositiveButton(getString(R.string.a11), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            selectNav(id);
                        }
                    })
                    .setNegativeButton(getString(R.string.a12), null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {

            if (id == R.id.nav_headlinesus || isHead) {
                new NewsAsyncTask().execute(getString(R.string.top_bussiness_headlines));
                setTitle(getString(R.string.a17));
                isFavoriteClicked = false;
                isHead = true;
                isBitcoin = false;
                isTechCrunch = false;
                isWall = false;

            } else if (id == R.id.nav_techcrunch || isTechCrunch) {
                new NewsAsyncTask().execute(getString(R.string.top_tech_crunch));
                setTitle(getString(R.string.a18));
                isFavoriteClicked = false;
                isTechCrunch = true;
                isHead = false;
                isBitcoin = false;
                isWall = false;

            } else if (id == R.id.nav_wallstreet || isWall) {
                new NewsAsyncTask().execute(getString(R.string.top_wall_street));
                setTitle(getString(R.string.a19));
                isFavoriteClicked = false;
                isHead = false;
                isBitcoin = false;
                isTechCrunch = false;
                isWall = true;

            } else if (id == R.id.nav_bitcoin || isBitcoin) {
                new NewsAsyncTask().execute(getString(R.string.top_bitcoin));
                setTitle(getString(R.string.a20));
                isFavoriteClicked = false;
                isHead = false;
                isBitcoin = true;
                isTechCrunch = false;
                isWall = false;

            } else if (id == R.id.nav_favorite || isFavoriteClicked) {
                isFavoriteClicked = true;
                isHead = false;
                isTechCrunch = false;
                isBitcoin = false;
                isWall = false;
                favorite();
            } else if (id == R.id.nav_logout) {
                logOut();
            }
        }
    }

    public void logOut() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_main), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.shared_mobile), "0");
        editor.putString(getString(R.string.shared_password), "0");
        editor.apply();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();

    }

    public void favorite() {

        final int[] x = new int[1];
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        newsViewModel.getGetAllNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newsDList) {
                newDBList = newsDList;
                assert newsDList != null;
                x[0] = newsDList.size();
                if (isFavoriteClicked) {
                    favoriteCon2(x);
                }
            }
        });
    }


    public void favoriteCon2(int[] x) {


        if (x[0] == 0) {
            Toast.makeText(this, getString(R.string.a5), Toast.LENGTH_LONG).show();
        } else {
            newsList.clear();
            setTitle(getString(R.string.a21));
            for (int i = 0; i < x[0]; i++) {
                newsList.add(new NewsModel(newDBList.get(i).getSourceId(), newDBList.get(i).getSourceName(), newDBList.get(i).getAuthor(),
                        newDBList.get(i).getTitle(), newDBList.get(i).getDescription(), newDBList.get(i).getUrl(),
                        newDBList.get(i).getImageLink(), newDBList.get(i).getPublishedat(), newDBList.get(i).getContent()));
            }
            newsRecyclerview.getRecycledViewPool().clear();
            newsAdapter.notifyDataSetChanged();
        }
    }

    public void setNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void admobADS() {
        if (!isAddShown) {
            MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {

                    mInterstitialAd.show();
                    isAddShown = true;
                }
            });
        }
    }

    public void onStart() {
        super.onStart();
        Log.d(tag, "In the onStart() event");
    }

    public void onRestart() {
        super.onRestart();
        Log.d(tag, "In the onRestart() event");
    }

    public void onResume() {
        super.onResume();
        Log.d(tag, "In the onResume() event");
    }

    public void onPause() {
        super.onPause();
        Log.d(tag, "In the onPause() event");
    }

    public void onStop() {
        super.onStop();
        Log.d(tag, "In the onStop() event");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "In the onDestroy() event");
    }
}
