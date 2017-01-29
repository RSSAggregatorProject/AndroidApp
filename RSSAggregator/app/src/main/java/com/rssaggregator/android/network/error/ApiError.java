package com.rssaggregator.android.network.error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Error class from the API.
 */
public class ApiError {
  @Expose
  @SerializedName("status")
  private String status;
  @Expose
  @SerializedName("error")
  private String errorDetails;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getErrorDetails() {
    return errorDetails;
  }

  public void setErrorDetails(String errorDetails) {
    this.errorDetails = errorDetails;
  }
}
