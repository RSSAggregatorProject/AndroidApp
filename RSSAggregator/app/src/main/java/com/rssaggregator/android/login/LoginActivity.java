package com.rssaggregator.android.login;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.rssaggregator.android.R;
import com.rssaggregator.android.login.adapter.ViewPagerAdapter;
import com.rssaggregator.android.login.view.LoginFragment;
import com.rssaggregator.android.login.view.SignUpFragment;
import com.rssaggregator.android.utils.BaseActivity;
import com.rssaggregator.android.utils.Globals;
import com.rssaggregator.android.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

  @BindView(R.id.tabs) TabLayout tabs;
  @BindView(R.id.viewpager) ViewPager viewPager;

  private Resources resources;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    this.resources = getResources();

    /**
     * Reset Shared Preferences
     */
    SharedPreferencesUtils.setApiToken(this, "");
    SharedPreferencesUtils.setUserEmail(this, "");
    SharedPreferencesUtils.setUserId(this, -1);

    /**
     * Get ids when user signs up.
     */
    String loginUser = getIntent().getStringExtra(Globals.EXTRA_KEY_SIGNUP_USERNAME);
    String passwordUser = getIntent().getStringExtra(Globals.EXTRA_KEY_SIGNUP_PASSWORD);

    /**
     * Initialize tab views.
     */
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    adapter.addFragment(LoginFragment.newInstance(loginUser, passwordUser),
        resources.getString(R.string.login_tab_login));
    adapter.addFragment(SignUpFragment.newInstance(),
        resources.getString(R.string.login_tab_signup));
    viewPager.setAdapter(adapter);

    // https://code.google.com/p/android/issues/detail?id=180462#c17
    if (ViewCompat.isLaidOut(tabs)) {
      tabs.setupWithViewPager(viewPager);
    } else {
      tabs.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                                   int oldTop, int oldRight, int oldBottom) {
          tabs.setupWithViewPager(viewPager);
          tabs.removeOnLayoutChangeListener(this);
        }
      });
    }
  }
}
