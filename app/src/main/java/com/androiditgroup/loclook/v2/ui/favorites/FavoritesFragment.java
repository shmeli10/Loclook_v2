package com.androiditgroup.loclook.v2.ui.favorites;

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

public class FavoritesFragment extends ParentFragment {

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        Bundle args = new Bundle();
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        return view;
    }

    @Override
    public String getFragmentTitle() {
        return LocLookApp.getInstance().getString(R.string.favorites_text);
    }
}
