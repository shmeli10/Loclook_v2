package com.androiditgroup.loclook.v2.ui.feed;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.LocLookApp_NEW;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.adapters.PublicationsListAdapter_NEW;
import com.androiditgroup.loclook.v2.data.PublicationController;
import com.androiditgroup.loclook.v2.models.PublicationModel;
import com.androiditgroup.loclook.v2.ui.general.MainActivity_NEW;
import com.androiditgroup.loclook.v2.ui.publication.PublicationFragment_NEW;
import com.androiditgroup.loclook.v2.utils.EmptyRecyclerView;
import com.androiditgroup.loclook.v2.utils.ParentFragment;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sostrovschi on 1/25/17.
 */

public class FeedFragment_NEW extends ParentFragment {

    private MainActivity_NEW            mMainActivity;
    private EmptyRecyclerView           mPublicationRV;

    private LocLookApp_NEW              locLookApp_NEW;
    private PublicationController       publicationController;

    private ArrayList<PublicationModel> mPublicationsList = new ArrayList<>();

    private PublicationsListAdapter_NEW mFeedAdapter;

    //private DBManager db = DBManager.getInstance();

    private TextView mAddPublicationTV;

    public FeedFragment_NEW() {
        // Required empty public constructor
    }

    public static FeedFragment_NEW newInstance() {
        Bundle args = new Bundle();
        FeedFragment_NEW fragment = new FeedFragment_NEW();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        mMainActivity   = (MainActivity_NEW) getActivity();
        locLookApp_NEW  = ((LocLookApp_NEW) mMainActivity.getApplication());

        publicationController = locLookApp_NEW.getAppManager().getPublicationController();

        // Fetch the empty view from the layout and set it on
        // the new recycler view
        View emptyView = view.findViewById(R.id.Feed_EmptyView);

        // mFeedAdapter = new PublicationsListAdapter(mPublicationsList);
        mFeedAdapter = new PublicationsListAdapter_NEW(mMainActivity, mPublicationsList);

        // Replaced RecyclerView with EmptyRecyclerView
        mPublicationRV = (EmptyRecyclerView) view.findViewById(R.id.Feed_PublicationsRV);
        mPublicationRV.setLayoutManager(new LinearLayoutManager(LocLookApp.context));
        mPublicationRV.setEmptyView(emptyView);
        mPublicationRV.setAdapter(mFeedAdapter);

        mAddPublicationTV = (TextView) view.findViewById(R.id.Feed_AddPublicationTV);
        mAddPublicationTV.setOnClickListener(addPublicationListener);
        //mAddPublicationTV.setOnClickListener(this);

        refreshFeedData();

        return view;
    }

    @Override
    public String getFragmentTitle() {
        return LocLookApp_NEW.getInstance().getString(R.string.feed_text);
    }

    private void refreshFeedData() {
        mPublicationsList.clear();

        mPublicationsList.addAll(publicationController.getAllPublicationsList());

        if(mPublicationsList.size() > 0) {
            Collections.reverse(mPublicationsList);
            mFeedAdapter.notifyDataSetChanged();
        }
    }

    View.OnClickListener addPublicationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainActivity.setFragment(PublicationFragment_NEW.newInstance(MainActivity_NEW.mInflater),
                                      false,
                                      true);
        }
    };

    /*@Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.Feed_AddPublicationTV:
                mMainActivity.setFragment(PublicationFragment.newInstance(MainActivity.mInflater), false, true);
                break;
        }
    }*/
}
