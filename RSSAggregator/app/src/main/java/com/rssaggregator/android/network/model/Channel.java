package com.rssaggregator.android.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Channel {

  @SerializedName("id_feed")
  @Expose
  private Integer channelId;

  @Expose
  private String name;

  @SerializedName("favicon_uri")
  @Expose
  private String faviconUri;

  @Expose
  private Integer unread;

  @Expose
  private List<Item> items;

  private Integer categoryId;

  public Integer getChannelId() {
    return channelId;
  }

  public void setChannelId(Integer channelId) {
    this.channelId = channelId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFaviconUri() {
    return faviconUri;
  }

  public void setFaviconUri(String faviconUri) {
    this.faviconUri = faviconUri;
  }

  public Integer getUnread() {
    return unread;
  }

  public void setUnread(Integer unread) {
    this.unread = unread;
  }

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }
}
