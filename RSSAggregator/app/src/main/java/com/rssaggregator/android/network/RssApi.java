package com.rssaggregator.android.network;

import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Credentials;

public interface RssApi {

  void logIn(Credentials credentials);

  void signUp(Credentials credentials);

  void fetchData();

  void addCategory(String categoryName);

  void subscribeFeed(Category category, String rssLink);

  void unsubscribeFeed(Integer channelId);
}
