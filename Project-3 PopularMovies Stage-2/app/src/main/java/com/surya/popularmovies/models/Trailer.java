package com.surya.popularmovies.models;

public class Trailer {

    public Trailer(String name, String key, String thumbnail) {
        this.name = name;
        this.key = key;
        this.thumbnail=thumbnail;
    }

    String name;
    String key;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    String thumbnail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
