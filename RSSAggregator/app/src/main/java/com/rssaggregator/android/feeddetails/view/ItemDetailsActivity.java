package com.rssaggregator.android.feeddetails.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;

import com.rssaggregator.android.R;
import com.rssaggregator.android.feeddetails.customtabs.CustomTabActivityHelper;
import com.rssaggregator.android.network.model.Item;
import com.rssaggregator.android.utils.BaseActivity;
import com.rssaggregator.android.utils.FormatterTime;
import com.rssaggregator.android.utils.Globals;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemDetailsActivity extends BaseActivity {

  @BindView(R.id.titleItem) AppCompatTextView titleItemTv;
  @BindView(R.id.pubDateItem) AppCompatTextView pubDateItemTv;
  @BindView(R.id.nameChannelItem) AppCompatTextView nameChannelItemTv;
  @BindView(R.id.descriptionItem) AppCompatTextView descriptionItemTv;

  private Item item;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_details);
    ButterKnife.bind(this);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      this.item = extras.getParcelable(Globals.EXTRA_ITEM);
    }

    initializeViews();
  }

  private void initializeViews() {
    // Set title app bar
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(item.getTitle());
    }

    titleItemTv.setText(item.getTitle());
    pubDateItemTv.setText(FormatterTime.formattedAsTimeAgo(this, item.getPubDate()));
    nameChannelItemTv.setText(item.getNameChannel());
    descriptionItemTv.setText(item.getDescription());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      this.finish();
    }
    return super.onOptionsItemSelected(item);
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
    //customTabsIntent.launchUrl(this, Uri.parse(item.getLinkUrl()));
  }
}
