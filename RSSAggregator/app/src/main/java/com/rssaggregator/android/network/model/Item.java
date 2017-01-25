package com.rssaggregator.android.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Item {

  @SerializedName("id_item")
  @Expose
  private Integer itemId;

  @Expose
  private String name;

  @Expose
  private String title;

  @Expose
  private String description;

  @SerializedName("link")
  @Expose
  private String linkUrl;

  @SerializedName("pubDate")
  @Expose
  private Date pubDate;

  @SerializedName("id_feed")
  @Expose
  private Integer channelId;

  @Expose
  private boolean read;

  @Expose
  private boolean starred;

  private Integer categoryId;

  public Integer getItemId() {
    return itemId;
  }

  public void setItemId(Integer itemId) {
    this.itemId = itemId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
  }

  public Date getPubDate() {
    return pubDate;
  }

  public void setPubDate(Date pubDate) {
    this.pubDate = pubDate;
  }

  public Integer getChannelId() {
    return channelId;
  }

  public void setChannelId(Integer channelId) {
    this.channelId = channelId;
  }

  public boolean isRead() {
    return read;
  }

  public void setRead(boolean read) {
    this.read = read;
  }

  public boolean isStarred() {
    return starred;
  }

  public void setStarred(boolean starred) {
    this.starred = starred;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }
}
