package com.surya.bakingapp.activity;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.surya.bakingapp.R;
import com.surya.bakingapp.adapter.IngredientsAdapter;
import com.surya.bakingapp.adapter.StepsAdapter;
import com.surya.bakingapp.models.IngredientsModel;
import com.surya.bakingapp.models.StepsModel;
import com.surya.bakingapp.widgets.ItemWidget;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ItemsActivity extends AppCompatActivity {

    ArrayList<IngredientsModel> ingList;
    IngredientsAdapter ingAdapter;
    StepsAdapter stepsAdapter;
    public static ArrayList<StepsModel> stepsList;
    RecyclerView ingRecyclerview, stepsRecyclerview;
    ProgressBar ingProgressBar, stepProgressBar;
    int position;
    String jsonLinkUrl;
    Boolean isTab = false;


    public void checkNow(){
        if(isNetworkAvailable()){
            ingSetRecyclerView();
            stepsSetRecyclerView();
        }else{

            alertDialogMain();
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        jsonLinkUrl = getString(R.string.jsonurl);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        setTitle(intent.getStringExtra("name"));

        ingProgressBar = findViewById(R.id.ing_progres_bar);
        stepProgressBar = findViewById(R.id.steps_progres_bar);

        if (findViewById(R.id.item_detail_container) != null) {
            isTab = true;
        }
        checkNow();

    }


    public void setIngWidget() {

        SharedPreferences sharedPreferences;
        StringBuilder stringBuilder = new StringBuilder();
        int ingPosition = 1;
        for (int i = 0; i < ingList.size(); i++) {
            stringBuilder.append("").append(ingPosition).append(". ").append(ingList.get(i).getName()).append("\n");
            ingPosition++;
        }
        String ingAll = stringBuilder.toString();
        sharedPreferences = getSharedPreferences("com.surya.batking", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("TotalIng", ingAll);
        edit.putString("RecipeName", getIntent().getStringExtra("name"));
        edit.apply();

        Intent intent = new Intent(this, ItemWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(
                new ComponentName(getApplication(), ItemWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }


    public void stepsSetRecyclerView() {

        stepsRecyclerview = findViewById(R.id.steps_recycler_view);
        stepsList = new ArrayList<>();
        stepsAdapter = new StepsAdapter(this, stepsList, isTab);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        stepsRecyclerview.setLayoutManager(mLayoutManager);
        stepsRecyclerview.setAdapter(stepsAdapter);
        new StepsAsyncTask().execute();
    }


    public void ingSetRecyclerView() {
        ingRecyclerview = findViewById(R.id.ingredients_recycler_view);
        ingList = new ArrayList<>();
        ingAdapter = new IngredientsAdapter(this, ingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        ingRecyclerview.setLayoutManager(mLayoutManager);
        ingRecyclerview.setAdapter(ingAdapter);
        new IngredientsAsyncTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class StepsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(jsonLinkUrl);
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

                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = jsonArray.getJSONObject(position);
                    JSONArray stepJsonArray = jsonObject.getJSONArray("steps");
                    for (int i = 0; i < stepJsonArray.length(); i++) {
                        JSONObject stepObject = stepJsonArray.getJSONObject(i);
                        int id = stepObject.getInt("id");
                        String desc = stepObject.getString("description");
                        String shortDesc = stepObject.getString("shortDescription");
                        String thumnailLink = stepObject.getString("thumbnailURL");
                        String videoLink = stepObject.getString("videoURL");
                        stepsList.add(new StepsModel(id, shortDesc, desc, videoLink, thumnailLink));
                    }

                    stepsAdapter.notifyDataSetChanged();
                    stepProgressBar.setVisibility(View.GONE);
                    stepsRecyclerview.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class IngredientsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(jsonLinkUrl);
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

                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = jsonArray.getJSONObject(position);
                    JSONArray ingJsonArray = jsonObject.getJSONArray("ingredients");
                    for (int i = 0; i < ingJsonArray.length(); i++) {
                        JSONObject ingObject = ingJsonArray.getJSONObject(i);
                        String name = ingObject.getString("ingredient");
                        String quantity = ingObject.getString("quantity");
                        String measure = ingObject.getString("measure");
                        ingList.add(new IngredientsModel(name, quantity, measure));
                    }
                    ingAdapter.notifyDataSetChanged();
                    ingProgressBar.setVisibility(View.GONE);
                    ingRecyclerview.setVisibility(View.VISIBLE);
                    setIngWidget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
