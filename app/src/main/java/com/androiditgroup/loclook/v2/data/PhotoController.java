package com.androiditgroup.loclook.v2.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.util.Log;

import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.models.PhotoModel;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DbBitmapUtility;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Serghei Ostrovschi on 7/17/17.
 */

public class PhotoController {

    private DatabaseHandler databaseHandler;
    private SQLiteDatabase  sqLiteDatabase;

    private Map<Integer, PhotoModel>        photoMap            = new TreeMap<>();
    private Map<Integer, ArrayList<Bitmap>> publicationPhotoMap = new TreeMap<>();

    public PhotoController(DatabaseHandler    databaseHandler,
                           SQLiteDatabase     sqLiteDatabase) throws Exception {

        if(databaseHandler == null)
            throw new Exception(ErrorConstants.DATABASE_HANDLER_NULL_ERROR);

        if(sqLiteDatabase == null)
            throw new Exception(ErrorConstants.SQLITE_DATABASE_NULL_ERROR);

        this.databaseHandler    = databaseHandler;
        this.sqLiteDatabase     = sqLiteDatabase;
    }

    private void addPhotoToMap(Cursor cursor) {
        //Log.e("LOG", "-------------------------------------");
        //Log.e("LOG", "PhotoController: addPhotoToMap()");

        boolean noErrors = true;

        PhotoModel photo = new PhotoModel();

        try {
            photo.setPhotoId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.ROW_ID)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PhotoController: addPhotoToMap(): set photo id error: " +exc.getMessage());
        }

        try {
            photo.setPublicationId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.PHOTO_PUBLICATION_ID)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PhotoController: addPhotoToMap(): set photo publication id error: " +exc.getMessage());
        }

        byte[] photoBinaryDataArr = cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.PHOTO_DATA));

        if(photoBinaryDataArr != null) {

            Log.e("LOG", "PhotoController: addPhotoToMap(): photoBinaryDataArr length= " +photoBinaryDataArr.length);

            Bitmap bitmap = DbBitmapUtility.getImage(photoBinaryDataArr);

            try {
                photo.setPhotoImg(bitmap);
            } catch (Exception exc) {
                noErrors = false;
                Log.e("LOG", "PhotoController: addPhotoToMap(): set photo image error: " +exc.getMessage());
            }
        }

        // ------------------------------------------------------------------------------- //

        if(noErrors){
            photoMap.put(photo.getPhotoId(), photo);

            if(publicationPhotoMap.containsKey(photo.getPublicationId())) {
                ArrayList<Bitmap> publicationPhotoList = publicationPhotoMap.get(photo.getPublicationId());
                publicationPhotoList.add(photo.getPhotoImg());
            }
            else {
                ArrayList<Bitmap> publicationPhotoList = new ArrayList<>();
                publicationPhotoList.add(photo.getPhotoImg());

                publicationPhotoMap.put(photo.getPublicationId(), publicationPhotoList);
            }

            Log.e("LOG", "PhotoController: addPhotoToMap(): photo: " +photo.getPhotoId()+ " added");
        }
        else {
            Log.e("LOG", "PhotoController: addPhotoToMap(): photo will not be added, error occured");
        }
    }

    public void populatePhotosMap() {
        //Log.e("LOG", "-------------------------------------");
        //Log.e("LOG", "PhotoController: addPhotoToMap()");

        Cursor cursor = databaseHandler.queryColumns(sqLiteDatabase, DatabaseConstants.PHOTO_TABLE, null);

        if((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();

            do{
                addPhotoToMap(cursor);
            } while(cursor.moveToNext());
        }
        else
            Log.e("LOG", "PublicationController: populateAllPublicationCollections(): publicationsPopulateListener is null");
    }

    public ArrayList<Bitmap> getPublicationPhotoList(int publicationId) throws Exception {
        //Log.e("LOG", "-------------------------------------");
        //Log.e("LOG", "PhotoController: getPublicationPhotoList()");

        if(publicationId <= 0)
            throw new Exception(ErrorConstants.PUBLICATION_ID_ERROR);

        if(publicationPhotoMap.containsKey(publicationId))
            return publicationPhotoMap.get(publicationId);
        else
            return new ArrayList<>();
    }

    public boolean addPublicationPhotos(int                 publicationId,
                                        ArrayList<Bitmap>   photoList) throws Exception {
        Log.e("LOG", "-------------------------------------");
        Log.e("LOG", "PhotoController: addPublicationPhotos()");

        boolean result   = false;
        boolean noErrors = true;

        int addedPhotosSum = 0;

        if(photoList == null)
            throw new Exception(ErrorConstants.PHOTO_LIST_NULL_ERROR);

        if(publicationId <= 0)
            throw new Exception(ErrorConstants.PUBLICATION_ID_ERROR);

        sqLiteDatabase.beginTransaction();

        try {

            String[] columnsArr = { DatabaseConstants.PHOTO_DATA,
                                    DatabaseConstants.PHOTO_PUBLICATION_ID};

            for(int i=0; i<photoList.size(); i++) {

                Bitmap photo = photoList.get(i);

                Log.e("LOG", "PhotoController: addPublicationPhotos(): photo is null: " +(photo == null));

                byte[] photoDataArr = DbBitmapUtility.getBytes(photo);

                Log.e("LOG", "PhotoController: addPublicationPhotos(): photoDataArr length= " +photoDataArr.length);

                String[] dataArr = {String.valueOf(photoDataArr),
                                    String.valueOf(publicationId)};

//                String[] dataArr = {String.valueOf(DbBitmapUtility.getBytes(photoList.get(i))),
//                                    String.valueOf(publicationId)};

                Cursor photoCursor = databaseHandler.insertRow( sqLiteDatabase,
                                                                DatabaseConstants.PHOTO_TABLE,
                                                                columnsArr,
                                                                dataArr);

                if((photoCursor == null) || (photoCursor.getCount() <= 0)) {
                    noErrors = false;
                }
                else
                    addedPhotosSum++;

                photoCursor.close();
            }

            if((noErrors) && (addedPhotosSum == photoList.size())) {

                Cursor photosCursor = databaseHandler.queryColumns( sqLiteDatabase,
                                                                    DatabaseConstants.PHOTO_TABLE,
                                                                    null,
                                                                    DatabaseConstants.PHOTO_PUBLICATION_ID,
                                                                    String.valueOf(publicationId));

                if((photosCursor != null) || (photosCursor.getCount() > 0)) {
                    photosCursor.moveToFirst();

                    do {
                        addPhotoToMap(photosCursor);
                    } while (photosCursor.moveToNext());

                    sqLiteDatabase.setTransactionSuccessful();

                    result = true;
                }

                photosCursor.close();
            }

        } catch(SQLiteException sqliteExc) {
            Log.e("LOG", "PhotoController: addPublicationPhotos(): SQLiteException: " +sqliteExc.getMessage());
        } catch(Exception exc) {
            //Error in between database transaction
            Log.e("LOG", "PhotoController: addPublicationPhotos(): Exception: " +exc.getMessage());
        } finally {
            sqLiteDatabase.endTransaction();
        }

        return result;
    }
}
