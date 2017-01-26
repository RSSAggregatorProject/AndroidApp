package com.rssaggregator.android.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddFeedWrapper {

  @SerializedName("id_cat")
  @Expose
  private Integer categoryId;
  @Expose
  private String name;
  @SerializedName("rssLink")
  @Expose
  private String uri;

  public AddFeedWrapper(Integer categoryId, String name, String uri) {
    this.categoryId = categoryId;
    this.name = name;
    this.uri = uri;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }
}
