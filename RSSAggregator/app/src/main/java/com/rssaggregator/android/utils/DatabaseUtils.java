package com.rssaggregator.android.utils;

public class DatabaseUtils {

  /**
   * Table names.
   */
  public static final String TABLE_CATEGORY = "Category";
  public static final String TABLE_CHANNEL = "Channel";
  public static final String TABLE_ITEM = "Item";

  /**
   * Category columns.
   */
  public static final String ID_CATEGORY = "id_category";
  public static final String NAME_CATEGORY = "name_category";
  public static final String UNREAD_CATEGORY = "unread_category";

  /**
   * Channel columns.
   */
  public static final String ID_CHANNEL = "id_channel";
  public static final String NAME_CHANNEL = "name_channel";
  public static final String UNREAD_CHANNEL = "unread_channel";
  public static final String FAVICON_URI_CHANNEL = "favicon_uri_channel";

  /**
   * Item columns.
   */
  public static final String ID_ITEM = "id_item";
  public static final String NAME_ITEM = "name_item";
  public static final String TITLE_ITEM = "title_item";
  public static final String DESCRIPTION_ITEM = "description_item";
  public static final String PUBDATE_ITEM = "pubdate_item";
  public static final String LINK_ITEM = "link_item";
  public static final String READ_ITEM = "read_item";
  public static final String STARRED_ITEM = "starred_item";

  /**
   * DELETE queries
   */
  public static final String DELETE_ALL_CATEGORIES = "DELETE FROM " + DatabaseUtils.TABLE_CATEGORY;
  public static final String DELETE_ALL_CHANNELS = "DELETE FROM " + DatabaseUtils.TABLE_CHANNEL;
  public static final String DELETE_ALL_ITEMS = "DELETE FROM " + DatabaseUtils.TABLE_ITEM;

  /**
   * SELECT queries
   */
  public static final String SELECT_ALL_CATEGORIES = "SELECT * FROM "
      + DatabaseUtils.TABLE_CATEGORY;
  public static final String SELECT_ALL_ITEMS = "SELECT * FROM " + DatabaseUtils.TABLE_ITEM
      + " ORDER BY " + DatabaseUtils.PUBDATE_ITEM + " DESC";
  public static final String SELECT_STARRED_ITEMS = "SELECT * FROM " + DatabaseUtils.TABLE_ITEM
      + " WHERE " + DatabaseUtils.STARRED_ITEM + "=1 "
      + " ORDER BY " + DatabaseUtils.PUBDATE_ITEM + " DESC";

  public static final String SELECT_ITEMS_BY_CATEGORY_ID(Integer categoryId) {
    return "SELECT * FROM " + DatabaseUtils.TABLE_ITEM
        + " WHERE " + DatabaseUtils.ID_CATEGORY + "= " + categoryId
        + " ORDER BY " + DatabaseUtils.PUBDATE_ITEM + " DESC";
  }

  public static final String SELECT_ITEMS_BY_CHANNEL_ID(Integer channelId) {
    return "SELECT * FROM " + DatabaseUtils.TABLE_ITEM
        + " WHERE " + DatabaseUtils.ID_CHANNEL + "= " + channelId
        + " ORDER BY " + DatabaseUtils.PUBDATE_ITEM + " DESC";
  }

  public static final String SELECT_CHANNELS_BY_CATEGORY_ID(Integer categoryId) {
    return "SELECT * FROM " + DatabaseUtils.TABLE_CHANNEL
        + " WHERE " + DatabaseUtils.ID_CATEGORY + "=" + categoryId;
  }

  public static final String SELECT_UNREAD_ITEMS = "SELECT * FROM " + TABLE_ITEM
      + " WHERE " + DatabaseUtils.READ_ITEM + "=0";

  /**
   * Only Unread SELECTS
   */
  public static final String SELECT_ALL_ITEMS_UNREAD = "SELECT * FROM " + DatabaseUtils.TABLE_ITEM
      + " WHERE " + DatabaseUtils.READ_ITEM + "=0 "
      + " ORDER BY " + DatabaseUtils.PUBDATE_ITEM + " DESC";

  public static final String SELECT_ITEMS_BY_CATEGORY_ID_UNREAD(Integer categoryId) {
    return "SELECT * FROM " + DatabaseUtils.TABLE_ITEM
        + " WHERE " + DatabaseUtils.ID_CATEGORY + "= " + categoryId
        + " AND " + DatabaseUtils.READ_ITEM + "=0 "
        + " ORDER BY " + DatabaseUtils.PUBDATE_ITEM + " DESC";
  }

  public static final String SELECT_ITEMS_BY_CHANNEL_ID_UNREAD(Integer channelId) {
    return "SELECT * FROM " + DatabaseUtils.TABLE_ITEM
        + " WHERE " + DatabaseUtils.ID_CHANNEL + "= " + channelId
        + " AND " + DatabaseUtils.READ_ITEM + "=0 "
        + " ORDER BY " + DatabaseUtils.PUBDATE_ITEM + " DESC";
  }
}
