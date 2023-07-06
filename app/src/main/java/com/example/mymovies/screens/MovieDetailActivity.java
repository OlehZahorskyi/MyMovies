package com.example.mymovies.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.ViewModel;
import com.example.mymovies.adapters.ReviewAdapter;
import com.example.mymovies.adapters.TrailerAdapter;
import com.example.mymovies.pojo.Movie;
import com.example.mymovies.pojo.ReviewsProperties;
import com.example.mymovies.pojo.VideosProperties;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView imageViewBigPoster;
    private TextView textViewOriginalTitle;
    private TextView textViewYear;
    private TextView textViewPopularity;
    private TextView textViewOverview;
    private ImageView imageViewFavouriteStar;
    private RecyclerView recyclerViewReviews;
    private RecyclerView recyclerViewTrailers;

    private Movie movie;
    private int id;

    private ViewModel viewModel;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.itemHome) {
            Intent intentToMain = new Intent(MovieDetailActivity.this, MainActivity.class);
            startActivity(intentToMain);
        } else if (id == R.id.itemFavourites) {
            Intent intentToFavourite = new Intent(MovieDetailActivity.this, FavouriteMoviesActivity.class);
            startActivity(intentToFavourite);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewYear = findViewById(R.id.textViewYear);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewOverview = findViewById(R.id.textViewOverview);
        imageViewFavouriteStar = findViewById(R.id.imageViewFavouriteStar);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        reviewAdapter = new ReviewAdapter();
        trailerAdapter = new TrailerAdapter();
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewAdapter);
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setAdapter(trailerAdapter);
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);
        } else {
            finish();
        }
        viewModel.getMovieById(id, m -> {
            this.movie = m;
            Picasso.get().load(movie.getBigPosterPath()).into(imageViewBigPoster);
            textViewOriginalTitle.setText(movie.getTitle());
            textViewYear.setText(movie.getReleaseDate());
            int userScore = (int) (movie.getVoteAverage() * 10);
            textViewPopularity.setText(String.format(Locale.getDefault(), "%s %% User Score", userScore));
            textViewOverview.setText(m.getOverview());
            checkFavourite(false, movie);
            actionBar.setTitle(movie.getTitle());
            viewModel.loadTrailers(id);
            viewModel.loadReviews(id);
        }, throwable -> {
            Log.i("MYRESULT", throwable.getMessage());
        });
        viewModel.getIsFavourite().observe(this, isFavourite -> {
            if (isFavourite) {
                imageViewFavouriteStar.setImageDrawable(AppCompatResources.getDrawable(MovieDetailActivity.this, R.drawable.star_filled));
            } else {
                imageViewFavouriteStar.setImageDrawable(AppCompatResources.getDrawable(MovieDetailActivity.this, R.drawable.star_empty));
            }
        });
        viewModel.getVideoProperties().observe(this, new Observer<List<VideosProperties>>() {
            @Override
            public void onChanged(List<VideosProperties> videosProperties) {
                trailerAdapter.setTrailers(videosProperties);
            }
        });
        viewModel.getReviewProperties().observe(this, new Observer<List<ReviewsProperties>>() {
            @Override
            public void onChanged(List<ReviewsProperties> reviewsProperties) {
                reviewAdapter.setReviewsProperties(reviewsProperties);
            }
        });
        trailerAdapter.setOnTrailerClickListener(new TrailerAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(String url) {
                Intent intentToTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentToTrailer);
            }
        });
    }

    public void onClickChangeFavourite(View view) {
        checkFavourite(true, movie);
    }

    private void checkFavourite(Boolean changedFavourite, Movie movie) {
        viewModel.getFavouriteMovieById(id, changedFavourite, movie);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clearDisposables();
    }
}