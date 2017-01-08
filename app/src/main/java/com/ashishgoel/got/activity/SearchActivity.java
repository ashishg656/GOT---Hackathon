package com.ashishgoel.got.activity;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashishgoel.got.R;
import com.ashishgoel.got.adapter.HomeListAdapter;
import com.ashishgoel.got.adapter.SearchSuggestionListAdapter;
import com.ashishgoel.got.objects.kingDetails.KingDetailsObject;
import com.ashishgoel.got.sqlite.kings.KingsSqliteHelper;
import com.ashishgoel.got.sqlite.search.SearchSqliteHelper;
import com.ashishgoel.got.sqlite.search.SearchSqliteObject;
import com.ashishgoel.got.utils.AndroidUtils;
import com.wefika.flowlayout.FlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ashish on 08/01/17.
 */

public class SearchActivity extends BaseActivity implements SearchView.OnCloseListener, View.OnClickListener, SearchView.OnQueryTextListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.search_suggestion_recycler)
    RecyclerView searchSuggestionRecyclerView;

    @BindView(R.id.search_suggestion_container)
    LinearLayout searchSuggestionContainer;
    @BindView(R.id.recent_search_container)
    LinearLayout recentSearchContainer;
    @BindView(R.id.recent_search_flow)
    FlowLayout recentSearchFlowLayout;
    @BindView(R.id.clearsearchresent)
    TextView clearRecentSearches;

    LinearLayoutManager searchSuggestionLayoutManager;

    KingsSqliteHelper kingsSqliteHelper;
    SearchSqliteHelper searchSqliteHelper;

    HomeListAdapter adapter;
    @BindView(R.id.search_results_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.search_results_recycler_container)
    FrameLayout recyclerViewContainer;
    LinearLayoutManager layoutManager;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_layout);
        ButterKnife.bind(this);

        setEmptyScreenVariables();

        searchSuggestionLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        searchSuggestionRecyclerView.setLayoutManager(searchSuggestionLayoutManager);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clearRecentSearches.setOnClickListener(this);

        hideSearchResultsLayout();

        showSuggestionAndPastSearches();
    }

    private void hideSearchResultsLayout() {
        recyclerViewContainer.setVisibility(View.GONE);
        recyclerView.setAdapter(null);
        adapter = null;
        hideEmptyScreenLayout();
    }

    void showSearchResults(List<KingDetailsObject> data) {
        if (data != null && data.size() > 0) {
            hideEmptyScreenLayout();
            adapter = new HomeListAdapter(data, this, false);
            recyclerView.setAdapter(adapter);
        } else {
            showEmptyScreenLayout();
        }
        recyclerViewContainer.setVisibility(View.VISIBLE);
    }

    private void showSuggestionAndPastSearches() {
        kingsSqliteHelper = new KingsSqliteHelper(this);
        List<KingDetailsObject> data = kingsSqliteHelper.getAllKings();
        if (data != null && data.size() > 0) {
            searchSuggestionContainer.setVisibility(View.VISIBLE);
            SearchSuggestionListAdapter adapter = new SearchSuggestionListAdapter(this, data);
            searchSuggestionRecyclerView.setAdapter(adapter);
        } else {
            searchSuggestionContainer.setVisibility(View.GONE);
        }

        setRecentSearches();
    }

    private void setRecentSearches() {
        searchSqliteHelper = new SearchSqliteHelper(this);
        List<SearchSqliteObject> recentSearches = searchSqliteHelper.getAllSearches();

        recentSearchFlowLayout.removeAllViews();
        if (recentSearches != null && recentSearches.size() > 0) {
            recentSearchContainer.setVisibility(View.VISIBLE);

            for (SearchSqliteObject obj : recentSearches) {
                View view = LayoutInflater.from(this).inflate(R.layout.search_tag_item_layout, recentSearchFlowLayout, false);
                TextView tag = (TextView) view.findViewById(R.id.text_tag_recent);

                tag.setText(obj.getSearchText());
                tag.setTag(obj.getSearchText());
                tag.setOnClickListener(this);

                recentSearchFlowLayout.addView(view);
            }
        } else {
            recentSearchContainer.setVisibility(View.GONE);
        }
    }

    private void loadData(String query) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnCloseListener(this);
        searchView.setOnSearchClickListener(this);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        MenuItemCompat.expandActionView(searchMenuItem);

        return true;
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_tag_recent:
                String text = (String) view.getTag();
                if (!AndroidUtils.isEmpty(text) && searchView != null) {
                    searchView.setQuery(text, true);
                }
                break;
            case R.id.clearsearchresent:
                searchSqliteHelper.deleteAllItems();
                setRecentSearches();
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (AndroidUtils.isEmpty(newText)) {
            hideSearchResultsLayout();
        } else {
            searchSqliteHelper.addItem(newText);

            List<KingDetailsObject> mData = kingsSqliteHelper.searchKings(newText);
            if (mData != null && mData.size() > 0) {
                showSearchResults(mData);
            } else {
                showSearchResults(mData);
            }
        }
        return false;
    }
}
