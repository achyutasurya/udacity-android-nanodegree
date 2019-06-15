package com.surya.popularmovies.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class FavViewModel extends AndroidViewModel {
    public FavRepos favoriteRepositary;
    public LiveData<List<Favorites>> getAllFav;
    public String favid;

    public FavViewModel(@NonNull Application application) {
        super(application);
        favoriteRepositary=new FavRepos(application);
        getAllFav=favoriteRepositary.getGetallfav();
    }
    public String readid(String id){

        favid=favoriteRepositary.readid(id);
        return favid;
    }
    public void addfav(Favorites favorites){
        favoriteRepositary.addfav(favorites);
    }
    public void removefav(Favorites favorites){
        favoriteRepositary.removefav(favorites);
    }
    public LiveData<List<Favorites>> getGetAllFav(){
        return getAllFav;
    }

}

