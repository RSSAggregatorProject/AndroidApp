package com.rssaggregator.android.feed.presenter;

import android.app.Activity;
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
import com.rssaggregator.android.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Presenter for the Main Activity.
 */
public class MainPresenterImpl implements MainPresenter {
  private RssApi rssApi;
  private EventBus eventBus;
  private MainView mainView;
  private Activity activity;
  private FeedsDataSource dataBase;
  private Integer channelId;

  @Inject
  public MainPresenterImpl(RssApi rssApi, EventBus eventBus) {
    this.rssApi = rssApi;
    this.eventBus = eventBus;
    this.eventBus.register(this);
  }

  /**
   * Sets Main View.
   *
   * @param mainView Main View.
   */
  @Override
  public void setMainView(MainView mainView) {
    this.mainView = mainView;
  }

  /**
   * Sets Database.
   *
   * @param context  Context
   * @param activity Activity.
   */
  @Override
  public void setDatabase(Context context, Activity activity) {
    this.dataBase = new FeedsDataSource(context);
    this.activity = activity;
  }

  /**
   * Called when the Main Activity is destroyed.
   */
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

  /**
   * Loads all data Online.
   */
  @Override
  public void loadAllData_Online() {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }
    this.rssApi.fetchData();
  }

  /**
   * Loads all data Offline.
   */
  @Override
  public void loadAllData_OffLine() {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }
    this.setMainCategoriesNavigation_Offline();
    List<Item> data = getAllItems_Database();
    this.mainView.showAllItemsContent(data);
  }

  /**
   * Fetches all data from database.
   */
  @Override
  public void fetchAllItems_Offline() {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }
    List<Item> data = getAllItems_Database();
    this.mainView.showAllItemsContent(data);
  }

  /**
   * Fetches starred items from database.
   */
  @Override
  public void fetchStarredItems_Offline() {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }
    List<Item> data = getStarredItems_Database();
    this.mainView.showStarredItemsContent(data);
  }

  /**
   * Fetches items by category from database.
   *
   * @param categoryId id of category.
   */
  @Override
  public void fetchItemsByCategoryId_Offline(Integer categoryId) {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }
    List<Item> data = getItemsByCategoryId_Database(categoryId);
    this.mainView.showItemsByCategoryIdContent(data);
  }

  /**
   * Fetches items by channel from database.
   *
   * @param channelId id of channel.
   */
  @Override
  public void fetchItemsByChannelId_Offline(Integer channelId) {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }
    List<Item> data = getItemsByChannelId_Database(channelId);
    this.mainView.showItemsByChannelIdContent(data);
  }

  /**
   * Unsubscribe to the channel.
   *
   * @param channel Channel to unsubscribe.
   */
  @Override
  public void unsubscribeChannel(Channel channel) {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }
    this.rssApi.unsubscribeFeed(channel.getChannelId());
  }

  /**
   * Puts all the items at read state.
   */
  @Override
  public void updateReadAllItems() {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }
    this.rssApi.updateReadAllItems();
  }

  /**
   * Puts items of a channel at read state.
   *
   * @param selectedChannel Channel to update.
   */
  @Override
  public void updateReadItemsByChannelId(Channel selectedChannel) {
    if (this.mainView != null) {
      this.mainView.showLoading();
    }
    this.channelId = selectedChannel.getChannelId();
    this.rssApi.updateReadItemsByChannelId(selectedChannel);
  }

  //
  //
  // Database methods.
  //
  //

  /**
   * Inserts data to the database.
   *
   * @param data Data
   */
  private void insertDataToDatabase(CategoriesWrapper data) {
    // Delete previous data in the database.
    deletePreviousData_Database();

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
        channel.setCategoryName(category.getName());
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
        item.setChannelName(channel.getName());
        item.setCategoryName(category.getName());
        this.dataBase.insertItem(item);
      }
    }
  }

  /**
   * Deletes rows in the database (All items from all channels)
   */
  private void deletePreviousData_Database() {
    this.dataBase.deleteAllCategories();
    this.dataBase.deleteAllChannels();
    this.dataBase.deleteAllItems();
  }

  /**
   * Gets categories from the database.
   *
   * @return List of Category.
   */
  private List<Category> getCategories_Database() {
    return this.dataBase.selectAllCategories();
  }

  /**
   * Get all items from the database.
   *
   * @return List of Items.
   */
  private List<Item> getAllItems_Database() {
    return this.dataBase.selectAllItems(SharedPreferencesUtils.getShowOnlyUnread(activity));
  }

  /**
   * Get starred items from the database.
   *
   * @return List of Items.
   */
  private List<Item> getStarredItems_Database() {
    return this.dataBase.selectStarredItems();
  }

  /**
   * Get items selected by the category id from the database.
   *
   * @param categoryId id of the category
   *
   * @return List of Items.
   */
  private List<Item> getItemsByCategoryId_Database(Integer categoryId) {
    return this.dataBase.selectItemsByCategoryId(categoryId,
        SharedPreferencesUtils.getShowOnlyUnread(activity));
  }

  /**
   * Get items selected by the channel id from the database.
   *
   * @param channelId id of the channel
   *
   * @return List of Items.
   */
  private List<Item> getItemsByChannelId_Database(Integer channelId) {
    return this.dataBase.selectItemsByChannelId(channelId,
        SharedPreferencesUtils.getShowOnlyUnread(activity));
  }

  private List<Channel> getChannelsByCategoryId_Database(Integer categoryId) {
    return this.dataBase.selectChannelsByCategoryId(categoryId);
  }

  /**
   * Gets number of unread items.
   *
   * @return Number of items.
   */
  public int getCountReadAllItems_Database() {
    return this.dataBase.selectCountUnreadAllItems();
  }

  public int getCountStarItems_Database() {
    return this.dataBase.selectCountStarItems();
  }

  /**
   * Updates All items to read state in the database.
   */
  private void updateReadAllItems_Database() {
    List<Item> items = getAllItems_Database();

    if (items != null && items.size() != 0) {
      for (Item item : items) {
        this.dataBase.updateReadStateItem(item, true);
      }
    }
  }

  /**
   * Updates items of a channel to read state in the database.
   *
   * @param channelId id of the channel.
   */
  private void updateReadItemsByChannelId_Database(Integer channelId) {
    List<Item> items = getItemsByChannelId_Database(channelId);

    if (items != null && items.size() != 0) {
      for (Item item : items) {
        this.dataBase.updateReadStateItem(item, true);
      }
    }
  }

  //
  //
  // Others methods.
  //
  //

  /**
   * Sets navigation drawer content when the app is offline.
   */
  private void setMainCategoriesNavigation_Offline() {
    List<Category> returnedCategoryList = new ArrayList<>();
    HashMap<Category, List<Channel>> returnedChannelList = new HashMap<>();

    List<Category> categoryList = getCategories_Database();
    if (categoryList != null && categoryList.size() != 0) {

      for (int i = 0; i < categoryList.size(); i++) {

        List<Channel> channelList = getChannelsByCategoryId_Database(
            categoryList.get(i).getCategoryId());

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
    this.mainView.setNavigationContent_Offline(returnedCategoryList, returnedChannelList);
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
        this.mainView.setNavigationContent_Online(data);
        insertDataToDatabase(data);
        fetchAllItems_Offline();
      }
    } else {
      List<Item> offlineData = getAllItems_Database();
      if (this.mainView != null) {
        this.setMainCategoriesNavigation_Offline();
        this.mainView.showAllItemsContent(offlineData);
        this.mainView.showSnackBarError(event.getThrowable().getMessage());
      }
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(UnsubscribeChannelEvent event) {
    if (event.isSuccess()) {
      if (this.mainView != null) {
        this.mainView.unsubscribeChannelSuccess();
        loadAllData_Online();
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
        this.updateReadAllItems_Database();
        this.fetchAllItems_Offline();
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
        this.updateReadItemsByChannelId_Database(this.channelId);
        this.fetchItemsByChannelId_Offline(this.channelId);
        this.channelId = null;
      }
    } else {
      this.mainView.showSnackBarError(event.getThrowable().getMessage());
    }
  }
}
