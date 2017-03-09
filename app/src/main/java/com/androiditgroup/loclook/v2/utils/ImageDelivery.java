package com.androiditgroup.loclook.v2.utils;

import java.util.ArrayList;
import android.graphics.Bitmap;
import com.androiditgroup.loclook.v2.LocLookApp;

/**
 * Created by sostrovschi on 3/9/17.
 */

public class ImageDelivery {

    public static Bitmap getPhotoById(String id) {
        // Log.e("ABC", "ImageDelivery: getPhotoById(): id is null:" +(id == null));

        if((id != null) && (LocLookApp.imagesMap.containsKey(id)))
            return LocLookApp.imagesMap.get(id);

        return null;
    }

    public static ArrayList<Bitmap> getPhotosListById(ArrayList<String> photosIdsList) {

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
    }
}
