package com.rssaggregator.android.utils;

import junit.framework.Assert;

import org.junit.Test;

public class DatabaseUtilsTest {

  @Test
  public void initializeTest() {
    Assert.assertTrue(true);
  }

  @Test
  public void selectItemsByCategoryIdTest() {
    Assert.assertEquals("SELECT * FROM Item WHERE id_category= 2 ORDER BY pubdate_item DESC",
        DatabaseUtils.SELECT_ITEMS_BY_CATEGORY_ID(2));
  }

  @Test
  public void selectItemsByCategoryIdUnreadTest() {
    Assert.assertEquals("SELECT * FROM Item WHERE id_category= 2 AND read_item=0  ORDER BY " +
        "pubdate_item DESC", DatabaseUtils.SELECT_ITEMS_BY_CATEGORY_ID_UNREAD(2));
  }

  @Test
  public void selectItemsByChannelIdTest() {
    Assert.assertEquals("SELECT * FROM Item WHERE id_channel= 2 ORDER BY pubdate_item DESC",
        DatabaseUtils.SELECT_ITEMS_BY_CHANNEL_ID(2));
  }

  @Test
  public void selectItemsByChannelIdUnreadTest() {
    Assert.assertEquals("SELECT * FROM Item WHERE id_channel= 2 AND read_item=0  ORDER BY " +
            "pubdate_item DESC",
        DatabaseUtils.SELECT_ITEMS_BY_CHANNEL_ID_UNREAD(2));
  }

  @Test
  public void selectChannelsByCategoryId() {
    Assert.assertEquals("SELECT * FROM Channel WHERE id_category=2",
        DatabaseUtils.SELECT_CHANNELS_BY_CATEGORY_ID(2));
  }
}
