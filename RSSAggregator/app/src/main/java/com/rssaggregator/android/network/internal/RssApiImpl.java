package com.rssaggregator.android.network.internal;

import android.content.Context;

import com.google.gson.Gson;
import com.rssaggregator.android.network.RssApi;
import com.rssaggregator.android.network.error.ApiError;
import com.rssaggregator.android.network.event.AccessTokenFetchedEvent;
import com.rssaggregator.android.network.event.AddCategoryEvent;
import com.rssaggregator.android.network.event.AddFeedEvent;
import com.rssaggregator.android.network.event.FetchDataEvent;
import com.rssaggregator.android.network.event.LogInEvent;
import com.rssaggregator.android.network.event.LogOutEvent;
import com.rssaggregator.android.network.event.SignUpEvent;
import com.rssaggregator.android.network.event.UnsubscribeChannelEvent;
import com.rssaggregator.android.network.event.UpdateReadAllItemsEvent;
import com.rssaggregator.android.network.event.UpdateReadItemsByChannelIdEvent;
import com.rssaggregator.android.network.event.UpdateReadStateItemEvent;
import com.rssaggregator.android.network.event.UpdateStarStateItemEvent;
import com.rssaggregator.android.network.model.AccessToken;
import com.rssaggregator.android.network.model.AddCategoryWrapper;
import com.rssaggregator.android.network.model.AddFeedWrapper;
import com.rssaggregator.android.network.model.CategoriesWrapper;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;
import com.rssaggregator.android.network.model.Credentials;
import com.rssaggregator.android.network.model.Item;
import com.rssaggregator.android.network.model.ItemStateWrapper;
import com.rssaggregator.android.network.utils.TokenRequestInterceptor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class which implements the Rest Service and does, asynchronously, requests to the API.
 */
public class RssApiImpl implements RssApi {
  private static AccessToken accessToken;
  private final Context context;
  private final RestService restService;
  private final EventBus eventBus;
  private final TokenRequestInterceptor requestInterceptor;

  @Inject
  public RssApiImpl(Context context, RestService restService, EventBus eventBus,
                    TokenRequestInterceptor requestInterceptor) {
    this.context = context;
    this.restService = restService;
    this.eventBus = eventBus;
    this.requestInterceptor = requestInterceptor;
    this.eventBus.register(this);
  }

  /**
   * Logs the user to the API.
   *
   * @param credentials User and Password of the user.
   */
  @Override
  public void logIn(Credentials credentials) {
    this.restService.logIn(credentials).enqueue(new Callback<AccessToken>() {
      @Override
      public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
        if (response.isSuccessful()) {
          AccessToken accessToken = response.body();
          eventBus.post(new LogInEvent(accessToken));
          requestInterceptor.setAccessToken(accessToken);
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);
            eventBus.post(new LogInEvent(new Throwable(error.getErrorDetails())));
          } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().length() != 0) {
              eventBus.post(new LogInEvent(new Throwable(e.getMessage())));
            } else {
              eventBus.post(new LogInEvent(new Throwable("Error")));
            }
          }
        }
      }

      @Override
      public void onFailure(Call<AccessToken> call, Throwable throwable) {
        if (throwable != null && throwable.getMessage() != null &&
            throwable.getMessage().length() != 0) {
          eventBus.post(new LogInEvent(throwable));
        } else {
          eventBus.post(new LogInEvent(new Throwable("Error")));
        }
      }
    });
  }

  /**
   * Signs up the user to the API.
   *
   * @param credentials User and password of the user.
   */
  @Override
  public void signUp(Credentials credentials) {
    this.restService.signUp(credentials).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()) {
          eventBus.post(new SignUpEvent());
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);
            eventBus.post(new SignUpEvent(new Throwable(error.getErrorDetails())));
          } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().length() != 0) {
              eventBus.post(new SignUpEvent(new Throwable(e.getMessage())));
            } else {
              eventBus.post(new SignUpEvent(new Throwable("Error")));
            }
          }
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable throwable) {
        if (throwable != null && throwable.getMessage() != null &&
            throwable.getMessage().length() != 0) {
          eventBus.post(new SignUpEvent(throwable));
        } else {
          eventBus.post(new SignUpEvent(new Throwable("Error")));
        }
      }
    });
  }

  /**
   * Fetch all the data from the api.
   */
  @Override
  public void fetchData() {
    this.restService.fetchData().enqueue(new Callback<CategoriesWrapper>() {
      @Override
      public void onResponse(Call<CategoriesWrapper> call, Response<CategoriesWrapper> response) {
        if (response.isSuccessful()) {
          CategoriesWrapper wrapper = response.body();
          eventBus.post(new FetchDataEvent(wrapper));
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);
            eventBus.post(new FetchDataEvent(new Throwable(error.getErrorDetails())));
          } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().length() != 0) {
              eventBus.post(new FetchDataEvent(new Throwable(e.getMessage())));
            } else {
              eventBus.post(new FetchDataEvent(new Throwable("Error")));
            }
          }
        }
      }

      @Override
      public void onFailure(Call<CategoriesWrapper> call, Throwable throwable) {
        if (throwable != null && throwable.getMessage() != null &&
            throwable.getMessage().length() != 0) {
          eventBus.post(new FetchDataEvent(throwable));
        } else {
          eventBus.post(new FetchDataEvent(new Throwable("Error")));
        }
      }
    });
  }

  /**
   * Adds a category to the user.
   *
   * @param categoryName name of the new category.
   */
  @Override
  public void addCategory(String categoryName) {
    AddCategoryWrapper wrapper = new AddCategoryWrapper(categoryName);
    this.restService.addCategory(wrapper).enqueue(new Callback<Category>() {
      @Override
      public void onResponse(Call<Category> call, Response<Category> response) {
        if (response.isSuccessful()) {
          Category category = response.body();
          eventBus.post(new AddCategoryEvent(category));
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);
            eventBus.post(new AddCategoryEvent(new Throwable(error.getErrorDetails())));
          } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().length() != 0) {
              eventBus.post(new AddCategoryEvent(new Throwable(e.getMessage())));
            } else {
              eventBus.post(new AddCategoryEvent(new Throwable("Error")));
            }
          }
        }
      }

      @Override
      public void onFailure(Call<Category> call, Throwable throwable) {
        if (throwable != null && throwable.getMessage() != null &&
            throwable.getMessage().length() != 0) {
          eventBus.post(new AddCategoryEvent(throwable));
        } else {
          eventBus.post(new AddCategoryEvent(new Throwable("Error")));
        }
      }
    });
  }


  /**
   * Subscribes/adds a feed.
   *
   * @param category category to add the feed
   * @param rssLink  link of the feed.
   */
  @Override
  public void subscribeFeed(Category category, String rssLink) {
    AddFeedWrapper wrapper = new AddFeedWrapper(category.getCategoryId(), rssLink);
    this.restService.subscribeFeed(wrapper).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()) {
          eventBus.post(new AddFeedEvent());
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);
            eventBus.post(new AddFeedEvent(new Throwable(error.getErrorDetails())));
          } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().length() != 0) {
              eventBus.post(new AddFeedEvent(new Throwable(e.getMessage())));
            } else {
              eventBus.post(new AddFeedEvent(new Throwable("Error")));
            }
          }
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable throwable) {
        if (throwable != null && throwable.getMessage() != null &&
            throwable.getMessage().length() != 0) {
          eventBus.post(new AddFeedEvent(throwable));
        } else {
          eventBus.post(new AddFeedEvent(new Throwable("Error")));
        }
      }
    });
  }

  /**
   * Unsubscribe the user to the channel.
   *
   * @param channelId id of the channel to unsubscribe.
   */
  @Override
  public void unsubscribeFeed(Integer channelId) {
    this.restService.unsubscribeFeed(channelId).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()) {
          eventBus.post(new UnsubscribeChannelEvent());
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);
            eventBus.post(new UnsubscribeChannelEvent(new Throwable(error.getErrorDetails())));
          } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().length() != 0) {
              eventBus.post(new UnsubscribeChannelEvent(new Throwable(e.getMessage())));
            } else {
              eventBus.post(new UnsubscribeChannelEvent(new Throwable("Error")));
            }
          }
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable throwable) {
        if (throwable != null && throwable.getMessage() != null &&
            throwable.getMessage().length() != 0) {
          eventBus.post(new UnsubscribeChannelEvent(throwable));
        } else {
          eventBus.post(new UnsubscribeChannelEvent(new Throwable("Error")));
        }
      }
    });
  }

  //
  //
  // Update read methods.
  //
  //

  /**
   * Updates all the items to read state.
   */
  @Override
  public void updateReadAllItems() {
    ItemStateWrapper wrapper = new ItemStateWrapper(true, null);
    this.restService.updateReadAllItems(wrapper).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()) {
          eventBus.post(new UpdateReadAllItemsEvent());
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);
            eventBus.post(new UpdateReadAllItemsEvent(new Throwable(error.getErrorDetails())));
          } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().length() != 0) {
              eventBus.post(new UpdateReadAllItemsEvent(new Throwable(e.getMessage())));
            } else {
              eventBus.post(new UpdateReadAllItemsEvent(new Throwable("Error")));
            }
          }
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable throwable) {
        if (throwable != null && throwable.getMessage() != null &&
            throwable.getMessage().length() != 0) {
          eventBus.post(new UpdateReadAllItemsEvent(throwable));
        } else {
          eventBus.post(new UpdateReadAllItemsEvent(new Throwable("Error")));
        }
      }
    });
  }

  /**
   * Updates all the items of a channel to read state.
   *
   * @param channel channel to update.
   */
  @Override
  public void updateReadItemsByChannelId(Channel channel) {
    ItemStateWrapper wrapper = new ItemStateWrapper(true, null);
    this.restService.updateReadItemsByChannelId(channel.getChannelId(), wrapper)
        .enqueue(new Callback<Void>() {

          @Override
          public void onResponse(Call<Void> call, Response<Void> response) {
            if (response.isSuccessful()) {
              eventBus.post(new UpdateReadItemsByChannelIdEvent());
            } else {
              try {
                String json = response.errorBody().string();
                ApiError error = new Gson().fromJson(json, ApiError.class);
                eventBus.post(new UpdateReadItemsByChannelIdEvent(new Throwable(error
                    .getErrorDetails())));
              } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().length() != 0) {
                  eventBus.post(new UpdateReadItemsByChannelIdEvent(new Throwable(e.getMessage())));
                } else {
                  eventBus.post(new UpdateReadItemsByChannelIdEvent(new Throwable("Error")));
                }
              }
            }
          }

          @Override
          public void onFailure(Call<Void> call, Throwable throwable) {
            if (throwable != null && throwable.getMessage() != null &&
                throwable.getMessage().length() != 0) {
              eventBus.post(new UpdateReadItemsByChannelIdEvent(throwable));
            } else {
              eventBus.post(new UpdateReadItemsByChannelIdEvent(new Throwable("Error")));
            }
          }
        });
  }

  /**
   * Updates the Read State.
   *
   * @param item  Item to update
   * @param state New read state.
   */
  @Override
  public void updateReadStateItem(Item item, Boolean state) {
    ItemStateWrapper wrapper = new ItemStateWrapper(state, item.isStarred());
    this.restService.updateStateItem(item.getItemId(), wrapper).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()) {
          eventBus.post(new UpdateReadStateItemEvent());
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);
            eventBus.post(new UpdateReadStateItemEvent(new Throwable(error
                .getErrorDetails())));
          } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().length() != 0) {
              eventBus.post(new UpdateReadStateItemEvent(new Throwable(e.getMessage())));
            } else {
              eventBus.post(new UpdateReadStateItemEvent(new Throwable("Error")));
            }
          }
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable throwable) {
        if (throwable != null && throwable.getMessage() != null &&
            throwable.getMessage().length() != 0) {
          eventBus.post(new UpdateReadStateItemEvent(throwable));
        } else {
          eventBus.post(new UpdateReadStateItemEvent(new Throwable("Error")));
        }
      }
    });
  }

  /**
   * Updates the Star State.
   *
   * @param item  Item to update
   * @param state The new state to update.
   */
  @Override
  public void updateStarStateItem(Item item, Boolean state) {
    ItemStateWrapper wrapper = new ItemStateWrapper(item.isRead(), state);
    this.restService.updateStateItem(item.getItemId(), wrapper).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()) {
          eventBus.post(new UpdateStarStateItemEvent());
        } else {
          try {
            String json = response.errorBody().string();
            ApiError error = new Gson().fromJson(json, ApiError.class);
            eventBus.post(new UpdateStarStateItemEvent(new Throwable(error
                .getErrorDetails())));
          } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().length() != 0) {
              eventBus.post(new UpdateStarStateItemEvent(new Throwable(e.getMessage())));
            } else {
              eventBus.post(new UpdateStarStateItemEvent(new Throwable("Error")));
            }
          }
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable throwable) {
        if (throwable != null && throwable.getMessage() != null &&
            throwable.getMessage().length() != 0) {
          eventBus.post(new UpdateStarStateItemEvent(throwable));
        } else {
          eventBus.post(new UpdateStarStateItemEvent(new Throwable("Error")));
        }
      }
    });
  }

  //
  //
  // Event methods
  //
  //

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(AccessTokenFetchedEvent event) {
    accessToken = event.getAccessToken();
    requestInterceptor.setAccessToken(accessToken);
  }

  @SuppressWarnings("UnusedDeclaration")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(LogOutEvent event) {
    accessToken = null;
    requestInterceptor.resetAccessToken();
  }
}
