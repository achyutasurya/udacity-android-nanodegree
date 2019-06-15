package com.surya.bakingapp.models;

public class StepsModel {

    int id;
    String shortDesc;
    String desc;

    public StepsModel(int id, String shortDesc, String desc, String videoLink, String thumbnailLink) {
        this.id = id;
        this.shortDesc = shortDesc;
        this.desc = desc;
        this.videoLink = videoLink;
        this.thumbnailLink = thumbnailLink;
    }

    String videoLink;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    String thumbnailLink;
}
