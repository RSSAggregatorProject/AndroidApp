package com.rssaggregator.android.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.rssaggregator.android.utils.AppCompatPreferenceActivity;

/**
 * Activity for settings view.
 *
 * https://android.googlesource.com/platform/development/+/master/samples/Support7Demos/src/com
 * /example/android/supportv7/app/AppCompatPreferenceActivity.java
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupActionBar();
    getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();
  }

  /**
   * Sets up the action bar.
   */
  private void setupActionBar() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
