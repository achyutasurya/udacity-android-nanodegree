package com.surya.dailynews.ui;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.surya.dailynews.R;
import com.surya.dailynews.db.News;
import com.surya.dailynews.db.NewsDB;
import com.surya.dailynews.db.NewsViewModel;
import com.surya.dailynews.widget.NewsWidget;

import java.util.Locale;

public class SingleNewsActivity extends AppCompatActivity {

    TextView titleTV, sourceTV, authorTV, descTV, publishedTV;
    ImageView newsImageView, soundImageView, muteImageView;


    String id = null;
    String source = null;
    String author = null;
    String url = null;
    String image = null;
    String desc = null;
    String content = null;
    String published = null;
    String title = null;
    String mainDesc = null;
    boolean isNewsFav;

    private TextToSpeech textToSpeech;
    NewsDB favoriteDatabase;
    NewsViewModel favViewModel;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty() || str.equals("null") || str.equals("Null");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news);
        isNewsFav = false;


        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.a1), Toast.LENGTH_SHORT).show();
                }
            }
        });


        favoriteDatabase = Room.databaseBuilder(this, NewsDB.class, "News").allowMainThreadQueries().build();

        favViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        soundImageView = findViewById(R.id.sound);
        muteImageView = findViewById(R.id.mute);
        soundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int speechStatus = textToSpeech.speak(mainDesc, TextToSpeech.QUEUE_FLUSH, null);
                if (speechStatus == TextToSpeech.ERROR) {
                    Toast.makeText(SingleNewsActivity.this,getString(R.string.error_text_speech),Toast.LENGTH_SHORT).show();
                }

            }
        });

        muteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textToSpeech != null) {
                    textToSpeech.stop();
                }
            }
        });


        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        source = intent.getStringExtra("source");
        author = intent.getStringExtra("author");
        url = intent.getStringExtra("url");
        image = intent.getStringExtra("image");
        desc = intent.getStringExtra("desc");
        content = intent.getStringExtra("content");
        published = intent.getStringExtra("published");
        title = intent.getStringExtra("title");
        mainDesc = null;

        String test;
        test = favoriteDatabase.newsDao().readid(title);

        if (test != null) {
            ImageView b = findViewById(R.id.facbutton);
            b.setBackgroundResource(R.drawable.in);
            isNewsFav = true;
        }

        newsImageView = findViewById(R.id.image_single);
        titleTV = findViewById(R.id.title_news_single);
        sourceTV = findViewById(R.id.source);
        authorTV = findViewById(R.id.author);
        descTV = findViewById(R.id.desc);
        publishedTV = findViewById(R.id.published);


        if (isNullOrEmpty(source)) {
            source = "Source: Not Found";
        } else {
            source = "Source: " + source;
        }

        if (isNullOrEmpty(author)) {
            author = "Author: Not Found";
        } else {
            author = "Author: " + author;
        }

        if (isNullOrEmpty(published)) {
            published = "Date of Published: Not Found";
        } else {
            published = "published on: " + published;
        }
        if (!isNullOrEmpty(desc) || !desc.equals("null")) {
            Log.i("-----------SuryaTeja", desc);
            mainDesc = desc;
        }

        if (!isNullOrEmpty(content)) {

            content = content.replaceAll("\n", " ");
            content = content.replaceAll("\r", "");
            content = content.replaceAll("&amp;", "&");
            mainDesc = mainDesc + "\n\n      " + content;
        }

        if (!isNullOrEmpty(image)) {
            Glide.with(this).load(image)
                    .apply(new RequestOptions().fitCenter().override(600, 200))
                    .into(newsImageView);
        }
        titleTV.setText(title);
        sourceTV.setText(source);
        authorTV.setText(author);
        descTV.setText(mainDesc);
        publishedTV.setText(published);
        setIngWidget(title);

    }

    public void addFav(View view) {
        News favorites = new News();

        if (!isNewsFav) {
            try {
                favorites.setId(title);
                favorites.setAuthor(author);
                favorites.setDescription(desc);
                favorites.setContent(content);
                favorites.setImageLink(image);
                favorites.setPublishedat(published);
                favorites.setTitle(title);
                favorites.setUrl(url);
                favorites.setSourceName(source);
                favViewModel.addnews(favorites);
                isNewsFav = true;
                Toast.makeText(this, getString(R.string.a2), Toast.LENGTH_SHORT).show();
                ImageView b = findViewById(R.id.facbutton);
                b.setBackgroundResource(R.drawable.in);


            } catch (SQLiteConstraintException e) {

                Toast.makeText(this, getString(R.string.a3), Toast.LENGTH_SHORT).show();

            }

        } else {
            favorites.setId(title);
            favViewModel.removenews(favorites);
            Toast.makeText(this, getString(R.string.a4), Toast.LENGTH_SHORT).show();
            isNewsFav = false;
            ImageView b = findViewById(R.id.facbutton);
            b.setBackgroundResource(R.drawable.out);

        }
    }


    public void share(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title + "\n\n   " + source + " \n\n   " + mainDesc + "\n \n " + url);
        sendIntent.setType("text/plain");
        String text = "Unable to Share";
        if (view.getId() == R.id.share_fb) {
            sendIntent.setPackage("com.facebook.orca");
            text = "Facebook Not Installed";
        } else if (view.getId() == R.id.share_whatsapp) {
            text = "Whatsapp Not Installed";
            sendIntent.setPackage("com.whatsapp");
        } else if (view.getId() == R.id.share_insta) {
            text = "Instagram Not Installed";
            sendIntent.setPackage("com.instagram.android");
        }
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendIntent);
        } else {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    public void setIngWidget(String title) {

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(getString(R.string.shared_fav_main), 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(getString(R.string.share_fav_desc), title + "\n\n   " + source + " \n\n   " + mainDesc);
        edit.apply();

        Intent intent = new Intent(this, NewsWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(
                new ComponentName(getApplication(), NewsWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }
}
