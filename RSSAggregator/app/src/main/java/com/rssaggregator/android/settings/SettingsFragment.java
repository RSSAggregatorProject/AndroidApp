package com.rssaggregator.android.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.rssaggregator.android.R;
import com.rssaggregator.android.RssAggregatorApplication;
import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.login.LoginActivity;
import com.rssaggregator.android.network.event.LogOutEvent;
import com.rssaggregator.android.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Fragment for the settings of the application.
 */
public class SettingsFragment extends PreferenceFragment {

  // Dependencies
  private EventBus eventBus;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injectDependencies();
    addPreferencesFromResource(R.xml.preferences);

    Preference userName;
    Preference logout;

    userName = findPreference(getString(R.string.key_name_profile));
    logout = findPreference(getString(R.string.key_logout));

    userName.setTitle(SharedPreferencesUtils.getUserEmail(getActivity()));
    logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {
        eventBus.post(new LogOutEvent());
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
        return false;
      }
    });
  }

  @Override
  public void onStop() {
    super.onStop();
  }

  /**
   * Inject dependencies.
   */
  private void injectDependencies() {
    AppComponent appComponent = RssAggregatorApplication.get(getActivity()).getAppComponent();
    eventBus = appComponent.bus();
  }
}
