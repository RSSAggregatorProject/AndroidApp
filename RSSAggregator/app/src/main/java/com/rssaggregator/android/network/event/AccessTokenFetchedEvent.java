package com.rssaggregator.android.network.event;

import com.rssaggregator.android.network.model.AccessToken;

public class AccessTokenFetchedEvent {
  private final AccessToken accessToken;

  public AccessTokenFetchedEvent(AccessToken accessToken) {
    this.accessToken = accessToken;
  }

  public AccessToken getAccessToken() {
    return accessToken;
  }
}
