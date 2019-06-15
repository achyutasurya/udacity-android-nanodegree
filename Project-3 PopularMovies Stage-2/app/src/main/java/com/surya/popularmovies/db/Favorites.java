package com.surya.popularmovies.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorites")
public class Favorites {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "poster")
    public String poster;
    @ColumnInfo(name="name")
    public String name;
    @ColumnInfo(name="rating")
    public String rating;
    @ColumnInfo(name="rel_date")
    public String relDate;
    @ColumnInfo(name="des")
    public String des;
    @ColumnInfo(name="thumbnail")
    public String thumbnail;

    public Favorites(@NonNull String id, String poster, String name, String rating, String relDate, String des, String thumbnail) {

        this.id = id;
        this.poster = poster;
        this.name = name;
        this.rating = rating;
        this.relDate = relDate;
        this.des = des;
        this.thumbnail = thumbnail;
    }

    public Favorites() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return relDate;
    }

    public void setDate(String date) {
        this.relDate = date;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
