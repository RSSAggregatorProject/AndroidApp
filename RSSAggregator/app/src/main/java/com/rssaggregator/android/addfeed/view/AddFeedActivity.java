package com.rssaggregator.android.addfeed.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.rssaggregator.android.R;
import com.rssaggregator.android.RssAggregatorApplication;
import com.rssaggregator.android.addfeed.presenter.AddFeedPresenterImpl;
import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFeedActivity extends BaseActivity implements AddFeedView {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.categoriesSpinner) AppCompatSpinner categoriesSp;
  @BindView(R.id.addCategory) AppCompatImageView addCategoryIg;
  @BindView(R.id.rssLink) AppCompatEditText rssLinkTv;

  private AddFeedPresenterImpl presenter;

  private List<Category> categories;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_feed);
    ButterKnife.bind(this);
    injectDependencies();

    initializeToolbar();
    this.presenter.fetchCategories();
  }

  private void injectDependencies() {
    AppComponent appComponent = RssAggregatorApplication.get(this).getAppComponent();
    this.presenter = appComponent.addFeedPresenterImpl();
    this.presenter.setAddFeedView(this);
    this.presenter.setDatabase(this);
  }

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

  @Override
  public void setCategoriesToSpinner(List<Category> data) {
    this.categories = data;
    if (data != null && data.size() != 0) {
      ArrayList<String> categoryNames = new ArrayList<>();

      for (Category category : data) {
        categoryNames.add(category.getName());
      }
      ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout
          .simple_spinner_dropdown_item, categoryNames);
      categoriesSp.setAdapter(adp);
    } else {
      ArrayList<String> defaultCategory = new ArrayList<>();
      defaultCategory.add("Create a new category");

      ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout
          .simple_spinner_dropdown_item, defaultCategory);
      categoriesSp.setAdapter(adp);
    }
  }

  //
  //
  // OnClick Methods
  //
  //
  @OnClick(R.id.addCategory)
  public void addCategory() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    final AlertDialog dialog;
    LayoutInflater inflater = getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.dialog_add_category, null);
    builder.setView(dialogView);

    final AppCompatEditText userIdEt = (AppCompatEditText) dialogView.findViewById(R.id.user_id);
    builder.setTitle("Add a category");
    builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
      }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        String groupName = userIdEt.getText().toString();
        InputMethodManager imm = (InputMethodManager) getSystemService(
            Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(userIdEt.getWindowToken(), 0);
        if (groupName.length() == 0) {
          Toast.makeText(AddFeedActivity.this, "Please fill the category name", Toast.LENGTH_SHORT)
              .show();
        } else {
          presenter.addCategory(groupName);
//          presenter.retrievePassword(groupName);
          dialog.dismiss();
        }
      }
    });
  }

  @OnClick(R.id.addFeed)
  public void addFeed() {
    String rssLink = rssLinkTv.getText().toString();

    if (rssLink.length() == 0) {
      Logger.e("NULL");
      return;
    }

    String categoryName = (String) categoriesSp.getSelectedItem();

    if ("Create a new category".equals(categoryName)) {
      Logger.e("ERROR");
      return;
    }

    Category category = getCategoryByName(categoryName);

    if (category != null) {
      this.presenter.addFeed(category, rssLink);
    } else {
      Logger.e("Error");
    }
  }

  private Category getCategoryByName(String name) {
    for (Category category : categories) {
      if (name.equals(category.getName())) {
        return category;
      }
    }
    return null;
  }

  @Override
  public void showFeedAdded() {
    setResult(RESULT_OK, null);
    finish();
  }
}
