package com.surya.dailynews.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert
    public  void  addnew(News news);

    @Delete
    public void removenew(News news);

    @Query("SELECT * FROM news")
    public LiveData<List<News>> readnews();

    @Query("SELECT id FROM news WHERE id= :newsId")
    public  String readid(String newsId);
}
