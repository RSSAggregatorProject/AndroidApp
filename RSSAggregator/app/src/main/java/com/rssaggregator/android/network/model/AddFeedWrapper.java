package com.rssaggregator.android.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class which represents the body for the API request add/subscribe feed.
 */
public class AddFeedWrapper {

  @SerializedName("id_cat")
  @Expose
  private Integer categoryId;
  @SerializedName("uri")
  @Expose
  private String rssLink;

  public AddFeedWrapper(Integer categoryId, String rssLink) {
    this.categoryId = categoryId;
    this.rssLink = rssLink;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  public String getRssLink() {
    return rssLink;
  }

  public void setRssLink(String rssLink) {
    this.rssLink = rssLink;
  }
}
