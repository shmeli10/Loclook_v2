package com.androiditgroup.loclook.v2.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.utils.ParentFragment;

/**
 * Created by sostrovschi on 1/25/17.
 */

public class FeedFragment extends ParentFragment {

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

        return view;
    }

    @Override
    public String getFragmentTitle() {
        return LocLookApp.getInstance().getString(R.string.feed_text);
    }
}
