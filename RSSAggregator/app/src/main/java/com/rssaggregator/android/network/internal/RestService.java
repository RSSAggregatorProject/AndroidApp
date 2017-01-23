package com.rssaggregator.android.network.internal;

import com.rssaggregator.android.network.model.AccessToken;
import com.rssaggregator.android.network.model.Credentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestService {

  // AUTH
  @POST("auth")
  Call<AccessToken> logIn(@Body Credentials credentials);

  @POST("user")
  Call<Void> signUp(@Body Credentials credentials);
}
