package com.rssaggregator.android.network.event;

public class UpdateReadAllItemsEvent extends BaseEvent<Void> {

  public UpdateReadAllItemsEvent(Throwable throwable) {
    super(throwable);
  }

  public UpdateReadAllItemsEvent() {
    super((Void) null);
  }
}
