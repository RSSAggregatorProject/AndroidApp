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
}
