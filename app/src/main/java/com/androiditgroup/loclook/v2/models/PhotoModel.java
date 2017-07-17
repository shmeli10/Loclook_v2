package com.androiditgroup.loclook.v2.models;

import android.graphics.Bitmap;

import com.androiditgroup.loclook.v2.constants.ErrorConstants;

/**
 * Created by Serghei Ostrovschi on 7/17/16.
 */

public class PhotoModel {

    private int photoId;
    private int publicationId;

    private Bitmap photoImg;

    // ------------------------------------- GETTERS ----------------------------------------- //


    public int getPhotoId() {
        return photoId;
    }

    public int getPublicationId() {
        return publicationId;
    }

    public Bitmap getPhotoImg() {
        return photoImg;
    }

    // ------------------------------------- SETTERS ----------------------------------------- //

    public void setPhotoId(int photoId) throws Exception {
        if(photoId > 0)
            this.photoId = photoId;
        else
            throw new Exception(ErrorConstants.PHOTO_ID_ERROR);
    }

    public void setPublicationId(int publicationId) throws Exception {
        if(publicationId > 0)
            this.publicationId = publicationId;
        else
            throw new Exception(ErrorConstants.PUBLICATION_ID_ERROR);
    }

    public void setPhotoImg(Bitmap photoImg) throws Exception {

        if(photoImg != null)
            this.photoImg = photoImg;
        else
            throw new Exception(ErrorConstants.PHOTO_BITMAP_NULL_ERROR);
    }
}
