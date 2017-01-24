package com.rssaggregator.android;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import com.orhanobut.logger.Logger;
import com.rssaggregator.android.navigationdrawer.ExpandableListAdapter;
import com.rssaggregator.android.network.event.LogInEvent;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.navigationView) NavigationView navigationView;
  @BindView(R.id.expandableListView) ExpandableListView expandableListView;

  // Adapter
  private ExpandableListAdapter adapter;

  // Data
  private List<Category> categoriesList;
  private HashMap<Category, List<Channel>> channelsList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(this.toolbar);

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawerLayout, toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawerLayout.setDrawerListener(toggle);
    toggle.syncState();

    createFakeData();

    this.adapter = new ExpandableListAdapter(this, categoriesList, channelsList,
        expandableListView);
    this.expandableListView.setAdapter(this.adapter);
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
    allCat.setName("All");
    Category starredItemsCat = new Category();
    starredItemsCat.setName("Starred Items");

    Category automobileCat = new Category();
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
    }
  }
}
