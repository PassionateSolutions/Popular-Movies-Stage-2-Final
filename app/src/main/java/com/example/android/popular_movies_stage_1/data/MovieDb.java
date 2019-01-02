package com.example.android.popular_movies_stage_1.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.example.android.popular_movies_stage_1.Movie;

// Got Guidance and Direction from @maluta [AND Graduate, Los Angeles, CA] from Slack!!
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDb extends RoomDatabase {

    private static final String LOG_TAG = MovieDb.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favoritemovies";
    private static MovieDb sInstance;


    public static MovieDb getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDb.class, MovieDb.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();
}