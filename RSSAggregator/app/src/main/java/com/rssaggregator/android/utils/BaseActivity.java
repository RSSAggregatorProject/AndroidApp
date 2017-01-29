package com.rssaggregator.android.utils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.rssaggregator.android.R;

/**
 * Activity to extend to have slide animations when starting and finishing activities.
 *
 * https://kylewbanks.com/blog/left-and-right-slide-animations-on-android-activity-or-view
 */
public class BaseActivity extends AppCompatActivity {

  @Override
  public void finish() {
    super.finish();
    overridePendingTransitionEnter();
  }

  @Override
  public void startActivity(Intent intent) {
    super.startActivity(intent);
    overridePendingTransitionExit();
  }

  /**
   * Overrides the pending Activity transition by performing the "Enter" animation.
   */
  protected void overridePendingTransitionEnter() {
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
  }

  /**
   * Overrides the pending Activity transition by performing the "Exit" animation.
   */
  protected void overridePendingTransitionExit() {
    overridePendingTransition(android.R.anim.slide_in_left,
        android.R.anim.slide_out_right);
  }

}