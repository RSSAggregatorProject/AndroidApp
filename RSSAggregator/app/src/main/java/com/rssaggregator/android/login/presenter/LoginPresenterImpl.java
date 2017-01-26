package com.rssaggregator.android.login.presenter;

import com.orhanobut.logger.Logger;
import com.rssaggregator.android.login.view.LoginView;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.event.LogInEvent;
import com.rssaggregator.android.network.model.AccessToken;
import com.rssaggregator.android.network.model.Credentials;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class LoginPresenterImpl implements LoginPresenter {
  private RssApi rssApi;
  private EventBus eventBus;
  private LoginView loginView;

  @Inject
  public LoginPresenterImpl(RssApi rssApi, EventBus eventBus) {
    this.rssApi = rssApi;
    this.eventBus = eventBus;
    this.eventBus.register(this);
  }

  @Override
  public void setLoginView(LoginView loginView) {
    this.loginView = loginView;
  }

  @Override
  public void logIn(String userEmail, String userPassword) {
    Credentials credentials = new Credentials();
    credentials.setEmail(userEmail);
    credentials.setPassword(userPassword);

    if (this.loginView != null) {
      this.loginView.showLoading();
    }

    rssApi.logIn(credentials);
  }

  @Override
  public void onDestroy() {
    this.loginView = null;
    this.eventBus.unregister(this);
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(LogInEvent event) {
    if (event.isSuccess()) {
      Logger.e("TOKEN: " + event.getData().getApiToken());
      AccessToken accessToken = event.getData();
      if (this.loginView != null) {
        this.loginView.loginSuccessful(accessToken);
      }
    } else {
      if (this.loginView != null) {
        this.loginView.showErrorSnackbar(event.getThrowable().getMessage());
      }
    }
  }
}
