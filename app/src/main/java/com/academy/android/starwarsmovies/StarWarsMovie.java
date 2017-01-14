package com.academy.android.starwarsmovies;

import android.graphics.Bitmap;
import java.util.Date;

class StarWarsMovie {
  private String name;
  private String description;
  private Bitmap imageBitmap;
  private Date releaseDate;

  public StarWarsMovie(String name, String description, Bitmap imageBitmap, Date releaseDate) {
    this.name = name;
    this.description = description;
    this.imageBitmap = imageBitmap;
    this.releaseDate = releaseDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Bitmap getImageBitmap() {
    return imageBitmap;
  }

  public void setImageBitmap(Bitmap imageBitmap) {
    this.imageBitmap = imageBitmap;
  }

  public Date getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
