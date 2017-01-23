package com.rssaggregator.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;
import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.event.LogInEvent;
import com.rssaggregator.android.network.model.Credentials;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

  private AppComponent appComponent;
  private EventBus eventBus;
  private RssApi rssApi;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    appComponent = RssAggregatorApplication.get(this).getAppComponent();
    eventBus = appComponent.bus();
    rssApi = appComponent.rssApi();

    this.eventBus.register(this);

    if (eventBus != null && rssApi != null) {
    }

    Credentials credentials = new Credentials();
    credentials.setLogin("iralala");
    credentials.setPassword("iralala");

    rssApi.logIn(credentials);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(LogInEvent event) {
    Logger.e("TOKEN: " + event.getData().getToken());
  }
}
