package com.example.android.popular_movies_stage_1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.android.popular_movies_stage_1.data.FavoritesViewModel;

import java.util.ArrayList;
import java.util.List;


// Followed the walkthrough of @Gill AND from Slack.  His video on Youtube helped me follow the direction
// to implement the working code.

// Updated Code highly inspired and guided by watching Jeriel NG Udacity's Guide
// on Youtube!

// Got help from @Aaron Quaday From Slack!
// Got Guidance and Direction from @maluta [AND Graduate, Los Angeles, CA] from Slack!!


public class MainActivity extends AppCompatActivity implements MainActivityInterface {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String sortBy;
    private MovieAdapter movieAdapter;
    private RecyclerView movieGrid;
    private GridLayoutManager layoutManager;

    private TextView errorMessage;
    private ProgressBar loadingIndicator;

    private static final String SORT_BY_MOST_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;
    private static final String SORT_BY_HIGHEST_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;
    private static final String SORT_BY_FAVORITES = "";

    private List<Movie> mFavoriteMovies;
    final String FAVORITE_TYPE = "favorite";
    private String movieType = "normal type";
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private static final String SCROLL_POSITION = "SCROLL_POSITION";


    // Stored data for the favorites

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieGrid = findViewById(R.id.grid_view);
        layoutManager = new GridLayoutManager(this, 2);
        movieGrid.setLayoutManager(layoutManager);
        movieAdapter = new MovieAdapter(this);
        movieGrid.setAdapter(movieAdapter);
        errorMessage = findViewById(R.id.empty_view);
        loadingIndicator = findViewById(R.id.loading_indicator);
        sortBy = "http://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;
    
        getMovies();
        getFavoritesDisplayed();
    }


    public String getSortBy() {return sortBy;}

    public MovieAdapter getMovieAdapter() {return movieAdapter;}

    public TextView getErrorMessage() {return errorMessage;}

    public ProgressBar getLoadingIndicator() {return loadingIndicator;}

    public RecyclerView getMovieGrid() {return movieGrid;}

    public void startDetailActivity (Movie movie) {
        Intent intent = new Intent(this, Details.class);
        boolean isFavorite = compareToFavorite(movie);
        intent.putExtra("movie", movie);
        intent.putExtra("isFavorite", isFavorite);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_most_popular) {
            sortBy = SORT_BY_MOST_POPULAR;
            getMovies();
            return true;
        }

        if (id == R.id.sort_highest_rated) {
            sortBy = SORT_BY_HIGHEST_RATED;
            getMovies();
            return true;
        }

        if (id == R.id.sort_favorites) {
            sortBy = SORT_BY_FAVORITES;
            movieType = FAVORITE_TYPE;
            getFavoritesDisplayed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

        private void getFavoritesDisplayed() {

            // GET LIST FROM MOVIE (Room Database)

                FavoritesViewModel viewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
                viewModel.getFavorites().observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(@Nullable List<Movie> movies) {
                        mMovies = (ArrayList<Movie>) movies;
                        mFavoriteMovies = movies;
                        if (movieType.equals(FAVORITE_TYPE)) {
                            Log.d(LOG_TAG, "Updating list of favorite movies from LiveData in ViewModel");
                            movieAdapter.setMovies(mMovies);
                            movieGrid.setAdapter(movieAdapter);
                        }
                    }
                });
            }


    // CHECK TO SEE IF MOVIE IS IN FAVORITES
    private boolean compareToFavorite(Movie movie) {
        if (mFavoriteMovies != null) {
            for (int i = 0; i < mFavoriteMovies.size(); i++) {
                if (movie.getID().equals(mFavoriteMovies.get(i).getID())) {
                    return true;
                }
            }
        }
        return false;
    }


    private void getMovies() {
        movieGrid.setVisibility(View.INVISIBLE);
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();

            if (info != null && info.isConnectedOrConnecting()){
                errorMessage.setVisibility(View.INVISIBLE);
                loadingIndicator.setVisibility(View.VISIBLE);
                new GetMoviesTask().execute(this);
            } else {
                errorMessage.setVisibility(View.VISIBLE);
                loadingIndicator.setVisibility(View.INVISIBLE);
            }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_POSITION, ((LinearLayoutManager)movieGrid.getLayoutManager()).findFirstVisibleItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int position = savedInstanceState.getInt(SCROLL_POSITION);
        movieGrid.postDelayed(new Runnable() {
            public void run() {
                movieGrid.scrollToPosition(position);
            }
        }, 300);
    }
}