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

  @Override
  public void setItemDetailsView(ItemDetailsView itemDetailsView) {
    this.itemDetailsView = itemDetailsView;
  }

  @Override
  public void setDatabase(Context context) {
    this.dataBase = new FeedsDataSource(context);
  }

  @Override
  public void onDestroy() {
    this.itemDetailsView = null;
    this.eventBus.unregister(this);
  }

  @Override
  public void updateReadItem(Item item) {
    this.item = item;
    this.rssApi.updateReadStateItem(item, true);
  }

  @Override
  public void updateStarStateItem(Item item) {
    this.item = item;

    if (item.isStarred()) {
      this.rssApi.updateStarStateItem(item, false);
    } else {
      this.rssApi.updateStarStateItem(item, true);
    }
  }

  private void updateReadItemByIdFromDatabase(Item item) {
    this.dataBase.updateReadStateItem(item, true);
  }

  private void updateStarItemByIdFromDatabase(Item item) {
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
        updateReadItemByIdFromDatabase(this.item);
        this.item = null;
        this.itemDetailsView.updateItemRead();
      }
    } else {
      this.itemDetailsView.showSnackBarError(event.getThrowable().getMessage());
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(UpdateStarStateItemEvent event) {
    if (event.isSuccess()) {
      if (this.itemDetailsView != null) {
        updateStarItemByIdFromDatabase(this.item);
        this.itemDetailsView.updateItemStarred(this.item.isStarred());
        this.item = null;
      }
    } else {
      this.itemDetailsView.showSnackBarError(event.getThrowable().getMessage());
    }
  }
}
