package com.androiditgroup.loclook.v2.ui.gallery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.adapters.GalleryPagerAdapter;
import com.androiditgroup.loclook.v2.utils.ParentFragment;

import java.util.ArrayList;

/**
 * Created by sostrovschi on 3/9/17.
 */

public class PhotoGalleryFragment_NEW extends ParentFragment {

    private int mPosition;
    private ArrayList<Bitmap> mPhotosList;

    public PhotoGalleryFragment_NEW() {
        // Required empty public constructor
    }

    public static PhotoGalleryFragment_NEW newInstance(int position, ArrayList<Bitmap> photosList) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putParcelableArrayList("photosList", photosList);
        PhotoGalleryFragment_NEW fragment = new PhotoGalleryFragment_NEW();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.PhotoGallery_ViewPager);
        viewPager.setOffscreenPageLimit(3);

        mPosition = getArguments().getInt("position");
        mPhotosList = getArguments().getParcelableArrayList("photosList");

        GalleryPagerAdapter adapter = new GalleryPagerAdapter(mPhotosList);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mPosition);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // прячем меню
        for(int i = 0; i < menu.size(); i++)
            menu.getItem(i).setVisible(false);

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public String getFragmentTitle() {
        return LocLookApp.getInstance().getString(R.string.photo_gallery_text);
    }
}
