package com.rssaggregator.android.network.utils;

import com.rssaggregator.android.network.model.AccessToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenRequestInterceptor implements Interceptor {
  AccessToken accessToken;

  @Override
  public Response intercept(Interceptor.Chain chain) throws IOException {
    Request original = chain.request();
    Request request;

    if (accessToken != null) {
      if (accessToken.getApiToken() != null) {
        request = original.newBuilder()
            .header("Content-Type", "application/json")
            .header("Authorization", accessToken.getApiToken())
            .method(original.method(), original.body())
            .build();
      } else {
        request = original.newBuilder()
            .header("Content-Type", "application/json")
            .method(original.method(), original.body())
            .build();
      }
    } else {
      request = original.newBuilder()
          .header("Content-Type", "application/json")
          .method(original.method(), original.body())
          .build();
    }
    return chain.proceed(request);
  }

  public void resetAccessToken() {
    accessToken = null;
  }

  public AccessToken getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(AccessToken accessToken) {
    this.accessToken = accessToken;
  }
}
