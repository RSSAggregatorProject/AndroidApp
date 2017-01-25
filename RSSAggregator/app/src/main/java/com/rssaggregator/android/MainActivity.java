package com.rssaggregator.android;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.feed.database.FeedsDataSource;
import com.rssaggregator.android.feed.event.NavigationItemClickedEvent;
import com.rssaggregator.android.feed.presenter.MainPresenterImpl;
import com.rssaggregator.android.feed.view.MainView;
import com.rssaggregator.android.navigationdrawer.ExpandableListAdapter;
import com.rssaggregator.android.network.event.LogInEvent;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView {

  @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.navigationView) NavigationView navigationView;
  @BindView(R.id.expandableListView) ExpandableListView expandableListView;

  // Main Views
  @BindView(R.id.contentView) RelativeLayout contentView;
  @BindView(R.id.loadingView) RelativeLayout loadingView;
  @BindView(R.id.errorView) AppCompatTextView errorView;
  @BindView(R.id.emptyView) AppCompatTextView emptyView;
  @BindView(R.id.numbers) AppCompatTextView numbersTv;

  // Adapter
  private ExpandableListAdapter adapter;

  // Data
  private List<Category> categoriesList;
  private HashMap<Category, List<Channel>> channelsList;

  // Network
  private AppComponent appComponent;
  private MainPresenterImpl presenter;
  private EventBus eventBus;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    injectDependencies();

    initializeNavigationDrawer();
    this.presenter.loadAllData();
  }

  @Override
  protected void onResume() {
    super.onResume();
    this.eventBus.register(this);
  }

  @Override
  protected void onPause() {
    this.eventBus.unregister(this);
    super.onPause();
  }

  private void initializeNavigationDrawer() {
    setSupportActionBar(this.toolbar);
    getSupportActionBar().setTitle(getString(R.string.category_all));

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawerLayout, toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawerLayout.setDrawerListener(toggle);
    toggle.syncState();

    createFakeData();

    this.adapter = new ExpandableListAdapter(this, categoriesList, channelsList,
        expandableListView, eventBus);
    this.expandableListView.setAdapter(this.adapter);
  }

  private void injectDependencies() {
    this.appComponent = RssAggregatorApplication.get(this).getAppComponent();
    this.eventBus = appComponent.bus();
    this.presenter = appComponent.mainPresenterImpl();
    this.presenter.setMainView(this);
  }

  @Override
  public void onBackPressed() {
    if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
      this.drawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(LogInEvent event) {
    Logger.e("TOKEN: " + event.getData().getToken());
  }

  private void createFakeData() {
    this.categoriesList = new ArrayList<Category>();
    this.channelsList = new HashMap<Category, List<Channel>>();

    Category allCat = new Category();
    allCat.setName(getString(R.string.category_all));
    Category starredItemsCat = new Category();
    starredItemsCat.setName(getString(R.string.category_star));

    this.categoriesList.add(allCat);
    this.categoriesList.add(starredItemsCat);


/*    Category automobileCat = new Category();
    automobileCat.setName("Automobile");
    Category sportCat = new Category();
    sportCat.setName("Sport");

    Channel turboChan = new Channel();
    turboChan.setName("Turbo.fr");
    turboChan.setUnread(10);
    Channel caradisiacChan = new Channel();
    caradisiacChan.setName("Caradisiac");
    caradisiacChan.setUnread(1);
    Channel eurosportChan = new Channel();
    eurosportChan.setName("Eurosport");
    eurosportChan.setUnread(235);

    List<Channel> channelList = new ArrayList<>();
    channelList.add(turboChan);
    channelList.add(caradisiacChan);
    automobileCat.setChannels(channelList);

    List<Channel> channelList2 = new ArrayList<>();
    channelList2.add(eurosportChan);
    sportCat.setChannels(channelList2);

    this.categoriesList.add(allCat);
    this.categoriesList.add(starredItemsCat);
    this.categoriesList.add(automobileCat);
    this.categoriesList.add(sportCat);

    for (Category category : this.categoriesList) {
      if (category.getChannels() == null) {
        this.channelsList.put(category, new ArrayList<Channel>());
      } else {
        this.channelsList.put(category, category.getChannels());
      }
    }*/
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
    if (event.isAll()) {
      getSupportActionBar().setTitle(getString(R.string.category_all));
      return;
    }
    if (event.isStar()) {
      getSupportActionBar().setTitle(getString(R.string.category_star));
      return;
    }
    if (event.getChannel() == null) {
      Category category = event.getCategory();
      getSupportActionBar().setTitle(category.getName());
    } else {
      Category category = event.getCategory();
      Channel channel = event.getChannel();
      getSupportActionBar().setTitle(channel.getName() + " / " + category.getName());
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
    if (errorMessage != null) {
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
  public void showContent(CategoriesWrapper wrapper) {
    /**
     * DATABASE
     */
    FeedsDataSource dataBase = new FeedsDataSource(this);

    if (wrapper.getCategories() != null && wrapper.getCategories().size() != 0) {
      List<Category> categories = wrapper.getCategories();
      String numberStr = "Number of categories: " + categories.size();

      for (Category category : categories) {
        if (category.getChannels() != null && category.getChannels().size() != 0) {
          numberStr += "\nNumber of channels: " + category.getChannels().size();
        }
      }

      for (Category category : categories) {
        if (category.getChannels() != null && category.getChannels().size() != 0) {
          for (Channel channel : category.getChannels()) {
            numberStr += "\nName of the channel: " + channel.getName();
            if (channel.getItems() != null && channel.getItems().size() != 0) {
              for (Item item : channel.getItems()) {
                numberStr += "\nItem Name: " + item.getName() + " | " + item.getDescription();
              }
            }
          }
        }
      }
      this.numbersTv.setText(numberStr);

      setDrawer(categories);

      /**
       * DATABASE
       */
      dataBase = new FeedsDataSource(this);

      // Clear database
      dataBase.deleteAllCategories();
      dataBase.deleteAllChannels();
      dataBase.deleteAllItems();

      String number = "";

      // Insert
      for (Category category : categories) {
        // Insert category first
        dataBase.insertCategory(category);

        // Insert channel second
        if (category.getChannels() != null && category.getChannels().size() != 0) {

          for (Channel channel : category.getChannels()) {
            channel.setCategoryId(category.getCategoryId());
            dataBase.insertChannel(channel);


            // Insert items third
            if (channel.getItems() != null && channel.getItems().size() != 0) {

              for (Item item : channel.getItems()) {
                item.setCategoryId(category.getCategoryId());
                item.setChannelId(channel.getChannelId());
                dataBase.insertItem(item);
              }

            } else {
              number += "\n No Items in the channel: " + channel.getName();
            }

          }

          List<Channel> fetchedChannels = dataBase.getChannelsByCategoryId(category.getCategoryId
              ());
          /**
           * FOR DEBUG
           */
          for (Channel channel : fetchedChannels) {
            number += "\nName of the channel : " + channel.getName();
          }

          number += "\n\nNumber of channels for " + category.getName() + " : " + fetchedChannels
              .size();
        } else {
          number += "\n\nNo channels";
        }
      }

      List<Category> fetchedCategories = dataBase.getCategories();
      number += "\nNumber of categories: " + fetchedCategories.size();
      List<Item> fetchedItems = dataBase.getAllItems();
      number += "\n\nNumber of items: " + fetchedItems.size();

      for (Item item : fetchedItems) {
        number += "\nItem: " + item.getTitle() + " - " + item.getPubDate().toString();
      }

      List<Item> starredItems = dataBase.getStarredItems(true);
      number += "\n\nNumber of starred items: " + starredItems.size();

      for (Item item : starredItems) {
        number += "\nItem: " + item.getTitle() + " - " + item.getPubDate().toString();
      }

      this.numbersTv.setText(number);


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

  private void setDrawer(List<Category> categories) {
    if (categories != null)
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
