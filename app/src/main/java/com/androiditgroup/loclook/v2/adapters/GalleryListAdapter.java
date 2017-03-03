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

import java.util.ArrayList;


/**
 * Created by sostrovschi on 3/1/17.
 */

public class GalleryListAdapter  extends RecyclerView.Adapter<GalleryListAdapter.ViewHolder> {

    private ArrayList<Bitmap> mGalleryPhotos;

    public GalleryListAdapter(ArrayList<Bitmap> photosList) {
        mGalleryPhotos = photosList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mGalleryPhotos.size() > position) {
            holder.mImageView.setImageBitmap(mGalleryPhotos.get(position));
        }
    }

    @Override
    public int getItemCount() {
        // return mGalleryPhotos.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView mImageView;
        // Bitmap mGalleryPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.Gallery_ImageView);
        }

        @Override
        public void onClick(View v) {
//            if (mOnGalleryImageClickListener != null) {
//                mOnGalleryImageClickListener.onGalleryImageClick(mGalleryImage, getAdapterPosition(), mGalleryImages);
//            }

            Log.e("ABC", "GalleryListAdapter: onClick(): imageView(" +getPosition()+ ")");
        }
    }
}
