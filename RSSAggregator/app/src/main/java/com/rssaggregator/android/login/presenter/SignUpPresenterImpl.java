package com.rssaggregator.android.login.presenter;

import com.orhanobut.logger.Logger;
import com.rssaggregator.android.login.view.SignUpView;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.event.LogInEvent;
import com.rssaggregator.android.network.event.SignUpEvent;
import com.rssaggregator.android.network.model.AccessToken;
import com.rssaggregator.android.network.model.Credentials;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class SignUpPresenterImpl implements SignUpPresenter {
  private RssApi rssApi;
  private EventBus eventBus;
  private SignUpView signUpView;

  @Inject
  public SignUpPresenterImpl(RssApi rssApi, EventBus eventBus) {
    this.rssApi = rssApi;
    this.eventBus = eventBus;
    this.eventBus.register(this);
  }

  @Override
  public void setSignUpView(SignUpView signUpView) {
    this.signUpView = signUpView;
  }

  @Override
  public void signUp(String userEmail, String userPassword) {
    if (this.signUpView != null) {
      this.signUpView.showLoading();
    }

    Credentials credentials = new Credentials();
    credentials.setLogin(userEmail);
    credentials.setPassword(userPassword);

//    this.rssApi.signUp(credentials);
    this.rssApi.logIn(credentials);
  }

  @Override
  public void onDestroy() {
    this.signUpView = null;
    this.eventBus.unregister(this);
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(SignUpEvent event) {
    if (event.isSuccess()) {
      if (this.signUpView != null) {
        this.signUpView.signUpSuccessful();
      }
    } else {
      if (this.signUpView != null) {
        this.signUpView.showErrorSnackbar(event.getThrowable().getMessage());
      }
    }
  }

/*
  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(LogInEvent event) {
    if (event.isSuccess()) {
      Logger.e("TOKEN: " + event.getData().getToken());
      AccessToken accessToken = event.getData();
      if (this.signUpView != null) {
        this.signUpView.signUpSuccessful();
      }
    } else {
      if (this.signUpView != null) {
        this.signUpView.showErrorSnackbar(event.getThrowable().getMessage());
      }
    }
  }*/
}
