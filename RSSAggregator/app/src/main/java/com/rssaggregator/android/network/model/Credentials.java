package com.rssaggregator.android.network.model;

import com.google.gson.annotations.Expose;

public class Credentials {

  @Expose
  private String login;
  @Expose
  private String password;

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
