package com.rssaggregator.android.feed.event;

import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;

/**
 * Event when the user clicks on a category/channel of the navigation drawer.
 */
public class NavigationItemClickedEvent {
  private boolean isAll;
  private boolean isStar;
  private Category category;
  private Channel channel;

  public NavigationItemClickedEvent(Category category) {
    this.category = category;
    this.channel = null;
    this.isAll = false;
    this.isStar = false;
  }

  public NavigationItemClickedEvent(Channel channel, Category category) {
    this.channel = channel;
    this.category = category;
    this.isAll = false;
    this.isStar = false;
  }

  public NavigationItemClickedEvent(boolean isAll, boolean isStar) {
    this.isAll = isAll;
    this.isStar = isStar;
    this.category = null;
    this.channel = null;
  }


  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Channel getChannel() {
    return channel;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  public boolean isAll() {
    return isAll;
  }

  public void setAll(boolean all) {
    isAll = all;
  }

  public boolean isStar() {
    return isStar;
  }

  public void setStar(boolean star) {
    isStar = star;
  }
}
