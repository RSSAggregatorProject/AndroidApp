package com.rssaggregator.android.feed.event;

import com.rssaggregator.android.network.model.Item;

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
