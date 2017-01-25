package com.rssaggregator.android.network.event;

import com.rssaggregator.android.network.model.CategoriesWrapper;

public class FetchDataEvent extends BaseEvent<CategoriesWrapper> {
  public FetchDataEvent(CategoriesWrapper wrapper) {
    super(wrapper);
  }

  public FetchDataEvent(Throwable throwable) {
    super(throwable);
  }
}
