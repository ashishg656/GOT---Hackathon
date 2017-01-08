package com.ashishgoel.got.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.ashishgoel.got.R;
import com.ashishgoel.got.extras.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ashish on 08/01/17.
 */

public class FilterListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    ArrayList<String> filters, selectedItems;
    Context context;

    LayoutInflater layoutInflater;

    public ArrayList<String> getSelectedItems() {
        return selectedItems;
    }

    public FilterListAdapter(ArrayList<String> filters, Context context, ArrayList<String> selectedItems) {
        this.filters = filters;
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
        if (selectedItems == null) {
            this.selectedItems = new ArrayList<>();
        } else {
            this.selectedItems = selectedItems;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AppConstants.TYPE_RECYCLER_VIEW_HEADER) {
            View view = layoutInflater.inflate(R.layout.filter_list_header_layout, parent, false);
            return new HeaderHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.filter_list_item_layout, parent, false);
            return new FlterListHolder(view);
        }
    }

    @Override
    public int getItemCount() {
        return filters.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return AppConstants.TYPE_RECYCLER_VIEW_HEADER;
        } else {
            return AppConstants.TYPE_RECYCLER_VIEW_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderCom, int position) {
        position = holderCom.getAdapterPosition();

        if (getItemViewType(position) == AppConstants.TYPE_RECYCLER_VIEW_NORMAL) {
            FlterListHolder holder = (FlterListHolder) holderCom;
            position = position - 1;

            String text = filters.get(position);

            holder.checkBox.setText(text);
            if (selectedItems.contains(text)) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }

            holder.container.setTag(R.integer.z_tag_position, position);
            holder.container.setTag(R.integer.z_tag_object, text);
            holder.container.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_checkbox_container:
                String filterText = (String) view.getTag(R.integer.z_tag_object);
                int position = (int) view.getTag(R.integer.z_tag_position);

                if (selectedItems.contains(filterText)) {
                    selectedItems.remove(filterText);
                } else {
                    selectedItems.add(filterText);
                }

                notifyItemChanged(position + 1);
                break;
        }
    }

    public void clearFilters() {
        try {
            selectedItems.clear();
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class FlterListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkBox_filter)
        CheckBox checkBox;
        @BindView(R.id.filter_checkbox_container)
        LinearLayout container;

        public FlterListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }
}
