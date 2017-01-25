package com.rssaggregator.android.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesUtils {

  public static final String PREF_FILE_NAME = "rss_preferences_files";

  public static void setApiToken(Activity activity, String apiToken) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(Globals.PREFERENCES_API_KEY, apiToken);
    editor.apply();
  }

  public static String getApiToken(Activity activity) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    return sharedPreferences.getString(Globals.PREFERENCES_API_KEY, null);
  }

  public static void setUserEmail(Activity activity, String userEmail) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(Globals.PREFERENCES_USER_EMAIL_KEY, userEmail);
    editor.apply();
  }

  public static String getUserEmail(Activity activity) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    return sharedPreferences.getString(Globals.PREFERENCES_USER_EMAIL_KEY, null);
  }

  public static void setUserId(Activity activity, Integer userId) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(Globals.PREFERENCES_USER_ID_KEY, userId);
    editor.apply();
  }

  public static Integer getUserId(Activity activity) {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_FILE_NAME,
        MODE_PRIVATE);
    return sharedPreferences.getInt(Globals.PREFERENCES_USER_ID_KEY, -1);
  }

}
