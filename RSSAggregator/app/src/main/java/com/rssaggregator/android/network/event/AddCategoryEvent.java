package com.rssaggregator.android.network.event;

public class AddCategoryEvent extends BaseEvent<Void> {

  public AddCategoryEvent(Throwable throwable) {
    super(throwable);
  }

  public AddCategoryEvent() {
    super((Void) null);
  }
}
