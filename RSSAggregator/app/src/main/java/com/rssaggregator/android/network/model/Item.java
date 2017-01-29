package com.rssaggregator.android.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Object representing an article in the RSS feed.
 */
public class Item implements Parcelable {

  @SerializedName("id_item")
  @Expose
  private Integer itemId;

  @Expose
  private String title;

  @Expose
  private String description;

  @SerializedName("pubDate")
  @Expose
  private Date pubDate;

  @SerializedName("link")
  @Expose
  private String linkUrl;

  @SerializedName("id_feed")
  @Expose
  private Integer channelId;

  @Expose
  private boolean read;

  @Expose
  private boolean starred;

  /**
   * Additional attributes for offline database.
   */
  private Integer categoryId;
  private String categoryName;
  private String channelName;

  public Integer getItemId() {
    return itemId;
  }

  public void setItemId(Integer itemId) {
    this.itemId = itemId;
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

  public Date getPubDate() {
    return pubDate;
  }

  public void setPubDate(Date pubDate) {
    this.pubDate = pubDate;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
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

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getChannelName() {
    return channelName;
  }

  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.itemId);
    dest.writeString(this.title);
    dest.writeString(this.description);
    dest.writeLong(this.pubDate != null ? this.pubDate.getTime() : -1);
    dest.writeString(this.linkUrl);
    dest.writeValue(this.channelId);
    dest.writeByte(this.read ? (byte) 1 : (byte) 0);
    dest.writeByte(this.starred ? (byte) 1 : (byte) 0);
    dest.writeValue(this.categoryId);
    dest.writeString(this.categoryName);
    dest.writeString(this.channelName);
  }

  public Item() {
  }

  protected Item(Parcel in) {
    this.itemId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.title = in.readString();
    this.description = in.readString();
    long tmpPubDate = in.readLong();
    this.pubDate = tmpPubDate == -1 ? null : new Date(tmpPubDate);
    this.linkUrl = in.readString();
    this.channelId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.read = in.readByte() != 0;
    this.starred = in.readByte() != 0;
    this.categoryId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.categoryName = in.readString();
    this.channelName = in.readString();
  }

  public static final Creator<Item> CREATOR = new Creator<Item>() {
    @Override
    public Item createFromParcel(Parcel source) {
      return new Item(source);
    }

    @Override
    public Item[] newArray(int size) {
      return new Item[size];
    }
  };
}
