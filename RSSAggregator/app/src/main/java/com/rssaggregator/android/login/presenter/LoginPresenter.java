package com.rssaggregator.android.login.presenter;

import com.rssaggregator.android.login.view.LoginView;

public interface LoginPresenter {

  void setLoginView(LoginView loginView);

  void logIn(String userEmail, String userPassword);

  void onDestroy();
}
