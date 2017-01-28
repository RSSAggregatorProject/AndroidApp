package com.rssaggregator.android.addfeed.presenter;

import android.content.Context;

import com.rssaggregator.android.addfeed.view.AddFeedView;
import com.rssaggregator.android.network.model.Category;

/**
 * Interface for AddFeedPresenter.
 */
public interface AddFeedPresenter {

  void setAddFeedView(AddFeedView addFeedView);

  void setDatabase(Context context);

  void addFeed(Category category, String rssLink);

  void fetchCategories();

  void addCategory(String categoryName);

  void onDestroy();
}
