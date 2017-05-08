package com.academy.android.starwarsmovies.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StarWarsMovieDao {

  @Query("select * from StarWarsMovie") LiveData<List<StarWarsMovie>> loadMovies();

  @Query("DELETE FROM StarWarsMovie") void deleteAll();

  @Insert(onConflict = REPLACE) void insertMovie(StarWarsMovie starWarsMovie);
}
