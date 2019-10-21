package com.academy.android.starwarsmovies.model;

import android.app.IntentService;
import android.content.Intent;
import com.academy.android.starwarsmovies.ResourcesUtil;
import com.academy.android.starwarsmovies.presenter.MainPresenter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class StarWarsService extends IntentService {
  public StarWarsService() {
    super("StarWarsService");
  }

  @Override protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      String json = ResourcesUtil.loadJson(this);
      Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy").create();
      ArrayList<StarWarsMovie> moviesList =
          gson.fromJson(json, new TypeToken<ArrayList<StarWarsMovie>>() {
          }.getType());

      Intent response = new Intent();
      response.setAction(MainPresenter.LOAD_COMPLETE_ACTION);
      response.putExtra(MainPresenter.DATA_KEY, moviesList);
      LocalBroadcastManager.getInstance(this).sendBroadcast(response);
    }
  }
}
