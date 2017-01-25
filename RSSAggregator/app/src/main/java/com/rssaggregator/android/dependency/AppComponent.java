package com.rssaggregator.android.dependency;

import com.rssaggregator.android.MainActivity;
import com.rssaggregator.android.RssAggregatorApplication;
import com.rssaggregator.android.feed.presenter.MainPresenterImpl;
import com.rssaggregator.android.login.presenter.LoginPresenterImpl;
import com.rssaggregator.android.login.presenter.SignUpPresenterImpl;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.internal.RestService;
import com.rssaggregator.android.network.module.ApiClientModule;
import com.rssaggregator.android.splashscreen.presenter.SplashScreenPresenterImpl;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ApiClientModule.class})
public interface AppComponent {

  RestService restService();

  RssApi rssApi();

  EventBus bus();

  RssAggregatorApplication getRssAggregatorApplication();

  SplashScreenPresenterImpl splashScreenPresenterImpl();

  LoginPresenterImpl loginPresenterImpl();

  SignUpPresenterImpl signUpPresenterImpl();

  MainPresenterImpl mainPresenterImpl();

  void inject(MainActivity mainActivity);
}
