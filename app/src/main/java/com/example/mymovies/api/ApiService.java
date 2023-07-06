package com.example.mymovies.api;

import com.example.mymovies.pojo.DiscoverMovieResponse;
import com.example.mymovies.pojo.ReviewsResponse;
import com.example.mymovies.pojo.VideosResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    String API_KEY_QUERY = "api_key";
    String LANGUAGE_QUERY = "language";
    String SORT_QUERY = "sort_by";
    String PAGE_QUERY = "page";
    String VOTE_COUNT = "vote_count.gte";

    @GET("discover/movie")
    Single<DiscoverMovieResponse> getMovies(@Query(API_KEY_QUERY) String apiKey,
                                            @Query(LANGUAGE_QUERY) String language,
                                            @Query(SORT_QUERY) String sortBy,
                                            @Query(PAGE_QUERY) String page,
                                            @Query(VOTE_COUNT) int voteCount);

    @GET("movie/{id}/videos")
    Single<VideosResponse> getTrailers(@Path("id") int id,
                                       @Query(API_KEY_QUERY) String apiKey);

    @GET("movie/{id}/reviews")
    Single<ReviewsResponse> getReviews(@Path("id") int id,
                                       @Query(API_KEY_QUERY) String apiKey);

}