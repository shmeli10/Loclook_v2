package com.androiditgroup.loclook.v2.ui.auth;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiditgroup.loclook.v2.R;

/**
 * Created by sostrovschi on 10.01.2017.
 */

public class PhoneNumberFragment extends Fragment {

    public PhoneNumberFragment() {
        // Required empty public constructor
    }

    public static PhoneNumberFragment newInstance() {
        Bundle args = new Bundle();
        PhoneNumberFragment fragment = new PhoneNumberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phone_number, container, false);

        return view;
    }
}
