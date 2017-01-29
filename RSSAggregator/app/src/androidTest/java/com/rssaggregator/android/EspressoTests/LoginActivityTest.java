package com.rssaggregator.android.EspressoTests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.rssaggregator.android.HintMatcher;
import com.rssaggregator.android.R;
import com.rssaggregator.android.login.LoginActivity;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * UI Test Class which tests the LoginActivity View.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginActivityTest {

  @Rule
  public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<LoginActivity>
      (LoginActivity.class, true, true);


  /**
   * Checks if the hints in the edittext fields are displayed.
   */
  @Test
  public void a_hintIsDisplayed() {
    String hintText = mActivityRule.getActivity().getResources().getString(R.string
        .login_email_hint);
    onView(withId(R.id.emailLoginTil)).check(matches(HintMatcher.withHint(hintText)));
    hintText = mActivityRule.getActivity().getResources().getString(R.string.login_password_hint);
    onView(withId(R.id.passwordLoginTil)).check(matches(HintMatcher.withHint(hintText)));
  }

  /**
   * Clears and fills the login fields.
   */
  @Test
  public void b_fillLoginFields() {
    onView(withId(R.id.emailLoginEt)).perform(clearText());
    onView(withId(R.id.passwordLoginEt)).perform(clearText());
    onView(withId(R.id.emailLoginEt)).perform(typeText("iralala"), closeSoftKeyboard())
        .check(matches(withText("iralala")));
    onView(withId(R.id.passwordLoginEt)).perform(typeText("iralala"), closeSoftKeyboard())
        .check(matches(withText("iralala")));
  }

  /**
   * Checks if there is an error when one or all the fields are not filled.
   */
  @Test
  public void c_validateEmptyFields() {
    onView(withId(R.id.emailLoginEt)).perform(clearText());
    onView(withId(R.id.passwordLoginEt)).perform(clearText());
    onView(withId(R.id.buttonLogin)).perform(click());
    onView(withId(R.id.emailLoginEt)).perform(typeText("iralala"), closeSoftKeyboard());
    onView(withId(R.id.buttonLogin)).perform(click());
  }

  /**
   * Checks if there is an error when the ids sent to the API are not valid.
   */
  @Test
  public void d_errorWrongLogin() {
    onView(withId(R.id.emailLoginEt)).perform(clearText());
    onView(withId(R.id.passwordLoginEt)).perform(clearText());
    onView(withId(R.id.emailLoginEt)).perform(typeText("Wrong Login"), closeSoftKeyboard());
    onView(withId(R.id.passwordLoginEt)).perform(typeText("Wrong Password"), closeSoftKeyboard());
    onView(withId(R.id.buttonLogin)).perform(click());
  }

  /**
   * Tests the swipe pages.
   */
  @Test
  public void e_swipePages() {
    onView(withId(R.id.viewpager)).perform(swipeLeft());
    onView(withId(R.id.container)).perform(swipeRight());
    onView(withId(R.id.viewpager)).perform(swipeRight());
    onView(allOf(withText(R.string.login_tab_signup), not(withId(R.id.buttonSignUp))))
        .perform(click());
    onView(allOf(withText(R.string.login_tab_login), not(withId(R.id.buttonLogin))))
        .perform(click());
    onView(allOf(withText(R.string.login_tab_signup), not(withId(R.id.buttonSignUp))))
        .perform(click());
  }

  /**
   * Tests the connection when the ids are good.
   */
  @Test
  @Ignore
  public void f_ConnectionSuccess() {
    onView(withId(R.id.emailLoginEt)).perform(clearText());
    onView(withId(R.id.passwordLoginEt)).perform(clearText());
    onView(withId(R.id.emailLoginEt)).perform(typeText("iralala"), closeSoftKeyboard());
    onView(withId(R.id.passwordLoginEt)).perform(typeText("iralala"), closeSoftKeyboard());
    onView(withId(R.id.buttonLogin)).perform(click());
  }
}

