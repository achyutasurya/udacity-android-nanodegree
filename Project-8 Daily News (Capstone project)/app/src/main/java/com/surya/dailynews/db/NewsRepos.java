package com.surya.dailynews.db;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class NewsRepos {
    public static NewsDao newsDao;

    private LiveData<List<News>> getallData;
    public static String newsId;

    public NewsRepos(Context context) {
        NewsDB favoriteDB = NewsDB.getInstance(context);
        newsDao = favoriteDB.newsDao();
        getallData = newsDao.readnews();

    }

    LiveData<List<News>> getGetallnews() {
        return getallData;
    }

    public void addfav(News news) {
        new InserTask().execute(news);
    }

    public String readid(String id) {
        new ReadIdTask().execute(id);
        return newsId;
    }

    public void removefav(News news) {
        new RemoveTask().execute(news);
    }

    public class InserTask extends AsyncTask<News, Void, Void> {

        @Override
        protected Void doInBackground(News... news) {
            newsDao.addnew(news[0]);
            return null;
        }
    }

    public class RemoveTask extends AsyncTask<News, Void, Void> {

        @Override
        protected Void doInBackground(News... news) {
            newsDao.removenew(news[0]);
            return null;
        }
    }

    public class ReadIdTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            newsId = newsDao.readid(strings[0]);
            return newsId;
        }
    }
}
