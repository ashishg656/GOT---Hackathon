package com.ashishgoel.got.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.VolleyError;
import com.ashishgoel.got.R;
import com.ashishgoel.got.adapter.HomeListAdapter;
import com.ashishgoel.got.extras.AppConstants;
import com.ashishgoel.got.extras.RequestTags;
import com.ashishgoel.got.fragment.FilterFragment;
import com.ashishgoel.got.interfaces.RankCalculatorListenerInterface;
import com.ashishgoel.got.objects.homeApi.HomeResponseObject;
import com.ashishgoel.got.objects.kingDetails.KingDetailsObject;
import com.ashishgoel.got.requests.HomeAppRequests;
import com.ashishgoel.got.serverApi.AppRequestListener;
import com.ashishgoel.got.urls.HomeRequestUrls;
import com.ashishgoel.got.utils.AndroidUtils;
import com.ashishgoel.got.utils.DebugUtils;
import com.ashishgoel.got.utils.UIUtils;
import com.ashishgoel.got.utils.VolleyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ashish on 07/01/17.
 */

public class HomeActivity extends BaseActivity implements AppRequestListener, RankCalculatorListenerInterface, FilterFragment.FilterApplyInterface, View.OnClickListener {

    private List<HomeResponseObject> mData;

    @BindView(R.id.home_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    LinearLayoutManager layoutManager;
    HomeListAdapter adapter;

    ArrayList<String> allBattleTypes;

    ArrayList<String> filterBattleTypes;

    private String requestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        ButterKnife.bind(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setProgressAndErrorLayoutVariables();

        retryButton.setOnClickListener(this);

        loadData();
    }

    private void loadData() {
        requestUrl = HomeRequestUrls.getHomeApiUrl();
        HomeAppRequests.makeHomeApiRequest(requestUrl, this, this);
    }

    @Override
    public void onRequestStarted(String requestTag) {
        if (requestTag.equalsIgnoreCase(RequestTags.HOME_REQUEST)) {
            hideErrorLayout();
            showProgressLayout();
        }
    }

    @Override
    public void onRequestFailed(String requestTag, VolleyError error, boolean networkError) {
        if (requestTag.equalsIgnoreCase(RequestTags.HOME_REQUEST)) {
            try {
                String response = VolleyUtils.getResponseFromCache(requestUrl);
                Type listType = new TypeToken<ArrayList<HomeResponseObject>>() {
                }.getType();
                mData = new Gson().fromJson(response, listType);

                new RatingCalculator(mData, this, null).execute();
            } catch (Exception e) {
                hideProgressLayout();
                showErrorLayout();
            }
        }
    }

    @Override
    public void onRequestCompleted(String requestTag, String response) {
        if (requestTag.equalsIgnoreCase(RequestTags.HOME_REQUEST)) {
            Type listType = new TypeToken<ArrayList<HomeResponseObject>>() {
            }.getType();
            mData = new Gson().fromJson(response, listType);

            new RatingCalculator(mData, this, null).execute();
        }
    }

    @Override
    public void onCalculationStared() {
        showProgressLayout();
        hideErrorLayout();
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onCalculationCompleted(HashMap<String, KingDetailsObject> result, Set<String> battleTypesData) {
        hideProgressLayout();
        hideErrorLayout();

        boolean setSqlData = true;
        if (filterBattleTypes != null) {
            setSqlData = false;
        }

        adapter = new HomeListAdapter(result, this, false, setSqlData);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);

        if (this.allBattleTypes == null && battleTypesData != null) {
            this.allBattleTypes = new ArrayList<>();
            this.allBattleTypes.addAll(battleTypesData);
        }
    }

    @Override
    public void onFilterApplied(ArrayList<String> data) {
        this.filterBattleTypes = data;

        new RatingCalculator(mData, this, filterBattleTypes).execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_error:
                loadData();
                break;
        }
    }

    static class RatingCalculator extends AsyncTask<String, Integer, String> {

        List<HomeResponseObject> mData;
        List<String> filters;
        HashMap<String, KingDetailsObject> hashMap;
        RankCalculatorListenerInterface listenerInterface;
        Set<String> battleTypes;

        public static final int N_FACTOR = 400;
        public static final int K_FACTOR = 32;

        public RatingCalculator(List<HomeResponseObject> mData, RankCalculatorListenerInterface listenerInterface, List<String> filters) {
            this.mData = mData;
            this.filters = filters;
            this.listenerInterface = listenerInterface;
            hashMap = new HashMap<>();
            battleTypes = new HashSet<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (listenerInterface != null) {
                listenerInterface.onCalculationStared();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (listenerInterface != null) {
                listenerInterface.onCalculationCompleted(hashMap, battleTypes);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            DebugUtils.log("Processed " + values[0] + " out of " + mData.size());
        }

        @Override
        protected String doInBackground(String... params) {
            DebugUtils.log("Started Calculating King Rankings");

            for (int i = 0; i < mData.size(); i++) {
                HomeResponseObject battle = mData.get(i);

                if (filters != null && filters.size() > 0) {
                    if (!filters.contains(battle.getBattle_type())) {
                        continue;
                    }
                }

                if (battle != null) {
                    battleTypes.add(battle.getBattle_type());
                }

                if (battle != null
                        && !AndroidUtils.isEmpty(battle.getAttacker_king())
                        && !AndroidUtils.isEmpty(battle.getDefender_king())) {
                    KingDetailsObject attacker, defender;

                    if (hashMap.containsKey(battle.getAttacker_king())) {
                        attacker = hashMap.get(battle.getAttacker_king());
                    } else {
                        attacker = new KingDetailsObject(battle.getAttacker_king());
                    }

                    if (hashMap.containsKey(battle.getDefender_king())) {
                        defender = hashMap.get(battle.getDefender_king());
                    } else {
                        defender = new KingDetailsObject(battle.getDefender_king());
                    }

                    float ratingAttacker = (float) Math.pow(10, (attacker.getCurrentRating() / N_FACTOR));
                    float ratingDefender = (float) Math.pow(10, (defender.getCurrentRating() / N_FACTOR));

                    float expectedScoreAttacker = ratingAttacker / (ratingAttacker + ratingDefender);
                    float expectedScoreDefender = ratingDefender / (ratingAttacker + ratingDefender);

                    float scoreAttacker = 0, scoreDefender = 0;
                    if (AndroidUtils.compareString(battle.getAttacker_outcome(), AppConstants.OUTCOME_WIN)) {
                        scoreAttacker = 1;

                        attacker.setNumberOfAttackWins(attacker.getNumberOfAttackWins() + 1);
                        defender.setNumberOfBattlesLost(defender.getNumberOfBattlesLost() + 1);
                    } else if (AndroidUtils.compareString(battle.getAttacker_outcome(), AppConstants.OUTCOME_LOSS)) {
                        scoreDefender = 1;

                        defender.setNumberOfDefenseWins(defender.getNumberOfDefenseWins() + 1);
                        attacker.setNumberOfBattlesLost(attacker.getNumberOfBattlesLost() + 1);
                    } else if (AndroidUtils.compareString(battle.getAttacker_outcome(), AppConstants.OUTCOME_DRAW)) {
                        scoreAttacker = 0.5f;
                        scoreDefender = 0.5f;

                        attacker.setNumberOfDraws(attacker.getNumberOfDraws() + 1);
                        defender.setNumberOfDraws(defender.getNumberOfDraws() + 1);
                    }

                    // r'(1) = r(1) + K * (S(1) – E(1))
                    float finalAttackerRating = attacker.getCurrentRating() + K_FACTOR * (scoreAttacker - expectedScoreAttacker);
                    float finalDefenderRating = defender.getCurrentRating() + K_FACTOR * (scoreDefender - expectedScoreDefender);

                    attacker.setCurrentRating(finalAttackerRating);
                    defender.setCurrentRating(finalDefenderRating);

                    hashMap.put(battle.getAttacker_king(), attacker);
                    hashMap.put(battle.getDefender_king(), defender);

                    publishProgress(i + 1);
                }
            }

            DebugUtils.log("Finished calculating king ratings");

            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                openSearchActivity();
                break;
            case R.id.menu_filter:
                if (allBattleTypes != null) {
                    openFilterFragment();
                } else {
                    UIUtils.makeSimpleAlertDialog(
                            "Error",
                            "Filter data has not been loaded yet. Please check internet connection and click retry button to try again",
                            this
                    );
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFilterFragment() {
        try {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(AppConstants.FRAGMENT_ARGUMENTS.ARGUMENT_ALL_FILTERS, allBattleTypes);
            bundle.putStringArrayList(AppConstants.FRAGMENT_ARGUMENTS.ARGUMENT_SELECTED_FILTERS, filterBattleTypes);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, FilterFragment.newInstance(bundle))
                    .addToBackStack("filter")
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
