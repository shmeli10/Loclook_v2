package com.androiditgroup.loclook.v2.ui.gallery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.adapters.GalleryPagerAdapter;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;
import com.androiditgroup.loclook.v2.ui.publication.PublicationFragment;
import com.androiditgroup.loclook.v2.utils.ImageDelivery;
import com.androiditgroup.loclook.v2.utils.ParentFragment;

import java.util.ArrayList;

/**
 * Created by sostrovschi on 3/9/17.
 */

public class PhotoGalleryFragment extends ParentFragment {

    private int mPosition;
    private ArrayList<Bitmap> mPhotosList;

    public PhotoGalleryFragment() {
        // Required empty public constructor
    }

    public static PhotoGalleryFragment newInstance(int position, ArrayList<Bitmap> photosList) {
        Bundle args = new Bundle();
        args.putInt("position", position);
//        args.putStringArrayList("photosIdsList", photosIdsList);
        args.putParcelableArrayList("photosList", photosList);
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.PhotoGallery_ViewPager);
        viewPager.setOffscreenPageLimit(3);

        mPosition = getArguments().getInt("position");
        // mPhotosList = ImageDelivery.getPhotosListById(savedInstanceState.getStringArrayList("photosIdsList"));
        mPhotosList = getArguments().getParcelableArrayList("photosList");

        // Log.e("ABC", "PhotoGalleryFragment: onCreateView(): mPosition= " +mPosition+ ", mPhotosList.size= " +mPhotosList.size());

        GalleryPagerAdapter adapter = new GalleryPagerAdapter(mPhotosList);

//        for (int i=0; i<mPhotosList.size(); i++) {
//            adapter.addFragment(GalleryImageFragment.newInstance(mPhotosList.get(i)), (i + 1) + " from " + mPhotosList.size());
//        }

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mPosition);

        return view;
    }

    @Override
    public String getFragmentTitle() {
        return LocLookApp.getInstance().getString(R.string.photo_gallery_text);
    }
}
