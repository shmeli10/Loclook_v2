package com.androiditgroup.loclook.v2.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.ui.gallery.PhotoGalleryFragment;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;
import com.androiditgroup.loclook.v2.ui.publication.PublicationFragment;

import java.util.ArrayList;


/**
 * Created by sostrovschi on 3/1/17.
 */

public class GalleryListAdapter  extends RecyclerView.Adapter<GalleryListAdapter.ViewHolder> {

    private MainActivity mMainActivity;
//    private ArrayList<Bitmap> mGalleryTumbnails = new ArrayList<>();
    private ArrayList<Bitmap> mGalleryPhotos = new ArrayList<>();
//    private OnGalleryItemInteractionListener mGalleryItemInteractionListener;

    // public GalleryListAdapter(ArrayList<Bitmap> photosList) {
    public GalleryListAdapter(MainActivity mainActivity, ArrayList<Bitmap> photosList) {
//    public GalleryListAdapter(MainActivity mainActivity, ArrayList<Bitmap> tumbnailsList, ArrayList<Bitmap> photosList) {
        mMainActivity       = mainActivity;
//        mGalleryTumbnails   = tumbnailsList;
        mGalleryPhotos      = photosList;

//        if(tumbnailsList != null)
//            mGalleryTumbnails.addAll(tumbnailsList);
//        else
//            Log.e("ABC", "GalleryListAdapter: onBindViewHolder(): tumbnailsList is null");
//
//        if(photosList != null)
//            mGalleryPhotos.addAll(photosList);
//        else
//            Log.e("ABC", "GalleryListAdapter: onBindViewHolder(): photosList is null");

//        if(mainActivity instanceof OnGalleryItemInteractionListener)
//            mGalleryItemInteractionListener = (OnGalleryItemInteractionListener) mainActivity;
//        else
//            Log.e("ABC", "GalleryListAdapter: GalleryListAdapter(): mainActivity does not implement OnGalleryItemInteractionListener interface");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(mGalleryPhotos.size() > position) {
            holder.mImageView.setImageBitmap(mGalleryPhotos.get(position));
        }

//        if(mGalleryTumbnails.size() > position) {
//            holder.mImageView.setImageBitmap(mGalleryTumbnails.get(position));
//        }

        if((position+1) <= mGalleryPhotos.size())
//        if((position+1) <= mGalleryTumbnails.size())
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ABC", "GalleryListAdapter: onBindViewHolder(): imageView(" +position+ ") clicked");
//                    Log.e("ABC", "GalleryListAdapter: onBindViewHolder(): imageView(" +position+ ") clicked, mGalleryPhotos size= " +mGalleryPhotos.size());

//                    if(mGalleryItemInteractionListener != null)
//                        mGalleryItemInteractionListener.onImageClick(mGalleryPhotos.get(position), position, mGalleryPhotos);

                    MainActivity.selectedFragment = MainActivity.SelectedFragment.photo_gallery;
                    mMainActivity.setFragment(PhotoGalleryFragment.newInstance(position, mGalleryPhotos), false, true);
                }
            });
    }

    @Override
    public int getItemCount() {
        int visiblePhotosSum = LocLookApp.getInstance().getResources().getInteger(R.integer.visible_photos_sum);

        if(mGalleryPhotos.size() < visiblePhotosSum)
            return visiblePhotosSum;
        else
            return mGalleryPhotos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.Gallery_ImageView);
        }
    }
}
