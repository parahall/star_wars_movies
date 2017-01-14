package com.academy.android.starwarsmovies.presenter;

import android.content.Context;

public interface MvpView {

  public Context getAppContext();
  public Context getActivityContext();
}