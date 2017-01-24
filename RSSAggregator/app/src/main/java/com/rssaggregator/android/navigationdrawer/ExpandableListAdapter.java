package com.rssaggregator.android.navigationdrawer;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.rssaggregator.android.R;
import com.rssaggregator.android.network.model.Category;
import com.rssaggregator.android.network.model.Channel;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

  private ExpandableListView expandableListView;
  private Context context;

  // Data
  private List<Category> categoryList;
  private HashMap<Category, List<Channel>> channelList;

  public ExpandableListAdapter(Context context, List<Category> categoryList,
                               HashMap<Category, List<Channel>> channelList,
                               ExpandableListView expandableListView) {
    this.context = context;
    this.categoryList = categoryList;
    this.channelList = channelList;
    this.expandableListView = expandableListView;
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
    Category category = (Category) getGroup(groupPosition);

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

    // Set All and Starred categories
    if ("All".equals(category.getName())) {
      viewHolder.iconCategory.setImageResource(R.drawable.ic_all_inclusive_black_24dp);
    } else if ("Starred Items".equals(category.getName())) {
      viewHolder.iconCategory.setImageResource(R.drawable.ic_star_black_24dp);
    } else {
      viewHolder.iconCategory.setImageResource(R.drawable.ic_chevron_right_black_24dp);
    }
    return view;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
      convertView, ViewGroup parent) {
    Channel channel = (Channel) getChild(groupPosition, childPosition);

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
    viewHolder.unreadChannel.setText(String.valueOf(channel.getUnread()));

    return view;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  public final class CategoryViewHolder {
    @BindView(R.id.rowCategory) View view;
    @BindView(R.id.iconCategory) AppCompatImageView iconCategory;
    @BindView(R.id.nameCategory) AppCompatTextView nameCategory;
    @BindView(R.id.unreadCategory) AppCompatTextView unreadCategory;

    public CategoryViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  public final class ChannelViewHolder {
    @BindView(R.id.rowChannel) View view;
    @BindView(R.id.iconChannel) AppCompatImageView iconChannel;
    @BindView(R.id.nameChannel) AppCompatTextView nameChannel;
    @BindView(R.id.unreadChannel) AppCompatTextView unreadChannel;

    public ChannelViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}