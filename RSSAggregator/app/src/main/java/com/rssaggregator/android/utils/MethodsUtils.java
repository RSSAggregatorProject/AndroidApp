package com.rssaggregator.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Class with some utility methods.
 */
public class MethodsUtils {

  /**
   * Check if the device is connected to internet.
   *
   * @param context
   *
   * @return Boolean.
   */
  public static boolean isNetworkAvailable(Context context) {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
}
