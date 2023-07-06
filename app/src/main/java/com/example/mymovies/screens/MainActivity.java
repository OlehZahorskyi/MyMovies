package com.example.mymovies.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.ViewModel;
import com.example.mymovies.adapters.MovieAdapter;
import com.example.mymovies.pojo.Movie;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Switch switchSort;
    private TextView textViewTrending;
    private TextView textViewTopRated;
    private ImageView imageViewMenu;

    private ViewModel viewModel;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        recyclerView = findViewById(R.id.recyclerViewMovies);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        textViewTrending = findViewById(R.id.textViewTrending);
        switchSort = findViewById(R.id.switchSort);
        imageViewMenu = findViewById(R.id.imageViewMenuMain);
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);
        viewModel.getMovies().observe(this, movies -> adapter.setMovies(movies));
        switchSort.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                textViewTopRated.setTextColor(getColor(android.R.color.holo_red_light));
                textViewTrending.setTextColor(getColor(R.color.white));
            } else {
                textViewTrending.setTextColor(getColor(android.R.color.holo_red_light));
                textViewTopRated.setTextColor(getColor(R.color.white));
            }
            viewModel.whenChangedSortMethod(b);
        });
        adapter.setOnReachEndListener(position -> viewModel.loadAllInfo(switchSort.isChecked()));
        adapter.setOnPosterClickListener(position -> {
            Movie movie = adapter.getMovies().get(position);
            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
            intent.putExtra("id", movie.getId());
            startActivity(intent);
        });
        viewModel.loadAllInfo(switchSort.isChecked());
    }

    public void onClickChangeSort(View view) {
        switchSort.setChecked(view.getId() == textViewTopRated.getId());
    }

    public void onClickShowMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.main_menu);
        popupMenu.getMenu().removeItem(R.id.itemHome);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.itemFavourites) {
                Intent intentToFavourites = new Intent(MainActivity.this, FavouriteMoviesActivity.class);
                startActivity(intentToFavourites);
            }
            return true;
        });
    }

}
