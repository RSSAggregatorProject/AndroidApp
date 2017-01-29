package com.rssaggregator.android.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * User Information.
 */
public class AccessToken {

  @Expose
  private String status;
  @SerializedName("id_user")
  @Expose
  private Integer userId;
  @SerializedName("token")
  @Expose
  private String apiToken;
  @SerializedName("exp_date")
  @Expose
  private Date expirationDate;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getApiToken() {
    return apiToken;
  }

  public void setApiToken(String apiToken) {
    this.apiToken = apiToken;
  }

  public Date getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }
}
