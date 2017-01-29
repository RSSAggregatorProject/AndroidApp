package com.rssaggregator.android.utils;

import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;
import com.rssaggregator.android.network.model.Item;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtilsTest {

  private List<Channel> setDataGetUnreadCategories() {
    List<Channel> channels = new ArrayList<>();

    Channel channel = new Channel();
    channel.setUnread(5);
    Channel channel1 = new Channel();
    channel1.setUnread(23);
    channels.add(channel);
    channels.add(channel1);
    return channels;
  }

  private List<Channel> setNullDataGetUnreadCategories() {
    List<Channel> channels = new ArrayList<>();

    Channel channel = new Channel();
    channel.setUnread(null);
    Channel channel1 = new Channel();
    channel1.setUnread(null);
    channels.add(channel);
    channels.add(channel1);
    return channels;
  }

  private List<Item> setDataGetPositionInList() {
    List<Item> items = new ArrayList<>();

    Item item1 = new Item();
    item1.setItemId(1);
    Item item2 = new Item();
    item2.setItemId(2);
    Item item3 = new Item();
    item3.setItemId(3);
    Item item4 = new Item();
    item4.setItemId(4);
    Item item5 = new Item();
    item1.setItemId(5);
    items.add(item1);
    items.add(item2);
    items.add(item3);
    items.add(item4);
    items.add(item5);

    return items;
  }

  private List<Category> setDataGetUnreadAllItemsCount() {
    List<Category> categories = new ArrayList<>();
    List<Channel> channels = new ArrayList<>();
    List<Item> items = new ArrayList<>();

    Category category = new Category();
    Channel channel = new Channel();
    Item itemFalse = new Item();
    itemFalse.setRead(false);
    Item itemTrue = new Item();
    itemTrue.setRead(true);
    Item item = new Item();
    item.setRead(false);
    items.add(item);
    items.add(itemFalse);
    items.add(itemTrue);
    channel.setItems(items);
    channels.add(channel);
    category.setChannels(channels);
    categories.add(category);

    return categories;
  }

  private List<Category> setDataGetStarItemsCount() {
    List<Category> categories = new ArrayList<>();
    List<Channel> channels = new ArrayList<>();
    List<Item> items = new ArrayList<>();

    Category category = new Category();
    Channel channel = new Channel();
    Item itemStarred = new Item();
    itemStarred.setStarred(true);
    Item itemTrue = new Item();
    itemTrue.setStarred(true);
    Item item = new Item();
    item.setStarred(true);
    items.add(item);
    items.add(itemStarred);
    items.add(itemTrue);
    channel.setItems(items);
    channels.add(channel);
    category.setChannels(channels);
    categories.add(category);

    return categories;
  }

  private List<Category> setDataGetCategoryByName() {
    List<Category> categories = new ArrayList<>();
    Category category = new Category();
    Category category1 = new Category();
    Category category2 = new Category();
    category.setName("Category");
    category.setCategoryId(0);
    category1.setName("Category1");
    category1.setCategoryId(1);
    category2.setName("Category2");
    category2.setCategoryId(2);
    categories.add(category);
    categories.add(category1);
    categories.add(category2);
    return categories;
  }

  @Test
  public void getUnreadCategoriesNull_Test() {
    Assert.assertEquals(0, ArrayUtils.getUnreadCategories(null));
  }

  @Test
  public void getUnreadCategoriesEmpty_Test() {
    Assert.assertEquals(0, ArrayUtils.getUnreadCategories(new ArrayList<Channel>()));
  }

  @Test
  public void getUnreadCategoriesFilled_Test() {
    Assert.assertEquals(28, ArrayUtils.getUnreadCategories(setDataGetUnreadCategories()));
  }

  @Test
  public void getUnreadCategoriesFilledNull_Test() {
    Assert.assertEquals(0, ArrayUtils.getUnreadCategories(setNullDataGetUnreadCategories()));
  }

  @Test
  public void getPositionInListNullNull_Test() {
    Assert.assertEquals(0, ArrayUtils.getPositionInList(null, null));
  }

  @Test
  public void getPositionInListEmptyNull_Test() {
    Assert.assertEquals(0, ArrayUtils.getPositionInList(new ArrayList<Item>(), null));
  }

  @Test
  public void getPositionInListEmptyEmpty_Test() {
    Assert.assertEquals(0, ArrayUtils.getPositionInList(new ArrayList<Item>(), new Item()));
  }

  @Test
  public void getPositionInListNullEmpty_Test() {
    Assert.assertEquals(0, ArrayUtils.getPositionInList(null, new Item()));
  }

  @Test
  public void getPositionInListFillNull_Test() {
    Assert.assertEquals(0, ArrayUtils.getPositionInList(setDataGetPositionInList(), null));
  }

  @Test
  public void getPositionInListFilled_Test() {
    Item item = new Item();
    item.setItemId(2);
    Assert.assertEquals(1, ArrayUtils.getPositionInList(setDataGetPositionInList(), item));
    item.setItemId(4);
    Assert.assertEquals(3, ArrayUtils.getPositionInList(setDataGetPositionInList(), item));
  }

  @Test
  public void getUnreadAllItemsCountNull_Test() {
    Assert.assertEquals(0, ArrayUtils.getUnreadAllItemsCount(null));
  }

  @Test
  public void getUnreadAllItemsCountEmpty_Test() {
    Assert.assertEquals(0, ArrayUtils.getUnreadAllItemsCount(new ArrayList<Category>()));
  }

  @Test
  public void getUnreadAllItemsCountFilled_Test() {
    Assert.assertEquals(2, ArrayUtils.getUnreadAllItemsCount(setDataGetUnreadAllItemsCount()));
  }

  @Test
  public void getStarItemsCountNull_Test() {
    Assert.assertEquals(0, ArrayUtils.getStarItemsCount(null));
  }

  @Test
  public void getStarItemsCountEmpty_Test() {
    Assert.assertEquals(0, ArrayUtils.getStarItemsCount(new ArrayList<Category>()));
  }

  @Test
  public void getStarItemsCountFilled_Test() {
    Assert.assertEquals(3, ArrayUtils.getStarItemsCount(setDataGetStarItemsCount()));
  }

  @Test
  public void getCategoryByNameNull_Test() {
    Assert.assertEquals(null, ArrayUtils.getCategoryByName(null, "Test"));
  }

  @Test
  public void getCategoryByNameEmpty_Test() {
    Assert.assertEquals(null, ArrayUtils.getCategoryByName(new ArrayList<Category>(), "Test"));
  }

  @Test
  public void getCategoryByNameFilled_Test() {
    Category category = ArrayUtils.getCategoryByName(setDataGetCategoryByName(), "Category1");
    Assert.assertEquals("1", category.getCategoryId().toString());
  }
}
