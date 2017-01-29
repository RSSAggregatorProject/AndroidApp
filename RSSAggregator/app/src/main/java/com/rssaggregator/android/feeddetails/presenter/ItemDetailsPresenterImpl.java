package com.rssaggregator.android.feeddetails.presenter;

import android.content.Context;

import com.rssaggregator.android.feed.database.FeedsDataSource;
import com.rssaggregator.android.feeddetails.view.ItemDetailsView;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.event.UpdateReadStateItemEvent;
import com.rssaggregator.android.network.event.UpdateStarStateItemEvent;
import com.rssaggregator.android.network.model.Item;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Presenter for Item Details View.
 */
public class ItemDetailsPresenterImpl implements ItemDetailsPresenter {
  private RssApi rssApi;
  private EventBus eventBus;
  private ItemDetailsView itemDetailsView;
  private FeedsDataSource dataBase;

  private Item item;

  @Inject
  public ItemDetailsPresenterImpl(RssApi rssApi, EventBus eventBus) {
    this.rssApi = rssApi;
    this.eventBus = eventBus;
    this.eventBus.register(this);
  }

  /**
   * Sets the Item Details View.
   *
   * @param itemDetailsView Item Details View.
   */
  @Override
  public void setItemDetailsView(ItemDetailsView itemDetailsView) {
    this.itemDetailsView = itemDetailsView;
  }

  /**
   * Sets the database.
   *
   * @param context Context.
   */
  @Override
  public void setDatabase(Context context) {
    this.dataBase = new FeedsDataSource(context);
  }

  /**
   * Called when the Item Details Activity is destroyed.
   */
  @Override
  public void onDestroy() {
    this.itemDetailsView = null;
    this.eventBus.unregister(this);
  }

  /**
   * Updates the read state to true.
   *
   * @param item Item to update.
   */
  @Override
  public void updateReadItem(Item item) {
    this.item = item;
    // Pass the item to read state in any case.
    updateReadItemById_Database(this.item);

    if (this.item.isRead()) {
      this.rssApi.updateReadStateItem(item, false);
    } else {
      this.rssApi.updateReadStateItem(item, true);
    }
  }

  /**
   * Updates the star state
   *
   * @param item Item to update.
   */
  @Override
  public void updateStarStateItem(Item item) {
    this.item = item;

    if (item.isStarred()) {
      this.rssApi.updateStarStateItem(item, false);
      updateStarItemById_Database(this.item);
    } else {
      this.rssApi.updateStarStateItem(item, true);
      updateStarItemById_Database(this.item);

    }
  }

  /**
   * Updates the read state item in the database
   *
   * @param item Item to update.
   */
  private void updateReadItemById_Database(Item item) {
    if (this.item.isRead()) {
      this.dataBase.updateReadStateItem(item, false);
    } else {
      this.dataBase.updateReadStateItem(item, true);
    }
  }

  /**
   * Updates the star state item in the database.
   *
   * @param item Item to update.
   */
  private void updateStarItemById_Database(Item item) {
    if (this.item.isStarred()) {
      this.dataBase.updateStarStateItem(item, false);
    } else {
      this.dataBase.updateStarStateItem(item, true);
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(UpdateReadStateItemEvent event) {
    if (event.isSuccess()) {
      if (this.itemDetailsView != null) {
        this.itemDetailsView.updateItemRead(this.item.isRead());
        this.item = null;
      }
    } else {
      this.itemDetailsView.showSnackBarError();
      this.itemDetailsView.updateItemRead(this.item.isRead());
      this.item = null;
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(UpdateStarStateItemEvent event) {
    if (event.isSuccess()) {
      if (this.itemDetailsView != null) {
        this.itemDetailsView.updateItemStarred(this.item.isStarred());
        this.item = null;
      }
    } else {
      this.itemDetailsView.showSnackBarError();
      this.itemDetailsView.updateItemStarred(this.item.isStarred());
      this.item = null;
    }
  }
}
