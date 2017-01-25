package com.rssaggregator.android.feed.view;

import com.rssaggregator.android.network.model.CategoriesWrapper;
import com.rssaggregator.android.network.model.Item;

import java.util.List;

public interface MainView {

  void showLoading();

  void showError(String errorMessage);

  void setNavigationContent(CategoriesWrapper wrapper);

  void showAllItemsContent(List<Item> data);

  void showStarredItemsContent(List<Item> data);

  void showItemsByCategoryIdContent(List<Item> data);

  void showItemsByChannelIdContent(List<Item> data);

  void showContent(CategoriesWrapper wrapper);
}
