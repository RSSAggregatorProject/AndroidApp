package com.rssaggregator.android.network.event;

public class AddFeedEvent extends BaseEvent<Void> {

  public AddFeedEvent(Throwable throwable) {
    super(throwable);
  }

  public AddFeedEvent() {
    super((Void) null);
  }
}
