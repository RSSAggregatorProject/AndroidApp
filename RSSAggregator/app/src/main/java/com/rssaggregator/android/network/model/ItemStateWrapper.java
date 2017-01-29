package com.rssaggregator.android.network.model;

import com.google.gson.annotations.Expose;

/**
 * Class which represents the body of the API request updateRead/StarState.
 */
public class ItemStateWrapper {

  @Expose
  private Boolean read;
  @Expose
  private Boolean starred;

  public ItemStateWrapper(Boolean read, Boolean starred) {
    this.read = read;
    this.starred = starred;
  }

  public Boolean getRead() {
    return read;
  }

  public void setRead(Boolean read) {
    this.read = read;
  }

  public Boolean getStarred() {
    return starred;
  }

  public void setStarred(Boolean starred) {
    this.starred = starred;
  }
}
