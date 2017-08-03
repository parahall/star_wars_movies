package com.academy.android.starwarsmovies.model;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.academy.android.starwarsmovies.StarWarsApplication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import javax.inject.Inject;

public class StarWarsService extends IntentService implements ValueEventListener {

  private static final String TAG = StarWarsService.class.getSimpleName();
  @Inject AppDatabase db;
  @Inject FirebaseDatabase mFirebaseDb;

  public StarWarsService() {
    super("StarWarsService");
  }

  @Override public void onCreate() {
    super.onCreate();
    ((StarWarsApplication) getApplication()).getAppComponent().inject(this);
  }

  @Override protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      Log.d(TAG, "Thread: " + Thread.currentThread().getName()); //worker thread
      DatabaseReference myRef = mFirebaseDb.getReference("star_wars_movies");
      myRef.addValueEventListener(this);
    }
  }

  @Override public void onDataChange(final DataSnapshot dataSnapshot) {
    Log.d(TAG, "Thread: " + Thread.currentThread().getName()); //main thread
    //TODO There is bug in Firebase where onDataChange returned on MainThread and not on the one it was called
    //no race condition in current example so.. bang, shmang and new Thread is used
    new Thread(new Runnable() {
      @Override public void run() {
        db.beginTransaction();
        try {
          db.starWarsMovieModel().deleteAll();
          for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
            StarWarsMovie starWarsMovie = movieSnapshot.getValue(StarWarsMovie.class);
            db.starWarsMovieModel().insertMovie(starWarsMovie);
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }
      }
    }).start();
  }

  @Override public void onCancelled(DatabaseError databaseError) {
    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
    // ...
  }
}