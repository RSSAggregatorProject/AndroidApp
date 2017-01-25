package com.rssaggregator.android.network.event;

public class SignUpEvent extends BaseEvent<Void> {

  public SignUpEvent(Throwable throwable) {
    super(throwable);
  }

  public SignUpEvent() {
    super((Void) null);
  }
}
