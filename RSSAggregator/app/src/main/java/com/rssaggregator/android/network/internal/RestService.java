package com.rssaggregator.android.network.internal;

import com.rssaggregator.android.network.model.AccessToken;
import com.rssaggregator.android.network.model.AddCategoryWrapper;
import com.rssaggregator.android.network.model.AddFeedWrapper;
import com.rssaggregator.android.network.model.CategoriesWrapper;
import com.rssaggregator.android.network.model.Credentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestService {

  // AUTH
  @POST("auth")
  Call<AccessToken> logIn(@Body Credentials credentials);

  @POST("users")
  Call<Void> signUp(@Body Credentials credentials);

  @GET("categories")
  Call<CategoriesWrapper> fetchData();

  @POST("categories")
  Call<Void> addCategory(@Body AddCategoryWrapper wrapper);

  @POST("feeds")
  Call<Void> subscribeFeed(@Body AddFeedWrapper wrapper);

  @DELETE("feeds/{id_feed}")
  Call<Void> unsubscribeFeed(@Path("id_feed") Integer channelId);
}
