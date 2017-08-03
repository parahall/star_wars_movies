package com.academy.android.starwarsmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.academy.android.starwarsmovies.StarWarsApplication;
import com.academy.android.starwarsmovies.model.RemoteConfig;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import java.util.regex.Pattern;
import javax.inject.Inject;

public class LoginViewModel extends AndroidViewModel implements OnCompleteListener<AuthResult> {

  private static final String TAG = LoginViewModel.class.getSimpleName();
  @Inject FirebaseAuth mAuth;
  @Inject FirebaseRemoteConfig firebaseRemoteConfig;

  private MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
  private MutableLiveData<RemoteConfig> remoteConfigLiveData = new MutableLiveData<>();

  public LoginViewModel(Application application) {
    super(application);
    ((StarWarsApplication) getApplication()).getAppComponent().inject(this);
    fetchRemoteConfig();
  }

  public boolean isPasswordValid(String password) {
    return !TextUtils.isEmpty(password) && password.length() > 4;
  }

  public boolean isEmailValid(String email) {
    String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

    Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
    return pattern.matcher(email).matches();
  }

  public void login(String email, String password) {
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser == null) {
      mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this);
    } else {
      userLiveData.setValue(currentUser);
    }
  }

  public void signup(String email, String password) {
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser == null) {
      mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this);
    } else {
      userLiveData.setValue(currentUser);
    }
  }

  public LiveData<FirebaseUser> getUserData() {
    return userLiveData;
  }

  public LiveData<RemoteConfig> getRemoteConfigData() {
    return remoteConfigLiveData;
  }

  @Override public void onComplete(@NonNull Task<AuthResult> task) {
    if (task.isSuccessful()) {
      // Sign in success, update UI with the signed-in user's information
      Log.d(TAG, "signInWithEmail:success");
      FirebaseUser user = mAuth.getCurrentUser();
      userLiveData.setValue(user);
    } else {
      // If sign in fails, display a message to the user.
      Log.w(TAG, "signInWithEmail:failure", task.getException());

      Toast.makeText(LoginViewModel.this.getApplication(),
          task.getException() != null ? task.getException().getMessage() : "Authentication failed",
          Toast.LENGTH_SHORT).show();
      userLiveData.setValue(null);
    }
  }

  public void googleSignIn(Intent data) {
    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
    if (result.isSuccess()) {
      // Google Sign In was successful, authenticate with Firebase
      //TODO There is a bug that Google Sign override account auth with email, therefore it's impossible to link since there is no exception.
      GoogleSignInAccount account = result.getSignInAccount();
      AuthCredential credential =
          GoogleAuthProvider.getCredential(account != null ? account.getIdToken() : null, null);
      mAuth.signInWithCredential(credential).addOnCompleteListener(this);
    } else {
      userLiveData.setValue(null);
    }
  }

  private void fetchRemoteConfig() {
    long cacheExpiration = 3600; // 1 hour in seconds.
    // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
    // retrieve values from the service.
    if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
      cacheExpiration = 0;
    }

    firebaseRemoteConfig.fetch(cacheExpiration)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
              firebaseRemoteConfig.activateFetched();
            }
            RemoteConfig remoteConfig = new RemoteConfig();
            remoteConfig.setGoogleSignInEnable(
                firebaseRemoteConfig.getBoolean(RemoteConfig.GOOGLE_SIGN_IN_ENABLE_KEY));

            remoteConfigLiveData.setValue(remoteConfig);
          }
        });
  }
}
