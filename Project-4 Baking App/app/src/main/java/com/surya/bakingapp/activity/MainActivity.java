package com.surya.bakingapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;
import com.surya.bakingapp.R;
import com.surya.bakingapp.adapter.ItemAdapter;
import com.surya.bakingapp.models.ItemModel;
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

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static RecyclerView itemRecyclerView;
    public static ArrayList<ItemModel> items;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private ProgressBar itemProgressBar;

    public void checkNow(){
        if(isNetworkAvailable()){
            new ItemAsyncTask().execute();
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
        setContentView(R.layout.activity_main);

        itemRecyclerView =findViewById(R.id.items_list_recyclerview);
        itemProgressBar = findViewById(R.id.items_progres_bar);
        items=new ArrayList<>();
        context=this;

        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemRecyclerView.setAdapter(new ItemAdapter(MainActivity.this,items));
        checkNow();

    }


    @SuppressLint("StaticFieldLeak")
    public class ItemAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                String link=MainActivity.context.getString(R.string.jsonurl);
                URL url=new URL(link);

                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream=connection.getInputStream();
                Scanner s=new Scanner(inputStream);
                s.useDelimiter("\\A");
                if(s.hasNext()){
                    return s.next();
                }
                else
                {
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
            if(s!=null){

                try {
                    JSONArray jsonArray=new JSONArray(s);
                    items.clear();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject itemJsonObject=jsonArray.getJSONObject(i);
                        String id=itemJsonObject.getString("id");
                        String name=itemJsonObject.getString("name");
                        MainActivity.items.add(new ItemModel(id,name));

                    }

                    itemRecyclerView.setAdapter(new ItemAdapter(context,items));
                    itemProgressBar.setVisibility(View.GONE);
                    itemRecyclerView.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                Toast.makeText(context, "Something went Wrong please try again", Toast.LENGTH_SHORT).show();

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
