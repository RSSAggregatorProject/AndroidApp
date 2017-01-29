package com.rssaggregator.android.navigationdrawer;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.rssaggregator.android.R;
import com.rssaggregator.android.feed.event.NavigationItemClickedEvent;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;
import com.rssaggregator.android.utils.ArrayUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for expandable list view.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

  private Context context;
  private Resources resources;

  // Data
  private List<Category> categoryList;
  private HashMap<Category, List<Channel>> channelList;

  // EventBus
  private EventBus eventBus;

  public ExpandableListAdapter(Context context, List<Category> categoryList,
                               HashMap<Category, List<Channel>> channelList, EventBus eventBus) {
    this.context = context;
    this.resources = context.getResources();
    this.categoryList = categoryList;
    this.channelList = channelList;
    this.eventBus = eventBus;
  }

  @Override
  public int getGroupCount() {
    return this.categoryList.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return this.channelList.get(this.categoryList.get(groupPosition)).size();
  }

  @Override
  public Object getGroup(int groupPosition) {
    return this.categoryList.get(groupPosition);
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return this.channelList.get(this.categoryList.get(groupPosition))
        .get(childPosition);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView,
                           ViewGroup
                               parent) {
    final Category category = (Category) getGroup(groupPosition);

    View view = convertView;
    final CategoryViewHolder viewHolder;

    if (view == null) {
      LayoutInflater inflater = (LayoutInflater)
          context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.row_category, parent, false);
      viewHolder = new CategoryViewHolder(view);
      view.setTag(viewHolder);
    } else {
      viewHolder = (CategoryViewHolder) view.getTag();
    }

    viewHolder.nameCategory.setText(category.getName());

    // Set unread items
    if (resources.getString(R.string.category_all).equals(category.getName())) {
      if (category.getUnread() != null) {
        viewHolder.unreadCategory.setText(String.valueOf(category.getUnread()));
      }
    } else if (resources.getString(R.string.category_star).equals(category.getName())) {
      if (category.getUnread() != null) {
        viewHolder.unreadCategory.setText(String.valueOf(category.getUnread()));
      }
    } else {
      viewHolder.unreadCategory.setText(String.valueOf(
          ArrayUtils.getUnreadCategories(category.getChannels())));
    }

    // Set All and Starred categories
    if (resources.getString(R.string.category_all).equals(category.getName())) {
      viewHolder.iconCategory.setImageResource(R.drawable.ic_all_inclusive_black_24dp);
    } else if (resources.getString(R.string.category_star).equals(category.getName())) {
      viewHolder.iconCategory.setImageResource(R.drawable.ic_star_black_24dp);
    } else {
      if (category.getChannels() == null || category.getChannels().size() == 0) {
        viewHolder.iconCategory.setImageDrawable(null);
      } else {
        viewHolder.iconCategory.setImageResource(R.drawable.ic_chevron_right_black_24dp);
      }
    }

    if (resources.getString(R.string.category_all).equals(category.getName())) {
      viewHolder.rowCategory.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          eventBus.post(new NavigationItemClickedEvent(true, false));
        }
      });
    } else if (resources.getString(R.string.category_star).equals(category.getName())) {
      viewHolder.rowCategory.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          eventBus.post(new NavigationItemClickedEvent(false, true));
        }
      });
    } else {
      viewHolder.nameCategory.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          eventBus.post(new NavigationItemClickedEvent(category));
        }
      });
    }


    return view;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
      convertView, ViewGroup parent) {
    final Channel channel = (Channel) getChild(groupPosition, childPosition);
    final Category category = (Category) getGroup(groupPosition);

    View view = convertView;
    ChannelViewHolder viewHolder;

    if (view == null) {
      LayoutInflater inflater = (LayoutInflater)
          context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.row_channel, parent, false);
      viewHolder = new ChannelViewHolder(view);
      view.setTag(viewHolder);
    } else {
      viewHolder = (ChannelViewHolder) view.getTag();
    }

    viewHolder.nameChannel.setText(channel.getName());

    if (channel.getUnread() == null) {
      viewHolder.unreadChannel.setText("0");
    } else {
      viewHolder.unreadChannel.setText(String.valueOf(channel.getUnread()));
    }

    viewHolder.rowChannel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        eventBus.post(new NavigationItemClickedEvent(channel, category));
      }
    });

    return view;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  public final class CategoryViewHolder {
    @BindView(R.id.rowCategory) View rowCategory;
    @BindView(R.id.iconCategory) AppCompatImageView iconCategory;
    @BindView(R.id.nameCategory) AppCompatTextView nameCategory;
    @BindView(R.id.unreadCategory) AppCompatTextView unreadCategory;

    public CategoryViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  public final class ChannelViewHolder {
    @BindView(R.id.rowChannel) View rowChannel;
    @BindView(R.id.iconChannel) AppCompatImageView iconChannel;
    @BindView(R.id.nameChannel) AppCompatTextView nameChannel;
    @BindView(R.id.unreadChannel) AppCompatTextView unreadChannel;

    public ChannelViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}