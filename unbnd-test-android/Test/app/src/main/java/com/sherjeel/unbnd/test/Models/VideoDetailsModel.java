package com.sherjeel.unbnd.test.Models;

public class VideoDetailsModel {
    private int id;
    private String title, thumbnail, url;

    public VideoDetailsModel(int id, String title, String thumbnail, String url) {
        this.id = id;
        this.title = title.trim();
        this.thumbnail = thumbnail.trim();
        this.url = url.trim();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getUrl() {
        return url;
    }

}
