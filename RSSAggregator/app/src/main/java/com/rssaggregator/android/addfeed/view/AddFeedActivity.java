package com.rssaggregator.android.addfeed.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.rssaggregator.android.R;
import com.rssaggregator.android.RssAggregatorApplication;
import com.rssaggregator.android.addfeed.presenter.AddFeedPresenterImpl;
import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.utils.ArrayUtils;
import com.rssaggregator.android.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity for Add Feed View.
 */
public class AddFeedActivity extends BaseActivity implements AddFeedView {

  @BindView(R.id.rootView) FrameLayout rootViewFl;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.categoriesSpinner) AppCompatSpinner categoriesSp;
  @BindView(R.id.addCategory) AppCompatImageView addCategoryIg;
  @BindView(R.id.rssLink) AppCompatEditText rssLinkTv;

  private AddFeedPresenterImpl presenter;

  private List<Category> categories;

  private Resources resources;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_feed);
    this.resources = getResources();
    ButterKnife.bind(this);
    injectDependencies();

    initializeToolbar();
    this.presenter.fetchCategories_Database();
  }

  /**
   * Injects dependencies.
   */
  private void injectDependencies() {
    AppComponent appComponent = RssAggregatorApplication.get(this).getAppComponent();
    this.presenter = appComponent.addFeedPresenterImpl();
    this.presenter.setAddFeedView(this);
    this.presenter.setDatabase(this);
  }

  /**
   * Initialize the toolbar.
   */
  private void initializeToolbar() {
    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle("");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  //
  //
  // Methods called by the presenter.
  //
  //

  /**
   * Sets the spinner view after fetching categories from the database.
   *
   * @param data List of Category.
   */
  @Override
  public void setCategoriesToSpinner(List<Category> data) {
    this.categories = data;
    if (data != null && data.size() != 0) {
      ArrayList<String> categoryNames = new ArrayList<>();

      for (Category category : data) {
        categoryNames.add(category.getName());
      }
      ArrayAdapter<String> adp = new ArrayAdapter<>(this, android.R.layout
          .simple_spinner_dropdown_item, categoryNames);
      categoriesSp.setAdapter(adp);
    } else {
      ArrayList<String> defaultCategory = new ArrayList<>();
      defaultCategory.add(resources.getString(R.string.create_new_category));

      ArrayAdapter<String> adp = new ArrayAdapter<>(this, android.R.layout
          .simple_spinner_dropdown_item, defaultCategory);
      categoriesSp.setAdapter(adp);
    }
  }

  //
  //
  // OnClick Methods
  //
  //

  /**
   * Action when user adds a category.
   */
  @OnClick(R.id.addCategory)
  public void addCategory() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    final AlertDialog dialog;
    LayoutInflater inflater = getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.dialog_add_category, null);
    builder.setView(dialogView);

    final AppCompatEditText categoryNameEt = (AppCompatEditText)
        dialogView.findViewById(R.id.categoryName);
    builder.setTitle(resources.getString(R.string.add_category));
    builder.setPositiveButton(resources.getString(R.string.add),
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
          }
        });
    builder.setNegativeButton(resources.getString(R.string.cancel),
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
    dialog = builder.create();
    dialog.show();
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String categoryName = categoryNameEt.getText().toString();
        InputMethodManager imm = (InputMethodManager) getSystemService(
            Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(categoryNameEt.getWindowToken(), 0);
        if (categoryName.length() < 2) {
          Toast.makeText(AddFeedActivity.this,
              resources.getString(R.string.create_category_empty_field),
              Toast.LENGTH_SHORT).show();
        } else {
          if (ArrayUtils.getCategoryByName(categories, categoryName) == null) {
            presenter.addCategory(categoryName);
            dialog.dismiss();
          } else {
            Toast.makeText(AddFeedActivity.this,
                resources.getString(R.string.create_category_already_existed),
                Toast.LENGTH_SHORT).show();
          }
        }
      }
    });
  }

  /**
   * Action when the user subscribes/adds a feed.
   */
  @OnClick(R.id.addFeed)
  public void addFeed() {
    String rssLink = rssLinkTv.getText().toString();

    if (rssLink.length() == 0) {
      Snackbar.make(this.rootViewFl, resources.getString(R.string.add_feed_rss_field_empty),
          Snackbar.LENGTH_SHORT).show();
      return;
    }

    String categoryName = (String) categoriesSp.getSelectedItem();

    if (resources.getString(R.string.create_new_category).equals(categoryName)) {
      Snackbar.make(this.rootViewFl, resources.getString(R.string.add_feed_category_error),
          Snackbar.LENGTH_SHORT).show();
      return;
    }

    Category category = ArrayUtils.getCategoryByName(this.categories, categoryName);

    if (category != null) {
      Toast.makeText(AddFeedActivity.this,
          resources.getString(R.string.add_feed_waiting), Toast.LENGTH_SHORT).show();
      this.presenter.addFeed(category, rssLink);
    } else {
      Snackbar.make(this.rootViewFl, resources.getString(R.string.add_feed_category_error),
          Snackbar.LENGTH_SHORT).show();
    }
  }

  /**
   * Updates the view after adding a feed.
   */
  @Override
  public void showFeedAdded() {
    setResult(RESULT_OK, null);
    finish();
  }

  /**
   * Shows a Snackbar with a error message.
   */
  @Override
  public void showSnackbarError(String errorMessage) {
    Snackbar.make(this.rootViewFl, errorMessage, Snackbar.LENGTH_SHORT).show();
  }

  /**
   * Displays a Snackbar indicating that the category has been created.
   *
   * @param categoryName Name of the new category created.
   */
  @Override
  public void updateCategoryCreated(String categoryName) {
    Snackbar.make(this.rootViewFl,
        resources.getString(R.string.create_category_success, categoryName),
        Snackbar.LENGTH_SHORT).show();
  }
}
