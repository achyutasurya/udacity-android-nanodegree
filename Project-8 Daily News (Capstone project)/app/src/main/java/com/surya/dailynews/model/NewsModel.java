package com.surya.dailynews.model;

public class NewsModel {

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

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NewsModel(String sourceId, String sourceName, String author, String title, String description,
                     String url, String imageLink, String publishedAt, String content) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageLink = imageLink;
        this.publishedAt = publishedAt;
        this.content = content;

    }

    String sourceId, sourceName, author, title, description, url, imageLink, publishedAt, content;


}
