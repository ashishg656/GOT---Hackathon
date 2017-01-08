package com.ashishgoel.got.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashishgoel.got.R;
import com.ashishgoel.got.activity.BaseActivity;
import com.ashishgoel.got.extras.AppConstants;
import com.ashishgoel.got.objects.kingDetails.KingDetailsObject;
import com.ashishgoel.got.sqlite.kings.KingsSqliteHelper;
import com.ashishgoel.got.utils.AndroidUtils;
import com.ashishgoel.got.utils.ImageUtils;
import com.ashishgoel.got.utils.UIUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ashish on 08/01/17.
 */

public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    List<KingDetailsObject> mdata;
    Context context;

    LayoutInflater layoutInflater;
    boolean isMoreAllowed;
    int imageRadius;

    public HomeListAdapter(HashMap<String, KingDetailsObject> hashMap, Context context, boolean isMoreAllowed, boolean setSqlData) {
        this.context = context;
        this.isMoreAllowed = isMoreAllowed;
        this.mdata = new ArrayList<>();

        if (hashMap != null) {
            for (String key : hashMap.keySet()) {
                mdata.add(hashMap.get(key));
            }
        }

        Collections.sort(mdata, Collections.<KingDetailsObject>reverseOrder());

        if (setSqlData) {
            new KingsSqliteHelper(context).setData(mdata);
        }

        imageRadius = context.getResources().getDimensionPixelSize(R.dimen.king_user_image_radius);

        layoutInflater = LayoutInflater.from(context);
    }

    public HomeListAdapter(List<KingDetailsObject> mdata, Context context, boolean isMoreAllowed) {
        this.context = context;
        this.isMoreAllowed = isMoreAllowed;
        this.mdata = mdata;

        Collections.sort(mdata, Collections.<KingDetailsObject>reverseOrder());
        imageRadius = context.getResources().getDimensionPixelSize(R.dimen.king_user_image_radius);

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return AppConstants.TYPE_RECYCLER_VIEW_HEADER;
        } else if (position > mdata.size()) {
            return AppConstants.TYPE_RECYCLER_VIEW_LOADING;
        } else {
            return AppConstants.TYPE_RECYCLER_VIEW_NORMAL;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.king_container:
                KingDetailsObject object = (KingDetailsObject) view.getTag();
                ((BaseActivity) context).openKingDetailActivity(object);
                break;
        }
    }

    private static class LoadingHolder extends RecyclerView.ViewHolder {

        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.header_text_count)
        TextView count;

        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class KingHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.king_image)
        ImageView imageView;
        @BindView(R.id.king_name)
        TextView name;
        @BindView(R.id.king_status)
        TextView status;
        @BindView(R.id.king_defense_container)
        LinearLayout defenseContainer;
        @BindView(R.id.king_defense_text)
        TextView defenseText;
        @BindView(R.id.king_defense_image)
        ImageView defenseImage;
        @BindView(R.id.king_ranking)
        TextView ranking;
        @BindView(R.id.king_container)
        LinearLayout container;

        public KingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AppConstants.TYPE_RECYCLER_VIEW_LOADING) {
            View view = layoutInflater.inflate(R.layout.loading_more_layout, parent, false);
            return new LoadingHolder(view);
        } else if (viewType == AppConstants.TYPE_RECYCLER_VIEW_HEADER) {
            View view = layoutInflater.inflate(R.layout.king_list_header_layout, parent, false);
            return new HeaderHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.king_list_item_layout, parent, false);
            return new KingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderCom, int position) {
        position = holderCom.getAdapterPosition();
        if (getItemViewType(position) == AppConstants.TYPE_RECYCLER_VIEW_NORMAL) {
            KingHolder holder = (KingHolder) holderCom;
            position = position - 1;

            KingDetailsObject object = mdata.get(position);

            holder.name.setText(object.getName());
            ImageUtils.loadKingImage(holder.imageView, object, context, imageRadius);

            String status = null;
            if (object.getTotalNumberOfWins() != 0 && object.getNumberOfBattlesLost() == 0) {
                status = "Won " + object.getTotalNumberOfWins() + " battles, haven't lost any battle.";
            } else if (object.getTotalNumberOfWins() != 0) {
                status = "Won " + object.getTotalNumberOfWins() + " battles.";
            } else if (object.getNumberOfBattlesLost() == 1) {
                status = "Lost " + object.getNumberOfBattlesLost() + " battle";
            } else if (object.getNumberOfBattlesLost() > 1) {
                status = "Lost " + object.getNumberOfBattlesLost() + " battles";
            }

            if (object.getCurrentRating() != null) {
                holder.ranking.setText(object.getCurrentRating().intValue() + "");
            } else {
                holder.ranking.setText("400");
            }

            if (!AndroidUtils.isEmpty(status)) {
                holder.status.setText(status);
                holder.status.setVisibility(View.VISIBLE);
            } else {
                holder.status.setVisibility(View.GONE);
            }

            if (object.getNumberOfAttackWins() == 0 && object.getNumberOfDefenseWins() == 0) {
                holder.defenseContainer.setVisibility(View.GONE);
            } else {
                holder.defenseContainer.setVisibility(View.VISIBLE);
                if (object.getNumberOfAttackWins() >= object.getNumberOfDefenseWins()) {
                    UIUtils.loadImage(holder.defenseImage, R.drawable.g_sword);
                    holder.defenseText.setText(context.getResources().getString(R.string.attack_text));
                } else if (object.getNumberOfAttackWins() < object.getNumberOfDefenseWins()) {
                    UIUtils.loadImage(holder.defenseImage, R.drawable.g_shield);
                    holder.defenseText.setText(context.getResources().getString(R.string.defense_text));
                }
            }

            holder.container.setTag(object);
            holder.container.setOnClickListener(this);
        } else if (getItemViewType(position) == AppConstants.TYPE_RECYCLER_VIEW_HEADER) {
            HeaderHolder holder = (HeaderHolder) holderCom;
            if (mdata.size() == 1) {
                holder.count.setText(mdata.size() + " Result");
            } else {
                holder.count.setText(mdata.size() + " Results");
            }
        }
    }

    @Override
    public int getItemCount() {
        int size = 1;

        if (mdata == null) {
            return 0;
        } else {
            size += mdata.size();
        }

        if (isMoreAllowed) {
            size++;
        }

        return size;
    }
}
