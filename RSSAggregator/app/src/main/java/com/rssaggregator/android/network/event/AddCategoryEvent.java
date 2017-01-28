package com.rssaggregator.android.network.event;

import com.rssaggregator.android.network.model.Category;

public class AddCategoryEvent extends BaseEvent<Category> {
  public AddCategoryEvent(Category category) {
    super(category);
  }

  public AddCategoryEvent(Throwable throwable) {
    super(throwable);
  }
}
