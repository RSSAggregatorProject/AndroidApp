package com.rssaggregator.android.feed.presenter;

import android.content.Context;

import com.rssaggregator.android.feed.database.FeedsDataSource;
import com.rssaggregator.android.feed.view.MainView;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.event.FetchDataEvent;
import com.rssaggregator.android.network.event.UnsubscribeChannelEvent;
import com.rssaggregator.android.network.event.UpdateReadAllItemsEvent;
import com.rssaggregator.android.network.event.UpdateReadItemsByChannelIdEvent;
import com.rssaggregator.android.network.model.CategoriesWrapper;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;
import com.rssaggregator.android.network.model.Item;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class MainPresenterImpl implements MainPresenter {
  private RssApi rssApi;
  private EventBus eventBus;
  private MainView mainView;
  private FeedsDataSource dataBase;
  private Integer channelId;

  @Inject
  public MainPresenterImpl(RssApi rssApi, EventBus eventBus) {
    this.rssApi = rssApi;
    this.eventBus = eventBus;
    this.eventBus.register(this);
  }

  @Override
  public void setMainView(MainView mainView) {
    this.mainView = mainView;
  }

  @Override
  public void setDatabase(Context context) {
    this.dataBase = new FeedsDataSource(context);
  }

  @Override
  public void onDestroy() {
    this.mainView = null;
    this.eventBus.unregister(this);
  }

  //
  //
  // Api Methods.
  //
  //
  @Override
  public void loadAllData() {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }

    this.rssApi.fetchData();
  }

  @Override
  public void loadAllDataOffLine() {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }

    // Get categories
    List<Category> returnedCategoryList = new ArrayList<>();
    HashMap<Category, List<Channel>> returnedChannelList = new HashMap<>();

    List<Category> categoryList = getCategories();
    if (categoryList != null && categoryList.size() != 0) {

      for (int i = 0; i < categoryList.size(); i++) {

        List<Channel> channelList = getChannelsByCategoryId(categoryList.get(i).getCategoryId());

        if (channelList != null && channelList.size() != 0) {
          categoryList.get(i).setChannels(channelList);
          returnedChannelList.put(categoryList.get(i), channelList);
        } else {
          categoryList.get(i).setChannels(new ArrayList<Channel>());
          returnedChannelList.put(categoryList.get(i), new ArrayList<Channel>());
        }

        returnedCategoryList.add(categoryList.get(i));
      }
    }
    this.mainView.setNavigationContentOffline(returnedCategoryList, returnedChannelList);

    List<Item> data = getAllItemsFromDatabase();
    this.mainView.showAllItemsContent(data);
  }

  @Override
  public void fetchAllItems() {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }

    List<Item> data = getAllItemsFromDatabase();
    this.mainView.showAllItemsContent(data);
  }

  @Override
  public void fetchStarredItems() {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }

    List<Item> data = getStarredItemsFromDatabase();
    this.mainView.showStarredItemsContent(data);
  }

  @Override
  public void fetchItemsByCategoryId(Integer categoryId) {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }

    List<Item> data = getItemsByCategoryIdFromDatabase(categoryId);
    this.mainView.showItemsByCategoryIdContent(data);
  }

  @Override
  public void fetchItemsByChannelId(Integer channelId) {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }

    List<Item> data = getItemsByChannelIdFrommDatabase(channelId);
    this.mainView.showItemsByChannelIdContent(data);
  }

  @Override
  public void unsubscribeChannel(Channel channel) {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }
    this.rssApi.unsubscribeFeed(channel.getChannelId());
  }

  @Override
  public void updateReadAllItems() {
    this.rssApi.updateReadAllItems();
  }

  @Override
  public void updateReadItemsByChannelId(Channel selectedChannel) {
    this.channelId = selectedChannel.getChannelId();
    this.rssApi.updateReadItemsByChannelId(selectedChannel);
  }

  //
  //
  // Database methods.
  //
  //
  private void insertDataToDatabase(CategoriesWrapper data) {
    // Delete previous data in the database.
    deletePreviousData();

    /**
     * Insert Categories.
     */
    // Check if there are some categories to insert.
    if (data.getCategories() != null && data.getCategories().size() != 0) {

      // Loop to insert all categories.
      for (Category category : data.getCategories()) {
        this.dataBase.insertCategory(category);
        insertChannelsToDatabase(category);
      }
    }
    /**
     * End Insert Data.
     */
  }

  /**
   * Inserts channels to the database.
   *
   * @param category Category containing the channels.
   */
  private void insertChannelsToDatabase(Category category) {
    // Insert channels.
    if (category.getChannels() != null && category.getChannels().size() != 0) {

      // Loop to insert all channels.
      for (Channel channel : category.getChannels()) {
        // Add the parent category to the channel.
        channel.setCategoryId(category.getCategoryId());
        this.dataBase.insertChannel(channel);
        insertItemsToDatabase(channel, category);
      }
    }
  }

  /**
   * Insert items to the database.
   *
   * @param channel  Channel containing the items.
   * @param category Category containing the channel.
   */
  private void insertItemsToDatabase(Channel channel, Category category) {
    // Insert items.
    if (channel.getItems() != null && channel.getItems().size() != 0) {

      // Loop to insert all items.
      for (Item item : channel.getItems()) {
        // Add the parent category and the parent channel to the item.
        item.setCategoryId(category.getCategoryId());
        item.setChannelId(channel.getChannelId());
        item.setNameChannel(channel.getName());
        this.dataBase.insertItem(item);
      }
    }
  }

  /**
   * Deletes rows in the database (All items from all channels)
   */
  private void deletePreviousData() {
    this.dataBase.deleteAllCategories();
    this.dataBase.deleteAllChannels();
    this.dataBase.deleteAllItems();
  }

  /**
   * Get all items from the database.
   *
   * @return List of Items.
   */
  private List<Item> getAllItemsFromDatabase() {
    return this.dataBase.selectAllItems();
  }

  /**
   * Get starred items from the database.
   *
   * @return List of Items.
   */
  private List<Item> getStarredItemsFromDatabase() {
    return this.dataBase.selectStarredItems();
  }

  /**
   * Get items selected by the category id from the database.
   *
   * @param categoryId id of the category
   *
   * @return List of Items.
   */
  private List<Item> getItemsByCategoryIdFromDatabase(Integer categoryId) {
    return this.dataBase.selectItemsByCategoryId(categoryId);
  }

  /**
   * Get items selected by the channel id from the database.
   *
   * @param channelId id of the channel
   *
   * @return List of Items.
   */
  private List<Item> getItemsByChannelIdFrommDatabase(Integer channelId) {
    return this.dataBase.selectItemsByChannelId(channelId);
  }

  /**
   * Gets categories from the database.
   *
   * @return List of Category.
   */
  private List<Category> getCategories() {
    return this.dataBase.selectAllCategories();
  }

  private List<Channel> getChannelsByCategoryId(Integer categoryId) {
    return this.dataBase.selectChannelsByCategoryId(categoryId);
  }

  private void updateReadAllItemsFromDatabase() {
    List<Item> items = getAllItemsFromDatabase();

    if (items != null && items.size() != 0) {
      for (Item item : items) {
        this.dataBase.updateReadStateItem(item, true);
      }
    }
  }

  private void updateReadItemsByChannelIdFromDatabase(Integer channelId) {
    List<Item> items = getItemsByChannelIdFrommDatabase(channelId);

    if (items != null && items.size() != 0) {
      for (Item item : items) {
        this.dataBase.updateReadStateItem(item, true);
      }
    }
  }

  //
  //
  // Events methods.
  //
  //
  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(FetchDataEvent event) {
    if (event.isSuccess()) {
      if (this.mainView != null) {
        CategoriesWrapper data = event.getData();
        this.mainView.setNavigationContent(data);
        insertDataToDatabase(data);
        fetchAllItems();
      }
    } else {
      List<Item> offlineData = getAllItemsFromDatabase();
      if (this.mainView != null) {
        this.mainView.showAllItemsContent(offlineData);
        this.mainView.showSnackBarError(event.getThrowable().getMessage());
      }
      /*if (this.mainView != null) {
        this.mainView.showError(event.getThrowable().getMessage());
      }*/
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(UnsubscribeChannelEvent event) {
    if (event.isSuccess()) {
      if (this.mainView != null) {
        this.mainView.unsubscribeChannelSuccess();
        loadAllData();
      }
    } else {
      this.mainView.showSnackBarError(event.getThrowable().getMessage());
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(UpdateReadAllItemsEvent event) {
    if (event.isSuccess()) {
      if (this.mainView != null) {
        this.updateReadAllItemsFromDatabase();
        this.fetchAllItems();
      }
    } else {
      this.mainView.showSnackBarError(event.getThrowable().getMessage());
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(UpdateReadItemsByChannelIdEvent event) {
    if (event.isSuccess()) {
      if (this.mainView != null) {
        this.updateReadItemsByChannelIdFromDatabase(this.channelId);
        this.fetchItemsByChannelId(this.channelId);
        this.channelId = null;
      }
    } else {
      this.mainView.showSnackBarError(event.getThrowable().getMessage());
    }
  }
}
