package com.rssaggregator.android.splashscreen.presenter;

import com.rssaggregator.android.network.RssApi;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class SplashScreenPresenterImpl implements SplashScreenPresenter {
  private RssApi rssApi;
  private EventBus eventBus;

  @Inject
  public SplashScreenPresenterImpl(RssApi rssApi, EventBus eventBus) {
    this.rssApi = rssApi;
    this.eventBus = eventBus;
  }
}
