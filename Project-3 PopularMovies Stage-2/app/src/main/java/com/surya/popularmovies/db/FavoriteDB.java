package com.surya.popularmovies.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = Favorites.class,version = 1,exportSchema = true)
public abstract class FavoriteDB extends RoomDatabase {

    public abstract FavorDao favDao();

    public static FavoriteDB inst;

    public static FavoriteDB getInstance(Context context){
        if(inst ==null){
            inst =Room.databaseBuilder(context,FavoriteDB.class,"Favorites")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return inst;
    }
}