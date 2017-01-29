package com.rssaggregator.android.network.model;

import com.google.gson.annotations.Expose;

/**
 * Class which represents the body for the API request AddCategory.
 */
public class AddCategoryWrapper {

  @Expose
  private String name;

  public AddCategoryWrapper(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
