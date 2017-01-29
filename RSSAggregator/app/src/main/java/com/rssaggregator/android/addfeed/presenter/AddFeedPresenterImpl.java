package com.rssaggregator.android.addfeed.presenter;

import android.content.Context;

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

/**
 * Presenter fot Add Feed View.
 */
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

  /**
   * Fetches categories from the database.
   */
  @Override
  public void fetchCategories_Database() {
    List<Category> categoryList = this.dataBase.selectAllCategories();
    this.addFeedView.setCategoriesToSpinner(categoryList);
  }

  /**
   * Adds a category thanks to the API.
   *
   * @param categoryName name of the category.
   */
  @Override
  public void addCategory(String categoryName) {
    this.rssApi.addCategory(categoryName);
  }

  /**
   * Subscribes to the feed thanks to the API.
   *
   * @param category category to add the feed.
   * @param rssLink  link of the feed
   */
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
        Category category = event.getData();
        category.setUnread(0);
        this.dataBase.insertCategory(category);
        fetchCategories_Database();
        this.addFeedView.updateCategoryCreated(category.getName());
      }
    } else {
      if (this.addFeedView != null) {
        this.addFeedView.showSnackbarError(event.getThrowable().getMessage());
      }
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
      if (this.addFeedView != null) {
        this.addFeedView.showSnackbarError(event.getThrowable().getMessage());
      }
    }
  }
}
