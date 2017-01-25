package com.rssaggregator.android.feed.presenter;

import com.rssaggregator.android.feed.view.MainView;

public interface MainPresenter {

  void setMainView(MainView mainView);

  void loadAllData();

  void onDestroy();
}
