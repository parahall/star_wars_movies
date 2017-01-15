package com.academy.android.starwarsmovies.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.academy.android.starwarsmovies.R;
import com.academy.android.starwarsmovies.model.StarWarsMovie;
import com.academy.android.starwarsmovies.model.StarWarsService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainPresenter extends BasePresenter<MainActivityView> {
  public static final String LOAD_COMPLETE_ACTION =
      "com.academy.android.starwarsmovies.load_complete";
  public static final String DATA_KEY = "data_key";
  private StarWarsMovieDataReceiver movieDataReceiver;
  private ArrayList<StarWarsMovie> movieList;

  @Override public void attachView(MainActivityView mainActivityView) {
    super.attachView(mainActivityView);
    registerLoadDataReceiver();

    if (isCacheAvailable()) {
      updateView(getView().getAppContext(), getView(), movieList);
    } else {
      Intent serviceIntent = new Intent(getView().getAppContext(), StarWarsService.class);
      getView().getAppContext().startService(serviceIntent);
    }
  }

  private boolean isCacheAvailable() {
    return movieList != null;
  }

  private void registerLoadDataReceiver() {
    movieDataReceiver = new StarWarsMovieDataReceiver();
    LocalBroadcastManager.getInstance(getView().getAppContext())
        .registerReceiver(movieDataReceiver, new IntentFilter(LOAD_COMPLETE_ACTION));
  }

  @Override public void detachView() {
    unRegisterLoadDataReceiver();
    Intent serviceIntent = new Intent(getView().getAppContext(), StarWarsService.class);
    getView().getAppContext().stopService(serviceIntent);
    super.detachView();
  }

  private void unRegisterLoadDataReceiver() {
    if (movieDataReceiver != null) {
      LocalBroadcastManager.getInstance(getView().getAppContext())
          .unregisterReceiver(movieDataReceiver);
    }
  }

  private class StarWarsMovieDataReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals(LOAD_COMPLETE_ACTION)) {

        if (isViewAttached()) {
          MainActivityView activityView = getView();
          movieList = (ArrayList<StarWarsMovie>) intent.getSerializableExtra(DATA_KEY);
          updateView(context, activityView, movieList);
        }
      }
    }
  }

  private void updateView(Context context, MainActivityView activityView,
      ArrayList<StarWarsMovie> list) {
    MovieAdapter movieAdapter = new MovieAdapter(context, list);
    activityView.getListView().setAdapter(movieAdapter);

    activityView.getProgressBar().setVisibility(View.GONE);
    activityView.getListView().setVisibility(View.VISIBLE);
  }

  private class MovieAdapter extends ArrayAdapter<StarWarsMovie> {
    public MovieAdapter(Context context, ArrayList<StarWarsMovie> users) {
      super(context, 0, users);
    }

    @NonNull @Override public View getView(int position, View convertView, ViewGroup parent) {
      StarWarsMovie starWarsMovie = getItem(position);
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
      }
      TextView tvName = (TextView) convertView.findViewById(R.id.tv_im_movie_name);
      TextView tvDescription = (TextView) convertView.findViewById(R.id.tv_im_movie_description);
      TextView tvDate = (TextView) convertView.findViewById(R.id.tv_im_movie_date);
      ImageView ivPoster = (ImageView) convertView.findViewById(R.id.iv_im_movie_poster);

      tvName.setText(starWarsMovie.getName());
      tvDescription.setText(starWarsMovie.getDescription());

      SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
      tvDate.setText(format.format(starWarsMovie.getReleaseDate()));
      ivPoster.setImageBitmap(starWarsMovie.getImageBitmap());

      return convertView;
    }
  }
}
