package com.rssaggregator.android.login.view;

import com.rssaggregator.android.network.model.AccessToken;

public interface LoginView {

  void showLoading();

  void showErrorSnackbar(String errorMessage);

  void loginSuccessful(AccessToken accessToken);
}
