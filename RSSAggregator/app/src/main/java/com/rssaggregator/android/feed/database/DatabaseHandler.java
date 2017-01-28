package com.rssaggregator.android.feed.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rssaggregator.android.utils.DatabaseUtils;

/**
 * Handler class for Database SQLite of Android.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

  // Database version
  private static final int DATABASE_VERSION = 1;

  // Database Name
  private static final String DATABASE_NAME = "feeds.db";

  public static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + DatabaseUtils.TABLE_CATEGORY
      + " ( "
      + DatabaseUtils.ID_CATEGORY + " INT NOT NULL, "
      + DatabaseUtils.NAME_CATEGORY + " VARCHAR(255) NOT NULL, "
      + DatabaseUtils.UNREAD_CATEGORY + " INT, "
      + "PRIMARY KEY (" + DatabaseUtils.ID_CATEGORY + ")"
      + ");";

  public static final String CREATE_TABLE_CHANNEL = "CREATE TABLE " + DatabaseUtils.TABLE_CHANNEL
      + " ( "
      + DatabaseUtils.ID_CHANNEL + " INT NOT NULL, "
      + DatabaseUtils.ID_CATEGORY + " INT NOT NULL, "
      + DatabaseUtils.NAME_CHANNEL + " VARCHAR(255) NOT NULL, "
      + DatabaseUtils.NAME_CATEGORY + " VARCHAR(255) NOT NULL, "
      + DatabaseUtils.UNREAD_CHANNEL + " INT, "
      + DatabaseUtils.FAVICON_URI_CHANNEL + " VARCHAR(255), "
      + "PRIMARY KEY (" + DatabaseUtils.ID_CHANNEL + ", " + DatabaseUtils.ID_CATEGORY + "), "
      + "FOREIGN KEY (" + DatabaseUtils.ID_CATEGORY + ") "
      + "REFERENCES " + DatabaseUtils.TABLE_CATEGORY + "(" + DatabaseUtils.ID_CATEGORY + ")"
      + ");";

  public static final String CREATE_TABLE_ITEM = "CREATE TABLE " + DatabaseUtils.TABLE_ITEM
      + " ( "
      + DatabaseUtils.ID_ITEM + " INT NOT NULL, "
      + DatabaseUtils.ID_CHANNEL + " INT NOT NULL, "
      + DatabaseUtils.ID_CATEGORY + " INT NOT NULL, "
      + DatabaseUtils.NAME_CHANNEL + " VARCHAR(255) NOT NULL, "
      + DatabaseUtils.NAME_CATEGORY + " VARCHAR(255) NOT NULL, "
      + DatabaseUtils.TITLE_ITEM + " VARCHAR(255) NOT NULL, "
      + DatabaseUtils.DESCRIPTION_ITEM + " TEXT NOT NULL, "
      + DatabaseUtils.PUBDATE_ITEM + " DATE NOT NULL, "
      + DatabaseUtils.LINK_ITEM + " VARCHAR(255) NOT NULL, "
      + DatabaseUtils.READ_ITEM + " BOOLEAN NOT NULL, "
      + DatabaseUtils.STARRED_ITEM + " BOOLEAN NOT NULL, "
      + "PRIMARY KEY (" + DatabaseUtils.ID_ITEM + ", " + DatabaseUtils.ID_CHANNEL + ", "
      + DatabaseUtils.ID_CATEGORY + "), "
      + "FOREIGN KEY (" + DatabaseUtils.ID_CHANNEL + ", " + DatabaseUtils.ID_CATEGORY + ") "
      + "REFERENCES " + DatabaseUtils.TABLE_CHANNEL + " ("
      + DatabaseUtils.ID_CHANNEL + ", " + DatabaseUtils.ID_CATEGORY + ")"
      + ");";

  public DatabaseHandler(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE_CATEGORY);
    db.execSQL(CREATE_TABLE_CHANNEL);
    db.execSQL(CREATE_TABLE_ITEM);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + DatabaseUtils.TABLE_CATEGORY);
    db.execSQL("DROP TABLE IF EXISTS " + DatabaseUtils.TABLE_CHANNEL);
    db.execSQL("DROP TABLE IF EXISTS " + DatabaseUtils.TABLE_ITEM);
  }
}
