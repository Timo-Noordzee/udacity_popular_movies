package com.northseadevs.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.northseadevs.popularmovies.movie.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies WHERE 1")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT EXISTS(SELECT * FROM movies WHERE id = :id ORDER BY id DESC)")
    int contains(int id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

}
