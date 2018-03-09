package com.academy.android.starwarsmovies.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.academy.android.starwarsmovies.R;
import com.academy.android.starwarsmovies.model.StarWarsMovie;
import com.academy.android.starwarsmovies.viewmodel.MainViewModel;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Observer<List<StarWarsMovie>> {

  @BindView(R.id.lv_am_movie_list) ListView listView;
  @BindView(R.id.pb_am_loading) ProgressBar progressBar;
  private MainViewModel mainViewModel;

  public static void start(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

    ButterKnife.bind(this);
    listView.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);

    subscribeUiUpdates();
  }

  private void subscribeUiUpdates() {
    mainViewModel.getMoviesLiveData().observe(this, this);
  }

  @Override public void onChanged(@Nullable List<StarWarsMovie> starWarsMovies) {
    if (starWarsMovies != null && starWarsMovies.size() > 0) {
      MovieAdapter movieAdapter = new MovieAdapter(this, starWarsMovies);
      listView.setAdapter(movieAdapter);

      progressBar.setVisibility(View.GONE);
      listView.setVisibility(View.VISIBLE);
    }
  }

  static class ViewHolder {
    @BindView(R.id.tv_im_movie_name) TextView tvName;
    @BindView(R.id.tv_im_movie_description) TextView tvDescription;
    @BindView(R.id.tv_im_movie_date) TextView tvDate;
    @BindView(R.id.iv_im_movie_poster) ImageView ivPoster;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  private class MovieAdapter extends ArrayAdapter<StarWarsMovie> {
    MovieAdapter(Context context, List<StarWarsMovie> users) {
      super(context, 0, users);
    }

    @NonNull @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
      StarWarsMovie starWarsMovie = getItem(position);
      ViewHolder holder;
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        holder = new ViewHolder(convertView);
        convertView.setTag(holder);
      }

      holder = (ViewHolder) convertView.getTag();
      assert starWarsMovie != null;
      holder.tvName.setText(starWarsMovie.getName());
      holder.tvDescription.setText(starWarsMovie.getDescription());
      holder.tvDate.setText(starWarsMovie.getReleaseDate());

      Glide.with(getContext())
          .load(starWarsMovie.getImageUrl())
          .placeholder(R.drawable.placeholder)
          .into(holder.ivPoster);

      return convertView;
    }
  }
}
