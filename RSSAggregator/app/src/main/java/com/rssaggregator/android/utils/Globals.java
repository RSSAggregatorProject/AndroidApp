package com.rssaggregator.android.utils;

public class Globals {

  /**
   * API urls.
   */
  public static final String API_URL = "http://dreamteamrssfeader.ddns.net:8080/rssserver/api/";

  /**
   * PREFERNCES KEYS
   */
  public static final String PREFERENCES_API_KEY = "preferences_api_key";
  public static final String PREFERENCES_USER_EMAIL_KEY = "preferences_user_email_key";
  public static final String PREFERENCES_USER_ID_KEY = "preferences_user_id_key";

  /**
   * ACTIVITY_CODE
   */
  public static final int ADD_FEED_ACTIVITY = 23;

  /**
   * TIME
   */
  private static final int SECOND = 1000;
  public static final int SPLASH_TIME = 3 * SECOND;
  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  /**
   * EXTRA DATA
   */
  public static final String EXTRA_KEY_SIGNUP_USERNAME = "key_signup_username";
  public static final String EXTRA_KEY_SIGNUP_PASSWORD = "key_signup_password";
  public static final String EXTRA_ITEM = "key_item";

  /**
   * Types
   */
  public static final int LIST_ALL_ITEMS_TYPE = 0;
  public static final int LIST_STAR_ITEMS_TYPE = 1;
  public static final int LIST_CATEGORY_ITEMS_TYPE = 2;
  public static final int LIST_CHANNEL_ITEMS_TYPE = 3;

}
