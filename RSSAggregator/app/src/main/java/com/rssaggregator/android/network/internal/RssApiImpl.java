package com.rssaggregator.android.network.internal;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.rssaggregator.android.R;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.error.ApiError;
import com.rssaggregator.android.network.event.FetchDataEvent;
import com.rssaggregator.android.network.event.LogInEvent;
import com.rssaggregator.android.network.event.SignUpEvent;
import com.rssaggregator.android.network.model.AccessToken;
import com.rssaggregator.android.network.model.CategoriesWrapper;
import com.rssaggregator.android.network.model.Credentials;
import com.rssaggregator.android.network.utils.DateDeserializer;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;

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

  @Override
  public void fetchData(String authorization) {
    this.restService.fetchData(authorization).enqueue(new Callback<CategoriesWrapper>() {
      @Override
      public void onResponse(Call<CategoriesWrapper> call, Response<CategoriesWrapper> response) {
        if (response.isSuccessful()) {
          CategoriesWrapper wrapper = response.body();
          eventBus.post(new FetchDataEvent(wrapper));
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.excludeFieldsWithoutExposeAnnotation();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            InputStream raw = context.getResources().openRawResource(R.raw.data);
            Reader rd = new BufferedReader(new InputStreamReader(raw));

            CategoriesWrapper wrapper = gson.fromJson(rd, CategoriesWrapper.class);
            eventBus.post(new FetchDataEvent(wrapper));
            //eventBus.post(new FetchDataEvent(new Throwable(error.getErrorDetails())));
          } catch (IOException e) {
            Logger.e("Exception");
            eventBus.post(new FetchDataEvent(new Throwable("Error")));
          }
        }
      }

      @Override
      public void onFailure(Call<CategoriesWrapper> call, Throwable t) {
        eventBus.post(new FetchDataEvent(new Throwable("Error")));
      }
    });
  }
}
