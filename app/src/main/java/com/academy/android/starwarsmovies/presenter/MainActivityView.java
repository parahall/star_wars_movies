package com.academy.android.starwarsmovies.presenter;

import android.widget.ListView;
import android.widget.ProgressBar;

public interface MainActivityView extends MvpView {

  ListView getListView();

  ProgressBar getProgressBar();
}
