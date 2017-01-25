package com.rssaggregator.android.feed.presenter;

import android.content.Context;

import com.rssaggregator.android.feed.view.MainView;

public interface MainPresenter {

  void setMainView(MainView mainView);

  void setDatabase(Context context);

  void loadAllData();

  void fetchAllItems();

  void fetchStarredItems();

  void fetchItemsByCategoryId(Integer categoryId);

  void fetchItemsByChannelId(Integer channelId);

  void onDestroy();
}
