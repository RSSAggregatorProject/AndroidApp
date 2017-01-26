package com.rssaggregator.android.utils;

import android.content.Context;
import android.content.res.Configuration;

import com.rssaggregator.android.BuildConfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.not;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class FormatterTimeFRTest {
  private Context context;

  @Before
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void setup() {
    context = RuntimeEnvironment.application;

    Locale locale = new Locale("fr");
    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.locale = locale;

    context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
  }

  public Date setMockDate(int year, int month, int day, int hours, int minutes, int secondes) {
    Calendar tmpCal = Calendar.getInstance();
    tmpCal.set(year, month, day, hours, minutes, secondes);
    return tmpCal.getTime();
  }

  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatTimeNullDateTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Date nonNullDate = new Date();

    String result = FormatterTime.formattedAsTimeAgo(context, null);
    Assert.assertEquals(result, "Date inconnue");
    result = FormatterTime.formattedAsTimeAgo(context, nonNullDate);
    Assert.assertThat(result, not("Date inconnue"));
  }


  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatJustNowTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[3];
    params[0] = Context.class;
    params[1] = Date.class;
    params[2] = Date.class;

    Method method = myTarget.getDeclaredMethod("selectCaseTimeAgo", params);
    method.setAccessible(true);

    Date currentDate = new Date();
    Date sameDate = new Date();
    String result = (String) method.invoke(method, context, currentDate, sameDate);
    Assert.assertEquals(result, "Maintenant");

    Date currentDate2 = setMockDate(2015, 4, 17, 9, 23, 23);
    Date thirtySecondesLater = setMockDate(2015, 4, 17, 9, 22, 53);
    String result2 = (String) method.invoke(method, context, currentDate2, thirtySecondesLater);
    Assert.assertEquals(result2, "Maintenant");

    Date currentDate3 = setMockDate(2015, 4, 17, 9, 23, 23);
    Date limitDate = setMockDate(2015, 4, 17, 9, 22, 24);
    String result3 = (String) method.invoke(method, context, currentDate3, limitDate);
    Assert.assertEquals(result3, "Maintenant");

    Date currentDate4 = setMockDate(2015, 4, 17, 9, 23, 23);
    Date afterDate = setMockDate(2015, 4, 17, 9, 22, 12);
    String result4 = (String) method.invoke(method, context, currentDate4, afterDate);
    Assert.assertEquals(result4, "Il y a 1 minute");
  }


  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatMinutesTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[3];
    params[0] = Context.class;
    params[1] = Date.class;
    params[2] = Date.class;

    Method method = myTarget.getDeclaredMethod("selectCaseTimeAgo", params);
    method.setAccessible(true);
    Date currentDate = setMockDate(2015, 4, 17, 9, 23, 23);
    Date simpleDate = setMockDate(2015, 4, 17, 9, 0, 15);
    String result = (String) method.invoke(method, context, currentDate, simpleDate);
    Assert.assertEquals(result, "Il y a 23 minutes");

    Date currentDate2 = setMockDate(2015, 4, 17, 9, 23, 23);
    Date simpleDate2 = setMockDate(2015, 4, 17, 9, 0, 24);
    String result2 = (String) method.invoke(method, context, currentDate2, simpleDate2);
    Assert.assertEquals(result2, "Il y a 22 minutes");

    Date currentDate3 = setMockDate(2015, 4, 17, 9, 23, 23);
    Date simpleDate3 = setMockDate(2015, 4, 17, 9, 0, 22);
    String result3 = (String) method.invoke(method, context, currentDate3, simpleDate3);
    Assert.assertEquals(result3, "Il y a 23 minutes");

    Date currentDate4 = setMockDate(2015, 4, 17, 9, 23, 23);
    Date afterLimit = setMockDate(2015, 4, 17, 8, 23, 6);
    String result4 = (String) method.invoke(method, context, currentDate4, afterLimit);
    Assert.assertEquals(result4, "Il y a 1 heure");
  }


  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatHoursTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[3];
    params[0] = Context.class;
    params[1] = Date.class;
    params[2] = Date.class;

    Method method = myTarget.getDeclaredMethod("selectCaseTimeAgo", params);
    method.setAccessible(true);
    Date currentDate = setMockDate(2015, 4, 17, 9, 23, 23);
    Date simpleDate = setMockDate(2015, 4, 17, 5, 0, 34);
    String result = (String) method.invoke(method, context, currentDate, simpleDate);
    Assert.assertEquals(result, "Il y a 4 heures");

    Date currentDate2 = setMockDate(2015, 4, 17, 9, 23, 23);
    Date yesterday = setMockDate(2015, 4, 16, 23, 23, 24);
    String result2 = (String) method.invoke(method, context, currentDate2, yesterday);
    Assert.assertEquals(result2, "Hier, à 23:23");

    Date currentDate3 = setMockDate(2015, 4, 17, 9, 23, 23);
    Date yesterdayLimit = setMockDate(2015, 4, 16, 0, 0, 0);
    String result3 = (String) method.invoke(method, context, currentDate3, yesterdayLimit);
    Assert.assertEquals(result3, "Hier, à 0:00");

    Date currentDate4 = setMockDate(2015, 4, 1, 9, 23, 23);
    Date yesterdayAndMonth = setMockDate(2015, 3, 30, 23, 23, 24);
    String result4 = (String) method.invoke(method, context, currentDate4, yesterdayAndMonth);
    Assert.assertEquals(result4, "Hier, à 23:23");
  }


  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatSevenDaysTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[3];
    params[0] = Context.class;
    params[1] = Date.class;
    params[2] = Date.class;

    Method method = myTarget.getDeclaredMethod("selectCaseTimeAgo", params);
    method.setAccessible(true);
    Date currentDate = setMockDate(2015, 3, 17, 9, 23, 23);
    Date simpleDate = setMockDate(2015, 3, 14, 23, 59, 59);
    String result = (String) method.invoke(method, context, currentDate, simpleDate);
    Assert.assertEquals(result, "mardi, à 23:59");

    Date currentDate2 = setMockDate(2015, 3, 17, 9, 23, 23);
    Date simpleDate2 = setMockDate(2015, 3, 10, 9, 23, 24);
    String result2 = (String) method.invoke(method, context, currentDate2, simpleDate2);
    Assert.assertEquals(result2, "vendredi, à 9:23");
  }

  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatMonthTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[3];
    params[0] = Context.class;
    params[1] = Date.class;
    params[2] = Date.class;

    Method method = myTarget.getDeclaredMethod("selectCaseTimeAgo", params);
    method.setAccessible(true);
    Date currentDate = setMockDate(2015, 3, 17, 9, 23, 23);
    Date simpleDate = setMockDate(2015, 3, 2, 23, 23, 23);
    String result = (String) method.invoke(method, context, currentDate, simpleDate);
    Assert.assertEquals(result, "2 avril, à 23:23");

    Date currentDate2 = setMockDate(2015, 3, 17, 9, 23, 23);
    Date simpleDate2 = setMockDate(2015, 2, 28, 23, 23, 23);
    String result2 = (String) method.invoke(method, context, currentDate2, simpleDate2);
    Assert.assertEquals(result2, "28 mars, à 23:23");
  }

  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatMoreTimeTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[3];
    params[0] = Context.class;
    params[1] = Date.class;
    params[2] = Date.class;

    Method method = myTarget.getDeclaredMethod("selectCaseTimeAgo", params);
    method.setAccessible(true);
    Date currentDate = setMockDate(2015, 3, 17, 9, 23, 23);
    Date simpleDate = setMockDate(2015, 2, 2, 23, 23, 23);
    String result = (String) method.invoke(method, context, currentDate, simpleDate);
    Assert.assertEquals(result, "2 mars");

    Date currentDate2 = setMockDate(2015, 3, 17, 9, 23, 23);
    Date simpleDate2 = setMockDate(2014, 2, 2, 23, 23, 23);
    String result2 = (String) method.invoke(method, context, currentDate2, simpleDate2);
    Assert.assertEquals(result2, "2 mars 2014");
  }

  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatMinutesAgoTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[2];
    params[0] = Context.class;
    params[1] = long.class;

    Method method = myTarget.getDeclaredMethod("formatMinutesAgo", params);
    method.setAccessible(true);
    long diffSecondes = 61;
    String result = (String) method.invoke(method, context, diffSecondes);
    Assert.assertEquals(result, "Il y a 1 minute");

    long diffSecondes2 = 1380;
    String result2 = (String) method.invoke(method, context, diffSecondes2);
    Assert.assertEquals(result2, "Il y a 23 minutes");
  }

  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatAsTodayTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[2];
    params[0] = Context.class;
    params[1] = long.class;

    Method method = myTarget.getDeclaredMethod("formatAsToday", params);
    method.setAccessible(true);
    long diffSecondes = 14400;
    String result = (String) method.invoke(method, context, diffSecondes);
    Assert.assertEquals(result, "Il y a 4 heures");
  }

  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatAsYesterdayTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[2];
    params[0] = Context.class;
    params[1] = Date.class;

    Method method = myTarget.getDeclaredMethod("formatAsYesterday", params);
    method.setAccessible(true);
    Date date = setMockDate(2015, 3, 23, 23, 23, 23);
    String result = (String) method.invoke(method, context, date);
    Assert.assertEquals(result, "Hier, à 23:23");
  }

  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatAsLastWeekTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[2];
    params[0] = Context.class;
    params[1] = Date.class;

    Method method = myTarget.getDeclaredMethod("formatAsLastWeek", params);
    method.setAccessible(true);
    Date date = setMockDate(2015, 3, 23, 23, 23, 23);
    String result = (String) method.invoke(method, context, date);
    Assert.assertEquals(result, "jeudi, à 23:23");
  }

  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatAsLastMonthTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[2];
    params[0] = Context.class;
    params[1] = Date.class;

    Method method = myTarget.getDeclaredMethod("formatAsLastMonth", params);
    method.setAccessible(true);
    Date date = setMockDate(2015, 3, 23, 23, 23, 23);
    String result = (String) method.invoke(method, context, date);
    Assert.assertEquals(result, "23 avril, à 23:23");
  }

  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatAsLastYearTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[3];
    params[0] = Context.class;
    params[1] = Date.class;
    params[2] = Date.class;

    Method method = myTarget.getDeclaredMethod("formatAsLastYear", params);
    method.setAccessible(true);
    Date date = setMockDate(2015, 3, 23, 23, 23, 23);
    Date currentDate = setMockDate(2015, 5, 23, 23, 23, 23);
    String result = (String) method.invoke(method, context, date, currentDate);
    Assert.assertEquals(result, "23 avril");
  }

  @Test
  @Config(constants = BuildConfig.class, qualifiers = "fr")
  public void formatAsOtherTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[2];
    params[0] = Context.class;
    params[1] = Date.class;

    Method method = myTarget.getDeclaredMethod("formatAsOther", params);
    method.setAccessible(true);
    Date date = setMockDate(2015, 3, 23, 23, 23, 23);
    String result = (String) method.invoke(method, context, date);
    Assert.assertEquals(result, "23 avril 2015");
  }

  @Test
  public void isSameDayTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[2];
    params[0] = Date.class;
    params[1] = Date.class;

    Method method = myTarget.getDeclaredMethod("isSameDay", params);
    method.setAccessible(true);
    Date date = setMockDate(2015, 3, 23, 23, 23, 23);
    Date date2 = setMockDate(2015, 3, 23, 23, 23, 23);
    boolean result = (boolean) method.invoke(method, date, date2);
    Assert.assertEquals(result, true);

    Date date3 = setMockDate(2015, 3, 23, 23, 23, 23);
    Date date4 = setMockDate(2015, 2, 23, 23, 23, 23);
    boolean result2 = (boolean) method.invoke(method, date3, date4);
    Assert.assertEquals(result2, false);
  }

  @Test
  public void isYesterdayTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[2];
    params[0] = Date.class;
    params[1] = Date.class;

    Method method = myTarget.getDeclaredMethod("isYesterday", params);
    method.setAccessible(true);
    Date date = setMockDate(2015, 3, 22, 23, 23, 23);
    Date date2 = setMockDate(2015, 3, 23, 23, 23, 23);
    boolean result = (boolean) method.invoke(method, date, date2);
    Assert.assertEquals(result, true);

    Date date3 = setMockDate(2015, 3, 23, 23, 23, 23);
    Date date4 = setMockDate(2015, 3, 21, 23, 23, 23);
    boolean result2 = (boolean) method.invoke(method, date3, date4);
    Assert.assertEquals(result2, false);
  }

  @Test
  public void isLastWeekTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[1];
    params[0] = long.class;

    Method method = myTarget.getDeclaredMethod("isLastWeek", params);
    method.setAccessible(true);
    long diffSecondes = 518400;
    boolean result = (boolean) method.invoke(method, diffSecondes);
    Assert.assertEquals(result, true);

    diffSecondes = 691200;
    result = (boolean) method.invoke(method, diffSecondes);
    Assert.assertEquals(result, false);
  }

  @Test
  public void isLastMonthTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[1];
    params[0] = long.class;

    Method method = myTarget.getDeclaredMethod("isLastMonth", params);
    method.setAccessible(true);
    long diffSecondes = 1296000;
    boolean result = (boolean) method.invoke(method, diffSecondes);
    Assert.assertEquals(result, true);

    diffSecondes = 3456000;
    result = (boolean) method.invoke(method, diffSecondes);
    Assert.assertEquals(result, false);
  }

  @Test
  public void isLastYearTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Class myTarget = FormatterTime.class;
    Class params[] = new Class[1];
    params[0] = long.class;

    Method method = myTarget.getDeclaredMethod("isLastYear", params);
    method.setAccessible(true);
    long diffSecondes = 12960000;
    boolean result = (boolean) method.invoke(method, diffSecondes);
    Assert.assertEquals(result, true);

    diffSecondes = 62208000;
    result = (boolean) method.invoke(method, diffSecondes);
    Assert.assertEquals(result, false);
  }
}

