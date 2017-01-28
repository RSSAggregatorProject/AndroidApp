package com.rssaggregator.android;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.rssaggregator.android.addfeed.view.AddFeedActivity;
import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.feed.adapter.ItemsAdapter;
import com.rssaggregator.android.feed.event.ItemClickedEvent;
import com.rssaggregator.android.feed.event.NavigationItemClickedEvent;
import com.rssaggregator.android.feed.presenter.MainPresenterImpl;
import com.rssaggregator.android.feed.view.MainView;
import com.rssaggregator.android.login.LoginActivity;
import com.rssaggregator.android.navigationdrawer.ExpandableListAdapter;
import com.rssaggregator.android.network.event.AccessTokenFetchedEvent;
import com.rssaggregator.android.network.event.LogOutEvent;
import com.rssaggregator.android.network.model.AccessToken;
import com.rssaggregator.android.network.model.CategoriesWrapper;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;
import com.rssaggregator.android.network.model.Item;
import com.rssaggregator.android.network.utils.TokenRequestInterceptor;
import com.rssaggregator.android.utils.ArrayUtils;
import com.rssaggregator.android.utils.BaseActivity;
import com.rssaggregator.android.utils.Globals;
import com.rssaggregator.android.utils.MethodsUtils;
import com.rssaggregator.android.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity for the Main View.
 */
public class MainActivity extends BaseActivity implements MainView {

  // Type of category/channel selected.
  private int LIST_ITEMS_TYPE;

  /**
   * Navigation Drawer Views.
   */
  @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.navigationView) NavigationView navigationView;
  @BindView(R.id.expandableListView) ExpandableListView expandableListView;

  /**
   * Content Views.
   */
  @BindView(R.id.rootView) RelativeLayout rootViewRl;
  @BindView(R.id.itemsRecyclerView) RecyclerView itemsRecyclerView;
  @BindView(R.id.contentView) RelativeLayout contentView;
  @BindView(R.id.loadingView) RelativeLayout loadingView;
  @BindView(R.id.errorView) AppCompatTextView errorView;
  @BindView(R.id.emptyView) AppCompatTextView emptyView;

  @Inject TokenRequestInterceptor requestInterceptor;

  /**
   * Adapters.
   */
  private ExpandableListAdapter adapter;
  private ItemsAdapter itemsAdapter;

  /**
   * Data.
   */
  private List<Category> categoriesList;
  private HashMap<Category, List<Channel>> channelsList;
  private Category selectedCategory = null;
  private Channel selectedChannel = null;
  private Item itemClicked = null;

  /**
   * Network
   */
  private MainPresenterImpl presenter;
  private EventBus eventBus;

  /**
   * Menu Items
   */
  private MenuItem unsubscribeItem;
  private MenuItem markAsReadItem;

  // Others
  private Resources resources;

  //
  //
  // Life Cycle Methods.
  //
  //
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    this.resources = getResources();
    injectDependencies();
    String apiToken = SharedPreferencesUtils.getApiToken(this);
    Integer userId = SharedPreferencesUtils.getUserId(this);

    if (apiToken != null && apiToken.length() != 0) {
      AccessToken accessToken = new AccessToken();
      accessToken.setApiToken(apiToken);
      accessToken.setUserId(userId);
      eventBus.post(new AccessTokenFetchedEvent(accessToken));
    }

    initializeNavigationDrawer();

    if (MethodsUtils.isNetworkAvailable(this)) {
      this.presenter.loadAllData_Online();
    } else {
      this.presenter.loadAllData_OffLine();
    }

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    this.itemsRecyclerView.addItemDecoration(new DividerItemDecoration(this,
        DividerItemDecoration.VERTICAL));
    this.itemsRecyclerView.setLayoutManager(linearLayoutManager);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (!this.eventBus.isRegistered(this)) {
      this.eventBus.register(this);
    }
  }

  @Override
  protected void onStop() {
    if (this.eventBus.isRegistered(this)) {
      this.eventBus.unregister(this);
    }
    super.onStop();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    if (LIST_ITEMS_TYPE == Globals.LIST_ALL_ITEMS_TYPE) {
      this.presenter.fetchAllItems_Offline();
    } else if (LIST_ITEMS_TYPE == Globals.LIST_STAR_ITEMS_TYPE) {
      this.presenter.fetchStarredItems_Offline();
    } else if (LIST_ITEMS_TYPE == Globals.LIST_CATEGORY_ITEMS_TYPE) {
      this.presenter.fetchItemsByCategoryId_Offline(selectedCategory.getCategoryId());
    } else if (LIST_ITEMS_TYPE == Globals.LIST_CHANNEL_ITEMS_TYPE) {
      this.presenter.fetchItemsByChannelId_Offline(selectedChannel.getChannelId());
    }
  }

  @Override
  protected void onDestroy() {
    this.presenter.onDestroy();
    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
    if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
      this.drawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    this.unsubscribeItem = menu.findItem(R.id.action_unsubscribe);
    this.unsubscribeItem.setVisible(false);
    this.markAsReadItem = menu.findItem(R.id.action_mark_as_read);
    this.markAsReadItem.setVisible(true);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case R.id.action_refresh:
        if (MethodsUtils.isNetworkAvailable(this)) {
          this.presenter.loadAllData_Online();
        } else {
          this.presenter.loadAllData_OffLine();
        }
        break;
      case R.id.action_add_channel:
        Intent intent = new Intent(this, AddFeedActivity.class);
        startActivityForResult(intent, Globals.ADD_FEED_ACTIVITY);
        break;
      case R.id.action_unsubscribe:
        if (MethodsUtils.isNetworkAvailable(this)) {
          this.presenter.unsubscribeChannel(selectedChannel);
        } else {
          Snackbar.make(this.rootViewRl, resources.getString(R.string.network_error),
              Snackbar.LENGTH_SHORT).show();
        }
        break;
      case R.id.action_mark_as_read:
        if (LIST_ITEMS_TYPE == Globals.LIST_ALL_ITEMS_TYPE) {
          this.presenter.updateReadAllItems();
        } else if (LIST_ITEMS_TYPE == Globals.LIST_CHANNEL_ITEMS_TYPE) {
          this.presenter.updateReadItemsByChannelId(selectedChannel);
        }
        break;
    }
    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Globals.ADD_FEED_ACTIVITY) {
      if (resultCode == RESULT_OK) {
        Snackbar.make(this.rootViewRl,
            resources.getString(R.string.add_feed_success), Snackbar.LENGTH_SHORT).show();
        if (MethodsUtils.isNetworkAvailable(this)) {
          this.presenter.loadAllData_Online();
        } else {
          this.presenter.loadAllData_OffLine();
        }
      }
    }
  }

  //
  //
  // Others methods.
  //
  //

  /**
   * Injects dependencies.
   */
  private void injectDependencies() {
    AppComponent appComponent = RssAggregatorApplication.get(this).getAppComponent();
    this.eventBus = appComponent.bus();
    this.presenter = appComponent.mainPresenterImpl();
    this.presenter.setMainView(this);
    this.presenter.setDatabase(this);
  }

  /**
   * Initializes the Navigation Drawer.
   */
  private void initializeNavigationDrawer() {
    // Set toolbar title.
    setSupportActionBar(this.toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(getString(R.string.category_all));
    }

    initializeHeader();
    initializeMainCategories(0, 0);

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawerLayout, toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawerLayout.setDrawerListener(toggle);
    toggle.syncState();

    // Set Expandable list view adapter.
    this.adapter = new ExpandableListAdapter(this, categoriesList, channelsList, eventBus);
    this.expandableListView.setAdapter(this.adapter);
  }

  /**
   * Initializes Navigation Header View.
   */
  private void initializeHeader() {
    View headerView = navigationView.getHeaderView(0);
    AppCompatTextView userEmailTv = (AppCompatTextView) headerView.findViewById(R.id.userEmail);
    AppCompatImageView logoutButtonIg = (AppCompatImageView)
        headerView.findViewById(R.id.logoutButton);

    String userEmail = SharedPreferencesUtils.getUserEmail(this);
    userEmailTv.setText(userEmail);

    logoutButtonIg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        eventBus.post(new LogOutEvent());
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
      }
    });
  }

  /**
   * Initializes the All And Starred Items categories.
   */
  private void initializeMainCategories(int unreadAll, int unreadStar) {
    this.categoriesList = new ArrayList<>();
    this.channelsList = new HashMap<>();

    Category allCategory = new Category();
    allCategory.setName(resources.getString(R.string.category_all));
    allCategory.setUnread(unreadAll);
    Category starredItemsCategory = new Category();
    starredItemsCategory.setName(resources.getString(R.string.category_star));
    starredItemsCategory.setUnread(unreadStar);
    this.categoriesList.add(allCategory);
    this.categoriesList.add(starredItemsCategory);
  }

  //
  //
  // EventBus methods.
  //
  //
  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(NavigationItemClickedEvent event) {
    if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
      this.drawerLayout.closeDrawer(GravityCompat.START);
    }
    /**
     * Click on All category.
     */
    if (event.isAll()) {
      this.LIST_ITEMS_TYPE = Globals.LIST_ALL_ITEMS_TYPE;
      this.unsubscribeItem.setVisible(false);
      this.markAsReadItem.setVisible(true);
      if (getSupportActionBar() != null) {
        getSupportActionBar().setTitle(getString(R.string.category_all));
        this.presenter.fetchAllItems_Offline();
      }
      return;
    }
    /**
     * Click on Starred Items category.
     */
    if (event.isStar()) {
      this.LIST_ITEMS_TYPE = Globals.LIST_STAR_ITEMS_TYPE;
      this.unsubscribeItem.setVisible(false);
      this.markAsReadItem.setVisible(false);
      if (getSupportActionBar() != null) {
        getSupportActionBar().setTitle(getString(R.string.category_star));
        this.presenter.fetchStarredItems_Offline();
      }
      return;
    }
    /**
     * Click on a Category.
     */
    if (event.getChannel() == null) {
      this.LIST_ITEMS_TYPE = Globals.LIST_CATEGORY_ITEMS_TYPE;
      this.unsubscribeItem.setVisible(false);
      this.markAsReadItem.setVisible(false);
      Category category = event.getCategory();
      this.selectedCategory = category;
      if (getSupportActionBar() != null) {
        getSupportActionBar().setTitle(category.getName());
      }
      this.presenter.fetchItemsByCategoryId_Offline(category.getCategoryId());
    } else {
      /**
       * Click on a Channel.
       */
      this.LIST_ITEMS_TYPE = Globals.LIST_CHANNEL_ITEMS_TYPE;
      this.unsubscribeItem.setVisible(true);
      this.markAsReadItem.setVisible(true);
      Category category = event.getCategory();
      Channel channel = event.getChannel();
      this.selectedChannel = channel;
      this.selectedCategory = category;
      if (getSupportActionBar() != null) {
        getSupportActionBar().setTitle(resources.getString(R.string.category_channel,
            channel.getName(), category.getName()));
      }
      this.presenter.fetchItemsByChannelId_Offline(channel.getChannelId());
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(ItemClickedEvent event) {
    this.itemClicked = event.getItem();
  }

  //
  //
  // Methods called from the presenter.
  //
  //

  /**
   * Shows loading view.
   */
  @Override
  public void showLoading() {
    this.contentView.setVisibility(View.GONE);
    this.loadingView.setVisibility(View.VISIBLE);
    this.errorView.setVisibility(View.GONE);
    this.emptyView.setVisibility(View.GONE);
  }

  /**
   * Shows snackbar error.
   *
   * @param errorMessage Error message.
   */
  @Override
  public void showSnackBarError(String errorMessage) {
    String error;
    if (errorMessage != null && errorMessage.length() != 0) {
      error = errorMessage;
    } else {
      error = resources.getString(R.string.unknown_error);
    }
    Snackbar.make(this.rootViewRl, error, Snackbar.LENGTH_SHORT).show();
  }

  /**
   * Sets the navigation content with the online data.
   *
   * @param wrapper data.
   */
  @Override
  public void setNavigationContent_Online(CategoriesWrapper wrapper) {
    if (wrapper.getCategories() != null && wrapper.getCategories().size() != 0) {
      List<Category> categories = wrapper.getCategories();
      int unreadCount = ArrayUtils.getUnreadAllItemsCount(categories);
      int starCount = ArrayUtils.getStarItemsCount(categories);
      initializeMainCategories(unreadCount, starCount);

      for (Category category : categories) {
        this.categoriesList.add(category);
        if (category.getChannels() == null) {
          this.channelsList.put(category, new ArrayList<Channel>());
        } else {
          this.channelsList.put(category, category.getChannels());
        }
      }

      this.adapter = new ExpandableListAdapter(this, categoriesList, channelsList, eventBus);
      this.expandableListView.setAdapter(this.adapter);
    }
  }

  /**
   * Sets the navigation content with the offline data.
   *
   * @param categories List of Categories
   * @param channels   List of Channels.
   */
  @Override
  public void setNavigationContent_Offline(List<Category> categories,
                                           HashMap<Category, List<Channel>> channels) {
    int unreadCount = this.presenter.getCountReadAllItems_Database();
    int starCount = this.presenter.getCountStarItems_Database();
    initializeMainCategories(unreadCount, starCount);

    this.categoriesList.addAll(categories);
    this.channelsList.putAll(channels);

    this.adapter = new ExpandableListAdapter(this, categoriesList, channelsList, eventBus);
    this.expandableListView.setAdapter(this.adapter);
  }


  /**
   * Shows the data in the ALL Category.
   *
   * @param data List of Items.
   */
  @Override
  public void showAllItemsContent(List<Item> data) {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(getString(R.string.category_all));
    }

    this.LIST_ITEMS_TYPE = Globals.LIST_ALL_ITEMS_TYPE;

    /**
     * Menu Items.
     */
    if (this.unsubscribeItem != null) {
      this.unsubscribeItem.setVisible(false);
    }
    if (this.markAsReadItem != null) {
      this.markAsReadItem.setVisible(true);
    }

    if (data != null && data.size() != 0) {
      this.itemsAdapter = new ItemsAdapter(this, data);
      this.itemsRecyclerView.setAdapter(itemsAdapter);

      if (this.itemClicked != null) {
        this.itemsRecyclerView.scrollToPosition(ArrayUtils.getPositionInList(data, itemClicked));
      }

      this.contentView.setVisibility(View.VISIBLE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.GONE);
    } else {
      this.emptyView.setText(resources.getString(R.string.empty_items));
      this.contentView.setVisibility(View.GONE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Shows starred items.
   *
   * @param data List of Items.
   */
  @Override
  public void showStarredItemsContent(List<Item> data) {
    if (data != null && data.size() != 0) {
      this.itemsAdapter = new ItemsAdapter(this, data);
      this.itemsRecyclerView.setAdapter(itemsAdapter);

      this.contentView.setVisibility(View.VISIBLE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.GONE);
    } else {
      this.emptyView.setText(resources.getString(R.string.empty_star_items));
      this.contentView.setVisibility(View.GONE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Shows items by category.
   *
   * @param data List of Items
   */
  @Override
  public void showItemsByCategoryIdContent(List<Item> data) {
    if (data != null && data.size() != 0) {
      this.itemsAdapter = new ItemsAdapter(this, data);
      this.itemsRecyclerView.setAdapter(itemsAdapter);

      this.contentView.setVisibility(View.VISIBLE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.GONE);
    } else {
      this.emptyView.setText(resources.getString(R.string.empty_items));
      this.contentView.setVisibility(View.GONE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Shows items by channel.
   *
   * @param data List of Items.
   */
  @Override
  public void showItemsByChannelIdContent(List<Item> data) {
    if (data != null && data.size() != 0) {
      this.itemsAdapter = new ItemsAdapter(this, data);
      this.itemsRecyclerView.setAdapter(itemsAdapter);

      this.contentView.setVisibility(View.VISIBLE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.GONE);
    } else {
      this.emptyView.setText(resources.getString(R.string.empty_channel_items));
      this.contentView.setVisibility(View.GONE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Updates view after unsubscribing to a channel
   */
  @Override
  public void unsubscribeChannelSuccess() {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(resources.getString(R.string.category_all));
    }
    Snackbar.make(this.rootViewRl, resources.getString(R.string.unsubscribe_feed_success),
        Snackbar.LENGTH_LONG).show();
  }
}
