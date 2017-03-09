package com.androiditgroup.loclook.v2.ui.feed;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.adapters.GalleryListAdapter;
import com.androiditgroup.loclook.v2.adapters.PublicationsListAdapter;
import com.androiditgroup.loclook.v2.models.Publication;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;
import com.androiditgroup.loclook.v2.ui.publication.PublicationFragment;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;
import com.androiditgroup.loclook.v2.utils.EmptyRecyclerView;
import com.androiditgroup.loclook.v2.utils.ParentFragment;
import com.androiditgroup.loclook.v2.utils.PublicationGenerator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sostrovschi on 1/25/17.
 */

public class FeedFragment   extends ParentFragment
                            implements View.OnClickListener {

    private MainActivity mMainActivity;
    private EmptyRecyclerView mPublicationRV;

    private ArrayList<Publication> mPublicationsList = new ArrayList<>();
    private PublicationsListAdapter mFeedAdapter;
    private DBManager db = DBManager.getInstance();

    private TextView mAddPublicationTV;

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance() {
        Bundle args = new Bundle();
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        mMainActivity = (MainActivity) getActivity();

        // Fetch the empty view from the layout and set it on
        // the new recycler view
        View emptyView = view.findViewById(R.id.Feed_EmptyView);

        mFeedAdapter = new PublicationsListAdapter(mPublicationsList);

        // Replaced RecyclerView with EmptyRecyclerView
        mPublicationRV = (EmptyRecyclerView) view.findViewById(R.id.Feed_PublicationsRV);
        mPublicationRV.setLayoutManager(new LinearLayoutManager(LocLookApp.context));
        mPublicationRV.setEmptyView(emptyView);
        mPublicationRV.setAdapter(mFeedAdapter);

        mAddPublicationTV = (TextView) view.findViewById(R.id.Feed_AddPublicationTV);
        mAddPublicationTV.setOnClickListener(this);

        refreshFeedData();

        return view;
    }

    @Override
    public String getFragmentTitle() {
        return LocLookApp.getInstance().getString(R.string.feed_text);
    }

    private void refreshFeedData() {
        mPublicationsList.clear();

        Cursor pCursor = db.queryColumns(db.getDataBase(), Constants.DataBase.PUBLICATION_TABLE, null);

        if((pCursor != null) && (pCursor.getCount() > 0)) {
            mPublicationsList.addAll(PublicationGenerator.getPublicationsList(pCursor));
        }

        if(mPublicationsList.size() > 0) {
            Collections.reverse(mPublicationsList);
            mFeedAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.Feed_AddPublicationTV:
                mMainActivity.setFragment(PublicationFragment.newInstance(MainActivity.mInflater), false, true);
                break;
        }
    }
}
