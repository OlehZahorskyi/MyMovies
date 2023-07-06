package com.example.mymovies.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.ViewModel;
import com.example.mymovies.adapters.MovieAdapter;
import com.example.mymovies.pojo.FavouriteMovie;
import com.example.mymovies.pojo.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMoviesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private MovieAdapter adapter;
    private ViewModel viewModel;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem favouriteItem = menu.findItem(R.id.itemFavourites);
        favouriteItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemHome) {
            Intent intentToMain = new Intent(this, MainActivity.class);
            startActivity(intentToMain);
        } else if (id == R.id.itemFavourites) {
            Intent intentToFavourites = new Intent(this, FavouriteMoviesActivity.class);
            startActivity(intentToFavourites);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Favourites");
        recyclerView = findViewById(R.id.recyclerViewFavouriteMovies);
        recyclerView.setLayoutManager(new GridLayoutManager(FavouriteMoviesActivity.this, 2));
        adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        LiveData<List<FavouriteMovie>> favouriteMovies = viewModel.getFavouriteMovies();
        favouriteMovies.observe(this, fM -> {
            if (fM != null) {
                List<Movie> movies = new ArrayList<>(fM);
                adapter.setMovies(movies);
            }
        });
        adapter.setOnPosterClickListener(position -> {
            Movie movie = adapter.getMovies().get(position);
            int id = movie.getId();
            Intent intent = new Intent(FavouriteMoviesActivity.this, MovieDetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

    }
}