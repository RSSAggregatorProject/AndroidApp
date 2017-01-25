package com.rssaggregator.android;

import android.app.Application;
import android.content.Context;

import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.dependency.AppModule;
import com.rssaggregator.android.dependency.DaggerAppComponent;
import com.rssaggregator.android.network.module.ApiClientModule;

import net.danlew.android.joda.JodaTimeAndroid;

public class RssAggregatorApplication extends Application {

  AppComponent appComponent;

  public static RssAggregatorApplication get(Context context) {
    return (RssAggregatorApplication) context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    JodaTimeAndroid.init(this);

    appComponent = DaggerAppComponent
        .builder()
        .apiClientModule(new ApiClientModule(this))
        .appModule(new AppModule(this))
        .build();
  }

  public AppComponent getAppComponent() {
    return appComponent;
  }

}
