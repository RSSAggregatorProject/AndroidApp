package com.rssaggregator.android.feeddetails.view;

/**
 * Interface for Item Details View.
 */
public interface ItemDetailsView {

  void updateItemRead();

  void updateItemStarred(Boolean oldState);

  void showSnackBarError(String errorMessage);
}
