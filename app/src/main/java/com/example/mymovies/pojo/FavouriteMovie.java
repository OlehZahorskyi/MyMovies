package com.example.mymovies.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie {
    public FavouriteMovie(int uniqueId, int id, String overview, double popularity, String posterPath, String smallPosterPath, String bigPosterPath, String releaseDate, String title, boolean video, double voteAverage, int voteCount) {
        super(uniqueId, id, overview, popularity, posterPath, smallPosterPath, bigPosterPath, releaseDate, title, video, voteAverage, voteCount);
    }

    @Ignore
    public FavouriteMovie(Movie movie) {
        super(movie.getUniqueId(), movie.getId(), movie.getOverview(), movie.getPopularity(), movie.getPosterPath(), movie.getSmallPosterPath(), movie.getBigPosterPath(), movie.getReleaseDate(), movie.getTitle(), movie.isVideo(), movie.getVoteAverage(), movie.getVoteCount());
    }
}