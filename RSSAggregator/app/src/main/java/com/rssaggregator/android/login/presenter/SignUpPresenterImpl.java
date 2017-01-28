package com.rssaggregator.android.login.presenter;

import com.rssaggregator.android.login.view.SignUpView;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.event.SignUpEvent;
import com.rssaggregator.android.network.model.Credentials;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Class which represents the Presenter of the Sign Up View. Requests data to the API and sends it
 * to the view.
 */
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

  /**
   * Signs Up to the application thanks to the API.
   *
   * @param userEmail
   * @param userPassword
   */
  @Override
  public void signUp(String userEmail, String userPassword) {
    if (this.signUpView != null) {
      this.signUpView.showLoading();
    }

    Credentials credentials = new Credentials();
    credentials.setEmail(userEmail);
    credentials.setPassword(userPassword);

    this.rssApi.signUp(credentials);
  }

  @Override
  public void onDestroy() {
    this.signUpView = null;
    this.eventBus.unregister(this);
  }

  //
  //
  // OnEvent Methods.
  //
  //
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
}
