package com.rssaggregator.android.network.error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiError {
  @Expose
  @SerializedName("error_code")
  private int code;
  @Expose
  @SerializedName("error")
  private String errorDetails;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getErrorDetails() {
    return errorDetails;
  }

  public void setErrorDetails(String errorDetails) {
    this.errorDetails = errorDetails;
  }
}
