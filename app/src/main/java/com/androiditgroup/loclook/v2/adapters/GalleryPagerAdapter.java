package com.androiditgroup.loclook.v2.adapters;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by sostrovschi on 3/9/17.
 */

public class GalleryPagerAdapter extends PagerAdapter {

    ArrayList<Bitmap> mPhotosList;

    public GalleryPagerAdapter(ArrayList<Bitmap> photosList) {
        mPhotosList = photosList;
    }

    public int getCount() {
        return mPhotosList.size();
    }

    public Object instantiateItem(View collection, int position) {
        View view = MainActivity.mInflater.inflate(R.layout.photo_gallery_item, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.PhotoGalleryItem_ImageView);
        imageView.setImageBitmap(mPhotosList.get(position));

        LinearLayout currentPositionBlock = (LinearLayout) view.findViewById(R.id.PhotoGalleryItem_CurrentPositionBlock);

        for(int i=0; i<mPhotosList.size(); i++) {
            ImageView positionView = new ImageView(LocLookApp.context);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

            lp.setMargins(LocLookApp.getPixelsFromDp(3), 0, LocLookApp.getPixelsFromDp(3), 0);
            positionView.setLayoutParams(lp);
            positionView.setImageResource(R.drawable.circle);

            if(i == position)
                positionView.setImageResource(R.drawable.circle_active);
            else
                positionView.setImageResource(R.drawable.circle);

            currentPositionBlock.addView(positionView);
        }

        ((ViewPager) collection).addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}