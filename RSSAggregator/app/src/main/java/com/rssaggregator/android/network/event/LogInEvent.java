package com.rssaggregator.android.network.event;

import com.rssaggregator.android.network.model.AccessToken;

public class LogInEvent extends BaseEvent<AccessToken> {
  public LogInEvent(AccessToken accessToken) {
    super(accessToken);
  }

  public LogInEvent(Throwable throwable) {
    super(throwable);
  }
}
