package com.surya.dailynews.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "news")
public class News {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "source_id")
    public String sourceId;
    @ColumnInfo(name="source_name")
    public String sourceName;
    @ColumnInfo(name="author")
    public String author;
    @ColumnInfo(name="title")
    public String title;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPublishedat() {
        return publishedat;
    }

    public void setPublishedat(String publishedat) {
        this.publishedat = publishedat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ColumnInfo(name="description")
    public String description;
    @ColumnInfo(name="url")
    public String url;
    @ColumnInfo(name="image_link")
    public String imageLink;
    @ColumnInfo(name="publishedat")
    public String publishedat;


    public News(){

    }

    @ColumnInfo(name="content")
    public String content;






}
