package com.rssaggregator.android.feed.view;

import com.rssaggregator.android.network.model.CategoriesWrapper;

public interface MainView {

  void showLoading();

  void showError(String errorMessage);

  void showContent(CategoriesWrapper wrapper);
}
