package com.rssaggregator.android.network.event;

public class UnsubscribeChannelEvent extends BaseEvent<Void> {

  public UnsubscribeChannelEvent(Throwable throwable) {
    super(throwable);
  }

  public UnsubscribeChannelEvent() {
    super((Void) null);
  }
}
