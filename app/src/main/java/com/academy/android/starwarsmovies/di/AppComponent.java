package com.academy.android.starwarsmovies.di;

import com.academy.android.starwarsmovies.model.StarWarsService;
import com.academy.android.starwarsmovies.view.LoginActivity;
import com.academy.android.starwarsmovies.viewmodel.LoginViewModel;
import com.academy.android.starwarsmovies.viewmodel.MainViewModel;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class }) public interface AppComponent {

  void inject(StarWarsService service);

  void inject(MainViewModel mainViewModel);

  void inject(LoginViewModel loginViewModel);

  void inject(LoginActivity loginActivity);
}
