package com.rssaggregator.android.feed.view;

import com.rssaggregator.android.network.model.CategoriesWrapper;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;
import com.rssaggregator.android.network.model.Item;

import java.util.HashMap;
import java.util.List;

public interface MainView {

  void showLoading();

  void showError(String errorMessage);

  void showSnackBarError(String errorMessage);

  void setNavigationContent(CategoriesWrapper wrapper);

  void showAllItemsContent(List<Item> data);

  void showStarredItemsContent(List<Item> data);

  void showItemsByCategoryIdContent(List<Item> data);

  void showItemsByChannelIdContent(List<Item> data);

  void showContent(CategoriesWrapper wrapper);

  /**
   * Success methods
   */
  void unsubscribeChannelSuccess();


  /**
   * Offline methods
   */
  void setNavigationContentOffline(List<Category> categories,
                                   HashMap<Category, List<Channel>> channels);

}
