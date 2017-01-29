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
   * @param activity Activity
   * @param apiToken Api Token of the session
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
   * @param activity Activity
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
   * @param activity  Activity
   * @param userEmail email of the user.
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
   * @param activity activity
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
   * @param activity activity
   * @param userId   id of the user.
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
   * @param activity activity
   *
   * @return Integer User Id.
   */
  public static Integer getUserId(Activity activity) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    return sharedPreferences.getInt(Globals.PREFERENCES_USER_ID_KEY, -1);
  }

  /**
   * Sets if the user wants to see all items or only unread items.
   *
   * @param activity     activity
   * @param isShowingAll if is showing all
   */
  public static void setShowOnlyUnread(Activity activity, boolean isShowingAll) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(Globals.PREFERENCES_SHOW_ALL_KEY, isShowingAll);
    editor.apply();
  }

  /**
   * Gets if the user wants to see all items or only unread items.
   *
   * @param activity activity
   *
   * @return boolean
   */
  public static boolean getShowOnlyUnread(Activity activity) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    return sharedPreferences.getBoolean(Globals.PREFERENCES_SHOW_ALL_KEY, false);
  }
}
