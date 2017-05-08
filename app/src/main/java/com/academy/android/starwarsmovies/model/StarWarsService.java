package com.academy.android.starwarsmovies.model;

import android.app.IntentService;
import android.content.Intent;
import com.academy.android.starwarsmovies.ResourcesUtil;
import com.academy.android.starwarsmovies.StarWarsApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import javax.inject.Inject;

public class StarWarsService extends IntentService {

  @Inject AppDatabase db;

  public StarWarsService() {
    super("StarWarsService");
  }

  @Override public void onCreate() {
    super.onCreate();
    ((StarWarsApplication) getApplication()).getAppComponent().inject(this);
  }

  @Override protected void onHandleIntent(Intent intent) {

    if (intent != null) {
      String json = ResourcesUtil.loadJson(this);
      Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy").create();
      ArrayList<StarWarsMovie> moviesList =
          gson.fromJson(json, new TypeToken<ArrayList<StarWarsMovie>>() {
          }.getType());

      db.starWarsMovieModel().deleteAll();
      for (StarWarsMovie starWarsMovie : moviesList) {
        db.starWarsMovieModel().insertMovie(starWarsMovie);
      }
    }
  }
}
