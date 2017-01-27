package com.rssaggregator.android.network.event;

public class UpdateReadItemsByChannelIdEvent extends BaseEvent<Void> {

  public UpdateReadItemsByChannelIdEvent(Throwable throwable) {
    super(throwable);
  }

  public UpdateReadItemsByChannelIdEvent() {
    super((Void) null);
  }
}
