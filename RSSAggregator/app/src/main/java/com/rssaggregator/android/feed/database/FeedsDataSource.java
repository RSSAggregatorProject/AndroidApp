package com.rssaggregator.android.feed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;
import com.rssaggregator.android.network.model.Item;
import com.rssaggregator.android.utils.DatabaseUtils;
import com.rssaggregator.android.utils.Globals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

  /**
   * Insert a category in the database.
   *
   * @param category Category to insert.
   *
   * @return id of the new element
   */
  public long insertCategory(Category category) {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(DatabaseUtils.ID_CATEGORY, category.getCategoryId());
    values.put(DatabaseUtils.NAME_CATEGORY, category.getName());
    values.put(DatabaseUtils.UNREAD_CATEGORY, category.getUnread());

    long rowId = database.insert(DatabaseUtils.TABLE_CATEGORY, null, values);
    database.close();

    return rowId;
  }

  /**
   * Gets all the categories in the database.
   *
   * @return List of Category.
   */
  public List<Category> selectAllCategories() {
    SQLiteDatabase database = this.databaseHandler.getReadableDatabase();

    List<Category> categories = new ArrayList<Category>();
    String selectQuery = DatabaseUtils.SELECT_ALL_CATEGORIES;

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

  /**
   * Delete all rows in the Category table.
   */
  public void deleteAllCategories() {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();
    database.execSQL(DatabaseUtils.DELETE_ALL_CATEGORIES);
    database.close();
  }

  //
  //
  // Channel methods
  //
  //

  /**
   * Insert a channel to the database.
   *
   * @param channel Channel to insert.
   *
   * @return Id of the new element created.
   */
  public long insertChannel(Channel channel) {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(DatabaseUtils.ID_CHANNEL, channel.getChannelId());
    values.put(DatabaseUtils.ID_CATEGORY, channel.getCategoryId());
    values.put(DatabaseUtils.NAME_CHANNEL, channel.getName());
    values.put(DatabaseUtils.UNREAD_CHANNEL, channel.getUnread());
    values.put(DatabaseUtils.FAVICON_URI_CHANNEL, channel.getFaviconUri());

    long rowId = database.insert(DatabaseUtils.TABLE_CHANNEL, null, values);
    database.close();

    return rowId;
  }

  public List<Channel> selectChannelsByCategoryId(Integer categoryId) {
    SQLiteDatabase database = this.databaseHandler.getReadableDatabase();

    List<Channel> channels = new ArrayList<Channel>();
    String selectQuery = DatabaseUtils.SELECT_CHANNELS_BY_CATEGORY_ID(categoryId);

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

  /**
   * Delete all rows in the Channel table.
   */
  public void deleteAllChannels() {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();
    database.execSQL(DatabaseUtils.DELETE_ALL_CHANNELS);
    database.close();
  }

  //
  //
  // Item methods.
  //
  //

  /**
   * Insert a item to the database.
   *
   * @param item Item to insert.
   *
   * @return id of the new element created.
   */
  public long insertItem(Item item) {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();

    SimpleDateFormat sdf = new SimpleDateFormat(Globals.DATE_FORMAT, Locale.getDefault());
    String pubDate = sdf.format(item.getPubDate());

    ContentValues values = new ContentValues();
    values.put(DatabaseUtils.ID_ITEM, item.getItemId());
    values.put(DatabaseUtils.ID_CHANNEL, item.getChannelId());
    values.put(DatabaseUtils.ID_CATEGORY, item.getCategoryId());
    values.put(DatabaseUtils.NAME_ITEM, item.getName());
    values.put(DatabaseUtils.NAME_CHANNEL, item.getNameChannel());
    values.put(DatabaseUtils.TITLE_ITEM, item.getTitle());
    values.put(DatabaseUtils.DESCRIPTION_ITEM, item.getDescription());
    values.put(DatabaseUtils.PUBDATE_ITEM, pubDate);
    values.put(DatabaseUtils.LINK_ITEM, item.getLinkUrl());
    values.put(DatabaseUtils.READ_ITEM, item.isRead());
    values.put(DatabaseUtils.STARRED_ITEM, item.isStarred());

    long rowId = database.insert(DatabaseUtils.TABLE_ITEM, null, values);
    database.close();

    return rowId;
  }

  /**
   * Select all items in the database.
   *
   * @return a List of Items.
   */
  public List<Item> selectAllItems() {
    SQLiteDatabase database = this.databaseHandler.getReadableDatabase();
    SimpleDateFormat sdf = new SimpleDateFormat(Globals.DATE_FORMAT, Locale.getDefault());

    List<Item> items = new ArrayList<>();
    String selectQuery = DatabaseUtils.SELECT_ALL_ITEMS;

    Cursor c = database.rawQuery(selectQuery, null);

    if (c.moveToFirst()) {
      do {
        Item item = new Item();
        item.setItemId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_ITEM)));
        item.setChannelId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CHANNEL)));
        item.setCategoryId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CATEGORY)));
        item.setName(c.getString(c.getColumnIndex(DatabaseUtils.NAME_ITEM)));
        item.setNameChannel(c.getString(c.getColumnIndex(DatabaseUtils.NAME_CHANNEL)));
        item.setTitle(c.getString(c.getColumnIndex(DatabaseUtils.TITLE_ITEM)));
        item.setDescription(c.getString(c.getColumnIndex(DatabaseUtils.DESCRIPTION_ITEM)));

        Date pubDate;
        try {
          pubDate = sdf.parse(
              c.getString(c.getColumnIndex(DatabaseUtils.PUBDATE_ITEM)));
        } catch (ParseException e) {
          e.printStackTrace();
          pubDate = new Date();
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

  /**
   * Select starred items in the database.
   *
   * @return a List of Items.
   */
  public List<Item> selectStarredItems() {
    SQLiteDatabase database = this.databaseHandler.getReadableDatabase();
    SimpleDateFormat sdf = new SimpleDateFormat(Globals.DATE_FORMAT, Locale.getDefault());

    List<Item> items = new ArrayList<>();
    String selectQuery = DatabaseUtils.SELECT_STARRED_ITEMS;

    Cursor c = database.rawQuery(selectQuery, null);

    if (c.moveToFirst()) {
      do {
        Item item = new Item();
        item.setItemId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_ITEM)));
        item.setChannelId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CHANNEL)));
        item.setCategoryId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CATEGORY)));
        item.setName(c.getString(c.getColumnIndex(DatabaseUtils.NAME_ITEM)));
        item.setNameChannel(c.getString(c.getColumnIndex(DatabaseUtils.NAME_CHANNEL)));
        item.setTitle(c.getString(c.getColumnIndex(DatabaseUtils.TITLE_ITEM)));
        item.setDescription(c.getString(c.getColumnIndex(DatabaseUtils.DESCRIPTION_ITEM)));

        Date pubDate;
        try {
          pubDate = sdf.parse(
              c.getString(c.getColumnIndex(DatabaseUtils.PUBDATE_ITEM)));
        } catch (ParseException e) {
          e.printStackTrace();
          pubDate = new Date();
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

  /**
   * Selects items from a category in the database thanks to the category ID.
   *
   * @param categoryId id of the category to fetch.
   *
   * @return List of Items.
   */
  public List<Item> selectItemsByCategoryId(Integer categoryId) {
    SQLiteDatabase database = this.databaseHandler.getReadableDatabase();
    SimpleDateFormat sdf = new SimpleDateFormat(Globals.DATE_FORMAT, Locale.getDefault());

    List<Item> items = new ArrayList<>();
    String selectQuery = DatabaseUtils.SELECT_ITEMS_BY_CATEGORY_ID(categoryId);

    Cursor c = database.rawQuery(selectQuery, null);

    if (c.moveToFirst()) {
      do {
        Item item = new Item();
        item.setItemId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_ITEM)));
        item.setChannelId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CHANNEL)));
        item.setCategoryId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CATEGORY)));
        item.setName(c.getString(c.getColumnIndex(DatabaseUtils.NAME_ITEM)));
        item.setNameChannel(c.getString(c.getColumnIndex(DatabaseUtils.NAME_CHANNEL)));
        item.setTitle(c.getString(c.getColumnIndex(DatabaseUtils.TITLE_ITEM)));
        item.setDescription(c.getString(c.getColumnIndex(DatabaseUtils.DESCRIPTION_ITEM)));

        Date pubDate;
        try {
          pubDate = sdf.parse(
              c.getString(c.getColumnIndex(DatabaseUtils.PUBDATE_ITEM)));
        } catch (ParseException e) {
          e.printStackTrace();
          pubDate = new Date();
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

  /**
   * Selects items from a channel in the database thanks to the channel ID.
   *
   * @param channelId id of the channel to fetch.
   *
   * @return List of Items.
   */
  public List<Item> selectItemsByChannelId(Integer channelId) {
    SQLiteDatabase database = this.databaseHandler.getReadableDatabase();
    SimpleDateFormat sdf = new SimpleDateFormat(Globals.DATE_FORMAT, Locale.getDefault());

    List<Item> items = new ArrayList<>();
    String selectQuery = DatabaseUtils.SELECT_ITEMS_BY_CHANNEL_ID(channelId);

    Cursor c = database.rawQuery(selectQuery, null);

    if (c.moveToFirst()) {
      do {
        Item item = new Item();
        item.setItemId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_ITEM)));
        item.setChannelId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CHANNEL)));
        item.setCategoryId(c.getInt(c.getColumnIndex(DatabaseUtils.ID_CATEGORY)));
        item.setName(c.getString(c.getColumnIndex(DatabaseUtils.NAME_ITEM)));
        item.setNameChannel(c.getString(c.getColumnIndex(DatabaseUtils.NAME_CHANNEL)));
        item.setTitle(c.getString(c.getColumnIndex(DatabaseUtils.TITLE_ITEM)));
        item.setDescription(c.getString(c.getColumnIndex(DatabaseUtils.DESCRIPTION_ITEM)));

        Date pubDate;
        try {
          pubDate = sdf.parse(
              c.getString(c.getColumnIndex(DatabaseUtils.PUBDATE_ITEM)));
        } catch (ParseException e) {
          e.printStackTrace();
          pubDate = new Date();
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

  public long updateReadStateItem(Item item, boolean state) {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();
    String whereClauseStr = DatabaseUtils.ID_ITEM + "=" + item.getItemId()
        + " AND " + DatabaseUtils.ID_CATEGORY + "=" + item.getCategoryId()
        + " AND " + DatabaseUtils.ID_CHANNEL + "=" + item.getChannelId();

    ContentValues values = new ContentValues();
    values.put(DatabaseUtils.READ_ITEM, state);

    long rowId = database.update(DatabaseUtils.TABLE_ITEM, values, whereClauseStr, null);

    return rowId;
  }

  public long updateStarStateItem(Item item, boolean state) {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();
    String whereClauseStr = DatabaseUtils.ID_ITEM + "=" + item.getItemId()
        + " AND " + DatabaseUtils.ID_CATEGORY + "=" + item.getCategoryId()
        + " AND " + DatabaseUtils.ID_CHANNEL + "=" + item.getChannelId();

    ContentValues values = new ContentValues();
    values.put(DatabaseUtils.STARRED_ITEM, state);

    long rowId = database.update(DatabaseUtils.TABLE_ITEM, values, whereClauseStr, null);

    return rowId;
  }

  /**
   * Deletes all rows in the Item table.
   */
  public void deleteAllItems() {
    SQLiteDatabase database = this.databaseHandler.getWritableDatabase();
    database.execSQL(DatabaseUtils.DELETE_ALL_ITEMS);
    database.close();
  }
}
