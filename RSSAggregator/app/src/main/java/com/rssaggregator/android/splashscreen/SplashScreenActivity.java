package com.rssaggregator.android.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.rssaggregator.android.MainActivity;
import com.rssaggregator.android.R;
import com.rssaggregator.android.RssAggregatorApplication;
import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.login.LoginActivity;
import com.rssaggregator.android.network.event.AccessTokenFetchedEvent;
import com.rssaggregator.android.network.model.AccessToken;
import com.rssaggregator.android.utils.BaseActivity;
import com.rssaggregator.android.utils.Globals;
import com.rssaggregator.android.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Activity for Splash Screen.
 */
public class SplashScreenActivity extends BaseActivity {

  private EventBus eventBus;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);
    injectDependencies();
    waitSplashScreen();
  }

  /**
   * Injects dependencies.
   */
  private void injectDependencies() {
    AppComponent appComponent = RssAggregatorApplication.get(this).getAppComponent();
    this.eventBus = appComponent.bus();
  }

  /**
   * Waits few seconds and redirects to the main activity if he is already connected. Otherwise, it
   * redirects to the login activity.
   */
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
          AccessToken accessToken = new AccessToken();
          accessToken.setApiToken(apiToken);
          accessToken.setUserId(SharedPreferencesUtils.getUserId(SplashScreenActivity.this));

          eventBus.post(new AccessTokenFetchedEvent(accessToken));

          Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
          startActivity(mainIntent);
          finish();
        }
      }
    }, Globals.SPLASH_TIME);
  }
}
