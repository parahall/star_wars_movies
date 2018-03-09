package com.academy.android.starwarsmovies.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.academy.android.starwarsmovies.R;
import com.academy.android.starwarsmovies.StarWarsApplication;
import com.academy.android.starwarsmovies.model.RemoteConfig;
import com.academy.android.starwarsmovies.viewmodel.LoginViewModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
        implements LifecycleOwner, Observer, OnClickListener,
    GoogleApiClient.OnConnectionFailedListener {

  private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 0;
  // UI references.
  @BindView(R.id.email) AutoCompleteTextView mEmailView;
  @BindView(R.id.password) EditText mPasswordView;
  @BindView(R.id.login_progress) View mProgressView;
  @BindView(R.id.login_form) View mLoginFormView;
  @Inject FirebaseAuth mAuth;

  LoginViewModel mLoginViewModel;
  private GoogleApiClient mGoogleApiClient;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ((StarWarsApplication) getApplication()).getAppComponent().inject(this);
    ButterKnife.bind(this);

    if (mAuth.getCurrentUser() != null) {
      MainActivity.start(this);
      finish();
      return;
    }

    mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    mLoginViewModel.getUserData().observe(this, this);
    mLoginViewModel.getRemoteConfigData().observe(this, this);

    findViewById(R.id.email_login_button).setOnClickListener(this);
    findViewById(R.id.email_register_button).setOnClickListener(this);

    GoogleSignInOptions gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
            getString(R.string.default_web_client_id)).requestEmail().build();

    mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this /* OnConnectionFailedListener */)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();
  }

  private void setupThirdPartyFeatures() {
    View googleSignInBtn = findViewById(R.id.google_sign_in_button);
    googleSignInBtn.setOnClickListener(LoginActivity.this);
    googleSignInBtn.setVisibility(View.VISIBLE);
  }

  /**
   * Attempts to sign in or register the account specified by the login form.
   * If there are form errors (invalid email, missing fields, etc.), the
   * errors are presented and no actual login attempt is made.
   */
  private void attemptAuthorize(Authorization authorizationType) {
    // Reset errors.
    mEmailView.setError(null);
    mPasswordView.setError(null);

    // Store values at the time of the login attempt.
    String email = mEmailView.getText().toString();
    String password = mPasswordView.getText().toString();

    boolean cancel = false;
    View focusView = null;

    boolean isPasswordValid = mLoginViewModel.isPasswordValid(password);
    // Check for a valid password, if the user entered one.
    if (!isPasswordValid) {
      mPasswordView.setError(getString(R.string.error_invalid_password));
      focusView = mPasswordView;
      cancel = true;
    }

    // Check for a valid email address.
    if (TextUtils.isEmpty(email)) {
      mEmailView.setError(getString(R.string.error_field_required));
      focusView = mEmailView;
      cancel = true;
    } else if (!mLoginViewModel.isEmailValid(email)) {
      mEmailView.setError(getString(R.string.error_invalid_email));
      focusView = mEmailView;
      cancel = true;
    }

    if (cancel) {
      focusView.requestFocus();
    } else {
      showProgress(true);
      if (authorizationType == Authorization.LOGIN) {
        mLoginViewModel.login(email, password);
      } else {
        mLoginViewModel.signup(email, password);
      }
    }
  }

  /**
   * Shows the progress UI and hides the login form.
   */
  private void showProgress(final boolean show) {
    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    mLoginFormView.animate()
        .setDuration(shortAnimTime)
        .alpha(show ? 0 : 1)
        .setListener(new AnimatorListenerAdapter() {
          @Override public void onAnimationEnd(Animator animation) {
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
          }
        });

    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    mProgressView.animate()
        .setDuration(shortAnimTime)
        .alpha(show ? 1 : 0)
        .setListener(new AnimatorListenerAdapter() {
          @Override public void onAnimationEnd(Animator animation) {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
          }
        });
  }

  @Override public void onChanged(@Nullable Object changeable) {
    if (changeable != null) {
      if (changeable instanceof FirebaseUser) {
        showProgress(false);
        MainActivity.start(this);
      }
      if (changeable instanceof RemoteConfig) {
        setupThirdPartyFeatures();
      }
    }
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.email_register_button:
        attemptAuthorize(Authorization.REGISTRATION);
        break;
      case R.id.email_login_button:
        attemptAuthorize(Authorization.LOGIN);
        break;
      case R.id.google_sign_in_button:
        googleSignIn();
        break;
    }
  }

  private void googleSignIn() {
    showProgress(true);
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
      mLoginViewModel.googleSignIn(data);
    }
  }

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Toast.makeText(this, "Google Sign In not available", Toast.LENGTH_LONG).show();
    findViewById(R.id.google_sign_in_button).setEnabled(false);
  }

  enum Authorization {
    REGISTRATION, LOGIN
  }
}


