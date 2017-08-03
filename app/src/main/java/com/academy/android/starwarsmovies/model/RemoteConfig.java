package com.academy.android.starwarsmovies.model;

public class RemoteConfig {

  public static String GOOGLE_SIGN_IN_ENABLE_KEY = "GOOGLE_SIGN_ENABLE";

  private boolean isGoogleSignInEnable;

  public boolean isGoogleSignInEnable() {
    return isGoogleSignInEnable;
  }

  public void setGoogleSignInEnable(boolean googleSignInEnable) {
    isGoogleSignInEnable = googleSignInEnable;
  }
}
