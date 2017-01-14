package com.academy.android.starwarsmovies.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.academy.android.starwarsmovies.R;
import com.academy.android.starwarsmovies.presenter.MainActivityView;
import com.academy.android.starwarsmovies.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainActivityView {

  private ListView listView;
  private ProgressBar progressBar;
  public static MainPresenter mainPresenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    listView = (ListView) findViewById(R.id.lv_am_movie_list);
    listView.setVisibility(View.GONE);
    progressBar = (ProgressBar) findViewById(R.id.pb_am_loading);
    progressBar.setVisibility(View.VISIBLE);

    if (mainPresenter == null) {
      mainPresenter = new MainPresenter();
    }
    mainPresenter.attachView(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mainPresenter.detachView();
    if (isFinishing()) mainPresenter = null;
  }

  @Override public Context getAppContext() {
    return getApplicationContext();
  }

  @Override public Context getActivityContext() {
    return this;
  }

  @Override public ListView getListView() {
    return listView;
  }

  @Override public ProgressBar getProgressBar() {
    return progressBar;
  }
}
