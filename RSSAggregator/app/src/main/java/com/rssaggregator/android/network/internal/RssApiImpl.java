package com.rssaggregator.android.network.internal;

import android.content.Context;

import com.google.gson.Gson;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.error.ApiError;
import com.rssaggregator.android.network.event.LogInEvent;
import com.rssaggregator.android.network.event.SignUpEvent;
import com.rssaggregator.android.network.model.AccessToken;
import com.rssaggregator.android.network.model.Credentials;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RssApiImpl implements RssApi {
  private static AccessToken accessToken;
  private final Context context;
  private final RestService restService;
  private final EventBus eventBus;

  @Inject
  public RssApiImpl(Context context, RestService restService, EventBus eventBus) {
    this.context = context;
    this.restService = restService;
    this.eventBus = eventBus;
//    this.eventBus.register(this);
  }

  @Override
  public void logIn(Credentials credentials) {
    this.restService.logIn(credentials).enqueue(new Callback<AccessToken>() {
      @Override
      public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
        if (response.isSuccessful()) {
          AccessToken accessToken = response.body();
          eventBus.post(new LogInEvent(accessToken));
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);
            eventBus.post(new LogInEvent(new Throwable(error.getErrorDetails())));
          } catch (IOException e) {
            eventBus.post(new LogInEvent(new Throwable("Error")));
          }
        }
      }

      @Override
      public void onFailure(Call<AccessToken> call, Throwable t) {
        eventBus.post(new LogInEvent(new Throwable("Error")));
      }
    });
  }

  @Override
  public void signUp(Credentials credentials) {
    this.restService.signUp(credentials).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()) {
          eventBus.post(new SignUpEvent());
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);
            eventBus.post(new SignUpEvent(new Throwable(error.getErrorDetails())));
          } catch (IOException e) {
            eventBus.post(new SignUpEvent(new Throwable("Error")));
          }
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable t) {
        eventBus.post(new SignUpEvent(new Throwable("Error")));
      }
    });
  }
}
