package com.academy.android.starwarsmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class StarWarsMovie {

  //private  String id;
  private @PrimaryKey String name;
  private String description;
  private String imageUrl;
  private String releaseDate;

  @Ignore public StarWarsMovie() {
  }

  public StarWarsMovie(String name, String description, String imageUrl, String releaseDate) {
    this.name = name;
    this.description = description;
    this.imageUrl = imageUrl;
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

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
