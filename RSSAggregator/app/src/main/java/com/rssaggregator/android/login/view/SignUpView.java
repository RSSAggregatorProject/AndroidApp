package com.rssaggregator.android.login.view;

/**
 * Interface for Sign Up View.
 */
public interface SignUpView {

  void showLoading();

  void showErrorSnackbar(String errorMessage);

  void signUpSuccessful();
}
