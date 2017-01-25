package com.rssaggregator.android.feed.presenter;

import com.rssaggregator.android.feed.view.MainView;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.event.FetchDataEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class MainPresenterImpl implements MainPresenter {
  private RssApi rssApi;
  private EventBus eventBus;
  private MainView mainView;

  @Inject
  public MainPresenterImpl(RssApi rssApi, EventBus eventBus) {
    this.rssApi = rssApi;
    this.eventBus = eventBus;
    this.eventBus.register(this);
  }

  @Override
  public void setMainView(MainView mainView) {
    this.mainView = mainView;
  }

  @Override
  public void loadAllData() {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }

    this.rssApi.fetchData("authorization");
  }

  @Override
  public void onDestroy() {
    this.mainView = null;
    this.eventBus.unregister(this);
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(FetchDataEvent event) {
    if (event.isSuccess()) {
      if (this.mainView != null) {
        this.mainView.showContent(event.getData());
      }
    } else {
      if (this.mainView != null) {
        this.mainView.showError(event.getThrowable().getMessage());
      }
    }
  }
}
