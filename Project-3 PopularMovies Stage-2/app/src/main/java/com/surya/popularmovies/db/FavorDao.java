package com.surya.popularmovies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavorDao {

    @Insert
    public  void  addfav(Favorites favorites);

    @Delete
    public void removefav(Favorites favorites);

    @Query("SELECT * FROM favorites")
    public LiveData<List<Favorites>> readfav();

    @Query("SELECT id FROM favorites WHERE id= :favorId")
    public  String readid(String favorId);
}