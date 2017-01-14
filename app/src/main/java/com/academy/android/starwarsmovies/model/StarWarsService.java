package com.academy.android.starwarsmovies.model;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import com.academy.android.starwarsmovies.ImageLoader;
import com.academy.android.starwarsmovies.ResourcesUtil;
import com.academy.android.starwarsmovies.presenter.MainPresenter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StarWarsService extends IntentService {
  public StarWarsService() {
    super("StarWarsService");
  }

  @Override protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      String json = ResourcesUtil.loadJson(this);
      ArrayList<StarWarsMovie> moviesList = null;
      try {
        JSONArray array = new JSONArray(json);
        moviesList = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
          JSONObject jsonObject = array.getJSONObject(i);
          String name = jsonObject.getString("name");
          String description = jsonObject.getString("description");
          String imageUrl = jsonObject.getString("imageUrl");
          String releaseDate = jsonObject.getString("releaseDate");

          SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
          Date date = format.parse(releaseDate);
          ImageLoader imgLoader = new ImageLoader(getApplicationContext());
          Bitmap bitmap = imgLoader.displayImage(imageUrl);

          moviesList.add(new StarWarsMovie(name, description, bitmap, date));
        }
      } catch (JSONException | ParseException e) {
        e.printStackTrace();
      }

      Intent response = new Intent();
      response.setAction(MainPresenter.LOAD_COMPLETE_ACTION);
      response.putExtra(MainPresenter.DATA_KEY, moviesList);
      LocalBroadcastManager.getInstance(this).sendBroadcast(response);
    }
  }
}
