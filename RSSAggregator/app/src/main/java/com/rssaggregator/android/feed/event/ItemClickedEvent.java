package com.rssaggregator.android.feed.event;

import com.rssaggregator.android.network.model.Item;

/**
 * Event when Item is clicked on the list.
 */
public class ItemClickedEvent {

  private Item item;

  public ItemClickedEvent(Item item) {
    this.item = item;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }
}
