package com.rssaggregator.android.feed.presenter;

import android.app.Activity;
import android.content.Context;

import com.rssaggregator.android.feed.view.MainView;
import com.rssaggregator.android.network.model.Channel;

public interface MainPresenter {

  void setMainView(MainView mainView);

  void setDatabase(Context context, Activity activity);

  void loadAllData_Online();

  void loadAllData_OffLine();

  //
  //
  // DataBase offline methods.
  //
  //
  void fetchAllItems_Offline();

  void fetchStarredItems_Offline();

  void fetchItemsByCategoryId_Offline(Integer categoryId);

  void fetchItemsByChannelId_Offline(Integer channelId);

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
