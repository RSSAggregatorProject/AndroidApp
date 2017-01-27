package com.rssaggregator.android.feeddetails.view;

public interface ItemDetailsView {

  void updateItemRead();

  void updateItemStarred(Boolean oldState);

  void showSnackBarError(String errorMessage);
}
