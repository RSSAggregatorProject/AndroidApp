package com.rssaggregator.android.dependency;

import com.rssaggregator.android.RssAggregatorApplication;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

  private RssAggregatorApplication context;

  public AppModule(RssAggregatorApplication context) {
    this.context = context;
  }

  @Provides
  RssAggregatorApplication provideComeOnApplication() {
    return context;
  }

  @Singleton
  @Provides
  EventBus provideBus() {
    return EventBus.getDefault();
  }
}