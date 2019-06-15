package com.surya.dailynews.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = News.class, version = 1, exportSchema = false)
public abstract class NewsDB extends RoomDatabase {

    public abstract NewsDao newsDao();

    public static NewsDB inst;

    public static NewsDB getInstance(Context context) {
        if (inst == null) {
            inst = Room.databaseBuilder(context, NewsDB.class, "News")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return inst;
    }
}
