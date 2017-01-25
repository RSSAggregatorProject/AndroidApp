package com.rssaggregator.android.network.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.internal.RestService;
import com.rssaggregator.android.network.internal.RssApiImpl;
import com.rssaggregator.android.network.utils.DateDeserializer;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiClientModule {

  protected Context context;

  public ApiClientModule(Context context) {
    this.context = context;
  }

  @Provides
  Context provideContext() {
    return context;
  }

  @Provides
  @Singleton
  protected RestService provideRestService() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    OkHttpClient client = builder.build();

    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.excludeFieldsWithoutExposeAnnotation();
    gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
    Gson gson = gsonBuilder.create();

    Retrofit retrofit = new Retrofit.Builder()
        .client(client)
        .baseUrl("http://api.comeon.io/1.0/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();

    return retrofit.create(RestService.class);
  }

  @Provides
  @Singleton
  RssApi provideApi(Context context, RestService restService, EventBus eventBus) {
    return new RssApiImpl(context, restService, eventBus);
  }
}
