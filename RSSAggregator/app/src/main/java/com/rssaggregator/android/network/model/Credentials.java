package com.rssaggregator.android.network.model;

import com.google.gson.annotations.Expose;

public class Credentials {

  @Expose
  private String email;
  @Expose
  private String password;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
