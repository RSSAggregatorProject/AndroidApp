package com.rssaggregator.android.login.view;

public interface SignUpView {

  void showLoading();

  void showErrorSnackbar(String errorMessage);

  void signUpSuccessful();
}
