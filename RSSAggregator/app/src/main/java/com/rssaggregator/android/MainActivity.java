package com.rssaggregator.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.rssaggregator.android.addfeed.view.AddFeedActivity;
import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.feed.adapter.ItemsAdapter;
import com.rssaggregator.android.feed.event.NavigationItemClickedEvent;
import com.rssaggregator.android.feed.presenter.MainPresenterImpl;
import com.rssaggregator.android.feed.view.MainView;
import com.rssaggregator.android.login.LoginActivity;
import com.rssaggregator.android.navigationdrawer.ExpandableListAdapter;
import com.rssaggregator.android.network.event.LogOutEvent;
import com.rssaggregator.android.network.model.CategoriesWrapper;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;
import com.rssaggregator.android.network.model.Item;
import com.rssaggregator.android.network.utils.TokenRequestInterceptor;
import com.rssaggregator.android.utils.BaseActivity;
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

public class MainActivity extends BaseActivity implements MainView {

  // Navigation Drawer Views.
  @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.navigationView) NavigationView navigationView;
  @BindView(R.id.expandableListView) ExpandableListView expandableListView;

  // Main Views
  @BindView(R.id.itemsRecyclerView) RecyclerView itemsRecyclerView;
  @BindView(R.id.contentView) RelativeLayout contentView;
  @BindView(R.id.loadingView) RelativeLayout loadingView;
  @BindView(R.id.errorView) AppCompatTextView errorView;
  @BindView(R.id.emptyView) AppCompatTextView emptyView;

  @Inject TokenRequestInterceptor requestInterceptor;

  // Adapter
  private ExpandableListAdapter adapter;
  private ItemsAdapter itemsAdapter;

  // Data
  private List<Category> categoriesList;
  private HashMap<Category, List<Channel>> channelsList;
  private Channel selectedChannel = null;

  // Network
  private MainPresenterImpl presenter;
  private EventBus eventBus;

  // Others
  private Resources resources;
  private MenuItem unsubscribeItem;

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

    RssAggregatorApplication.get(this).getAppComponent().inject(this);
    if (requestInterceptor.getAccessToken() == null) {
      startActivity(new Intent(this, LoginActivity.class));
      finish();
    }

    initializeNavigationDrawer();

    if (isNetworkAvailable()) {
      this.presenter.loadAllData();
    } else {
      this.presenter.loadAllDataOffLine();
    }

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    this.itemsRecyclerView.addItemDecoration(new DividerItemDecoration(this,
        DividerItemDecoration.VERTICAL));
    this.itemsRecyclerView.setLayoutManager(linearLayoutManager);
  }

  @Override
  protected void onResume() {
    super.onResume();
    this.eventBus.register(this);
  }

  @Override
  protected void onStop() {
    this.eventBus.unregister(this);
    super.onStop();
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
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_add_channel) {
      Intent intent = new Intent(this, AddFeedActivity.class);
      startActivityForResult(intent, 23);
      return true;
    } else if (id == R.id.action_refresh) {
      if (isNetworkAvailable()) {
        this.presenter.loadAllData();
      } else {
        this.presenter.loadAllDataOffLine();
      }
      return true;
    } else if (id == R.id.action_unsubscribe) {
      Logger.e("Selected channel:" + selectedChannel.getName());
      if (isNetworkAvailable()) {
        this.presenter.unsubscribeChannel(selectedChannel);
      } else {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
      }
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 23) {
      if (resultCode == RESULT_OK) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        if (isNetworkAvailable()) {
          this.presenter.loadAllData();
        } else {
          this.presenter.loadAllDataOffLine();
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

  private void initializeNavigationDrawer() {
    // Set toolbar title.
    setSupportActionBar(this.toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(getString(R.string.category_all));
    }

    initializeHeader();
    initializeMainCategories();

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawerLayout, toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawerLayout.setDrawerListener(toggle);
    toggle.syncState();

    // Set Expandable list view adapter.
    this.adapter = new ExpandableListAdapter(this, categoriesList, channelsList,
        expandableListView, eventBus);
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
  private void initializeMainCategories() {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(getString(R.string.category_all));
    }
    this.categoriesList = new ArrayList<>();
    this.channelsList = new HashMap<>();

    Category allCategory = new Category();
    allCategory.setName(resources.getString(R.string.category_all));
    Category starredItemsCategory = new Category();
    starredItemsCategory.setName(resources.getString(R.string.category_star));
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
      this.unsubscribeItem.setVisible(false);
      if (getSupportActionBar() != null) {
        getSupportActionBar().setTitle(getString(R.string.category_all));
        this.presenter.fetchAllItems();
      }
      return;
    }
    /**
     * Click on Starred Items category.
     */
    if (event.isStar()) {
      this.unsubscribeItem.setVisible(false);
      if (getSupportActionBar() != null) {
        getSupportActionBar().setTitle(getString(R.string.category_star));
        this.presenter.fetchStarredItems();
      }
      return;
    }
    /**
     * Click on a Category.
     */
    if (event.getChannel() == null) {
      this.unsubscribeItem.setVisible(false);
      Category category = event.getCategory();
      if (getSupportActionBar() != null) {
        getSupportActionBar().setTitle(category.getName());
      }
      this.presenter.fetchItemsByCategoryId(category.getCategoryId());
    } else {
      /**
       * Click on a Channel.
       */
      this.unsubscribeItem.setVisible(true);
      Category category = event.getCategory();
      Channel channel = event.getChannel();
      this.selectedChannel = channel;
      if (getSupportActionBar() != null) {
        getSupportActionBar().setTitle(resources.getString(R.string.category_channel,
            channel.getName(), category.getName()));
      }
      this.presenter.fetchItemsByChannelId(channel.getChannelId());
    }
  }

  //
  //
  // Methods called from the presenter.
  //
  //
  @Override
  public void showLoading() {
    this.contentView.setVisibility(View.GONE);
    this.loadingView.setVisibility(View.VISIBLE);
    this.errorView.setVisibility(View.GONE);
    this.emptyView.setVisibility(View.GONE);
  }

  @Override
  public void showError(String errorMessage) {
    if (errorMessage != null && errorMessage.length() != 0) {
      this.errorView.setText(errorMessage);
    } else {
      this.errorView.setText("unknown error");
    }

    this.contentView.setVisibility(View.GONE);
    this.loadingView.setVisibility(View.GONE);
    this.errorView.setVisibility(View.VISIBLE);
    this.emptyView.setVisibility(View.GONE);
  }

  @Override
  public void showSnackBarError(String errorMessage) {
    String error;
    if (errorMessage != null && errorMessage.length() != 0) {
      error = errorMessage;
    } else {
      error = "Unknown error";
    }
    Snackbar.make(contentView, error, Snackbar.LENGTH_SHORT).show();
  }

  @Override
  public void setNavigationContent(CategoriesWrapper wrapper) {
    if (wrapper.getCategories() != null && wrapper.getCategories().size() != 0) {
      List<Category> categories = wrapper.getCategories();
      initializeMainCategories();

      for (Category category : categories) {
        this.categoriesList.add(category);
        if (category.getChannels() == null) {
          this.channelsList.put(category, new ArrayList<Channel>());
        } else {
          this.channelsList.put(category, category.getChannels());
        }
      }

      this.adapter = new ExpandableListAdapter(this, categoriesList, channelsList,
          expandableListView, eventBus);
      this.expandableListView.setAdapter(this.adapter);
    }
  }

  @Override
  public void setNavigationContentOffline(List<Category> categories,
                                          HashMap<Category, List<Channel>> channels) {

    initializeMainCategories();

    this.categoriesList.addAll(categories);
    this.channelsList.putAll(channels);

    this.adapter = new ExpandableListAdapter(this, categoriesList, channelsList,
        expandableListView, eventBus);
    this.expandableListView.setAdapter(this.adapter);
  }

  @Override
  public void showAllItemsContent(List<Item> data) {
    this.unsubscribeItem.setVisible(false);
    if (data != null && data.size() != 0) {
      this.itemsAdapter = new ItemsAdapter(this, data);
      this.itemsRecyclerView.setAdapter(itemsAdapter);

      this.contentView.setVisibility(View.VISIBLE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.GONE);
    } else {
      this.emptyView.setText("No items");
      this.contentView.setVisibility(View.GONE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.VISIBLE);
    }
  }

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
      this.emptyView.setText("No starred items.");
      this.contentView.setVisibility(View.GONE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.VISIBLE);
    }
  }

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
      this.emptyView.setText("No items in this category.");
      this.contentView.setVisibility(View.GONE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.VISIBLE);
    }
  }

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
      this.emptyView.setText("No items in this channel.");
      this.contentView.setVisibility(View.GONE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void showContent(CategoriesWrapper wrapper) {
    if (wrapper.getCategories() != null && wrapper.getCategories().size() != 0) {
      this.contentView.setVisibility(View.VISIBLE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.GONE);
    } else {
      this.emptyView.setText("No elements");
      this.contentView.setVisibility(View.GONE);
      this.loadingView.setVisibility(View.GONE);
      this.errorView.setVisibility(View.GONE);
      this.emptyView.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void unsubscribeChannelSuccess() {
    Toast.makeText(this, "Channel unsubscribed!", Toast.LENGTH_SHORT).show();
  }

  //
  //
  // Other methods
  //
  //
  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
}
