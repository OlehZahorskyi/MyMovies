package com.example.mymovies.pojo;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int uniqueId;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("popularity")
    @Expose
    private double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("smallPosterPath")
    @Expose
    private String smallPosterPath;
    @SerializedName("bigPosterPath")
    @Expose
    private String bigPosterPath;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("video")
    @Expose
    private boolean video;
    @SerializedName("vote_average")
    @Expose
    private double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private int voteCount;

    public Movie(int uniqueId, int id, String overview, double popularity, String posterPath, String smallPosterPath, String bigPosterPath, String releaseDate, String title, boolean video, double voteAverage, int voteCount) {
        this.uniqueId = uniqueId;
        this.id = id;
        this.overview = overview;
        this.popularity = popularity;
        this.smallPosterPath = smallPosterPath;
        this.posterPath = posterPath;
        this.bigPosterPath = bigPosterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    @Ignore
    public Movie(int id, String overview, double popularity, String posterPath, String smallPosterPath, String bigPosterPath, String releaseDate, String title, boolean video, double voteAverage, int voteCount) {
        this.id = id;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.smallPosterPath = smallPosterPath;
        this.bigPosterPath = bigPosterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public String getSmallPosterPath() {
        return smallPosterPath;
    }

    public void setSmallPosterPath(String smallPosterPath) {
        this.smallPosterPath = smallPosterPath;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public int getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getBigPosterPath() {
        return bigPosterPath;
    }

    public void setBigPosterPath(String bigPosterPath) {
        this.bigPosterPath = bigPosterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }
}
