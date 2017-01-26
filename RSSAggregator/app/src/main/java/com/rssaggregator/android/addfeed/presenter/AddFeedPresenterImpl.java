package com.rssaggregator.android.addfeed.presenter;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.rssaggregator.android.addfeed.view.AddFeedView;
import com.rssaggregator.android.feed.database.FeedsDataSource;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.event.AddCategoryEvent;
import com.rssaggregator.android.network.event.AddFeedEvent;
import com.rssaggregator.android.network.model.Category;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

public class AddFeedPresenterImpl implements AddFeedPresenter {
  private RssApi rssApi;
  private EventBus eventBus;
  private AddFeedView addFeedView;
  private FeedsDataSource dataBase;

  @Inject
  public AddFeedPresenterImpl(RssApi rssApi, EventBus eventBus) {
    this.rssApi = rssApi;
    this.eventBus = eventBus;
    this.eventBus.register(this);
  }

  @Override
  public void setAddFeedView(AddFeedView addFeedView) {
    this.addFeedView = addFeedView;
  }

  @Override
  public void setDatabase(Context context) {
    this.dataBase = new FeedsDataSource(context);
  }

  @Override
  public void onDestroy() {
    this.addFeedView = null;
    this.eventBus.unregister(this);
  }

  @Override
  public void fetchCategories() {
    List<Category> categoryList = this.dataBase.getCategories();
    this.addFeedView.setCategoriesToSpinner(categoryList);
  }

  @Override
  public void addCategory(String categoryName) {
    this.rssApi.addCategory(categoryName);
  }

  @Override
  public void addFeed(Category category, String rssLink) {
    this.rssApi.subscribeFeed(category, rssLink);
  }


  //
  //
  // Event methods
  //
  //
  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(AddCategoryEvent event) {
    if (event.isSuccess()) {
      if (this.addFeedView != null) {
        Category category = new Category();
        category.setName("test");
        category.setCategoryId(10);
        category.setUnread(0);
        this.dataBase.insertCategory(category);
        fetchCategories();
      }
    } else {
      Logger.e("Error");
    }
  }


  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(AddFeedEvent event) {
    if (event.isSuccess()) {
      if (this.addFeedView != null) {
        this.addFeedView.showFeedAdded();
      }
    } else {
      Logger.e("Error");
    }
  }
}
