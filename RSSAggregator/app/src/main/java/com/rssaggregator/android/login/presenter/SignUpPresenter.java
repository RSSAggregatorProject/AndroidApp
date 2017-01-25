package com.rssaggregator.android.login.presenter;

import com.rssaggregator.android.login.view.SignUpView;

public interface SignUpPresenter {

  void setSignUpView(SignUpView signUpView);

  void signUp(String userEmail, String userPassword);

  void onDestroy();
}
