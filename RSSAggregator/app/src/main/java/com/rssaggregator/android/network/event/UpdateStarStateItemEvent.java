package com.rssaggregator.android.network.event;


public class UpdateStarStateItemEvent extends BaseEvent<Void> {

  public UpdateStarStateItemEvent(Throwable throwable) {
    super(throwable);
  }

  public UpdateStarStateItemEvent() {
    super((Void) null);
  }
}
