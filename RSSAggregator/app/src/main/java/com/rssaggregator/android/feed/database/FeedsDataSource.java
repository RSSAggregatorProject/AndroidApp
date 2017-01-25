package com.rssaggregator.android.feed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;
import com.rssaggregator.android.network.model.Item;
import com.rssaggregator.android.utils.DatabaseUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedsDataSource {

  private DatabaseHandler databaseHandler;

  public FeedsDataSource(Context context) {
    this.databaseHandler = new DatabaseHandler(context);
  }

  //
  //
  // Categories methods.
  //
  //
  public long insertCategory(Category category) {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(DatabaseUtils.ID_CATEGORY, category.getCategoryId());
    values.put(DatabaseUtils.NAME_CATEGORY, category.getName());
    values.put(DatabaseUtils.UNREAD_CATEGORY, category.getUnread());

    long rowId = database.insert(DatabaseUtils.TABLE_CATEGORY, null, values);

    return rowId;
  }

  public void deleteAllCategories() {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();
    database.execSQL("DELETE FROM " + DatabaseUtils.TABLE_CATEGORY);
    database.close();
  }

  public List<Category> getCategories() {
    SQLiteDatabase database = this.databaseHandler.getReadableDatabase();

    List<Category> categories = new ArrayList<Category>();
    String selectQuery = "SELECT * FROM " + DatabaseUtils.TABLE_CATEGORY;

    Cursor c = database.rawQuery(selectQuery, null);

    if (c.moveToFirst()) {
      do {
        Category category = new Category();
        category.setCategoryId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CATEGORY)));
        category.setName(c.getString(c.getColumnIndex(DatabaseUtils.NAME_CATEGORY)));
        category.setUnread(c.getInt(c.getColumnIndex(DatabaseUtils.UNREAD_CATEGORY)));
        categories.add(category);
      } while (c.moveToNext());
    }
    c.close();
    database.close();
    return categories;
  }

  //
  //
  // Channel methods
  //
  //
  public long insertChannel(Channel channel) {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(DatabaseUtils.ID_CHANNEL, channel.getChannelId());
    values.put(DatabaseUtils.ID_CATEGORY, channel.getCategoryId());
    values.put(DatabaseUtils.NAME_CHANNEL, channel.getName());
    values.put(DatabaseUtils.UNREAD_CHANNEL, channel.getUnread());
    values.put(DatabaseUtils.FAVICON_URI_CHANNEL, channel.getFaviconUri());

    long rowId = database.insert(DatabaseUtils.TABLE_CHANNEL, null, values);

    return rowId;
  }

  public void deleteAllChannels() {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();
    database.execSQL("DELETE FROM " + DatabaseUtils.TABLE_CHANNEL);
    database.close();
  }

  public List<Channel> getChannelsByCategoryId(Integer categoryId) {
    SQLiteDatabase database = this.databaseHandler.getReadableDatabase();

    List<Channel> channels = new ArrayList<Channel>();
    String selectQuery = "SELECT * FROM " + DatabaseUtils.TABLE_CHANNEL + " WHERE "
        + DatabaseUtils.ID_CATEGORY + "=" + categoryId;

    Cursor c = database.rawQuery(selectQuery, null);

    if (c.moveToFirst()) {
      do {
        Channel channel = new Channel();
        channel.setChannelId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CHANNEL)));
        channel.setCategoryId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CATEGORY)));
        channel.setName(c.getString(c.getColumnIndex(DatabaseUtils.NAME_CHANNEL)));
        channel.setUnread(c.getInt(c.getColumnIndex(DatabaseUtils.UNREAD_CHANNEL)));
        channel.setFaviconUri(c.getString(c.getColumnIndex(DatabaseUtils.FAVICON_URI_CHANNEL)));
        channels.add(channel);
      } while (c.moveToNext());
    }
    c.close();
    database.close();
    return channels;
  }

  //
  //
  // Item methods.
  //
  //
  public long insertItem(Item item) {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String pubDate = sdf.format(item.getPubDate());

    ContentValues values = new ContentValues();
    values.put(DatabaseUtils.ID_ITEM, item.getItemId());
    values.put(DatabaseUtils.ID_CHANNEL, item.getChannelId());
    values.put(DatabaseUtils.ID_CATEGORY, item.getCategoryId());
    values.put(DatabaseUtils.NAME_ITEM, item.getName());
    values.put(DatabaseUtils.TITLE_ITEM, item.getTitle());
    values.put(DatabaseUtils.DESCRIPTION_ITEM, item.getDescription());
    values.put(DatabaseUtils.PUBDATE_ITEM, pubDate);
    values.put(DatabaseUtils.LINK_ITEM, item.getLinkUrl());
    values.put(DatabaseUtils.READ_ITEM, item.isRead());
    values.put(DatabaseUtils.STARRED_ITEM, item.isStarred());

    long rowId = database.insert(DatabaseUtils.TABLE_ITEM, null, values);

    return rowId;
  }

  public void deleteAllItems() {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();
    database.execSQL("DELETE FROM " + DatabaseUtils.TABLE_ITEM);
    database.close();
  }

  public List<Item> getAllItems() {
    SQLiteDatabase database = this.databaseHandler.getReadableDatabase();

    List<Item> items = new ArrayList<Item>();
    String selectQuery = "SELECT * FROM " + DatabaseUtils.TABLE_ITEM + " ORDER BY "
        + DatabaseUtils.PUBDATE_ITEM + " DESC";

    Cursor c = database.rawQuery(selectQuery, null);

    if (c.moveToFirst()) {
      do {
        Item item = new Item();
        item.setItemId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_ITEM)));
        item.setChannelId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CHANNEL)));
        item.setCategoryId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CATEGORY)));
        item.setName(c.getString(c.getColumnIndex(DatabaseUtils.NAME_ITEM)));
        item.setTitle(c.getString(c.getColumnIndex(DatabaseUtils.TITLE_ITEM)));
        item.setDescription(c.getString(c.getColumnIndex(DatabaseUtils.DESCRIPTION_ITEM)));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date pubDate = new Date();
        try {
          pubDate = simpleDateFormat.parse(c.getString(c.getColumnIndex(DatabaseUtils
              .PUBDATE_ITEM)));
        } catch (ParseException e) {
          e.printStackTrace();
        }
        item.setPubDate(pubDate);
        item.setLinkUrl(c.getString(c.getColumnIndex(DatabaseUtils.LINK_ITEM)));
        item.setRead(c.getInt(c.getColumnIndex(DatabaseUtils.READ_ITEM)) > 0);
        item.setStarred(c.getInt(c.getColumnIndex(DatabaseUtils.STARRED_ITEM)) > 0);
        items.add(item);
      } while (c.moveToNext());
    }
    c.close();
    database.close();
    return items;
  }

  public List<Item> getStarredItems(Boolean isStarred) {
    SQLiteDatabase database = this.databaseHandler.getReadableDatabase();

    List<Item> items = new ArrayList<Item>();
    String selectQuery = "SELECT * FROM " + DatabaseUtils.TABLE_ITEM
        + " WHERE " + DatabaseUtils.STARRED_ITEM + "=1 " +
        " ORDER BY " + DatabaseUtils.PUBDATE_ITEM + " DESC";

    Cursor c = database.rawQuery(selectQuery, null);

    if (c.moveToFirst()) {
      do {
        Item item = new Item();
        item.setItemId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_ITEM)));
        item.setChannelId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CHANNEL)));
        item.setCategoryId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CATEGORY)));
        item.setName(c.getString(c.getColumnIndex(DatabaseUtils.NAME_ITEM)));
        item.setTitle(c.getString(c.getColumnIndex(DatabaseUtils.TITLE_ITEM)));
        item.setDescription(c.getString(c.getColumnIndex(DatabaseUtils.DESCRIPTION_ITEM)));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date pubDate = new Date();
        try {
          pubDate = simpleDateFormat.parse(c.getString(c.getColumnIndex(DatabaseUtils
              .PUBDATE_ITEM)));
        } catch (ParseException e) {
          e.printStackTrace();
        }
        item.setPubDate(pubDate);
        item.setLinkUrl(c.getString(c.getColumnIndex(DatabaseUtils.LINK_ITEM)));
        item.setRead(c.getInt(c.getColumnIndex(DatabaseUtils.READ_ITEM)) > 0);
        item.setStarred(c.getInt(c.getColumnIndex(DatabaseUtils.STARRED_ITEM)) > 0);
        items.add(item);
      } while (c.moveToNext());
    }
    c.close();
    database.close();
    return items;
  }

}
