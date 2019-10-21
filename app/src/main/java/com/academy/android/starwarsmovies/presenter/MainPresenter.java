package com.academy.android.starwarsmovies.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.academy.android.starwarsmovies.R;
import com.academy.android.starwarsmovies.model.StarWarsMovie;
import com.academy.android.starwarsmovies.model.StarWarsService;
import com.bumptech.glide.Glide;
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

  static class ViewHolder {
    @BindView(R.id.tv_im_movie_name) TextView tvName;
    @BindView(R.id.tv_im_movie_description) TextView tvDescription;
    @BindView(R.id.tv_im_movie_date) TextView tvDate;
    @BindView(R.id.iv_im_movie_poster) ImageView ivPoster;

    public ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  private class MovieAdapter extends ArrayAdapter<StarWarsMovie> {
    public MovieAdapter(Context context, ArrayList<StarWarsMovie> users) {
      super(context, 0, users);
    }

    @NonNull @Override public View getView(int position, View convertView, ViewGroup parent) {
      StarWarsMovie starWarsMovie = getItem(position);
      ViewHolder holder;
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        holder = new ViewHolder(convertView);
        convertView.setTag(holder);
      }

      holder = (ViewHolder) convertView.getTag();
      holder.tvName.setText(starWarsMovie.getName());
      holder.tvDescription.setText(starWarsMovie.getDescription());

      SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
      holder.tvDate.setText(format.format(starWarsMovie.getReleaseDate()));
      Glide.with(getContext())
          .load(starWarsMovie.getImageUrl())
          .placeholder(R.drawable.placeholder)
          .into(holder.ivPoster);

      return convertView;
    }
  }
}
