package com.academy.android.starwarsmovies.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = { StarWarsMovie.class }, version = 1) public abstract class AppDatabase
    extends RoomDatabase {
  public abstract StarWarsMovieDao starWarsMovieModel();
}
