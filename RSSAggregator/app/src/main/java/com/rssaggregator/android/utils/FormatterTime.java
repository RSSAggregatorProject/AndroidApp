package com.rssaggregator.android.utils;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.rssaggregator.android.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class with a public static function "formattedAsTimeAgo" which change the date format,
 * like the style "Time Ago".
 */
public class FormatterTime {

  private static final double SECOND = 1;
  private static final double MINUTE = SECOND * 60;
  private static final double HOUR = MINUTE * 60;
  private static final double DAY = HOUR * 24;
  private static final double WEEK = DAY * 7;
  private static final double MONTH = DAY * 31;
  private static final double YEAR = DAY * 365.25;

  /**
   * Change the simple date format to a "Time Ago" style. The style change according to the current
   * date. Example: < 1 hour  =>  "x minutes ago"
   *
   * @param context  {@link Context} of the application.
   * @param dateItem {@link Date} of the item formatted.
   *
   * @return The new formatted String date.
   */
  public static String formattedAsTimeAgo(Context context, Date dateItem) {
    if (dateItem == null)
      return context == null ?
          "Unknown date" : context.getResources().getString(R.string.unknown_date_formatter);
    Date currentDate = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dateItem);
    calendar.add(Calendar.HOUR_OF_DAY, -1);
    dateItem = calendar.getTime();

    return selectCaseTimeAgo(context, currentDate, dateItem);
  }

  /**
   * Return the new date format according to the current date and the date of the item.
   *
   * @param context     {@link Context} of the application.
   * @param currentDate {@link Date} and time of the current date.
   * @param dateItem    {@link Date} of the item.
   *
   * @return The new {@link String} date format
   */
  private static String selectCaseTimeAgo(Context context, Date currentDate, Date dateItem) {
    long diffSecondes = (currentDate.getTime() - dateItem.getTime()) / 1000;

    // Impossible case but for future case
    if (diffSecondes < 0) {
      Logger.e("Current: " + currentDate.toString() + " | Date item : " + dateItem.toString());
      return context == null ?
          "In the future" : context.getResources().getString(R.string.future_time_formatter);
    }

    // < 1 minute  =>  "Just now"
    if (diffSecondes < MINUTE)
      return context == null ?
          "Just now" : context.getResources().getString(R.string.now_time_formatter);

    if (diffSecondes < HOUR)
      return formatMinutesAgo(context, diffSecondes);

    if (isSameDay(dateItem, currentDate))
      return formatAsToday(context, diffSecondes);

    if (isYesterday(dateItem, currentDate))
      return formatAsYesterday(context, dateItem);

    if (isLastWeek(diffSecondes))
      return formatAsLastWeek(context, dateItem);

    if (isLastMonth(diffSecondes))
      return formatAsLastMonth(context, dateItem);

    if (isLastYear(diffSecondes))
      return formatAsLastYear(context, dateItem, currentDate);

    return formatAsOther(context, dateItem);
  }


  /**
   * Return the new formatted date like "x minute(s) ago".
   *
   * @param context      {@link Context} of the application.
   * @param diffSecondes Number of secondes between the current date and the date of the issue.
   *
   * @return The new formatted {@link String} date.
   */
  private static String formatMinutesAgo(Context context, long diffSecondes) {
    int diffMinutes = (int) (diffSecondes / MINUTE);

    if (context == null)
      return (diffMinutes == 1) ? "1 minute ago" : diffMinutes + " minutes ago";

    return context.getResources()
        .getQuantityString(R.plurals.minute_time_formatter, diffMinutes, diffMinutes);
  }


  /**
   * Return the new formatted date like "x hour(s) ago".
   *
   * @param context      {@link Context} of the application.
   * @param diffSecondes Number of secondes between the current date and the date of the issue.
   *
   * @return The new formatted {@link String} date.
   */
  private static String formatAsToday(Context context, long diffSecondes) {
    int diffHours = (int) (diffSecondes / HOUR);

    if (context == null)
      return (diffHours == 1) ? "1 hour ago" : diffHours + " hours ago";

    return context.getResources()
        .getQuantityString(R.plurals.hour_time_formatter, diffHours, diffHours);
  }

  /**
   * Return the new formatted date like "Yesterday at h:mm AM/PM".
   *
   * @param context   {@link Context} of the application.
   * @param dateIssue {@link Date} of the issue.
   *
   * @return The new formatted {@link String} date.
   */
  private static String formatAsYesterday(Context context, Date dateIssue) {
    SimpleDateFormat format = new SimpleDateFormat(
        context.getResources().getString(R.string.time_formatter), Locale.getDefault());

    if (context == null)
      return "Yesterday at " + format.format(dateIssue);

    return String.format(context.getResources().getString(R.string.yesterday_time_formatter),
        format.format(dateIssue));
  }

  /**
   * Return the new formatted date like "WeekDay at h:mm AM/PM".
   *
   * @param context   {@link Context} of the application.
   * @param dateIssue {@link Date} of the issue.
   *
   * @return The new formatted {@link String} date.
   */
  private static String formatAsLastWeek(Context context, Date dateIssue) {
    SimpleDateFormat formatDate = new SimpleDateFormat(
        context.getResources().getString(R.string.day_time_formatter), Locale.getDefault());
    SimpleDateFormat formatTime = new SimpleDateFormat(
        context.getResources().getString(R.string.time_formatter), Locale.getDefault());
    if (context == null)
      return formatDate.format(dateIssue) + " at " + formatTime.format(dateIssue);

    return String.format(context.getResources().getString(R.string.at_time_formatter),
        formatDate.format(dateIssue), formatTime.format(dateIssue));
  }

  /**
   * Return the new formatted date like "Month Day at h:mm AM/PM".
   *
   * @param context   {@link Context} of the application.
   * @param dateIssue {@link Date} of the issue.
   *
   * @return The new formatted {@link String} date.
   */
  private static String formatAsLastMonth(Context context, Date dateIssue) {
    SimpleDateFormat formatMonth = new SimpleDateFormat(
        context.getResources().getString(R.string.month_time_formatter), Locale.getDefault());
    SimpleDateFormat formatTime = new SimpleDateFormat(
        context.getResources().getString(R.string.time_formatter), Locale.getDefault());

    if (context == null)
      return formatMonth.format(dateIssue) + " at " + formatTime.format(dateIssue);

    return String.format(context.getResources().getString(R.string.at_time_formatter),
        formatMonth.format(dateIssue), formatTime.format(dateIssue));
  }

  /**
   * Return the new formatted date like "Month Day".
   *
   * @param context   {@link Context} of the application.
   * @param dateIssue {@link Date} of the issue.
   *
   * @return The new formatted {@link String} date.
   */
  private static String formatAsLastYear(Context context, Date dateIssue, Date currentDate) {
    Calendar calIssue = Calendar.getInstance();
    calIssue.setTime(dateIssue);
    Calendar calCurrent = Calendar.getInstance();
    calCurrent.setTime(currentDate);
    SimpleDateFormat format;

    if (calIssue.get(Calendar.YEAR) == calCurrent.get(Calendar.YEAR)) {
      format = new SimpleDateFormat(
          context.getResources().getString(R.string.month_time_formatter),
          Locale.getDefault());
    } else {
      format = new SimpleDateFormat(
          context.getResources().getString(R.string.full_time_formatter),
          Locale.getDefault());
    }
    return format.format(dateIssue);
  }

  /**
   * Return the new formatted date like "Month Day, Year"
   *
   * @param context   {@link Context} of the application.
   * @param dateIssue {@link Date} of the issue.
   *
   * @return The new formatted {@link String} date.
   */
  private static String formatAsOther(Context context, Date dateIssue) {
    SimpleDateFormat format = new SimpleDateFormat(
        context.getResources().getString(R.string.full_time_formatter),
        Locale.getDefault());
    return format.format(dateIssue);
  }


  /**
   * Compare the current date and the date of the issue and check if these two dates have the same
   * day.
   *
   * @param dateToCompare {@link Date} to compare with the current date.
   * @param currentDate   Current {@link Date}.
   *
   * @return True if it's the same day.
   */
  private static boolean isSameDay(Date dateToCompare, Date currentDate) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    String dateToCompareString = format.format(dateToCompare);
    String currentDateString = format.format(currentDate);

    return dateToCompareString.equals(currentDateString);
  }

  /**
   * Compare the current date and the date of the issue and check if there is one day between the
   * two dates.
   *
   * @param dateIssue   {@link Date} of the issue.
   * @param currentDate Current {@link Date}.
   *
   * @return True if it's there is one day between the two dates.
   */
  private static boolean isYesterday(Date dateIssue, Date currentDate) {
    Calendar tmpCal = Calendar.getInstance();
    tmpCal.setTime(dateIssue);
    tmpCal.add(Calendar.DAY_OF_MONTH, 1);
    dateIssue = tmpCal.getTime();
    return isSameDay(dateIssue, currentDate);
  }

  /**
   * Check if the difference (in secondes) between the current date and the date of the issue is
   * less than a week (604800 secondes).
   *
   * @param diffSecondes Number of secondes between the current date and the date of the issue.
   *
   * @return True if it's less than a week.
   */
  private static boolean isLastWeek(long diffSecondes) {
    return diffSecondes < WEEK;
  }

  /**
   * Check if the difference (in secondes) between the current date and the date of the issue is
   * less than a month (2678400 secondes).
   *
   * @param diffSecondes Number of secondes between the current date and the date of the issue.
   *
   * @return True if it's less than a month.
   */
  private static boolean isLastMonth(long diffSecondes) {
    return diffSecondes < MONTH;
  }

  /**
   * Check if the difference (in secondes) between the current date and the date of the issue is
   * less than a year (31557600 secondes).
   *
   * @param diffSecondes Number of secondes between the current date and the date of the issue.
   *
   * @return True if it's less than a year.
   */
  private static boolean isLastYear(long diffSecondes) {
    return diffSecondes < YEAR;
  }

}
