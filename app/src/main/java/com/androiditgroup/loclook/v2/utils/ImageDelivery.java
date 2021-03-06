package com.androiditgroup.loclook.v2.utils;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import com.androiditgroup.loclook.v2.LocLookApp;

/**
 * Created by sostrovschi on 3/9/17.
 */

public class ImageDelivery {

/*    public static Bitmap getPhotoById(String id) {
//        LocLookApp.showLog("ImageDelivery: getPhotoById(): id is null:" +(id == null));

        if((id != null) && (LocLookApp.imagesMap.containsKey(id)))
            return LocLookApp.imagesMap.get(id);

        return null;
    }*/

//    public static Bitmap getTumbnailById(String id) {
//        if((id != null) && (LocLookApp.tumbnailsMap.containsKey(id)))
//            return LocLookApp.tumbnailsMap.get(id);
//
//        return null;
//    }

/*    public static ArrayList<Bitmap> getPhotosListById(ArrayList<String> photosIdsList) {

        ArrayList<Bitmap> result = new ArrayList<>();

        if(photosIdsList != null){
            for(String photoId: photosIdsList) {
                Bitmap photo = getPhotoById(photoId);

                if(photo != null) {
                    result.add(photo);
                }
            }
        }

        return result;
    }*/

//    public static ArrayList<Bitmap> getTumbnailsListById(ArrayList<String> photosIdsList) {
//
//        ArrayList<Bitmap> result = new ArrayList<>();
//
//        if(photosIdsList != null){
//            for(String photoId: photosIdsList) {
//                Bitmap photo = getTumbnailById(photoId);
//
//                if(photo != null) {
//                    result.add(photo);
//                }
//            }
//        }
//
//        return result;
//    }

    public static Bitmap getResizedBitmap(Bitmap src, int width, int height) {
        // int width = LocLookApp.getPixelsFromDp(Constants.TUMBNAIL_WIDTH);
        // int height = LocLookApp.getPixelsFromDp(Constants.TUMBNAIL_HEIGHT);
        return ThumbnailUtils.extractThumbnail(src, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
    }
}
