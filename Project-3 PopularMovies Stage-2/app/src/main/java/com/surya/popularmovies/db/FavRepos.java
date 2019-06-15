package com.surya.popularmovies.db;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class FavRepos {
    public static FavorDao favorDao;

    LiveData<List<Favorites>> getallData;
    public static String favorId;

    public FavRepos(Context context) {
        FavoriteDB favoriteDB = FavoriteDB.getInstance(context);
        favorDao = favoriteDB.favDao();
        getallData = favorDao.readfav();

    }

    LiveData<List<Favorites>> getGetallfav() {
        return getallData;
    }

    public void addfav(Favorites favorites) {
        new InserTask().execute(favorites);
    }

    public String readid(String id) {
        new ReadIdTask().execute(id);
        return favorId;
    }

    public void removefav(Favorites favorites) {
        new RemoveTask().execute(favorites);
    }

    public class InserTask extends AsyncTask<Favorites, Void, Void> {

        @Override
        protected Void doInBackground(Favorites... favorites) {
            favorDao.addfav(favorites[0]);
            return null;
        }
    }

    public class RemoveTask extends AsyncTask<Favorites, Void, Void> {

        @Override
        protected Void doInBackground(Favorites... favorites) {
            favorDao.removefav(favorites[0]);
            return null;
        }
    }

    public class ReadIdTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            favorId = favorDao.readid(strings[0]);
            return favorId;
        }
    }
}
