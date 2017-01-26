package com.rssaggregator.android.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Item implements Parcelable {

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
  private String nameChannel;

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

  public String getNameChannel() {
    return nameChannel;
  }

  public void setNameChannel(String nameChannel) {
    this.nameChannel = nameChannel;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.itemId);
    dest.writeString(this.name);
    dest.writeString(this.title);
    dest.writeString(this.description);
    dest.writeString(this.linkUrl);
    dest.writeLong(this.pubDate != null ? this.pubDate.getTime() : -1);
    dest.writeValue(this.channelId);
    dest.writeByte(this.read ? (byte) 1 : (byte) 0);
    dest.writeByte(this.starred ? (byte) 1 : (byte) 0);
    dest.writeValue(this.categoryId);
    dest.writeString(this.nameChannel);
  }

  public Item() {
  }

  protected Item(Parcel in) {
    this.itemId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.name = in.readString();
    this.title = in.readString();
    this.description = in.readString();
    this.linkUrl = in.readString();
    long tmpPubDate = in.readLong();
    this.pubDate = tmpPubDate == -1 ? null : new Date(tmpPubDate);
    this.channelId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.read = in.readByte() != 0;
    this.starred = in.readByte() != 0;
    this.categoryId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.nameChannel = in.readString();
  }

  public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
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
