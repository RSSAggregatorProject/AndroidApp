package com.rssaggregator.android.feed.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rssaggregator.android.R;
import com.rssaggregator.android.feeddetails.view.ItemDetailsActivity;
import com.rssaggregator.android.network.model.Item;
import com.rssaggregator.android.utils.FormatterTime;
import com.rssaggregator.android.utils.Globals;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

  private List<Item> items;
  private Context context;

  public ItemsAdapter(Context context, List<Item> items) {
    this.context = context;
    this.items = items;
  }

  @Override
  public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    view = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
    return new ItemViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
    final Item item = this.items.get(position);

    if (item.isRead()) {
      viewHolder.titleItemTv.setTextColor(Color.parseColor("#757575"));
    } else {
      viewHolder.titleItemTv.setTextColor(Color.parseColor("#512DA8"));
    }
    viewHolder.titleItemTv.setText(item.getName());
    viewHolder.pubDateItemTv.setText(FormatterTime.formattedAsTimeAgo(context, item.getPubDate()));
    viewHolder.nameChannelTv.setText(item.getNameChannel());

    if (item.isStarred()) {
      viewHolder.starItemIg.setImageResource(R.drawable.ic_star);
    } else {
      viewHolder.starItemIg.setImageResource(R.drawable.ic_star_border);
    }

    viewHolder.rowItemRl.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, ItemDetailsActivity.class);
        intent.putExtra(Globals.EXTRA_ITEM, item);
        context.startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return this.items.size();
  }

  static public class ItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.rowItem) LinearLayout rowItemRl;
    @BindView(R.id.titleItem) AppCompatTextView titleItemTv;
    @BindView(R.id.pubDateItem) AppCompatTextView pubDateItemTv;
    @BindView(R.id.nameChannel) AppCompatTextView nameChannelTv;
    @BindView(R.id.starItem) AppCompatImageView starItemIg;

    public ItemViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
