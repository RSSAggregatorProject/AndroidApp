package com.rssaggregator.android.splashscreen.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.rssaggregator.android.MainActivity;
import com.rssaggregator.android.R;
import com.rssaggregator.android.RssAggregatorApplication;
import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.login.LoginActivity;
import com.rssaggregator.android.splashscreen.presenter.SplashScreenPresenterImpl;
import com.rssaggregator.android.utils.BaseActivity;
import com.rssaggregator.android.utils.Globals;
import com.rssaggregator.android.utils.SharedPreferencesUtils;

public class SplashScreenActivity extends BaseActivity {

  private AppComponent appComponent;
  private SplashScreenPresenterImpl presenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);
    injectDependencies();
    waitSplashScreen();
  }

  private void injectDependencies() {
    this.appComponent = RssAggregatorApplication.get(this).getAppComponent();
    this.presenter = appComponent.splashScreenPresenterImpl();
  }

  private void waitSplashScreen() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        String apiToken = SharedPreferencesUtils.getApiToken(SplashScreenActivity.this);
        if (apiToken == null || apiToken.length() == 0) {
          Intent loginIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
          startActivity(loginIntent);
          finish();
        } else {
          Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
          startActivity(mainIntent);
          finish();
        }
      }
    }, Globals.SPLASH_TIME);
  }
}
