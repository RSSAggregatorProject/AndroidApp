package com.rssaggregator.android.addfeed.view;

import com.rssaggregator.android.network.model.Category;

import java.util.List;

/**
 * Interface for the Add Feed View.
 */
public interface AddFeedView {

  void showFeedAdded();

  void showSnackbarError(String errorMessage);

  void setCategoriesToSpinner(List<Category> data);

  void updateCategoryCreated(String categoryName);
}
