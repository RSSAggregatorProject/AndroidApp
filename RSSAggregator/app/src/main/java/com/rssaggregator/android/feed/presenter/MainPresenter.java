package com.rssaggregator.android.feed.presenter;

import android.content.Context;

import com.rssaggregator.android.feed.view.MainView;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;

public interface MainPresenter {

  void setMainView(MainView mainView);

  void setDatabase(Context context);

  void loadAllData();

  void loadAllDataOffLine();

  void fetchAllItems();

  void fetchStarredItems();

  void fetchItemsByCategoryId(Integer categoryId);

  void fetchItemsByChannelId(Integer channelId);

  void unsubscribeChannel(Channel channel);

  //
  //
  // Update Read Items. Mark as read
  //
  //
  void updateReadAllItems();

  void updateReadItemsByChannelId(Channel selectedChannel);

  void onDestroy();
}
