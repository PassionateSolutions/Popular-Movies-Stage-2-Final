package com.example.android.popular_movies_stage_1.data;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popular_movies_stage_1.Movie;

import java.util.List;

// Got Guidance and Direction from @maluta [AND Graduate, Los Angeles, CA] from Slack!!

public class FavoritesViewModel extends AndroidViewModel {


    private LiveData<List<Movie>> favorites;


    public FavoritesViewModel(@NonNull Application application) {
        super(application);

        MovieDb database = FavoritesDbSingle.getInstance(this.getApplication());
        favorites = database.movieDao().getAllFavorites();
    }


    public LiveData<List<Movie>> getFavorites() {
        return favorites;
    }
}