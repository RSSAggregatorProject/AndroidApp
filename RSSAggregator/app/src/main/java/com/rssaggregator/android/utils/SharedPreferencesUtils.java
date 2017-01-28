package com.rssaggregator.android.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Utility class for setting and getting shared preferences.
 */
public class SharedPreferencesUtils {

  public static final String PREF_FILE_NAME = "rss_preferences_files";

  /**
   * Sets API token of the user to the Shared Preferences.
   *
   * @param activity
   * @param apiToken
   */
  public static void setApiToken(Activity activity, String apiToken) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(Globals.PREFERENCES_API_KEY, apiToken);
    editor.apply();
  }

  /**
   * Gets the API token of the user.
   *
   * @param activity
   *
   * @return String API token
   */
  public static String getApiToken(Activity activity) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    return sharedPreferences.getString(Globals.PREFERENCES_API_KEY, null);
  }

  /**
   * Sets the Email User.
   *
   * @param activity
   * @param userEmail
   */
  public static void setUserEmail(Activity activity, String userEmail) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(Globals.PREFERENCES_USER_EMAIL_KEY, userEmail);
    editor.apply();
  }

  /**
   * Gets the Email User.
   *
   * @param activity
   *
   * @return String Email user.
   */
  public static String getUserEmail(Activity activity) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    return sharedPreferences.getString(Globals.PREFERENCES_USER_EMAIL_KEY, null);
  }

  /**
   * Sets the User ID.
   *
   * @param activity
   * @param userId
   */
  public static void setUserId(Activity activity, Integer userId) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(Globals.PREFERENCES_USER_ID_KEY, userId);
    editor.apply();
  }

  /**
   * Gets the User Id
   *
   * @param activity
   *
   * @return Integer User Id.
   */
  public static Integer getUserId(Activity activity) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    return sharedPreferences.getInt(Globals.PREFERENCES_USER_ID_KEY, -1);
  }
}
