package com.rssaggregator.android.utils;

import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;
import com.rssaggregator.android.network.model.Item;

import java.util.List;

public class ArrayUtils {

  /**
   * Counts the number of unread items in a category.
   *
   * @param channelList
   *
   * @return int Number of unreads items.
   */
  public static int getUnreadCategories(List<Channel> channelList) {
    int count = 0;

    if (channelList == null || channelList.size() == 0) {
      return count;
    }

    for (Channel channel : channelList) {
      if (channel != null && channel.getUnread() != null) {
        count += channel.getUnread();
      }
    }
    return count;
  }

  public static int getPositionInList(List<Item> itemList, Item item) {
    if (item == null || itemList == null || itemList.size() == 0) {
      return 0;
    }
    for (int i = 0; i < itemList.size(); i++) {
      if (item.getItemId().equals(itemList.get(i).getItemId())) {
        return i;
      }
    }
    return 0;
  }

  public static int getUnreadAllItemsCount(List<Category> categories) {
    int count = 0;

    if (categories == null || categories.size() == 0) {
      return 0;
    }
    for (Category category : categories) {
      if (category.getChannels() != null && category.getChannels().size() != 0) {
        for (Channel channel : category.getChannels()) {
          if (channel.getItems() != null && channel.getItems().size() != 0) {
            for (Item item : channel.getItems()) {
              if (item != null) {
                if (!item.isRead()) {
                  count++;
                }
              }
            }
          }
        }
      }
    }
    return count;
  }

  public static int getStarItemsCount(List<Category> categories) {
    int count = 0;

    if (categories == null || categories.size() == 0) {
      return 0;
    }
    for (Category category : categories) {
      if (category.getChannels() != null && category.getChannels().size() != 0) {
        for (Channel channel : category.getChannels()) {
          if (channel.getItems() != null && channel.getItems().size() != 0) {
            for (Item item : channel.getItems()) {
              if (item != null) {
                if (item.isStarred()) {
                  count++;
                }
              }
            }
          }
        }
      }
    }
    return count;
  }

  public static Category getCategoryByName(List<Category> categories, String categoryName) {
    if (categories == null || categories.size() == 0) {
      return null;
    }

    for (Category category : categories) {
      if (categoryName.equals(category.getName())) {
        return category;
      }
    }
    return null;
  }
}
