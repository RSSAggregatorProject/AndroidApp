package com.rssaggregator.android.network.internal;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.rssaggregator.android.R;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.error.ApiError;
import com.rssaggregator.android.network.event.AccessTokenFetchedEvent;
import com.rssaggregator.android.network.event.AddCategoryEvent;
import com.rssaggregator.android.network.event.AddFeedEvent;
import com.rssaggregator.android.network.event.FetchDataEvent;
import com.rssaggregator.android.network.event.LogInEvent;
import com.rssaggregator.android.network.event.LogOutEvent;
import com.rssaggregator.android.network.event.SignUpEvent;
import com.rssaggregator.android.network.event.UnsubscribeChannelEvent;
import com.rssaggregator.android.network.model.AccessToken;
import com.rssaggregator.android.network.model.AddCategoryWrapper;
import com.rssaggregator.android.network.model.AddFeedWrapper;
import com.rssaggregator.android.network.model.CategoriesWrapper;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Credentials;
import com.rssaggregator.android.network.utils.DateDeserializer;
import com.rssaggregator.android.network.utils.TokenRequestInterceptor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
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
  private final TokenRequestInterceptor requestInterceptor;

  @Inject
  public RssApiImpl(Context context, RestService restService, EventBus eventBus,
                    TokenRequestInterceptor requestInterceptor) {
    this.context = context;
    this.restService = restService;
    this.eventBus = eventBus;
    this.requestInterceptor = requestInterceptor;
    this.eventBus.register(this);
  }

  @Override
  public void logIn(Credentials credentials) {
    this.restService.logIn(credentials).enqueue(new Callback<AccessToken>() {
      @Override
      public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
        if (response.isSuccessful()) {
          AccessToken accessToken = response.body();
          eventBus.post(new LogInEvent(accessToken));
          requestInterceptor.setAccessToken(accessToken);
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);
            eventBus.post(new LogInEvent(new Throwable(error.getErrorDetails())));
          } catch (Exception e) {
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
          } catch (Exception e) {
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
  public void fetchData() {
    this.restService.fetchData().enqueue(new Callback<CategoriesWrapper>() {
      @Override
      public void onResponse(Call<CategoriesWrapper> call, Response<CategoriesWrapper> response) {
        if (response.isSuccessful()) {
          //  CategoriesWrapper wrapper = response.body();

          /**
           * TEMP
           */
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
          } catch (Exception e) {
            Logger.e("Exception");
            eventBus.post(new FetchDataEvent(new Throwable("Error")));
          }

//          eventBus.post(new FetchDataEvent(wrapper));
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
          } catch (Exception e) {
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

  @Override
  public void addCategory(String categoryName) {
    AddCategoryWrapper wrapper = new AddCategoryWrapper(categoryName);
    this.restService.addCategory(wrapper).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        eventBus.post(new AddCategoryEvent());
      }

      @Override
      public void onFailure(Call<Void> call, Throwable t) {
        eventBus.post(new AddCategoryEvent(t));
      }
    });
  }

  @Override
  public void subscribeFeed(Category category, String rssLink) {
    AddFeedWrapper wrapper = new AddFeedWrapper(category.getCategoryId(), "Name", rssLink);
    this.restService.subscribeFeed(wrapper).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        eventBus.post(new AddFeedEvent());
      }

      @Override
      public void onFailure(Call<Void> call, Throwable t) {
        eventBus.post(new AddFeedEvent(t));
      }
    });
  }

  @Override
  public void unsubscribeFeed(Integer channelId) {
    this.restService.unsubscribeFeed(channelId).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        eventBus.post(new UnsubscribeChannelEvent());
      }

      @Override
      public void onFailure(Call<Void> call, Throwable t) {
        eventBus.post(new UnsubscribeChannelEvent());
      }
    });
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(AccessTokenFetchedEvent event) {
    accessToken = event.getAccessToken();
    requestInterceptor.setAccessToken(accessToken);
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(LogOutEvent event) {
    accessToken = null;
    requestInterceptor.resetAccessToken();
  }
}
