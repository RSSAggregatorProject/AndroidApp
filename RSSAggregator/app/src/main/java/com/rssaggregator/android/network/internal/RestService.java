package com.rssaggregator.android.network.internal;

import com.rssaggregator.android.network.model.AccessToken;
import com.rssaggregator.android.network.model.AddCategoryWrapper;
import com.rssaggregator.android.network.model.AddFeedWrapper;
import com.rssaggregator.android.network.model.CategoriesWrapper;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Credentials;
import com.rssaggregator.android.network.model.ItemStateWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestService {

  //
  //
  // AUTH
  //
  //
  @POST("auth")
  Call<AccessToken> logIn(@Body Credentials credentials);

  @POST("users")
  Call<Void> signUp(@Body Credentials credentials);

  //
  //
  // Fetch Data
  //
  //
  @GET("data")
  Call<CategoriesWrapper> fetchData();

  @POST("categories")
  Call<Category> addCategory(@Body AddCategoryWrapper wrapper);

  @POST("feeds")
  Call<Void> subscribeFeed(@Body AddFeedWrapper wrapper);

  @DELETE("feeds/{id_feed}")
  Call<Void> unsubscribeFeed(@Path("id_feed") Integer channelId);

  //
  //
  // Update Read Items. Mark as read.
  //
  //
  @PUT("items")
  Call<Void> updateReadAllItems(@Body ItemStateWrapper wrapper);

  @PUT("feeds/{id_feed}/items/")
  Call<Void> updateReadItemsByChannelId(@Path("id_feed") Integer channelId,
                                        @Body ItemStateWrapper wrapper);

  @PUT("items/{id_item}")
  Call<Void> updateStateItem(@Path("id_item") Integer itemId,
                             @Body ItemStateWrapper wrapper);
}
