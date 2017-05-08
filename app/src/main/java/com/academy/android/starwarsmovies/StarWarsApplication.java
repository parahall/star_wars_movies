package com.academy.android.starwarsmovies;

import android.app.Application;
import com.academy.android.starwarsmovies.di.AppComponent;
import com.academy.android.starwarsmovies.di.AppModule;
import com.academy.android.starwarsmovies.di.DaggerAppComponent;

public class StarWarsApplication extends Application {

  private AppComponent appComponent;

  @Override public void onCreate() {
    super.onCreate();
    appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
  }

  public AppComponent getAppComponent() {
    return appComponent;
  }
}
