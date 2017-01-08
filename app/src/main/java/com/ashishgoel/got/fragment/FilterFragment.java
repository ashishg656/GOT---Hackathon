package com.ashishgoel.got.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashishgoel.got.R;
import com.ashishgoel.got.adapter.FilterListAdapter;
import com.ashishgoel.got.extras.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ashish on 08/01/17.
 */

public class FilterFragment extends BaseFragment implements View.OnClickListener {

    ArrayList<String> filterTypes;
    ArrayList<String> selectedFilters;

    @BindView(R.id.filter_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.exit_filter)
    LinearLayout crossButton;
    @BindView(R.id.clear_filters)
    TextView clearFilters;
    @BindView(R.id.apply_filters)
    FrameLayout applyFilters;

    LinearLayoutManager layoutManager;
    FilterListAdapter adapter;

    FilterApplyInterface filterApplyInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            filterApplyInterface = (FilterApplyInterface) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FilterFragment newInstance(Bundle args) {
        FilterFragment fragment = new FilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.filter_fragment_layout, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        if (getArguments().containsKey(AppConstants.FRAGMENT_ARGUMENTS.ARGUMENT_ALL_FILTERS)) {
            filterTypes = getArguments().getStringArrayList(AppConstants.FRAGMENT_ARGUMENTS.ARGUMENT_ALL_FILTERS);
        }

        if (getArguments().containsKey(AppConstants.FRAGMENT_ARGUMENTS.ARGUMENT_SELECTED_FILTERS)) {
            selectedFilters = getArguments().getStringArrayList(AppConstants.FRAGMENT_ARGUMENTS.ARGUMENT_SELECTED_FILTERS);
        }

        crossButton.setOnClickListener(this);
        clearFilters.setOnClickListener(this);
        applyFilters.setOnClickListener(this);

        setData();
    }

    private void setData() {
        if (filterTypes != null) {
            adapter = new FilterListAdapter(filterTypes, getActivity(), selectedFilters);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit_filter:
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                break;
            case R.id.clear_filters:
                if (adapter != null) {
                    adapter.clearFilters();
                }
                break;
            case R.id.apply_filters:
                if (adapter != null && filterApplyInterface != null) {
                    ArrayList<String> selected = adapter.getSelectedItems();
                    filterApplyInterface.onFilterApplied(selected);
                }
                getActivity().onBackPressed();
                break;
        }
    }

    public interface FilterApplyInterface {

        void onFilterApplied(ArrayList<String> data);
    }
}
