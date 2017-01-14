package com.academy.android.starwarsmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

  private ListView listView;
  private ProgressBar progressBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    listView = (ListView) findViewById(R.id.lv_am_movie_list);
    listView.setVisibility(View.GONE);
    progressBar = (ProgressBar) findViewById(R.id.pb_am_loading);
    progressBar.setVisibility(View.VISIBLE);
    StarWarsAsyncTask asyncTask = new StarWarsAsyncTask();
    asyncTask.execute();
  }

  private class MovieAdapter extends ArrayAdapter<StarWarsMovie> {
    public MovieAdapter(Context context, ArrayList<StarWarsMovie> users) {
      super(context, 0, users);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
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

  private class StarWarsAsyncTask extends AsyncTask<Void, Void, ArrayList<StarWarsMovie>> {

    @Override protected ArrayList<StarWarsMovie> doInBackground(Void... params) {
      String json = ResourcesUtil.loadJson(MainActivity.this);
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
      } catch (JSONException e) {
        e.printStackTrace();
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return moviesList;
    }

    @Override protected void onPostExecute(ArrayList<StarWarsMovie> starWarsMovies) {
      progressBar.setVisibility(View.GONE);
      listView.setVisibility(View.VISIBLE);
      MovieAdapter movieAdapter = new MovieAdapter(MainActivity.this, starWarsMovies);
      listView.setAdapter(movieAdapter);
    }
  }
}
