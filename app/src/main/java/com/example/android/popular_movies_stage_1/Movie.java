package com.example.android.popular_movies_stage_1;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.popular_movies_stage_1.data.FavoritesRoomObject;

// Followed the walkthrough of @Gill AND from Slack.  His video on Youtube helped me follow the direction
// to implement the working code.

// Got Guidance and Direction from @maluta [AND Graduate, Los Angeles, CA] from Slack!!

@Entity(tableName = "favorites")
public class Movie implements Parcelable {


    @ColumnInfo(name = "release_date")
     private String date;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "vote_average")
    private String vote;
    @ColumnInfo(name = "overview")
    private String overview;
    @ColumnInfo(name = "poster_path")
    private String poster;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id;

    public Movie (String date, String title, String vote, String overview, String poster, String id) {
        this.date = date;
        this.title = title;
        this.vote = vote;
        this.overview = overview;
        this.poster = poster;
        this.id = id;
    }

    private Movie(Parcel source){
        this.date = source.readString();
        this.title = source.readString();
        this.vote = source.readString();
        this.overview = source.readString();
        this.poster = source.readString();
        this.id = source.readString();
    }

    // Returns Date
    public String getDate() {return date;}

    // Returns Title
    public String getTitle() {return title;}

    // Returns Vote
    public String getVote() { return vote; }

    // Returns Overview
    public String getOverview() {return overview;}

    // Returns Poster
    public String getPoster() {return poster;}

    // Returns ID
    public String getID() {return id;}

    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel (Parcel dest, int flags){
        dest.writeString(date);
        dest.writeString(title);
        dest.writeString(vote);
        dest.writeString(overview);
        dest.writeString(poster);
        dest.writeString(id);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel (Parcel source){return new Movie(source);}

        @Override
        public Movie[] newArray(int size) {return new Movie[size]; }
    };

}