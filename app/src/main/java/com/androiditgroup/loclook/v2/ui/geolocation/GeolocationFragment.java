package com.androiditgroup.loclook.v2.ui.geolocation;

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

public class GeolocationFragment extends ParentFragment {

    public GeolocationFragment() {
        // Required empty public constructor
    }

    public static GeolocationFragment newInstance() {
        Bundle args = new Bundle();
        GeolocationFragment fragment = new GeolocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geolocation, container, false);

        return view;
    }

    @Override
    public String getFragmentTitle() {
        return LocLookApp.getInstance().getString(R.string.geolocation_text);
    }
}
