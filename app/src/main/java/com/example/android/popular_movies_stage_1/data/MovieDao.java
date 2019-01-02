package com.example.android.popular_movies_stage_1.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.example.android.popular_movies_stage_1.Movie;

import java.util.List;

// Got Guidance and Direction from @maluta [AND Graduate, Los Angeles, CA] from Slack!!
@Dao
public interface MovieDao {

    @Query("SELECT * FROM favorites")
    LiveData<List<Movie>> getAllFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}