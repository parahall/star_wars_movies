package com.academy.android.starwarsmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import com.academy.android.starwarsmovies.StarWarsApplication;
import com.academy.android.starwarsmovies.model.AppDatabase;
import com.academy.android.starwarsmovies.model.StarWarsMovie;
import com.academy.android.starwarsmovies.model.StarWarsService;
import java.util.List;
import javax.inject.Inject;

public class MainViewModel extends AndroidViewModel {

  private LiveData<List<StarWarsMovie>> moviesLiveData;
  @Inject AppDatabase mDb;

  public MainViewModel(Application application) {
    super(application);
    ((StarWarsApplication) getApplication()).getAppComponent().inject(this);
    requestDataUpdates();

    subscribeToDbChanges();
  }

  private void requestDataUpdates() {
    Intent serviceIntent = new Intent(this.getApplication(), StarWarsService.class);
    getApplication().startService(serviceIntent);
  }

  private void subscribeToDbChanges() {
    moviesLiveData = mDb.starWarsMovieModel().loadMovies();
  }

  public LiveData<List<StarWarsMovie>> getMoviesLiveData() {
    return moviesLiveData;
  }
}
