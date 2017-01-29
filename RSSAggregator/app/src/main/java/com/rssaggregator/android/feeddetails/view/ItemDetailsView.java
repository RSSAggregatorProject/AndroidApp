package com.rssaggregator.android.feeddetails.view;

/**
 * Interface for Item Details View.
 */
public interface ItemDetailsView {

  void updateItemRead(boolean oldState);

  void updateItemStarred(Boolean oldState);

  void showSnackBarError();
}
