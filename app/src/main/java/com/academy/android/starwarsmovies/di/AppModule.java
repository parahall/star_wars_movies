package com.academy.android.starwarsmovies.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import com.academy.android.starwarsmovies.BuildConfig;
import com.academy.android.starwarsmovies.R;
import com.academy.android.starwarsmovies.model.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
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

  @Provides @Singleton FirebaseAuth providesFirebaseAuth() {
    return FirebaseAuth.getInstance();
  }

  @Provides @Singleton FirebaseDatabase providesDatabaseReference() {
    return FirebaseDatabase.getInstance();
  }

  @Provides @Singleton FirebaseRemoteConfig providesRemoteConfig() {
    FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
    FirebaseRemoteConfigSettings configSettings =
        new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build();
    remoteConfig.setConfigSettings(configSettings);
    remoteConfig.setDefaults(R.xml.remote_config_defaults);
    return remoteConfig;
  }
}
