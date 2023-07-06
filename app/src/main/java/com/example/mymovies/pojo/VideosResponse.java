package com.example.mymovies.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<VideosProperties> videosProperties;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<VideosProperties> getVideosProperties() {
        return videosProperties;
    }

    public void setVideosProperties(List<VideosProperties> videosProperties) {
        this.videosProperties = videosProperties;
    }
}
