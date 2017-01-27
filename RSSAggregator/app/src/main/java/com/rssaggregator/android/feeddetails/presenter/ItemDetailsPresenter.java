package com.rssaggregator.android.feeddetails.presenter;

import android.content.Context;

import com.rssaggregator.android.feeddetails.view.ItemDetailsView;
import com.rssaggregator.android.network.model.Item;

public interface ItemDetailsPresenter {

  void setItemDetailsView(ItemDetailsView itemDetailsView);

  void setDatabase(Context context);

  void onDestroy();

  void updateReadItem(Item item);

  void updateStarStateItem(Item item);

}
