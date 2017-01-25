package com.rssaggregator.android.network;

import com.rssaggregator.android.network.model.Credentials;

public interface RssApi {

  void logIn(Credentials credentials);

  void signUp(Credentials credentials);

  void fetchData(String authorization);
}
