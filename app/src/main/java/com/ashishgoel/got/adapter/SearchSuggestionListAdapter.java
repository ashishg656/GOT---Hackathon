package com.ashishgoel.got.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashishgoel.got.R;
import com.ashishgoel.got.objects.kingDetails.KingDetailsObject;
import com.ashishgoel.got.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ashish on 08/01/17.
 */

public class SearchSuggestionListAdapter extends RecyclerView.Adapter<SearchSuggestionListAdapter.SearchSuggestionHolder> {

    Context context;
    List<KingDetailsObject> mData;

    LayoutInflater layoutInflater;
    int imageRadius;

    public SearchSuggestionListAdapter(Context context, List<KingDetailsObject> mData) {
        this.context = context;
        this.mData = mData;

        imageRadius = context.getResources().getDimensionPixelSize(R.dimen.search_king_user_image_radius);

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public SearchSuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.search_suggestion_list_item, parent, false);
        return new SearchSuggestionHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchSuggestionHolder holder, int position) {
        KingDetailsObject object = mData.get(position);

        holder.name.setText(object.getName());
        ImageUtils.loadKingImage(holder.imageView, object, context, imageRadius);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class SearchSuggestionHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.king_image)
        ImageView imageView;
        @BindView(R.id.king_name)
        TextView name;

        public SearchSuggestionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
