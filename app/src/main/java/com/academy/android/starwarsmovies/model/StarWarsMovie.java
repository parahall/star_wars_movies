package com.academy.android.starwarsmovies.model;

import java.util.Date;

public class StarWarsMovie {
  private String name;
  private String description;
  private String imageUrl;
  private Date releaseDate;

  public StarWarsMovie(String name, String description, String imageBitmap, Date releaseDate) {
    this.name = name;
    this.description = description;
    this.imageUrl = imageBitmap;
    this.releaseDate = releaseDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
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
