package com.rssaggregator.android.feed.view;

import com.rssaggregator.android.network.model.CategoriesWrapper;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;
import com.rssaggregator.android.network.model.Item;

import java.util.HashMap;
import java.util.List;

/**
 * Interface for the MainActivity.
 */
public interface MainView {

  void showLoading();

  void showSnackBarError(String errorMessage);

  void setNavigationContent_Online(CategoriesWrapper wrapper);

  void showAllItemsContent(List<Item> data);

  void showStarredItemsContent(List<Item> data);

  void showItemsByCategoryIdContent(List<Item> data);

  void showItemsByChannelIdContent(List<Item> data);

  /**
   * Success methods
   */
  void unsubscribeChannelSuccess();


  /**
   * Offline methods
   */
  void setNavigationContent_Offline(List<Category> categories,
                                    HashMap<Category, List<Channel>> channels);

}
