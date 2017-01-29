package com.rssaggregator.android.network.event;

public class UpdateReadStateItemEvent extends BaseEvent<Void> {

  public UpdateReadStateItemEvent(Throwable throwable) {
    super(throwable);
  }

  public UpdateReadStateItemEvent() {
    super((Void) null);
  }
}
