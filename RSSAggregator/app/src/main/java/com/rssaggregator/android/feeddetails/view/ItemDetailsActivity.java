package com.rssaggregator.android.feeddetails.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.rssaggregator.android.R;
import com.rssaggregator.android.RssAggregatorApplication;
import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.feeddetails.customtabs.CustomTabActivityHelper;
import com.rssaggregator.android.feeddetails.presenter.ItemDetailsPresenterImpl;
import com.rssaggregator.android.network.model.Item;
import com.rssaggregator.android.utils.BaseActivity;
import com.rssaggregator.android.utils.FormatterTime;
import com.rssaggregator.android.utils.Globals;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity for Item Details View.
 */
public class ItemDetailsActivity extends BaseActivity implements ItemDetailsView {

  @BindView(R.id.rootView) ScrollView rootViewSv;
  @BindView(R.id.titleItem) AppCompatTextView titleItemTv;
  @BindView(R.id.pubDateItem) AppCompatTextView pubDateItemTv;
  @BindView(R.id.nameChannelItem) AppCompatTextView nameChannelItemTv;
  @BindView(R.id.descriptionItem) WebView descriptionItemWv;

  private ItemDetailsPresenterImpl presenter;

  private Item item;

  /**
   * Menu Items.
   */
  private MenuItem starItem;
  private MenuItem readItem;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_details);
    ButterKnife.bind(this);
    injectDependencies();

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      this.item = extras.getParcelable(Globals.EXTRA_ITEM);
    }

    if (this.item != null) {
      initializeViews();
    } else {
      Snackbar.make(this.rootViewSv, getString(R.string.item_details_null),
          Snackbar.LENGTH_SHORT).show();
    }

    if (this.item != null && !this.item.isRead()) {
      this.presenter.updateReadItem(this.item);
    }
  }

  /**
   * Injects dependencies.
   */
  private void injectDependencies() {
    AppComponent appComponent = RssAggregatorApplication.get(this).getAppComponent();
    this.presenter = appComponent.itemDetailsPresenterImpl();
    this.presenter.setItemDetailsView(this);
    this.presenter.setDatabase(this);
  }

  /**
   * Initializes the view.
   */
  private void initializeViews() {
    // Set title app bar
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(item.getTitle());
    }
    String title = this.item.getTitle();
    Date pubDate = this.item.getPubDate();
    String channelName = this.item.getChannelName();
    String description = this.item.getDescription();

    if (title != null) {
      this.titleItemTv.setText(title);
    }

    if (pubDate != null) {
      this.pubDateItemTv.setText(FormatterTime.formattedAsTimeAgo(this, pubDate));
    }

    if (channelName != null) {
      this.nameChannelItemTv.setText(channelName);
    }

    if (description != null) {
      descriptionItemWv.getSettings();
      descriptionItemWv.setBackgroundColor(Color.TRANSPARENT);
      descriptionItemWv.loadDataWithBaseURL("", description, "text/html", "utf-8", "");
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_item_details, menu);
    this.starItem = menu.findItem(R.id.action_star_item);
    this.readItem = menu.findItem(R.id.action_see_item);

    if (item.isStarred()) {
      this.starItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_white_24dp));
    } else {
      this.starItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_border_white_24dp));
    }

    if (item.isRead()) {
      this.readItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_visibility_white_24dp));
    } else {
      this.readItem.setIcon(ContextCompat.getDrawable(this,
          R.drawable.ic_visibility_off_white_24dp));
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case android.R.id.home:
        this.finish();
        break;
      case R.id.action_star_item:
        this.presenter.updateStarStateItem(this.item);
        break;
      case R.id.action_see_item:
        this.presenter.updateReadItem(this.item);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy() {
    this.presenter.onDestroy();
    super.onDestroy();
  }

  /**
   * Updates the item after a updateReadState succeed.
   */
  @Override
  public void updateItemRead(boolean oldState) {
    if (oldState) {
      this.item.setRead(false);
      if (this.readItem != null) {
        this.readItem.setIcon(ContextCompat.getDrawable(this,
            R.drawable.ic_visibility_off_white_24dp));
      }
    } else {
      this.item.setRead(true);
      if (this.readItem != null) {
        this.readItem.setIcon(ContextCompat.getDrawable(this,
            R.drawable.ic_visibility_white_24dp));
      }
    }
  }

  /**
   * Updates the item after a updateStarState succeed.
   *
   * @param oldState old state before the request.
   */
  @Override
  public void updateItemStarred(Boolean oldState) {
    if (oldState) {
      this.item.setStarred(false);
      this.starItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_border_white_24dp));
    } else {
      this.item.setStarred(true);
      this.starItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_white_24dp));
    }
  }

  /**
   * Shows a SnackBar Error.
   */
  @Override
  public void showSnackBarError() {
    Snackbar.make(this.rootViewSv, getString(R.string.error_star_item),
        Snackbar.LENGTH_LONG).show();
  }

  //
  //
  // On Click methods
  //
  //
  @OnClick(R.id.visitWebsite)
  public void visitWebsite() {
    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

    // set toolbar color
    builder.setToolbarColor(ContextCompat.getColor(this, R.color.primaryColor));
    builder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.primaryDarkColor));

    // set start and exit animations
    builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
    builder.setExitAnimations(this, android.R.anim.slide_in_left,
        android.R.anim.slide_out_right);

    // Title
    builder.setShowTitle(true);

    // Share Menu
    builder.addDefaultShareMenuItem();

    // Close Button
    builder.setCloseButtonIcon(BitmapFactory.decodeResource(
        getResources(), R.drawable.ic_arrow_back_white_24dp));

    CustomTabsIntent customTabsIntent = builder.build();

    CustomTabActivityHelper.openCustomTab(this, customTabsIntent, Uri.parse(item.getLinkUrl()),
        new CustomTabActivityHelper.CustomTabFallback() {
          @Override
          public void openUri(Activity activity, Uri uri) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
          }
        });
  }
}
