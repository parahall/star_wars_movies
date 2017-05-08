package com.academy.android.starwarsmovies.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import com.academy.android.starwarsmovies.model.AppDatabase;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class AppModule {

  private Application application;

  public AppModule(Application application) {
    this.application = application;
  }

  @Provides @Singleton Context providesAppContext() {
    return application.getApplicationContext();
  }

  @Provides @Singleton AppDatabase providesDatabase(Context context) {
    return Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class).build();
  }
}
