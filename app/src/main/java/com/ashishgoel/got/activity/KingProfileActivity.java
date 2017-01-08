package com.ashishgoel.got.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashishgoel.got.R;
import com.ashishgoel.got.extras.AppConstants;
import com.ashishgoel.got.objects.kingDetails.KingDetailsObject;
import com.ashishgoel.got.sqlite.kings.KingsSqliteHelper;
import com.ashishgoel.got.utils.AndroidUtils;
import com.ashishgoel.got.utils.ImageUtils;
import com.ashishgoel.got.utils.UIUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KingProfileActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pie_chart)
    PieChart mChart;

    KingDetailsObject mData;

    private float[] yData = {0, 0, 0};
    private String[] xData = {"Won", "Lost", "Draw"};

    @BindView(R.id.king_image)
    ImageView kingImage;
    @BindView(R.id.king_name)
    TextView kingName;
    @BindView(R.id.king_status)
    TextView kingStatus;
    @BindView(R.id.king_battleslost)
    TextView battlesLost;
    @BindView(R.id.king_battleswon)
    TextView battlesWon;
    @BindView(R.id.king_battle_strength)
    TextView strength;
    @BindView(R.id.king_battlesdraw)
    TextView battleDraw;
    @BindView(R.id.strenthtype)
    TextView strengthType;
    @BindView(R.id.strenthtype_battletype)
    TextView strenthInBattleType;
    @BindView(R.id.strenthtypeimage)
    ImageView strengthTypeImage;
    @BindView(R.id.pie_chart_container)
    LinearLayout pieChartContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.king_profile_activity_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab.setOnClickListener(this);

        mData = getIntent().getParcelableExtra(AppConstants.INTENT_DATA.INTENT_DATA_KING_OBJECT);
        KingsSqliteHelper helper = new KingsSqliteHelper(this);
        List<KingDetailsObject> results = helper.getAllKings();
        if (results != null) {
            for (KingDetailsObject obj : results) {
                if (AndroidUtils.compareString(obj.getName(), mData.getName())) {
                    mData = obj;
                    break;
                }
            }
        }

        setData();
    }

    private void setData() {
        getSupportActionBar().setTitle(mData.getName());
        ImageUtils.loadKingImage(kingImage, mData, this, getResources().getDimensionPixelSize(R.dimen.king_user_image_radius));
        kingName.setText(mData.getName());
        battlesLost.setText("Battles lost : " + mData.getNumberOfBattlesLost());
        battlesWon.setText("Battles won : " + mData.getTotalNumberOfWins());
        battleDraw.setText("Battles drawn : " + mData.getNumberOfDraws());

        String status;
        if (mData.getTotalNumberOfWins() != 0 && mData.getNumberOfBattlesLost() == 0) {
            status = "Won " + mData.getTotalNumberOfWins() + " battles, haven't lost any battle.";
        } else if (mData.getTotalNumberOfWins() != 0) {
            status = "Won " + mData.getTotalNumberOfWins() + " battles.";
        } else if (mData.getNumberOfBattlesLost() == 1) {
            status = "Lost " + mData.getNumberOfBattlesLost() + " battle";
        } else if (mData.getNumberOfBattlesLost() > 1) {
            status = "Lost " + mData.getNumberOfBattlesLost() + " battles";
        } else {
            status = "Won " + mData.getTotalNumberOfWins() + " battles, Lost " + mData.getNumberOfBattlesLost() + " battles";
        }
        kingStatus.setText(status);

        if (mData.getCurrentRating() != null) {
            strength.setText("Rating : " + mData.getCurrentRating().intValue());
        } else {
            strength.setText("Rating : 400");
        }

        if (mData.getNumberOfAttackWins() >= mData.getNumberOfDefenseWins()) {
            UIUtils.loadImage(strengthTypeImage, R.drawable.g_sword);
            strengthType.setText("Strength : Attack");
        } else if (mData.getNumberOfAttackWins() < mData.getNumberOfDefenseWins()) {
            UIUtils.loadImage(strengthTypeImage, R.drawable.g_shield);
            strengthType.setText("Strength : Defense");
        }

        if (mData.getStrengthForBattleType() != null) {
            int maxValue = -1;
            String maxKey = null;

            for (String key : mData.getStrengthForBattleType().keySet()) {
                Integer value = mData.getStrengthForBattleType().get(key);
                if (value != null && value > maxValue) {
                    maxKey = key;
                }
            }

            if (maxKey != null) {
                strenthInBattleType.setText("Strength in battle type : " + maxKey.toUpperCase());
            }
        }

        yData[0] = mData.getTotalNumberOfWins();
        yData[1] = mData.getNumberOfBattlesLost();
        yData[2] = mData.getNumberOfDraws();

        setPieChartData();
    }

    private void setPieChartData() {
        pieChartContainer.setVisibility(View.VISIBLE);

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.TRANSPARENT);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(30f);
        mChart.setTransparentCircleRadius(31f);

        mChart.setDrawCenterText(false);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        setDataForPieChart();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);
    }

    private void setDataForPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < xData.length; i++) {
            entries.add(new PieEntry(yData[i], xData[i]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Battles");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:

                break;
        }
    }
}
