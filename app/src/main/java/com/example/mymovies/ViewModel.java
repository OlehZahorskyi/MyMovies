package com.example.mymovies;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mymovies.api.ApiFactory;
import com.example.mymovies.api.ApiService;
import com.example.mymovies.data.MovieDatabase;
import com.example.mymovies.pojo.DiscoverMovieResponse;
import com.example.mymovies.pojo.FavouriteMovie;
import com.example.mymovies.pojo.Movie;
import com.example.mymovies.pojo.ReviewsProperties;
import com.example.mymovies.pojo.ReviewsResponse;
import com.example.mymovies.pojo.VideosProperties;
import com.example.mymovies.pojo.VideosResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ViewModel extends AndroidViewModel {

    private static MovieDatabase database;
    private LiveData<List<Movie>> movies;
    private LiveData<List<FavouriteMovie>> favouriteMovies;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int page;
    private MutableLiveData<Boolean> isFavourite;
    private MutableLiveData<List<VideosProperties>> videoProperties;
    private MutableLiveData<List<ReviewsProperties>> reviewProperties;

    public MutableLiveData<List<VideosProperties>> getVideoProperties() {
        return videoProperties;
    }

    public MutableLiveData<List<ReviewsProperties>> getReviewProperties() {
        return reviewProperties;
    }

    public MutableLiveData<Boolean> getIsFavourite() {
        return isFavourite;
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<List<FavouriteMovie>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public ViewModel(@NonNull Application application) {
        super(application);
        page = 1;
        database = MovieDatabase.getInstance(application);
        movies = database.movieDao().getAllMovies();
        favouriteMovies = database.movieDao().getAllFavouriteMovies();
        isFavourite = new MutableLiveData<>();
        videoProperties = new MutableLiveData<>();
        reviewProperties = new MutableLiveData<>();
    }

    public void loadAllInfo(Boolean b) {
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        Disposable disposable = apiService.getMovies(API_KEY_VALUE,
                        LANGUAGE_VALUE,
                        methodOfSort(b),
                        Integer.toString(page),
                        voteCount(b))
                .map(DiscoverMovieResponse::getResults)
                .subscribeOn(Schedulers.io())
                .subscribe(movies -> {
                    makePosterUrl(movies);
                    if (page == 1) {
                        database.movieDao().deleteMovies();
                    }
                    database.movieDao().insertMovies(movies);
                    page++;
                }, throwable -> Toast.makeText(getApplication(), "Error", Toast.LENGTH_SHORT).show());
        compositeDisposable.add(disposable);
    }

    public void loadTrailers(int id) {
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        Disposable disposable = apiService.getTrailers(id, API_KEY_VALUE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread() )
                .map(VideosResponse::getVideosProperties)
                .flatMap(videosProperties -> Observable.fromIterable(videosProperties)
                        .filter(videosProperties1 -> videosProperties1.isOfficial())
                        .toList())
                .subscribe(properties -> {
                    makeTrailerUrl(properties);
                    videoProperties.setValue(properties);
                    }, throwable -> Log.i("MYRESULT", "trailers " + throwable.getMessage()));
        compositeDisposable.add(disposable);
    }

    public void loadReviews(int id) {
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        Disposable disposable = apiService.getReviews(id, API_KEY_VALUE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ReviewsResponse::getReviewsProperties)
                .subscribe(properties -> reviewProperties.setValue(properties)
                        , throwable -> Log.i("MYRESULT", "reviews " + throwable.getMessage()));
        compositeDisposable.add(disposable);
    }

    public void getMovieById(int id, Consumer<Movie> onSuccess, Consumer<Throwable> onError) {
        Disposable disposable = database.movieDao().getMovieById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError);
        compositeDisposable.add(disposable);
    }

    public void insertFavouriteMovie(FavouriteMovie favouriteMovie) {
        Completable.fromAction(() -> database.movieDao().insertFavouriteMovie(favouriteMovie))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteFavouriteMovie(FavouriteMovie favouriteMovie) {
        Completable.fromAction(() -> database.movieDao().deleteFavouriteMovie(favouriteMovie))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void getFavouriteMovieById(int id, Boolean changedFavourite, Movie movie) {
        Disposable disposable = database.movieDao().getFavouriteMovieById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        favouriteMovie -> {
                            if (changedFavourite) {
                                deleteFavouriteMovie(favouriteMovie);
                                isFavourite.setValue(false);
                            } else {
                                isFavourite.setValue(true);
                            }
                        },
                        throwable -> {
                            if (changedFavourite) {
                                insertFavouriteMovie(new FavouriteMovie(movie));
                                isFavourite.setValue(true);
                            } else {
                                isFavourite.setValue(false);
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    private static final String API_KEY_VALUE = "29dcdc2702ebef8323c0b063fb963578";
    private static final String LANGUAGE_VALUE = "en-US";
    private static final String SORT_BY_POPULARITY_DESC_VALUE = "popularity.desc";
    private static final String SORT_BY_RATING_DESC_VALUE = "vote_average.desc";
    private static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private static final String SMALL_POSTER_SIZE = "w185";
    private static final String BIG_POSTER_SIZE = "w500";
    private static final String TRAILER_PATH = "https://www.youtube.com/watch?v=";

    public void whenChangedSortMethod(Boolean b) {
        page = 1;
        loadAllInfo(b);
    }

    private void makeTrailerUrl(List<VideosProperties> videosProperties) {
        for (VideosProperties properties : videosProperties) {
            properties.setTrailerPath(TRAILER_PATH + properties.getKey());
        }
    }

    private void makePosterUrl(List<Movie> movies) {
        for (Movie movie : movies) {
            movie.setSmallPosterPath(BASE_POSTER_URL + SMALL_POSTER_SIZE + movie.getPosterPath());
            movie.setBigPosterPath(BASE_POSTER_URL + BIG_POSTER_SIZE + movie.getPosterPath());
        }
    }

    private String methodOfSort(Boolean b) {
        if (b) {
            return SORT_BY_RATING_DESC_VALUE;
        } else {
            return SORT_BY_POPULARITY_DESC_VALUE;
        }
    }

    private int voteCount(Boolean b) {
        if (b) {
            return 10000;
        } else {
            return 0;
        }
    }

    public void clearDisposables() {
        compositeDisposable.clear();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        clearDisposables();
    }
}

