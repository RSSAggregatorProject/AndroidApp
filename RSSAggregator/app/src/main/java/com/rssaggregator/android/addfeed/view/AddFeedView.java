package com.rssaggregator.android.addfeed.view;

import com.rssaggregator.android.network.model.Category;

import java.util.List;

public interface AddFeedView {

  void showFeedAdded();

  void setCategoriesToSpinner(List<Category> data);
}
