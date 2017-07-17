package com.androiditgroup.loclook.v2.data;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.interfaces.PublicationCreateInterface;
import com.androiditgroup.loclook.v2.interfaces.PublicationsPopulateInterface;
import com.androiditgroup.loclook.v2.models.PublicationModel;
import com.androiditgroup.loclook.v2.models.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Serghei Ostrovschi on 7/4/16.
 */

public class PublicationController {

    private DatabaseHandler                 databaseHandler;
    private SQLiteDatabase                  sqLiteDatabase;

    private UserController                  userController;

    private PublicationsPopulateInterface   publicationsPopulateListener;

    private Map<Integer, PublicationModel>  publicationMap          = new TreeMap<>();
    private Map<Integer, ArrayList<Bitmap>> publicationPhotosMap    = new TreeMap<>();

    private List<PublicationModel>          allPublicationsList = new ArrayList<>();


    public PublicationController(DatabaseHandler    databaseHandler,
                                 SQLiteDatabase     sqLiteDatabase,
                                 UserController     userController) throws Exception {
        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp.showLog("PublicationController: constructor.");

        if(databaseHandler == null)
            throw new Exception(ErrorConstants.DATABASE_HANDLER_NULL_ERROR);

        if(sqLiteDatabase == null)
            throw new Exception(ErrorConstants.SQLITE_DATABASE_NULL_ERROR);

        if(userController == null)
            throw new Exception(ErrorConstants.USER_CONTROLLER_NULL_ERROR);

        this.databaseHandler    = databaseHandler;
        this.sqLiteDatabase     = sqLiteDatabase;
        this.userController     = userController;

        //populateAllPublicationCollections();
    }

    public List<PublicationModel> getAllPublicationsList() {

        if(allPublicationsList == null)
            return new ArrayList<>();

        return allPublicationsList;
    }

    public void setPublicationsPopulateListener(PublicationsPopulateInterface publicationsPopulateListener) throws Exception {

        if(publicationsPopulateListener == null)
            throw new Exception(ErrorConstants.PUBLICATION_POPULATE_INTERFACE_NULL_ERROR);

        this.publicationsPopulateListener = publicationsPopulateListener;
    }

    private void addPublicationToCollections(Cursor cursor) {
        Log.e("LOG", "-------------------------------------");
        Log.e("LOG", "PublicationController: addPublicationToCollections()");

        boolean noErrors = true;

        PublicationModel publication = new PublicationModel();

        try {
            publication.setPublicationId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.ROW_ID)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PublicationController: addPublicationToCollections(): set publication id error: " +exc.getMessage());
        }

        try {
            publication.setPublicationAuthorId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_AUTHOR_ID)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PublicationController: addPublicationToCollections(): set publication author id error: " +exc.getMessage());
        }

        try {
            publication.setPublicationBadgeId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_BADGE_ID)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PublicationController: addPublicationToCollections(): set publication badge id error: " +exc.getMessage());
        }

        try {
            publication.setPublicationCreatedAt(cursor.getString(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_CREATED_AT)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PublicationController: PublicationController(): set publication created at error: " +exc.getMessage());
        }

        try {
            publication.setPublicationLatitude(cursor.getString(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_LATITUDE)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PublicationController: PublicationController(): set publication latitude error: " +exc.getMessage());
        }

        try {
            publication.setPublicationLongitude(cursor.getString(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_LONGITUDE)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PublicationController: PublicationController(): set publication longitude error: " +exc.getMessage());
        }

        try {
            publication.setPublicationRegionName(cursor.getString(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_REGION_NAME)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PublicationController: PublicationController(): set publication region name error: " +exc.getMessage());
        }

        try {
            publication.setPublicationStreetName(cursor.getString(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_STREET_NAME)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PublicationController: PublicationController(): set publication street name error: " +exc.getMessage());
        }

        try {
            publication.setPublicationText(cursor.getString(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_TEXT)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PublicationController: PublicationController(): set publication text error: " +exc.getMessage());
        }

        try {
            publication.setPublicationHasQuiz(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_HAS_QUIZ)) > 0);
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PublicationController: addPublicationToCollections(): set publication has quiz error: " +exc.getMessage());
        }

        try {
            publication.setPublicationHasImages(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_HAS_IMAGES)) > 0);
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PublicationController: addPublicationToCollections(): set publication has images error: " +exc.getMessage());
        }

        try {
            publication.setPublicationIsAnonymous(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_IS_ANONYMOUS)) > 0);
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "PublicationController: addPublicationToCollections(): set publication is anonymous error: " +exc.getMessage());
        }

        // ------------------------------------------------------------------------------- //

        if(noErrors){
            publicationMap.put(publication.getPublicationId(), publication);
            allPublicationsList.add(publication);
            Log.e("LOG", "PublicationController: addPublicationToCollections(): publication: " +publication.getPublicationId()+ " added");
        }
        else {
            Log.e("LOG", "PublicationController: addPublicationToCollections(): publication will not be added, error occured");
        }
    }

    private void addPublicationPhotos(int publicationId) {
        //Log.e("LOG", "-------------------------------------");
        //Log.e("LOG", "PublicationController: addPublicationPhotos()");

        if(publicationId > 0) {

        }
        else
            Log.e("LOG", "PublicationController: addPublicationPhotos(): publicationId is incorrect");

    }

    public void populateAllPublicationCollections() {
        //Log.e("LOG", "-------------------------------------");
        //Log.e("LOG", "PublicationController: populateAllPublicationCollections()");

        Cursor cursor = databaseHandler.queryColumns(sqLiteDatabase, DatabaseConstants.PUBLICATION_TABLE, null);

        // ------------------------------------------------------------------------------------ //

        //Log.e("LOG", "PublicationController: populateAllPublicationCollections(): cursor is null: " +(cursor == null));

//        if(cursor != null)
//            Log.e("LOG", "PublicationController: populateAllPublicationCollections(): cursor rows: " +cursor.getCount());

        // ------------------------------------------------------------------------------------ //

        if((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();

            // Log.e("LOG", "UserController: populateUserMap(): cursor rows: " +cursor.getCount());

            do{
                addPublicationToCollections(cursor);
            } while(cursor.moveToNext());

            if((publicationMap != null) && (publicationMap.size() >= 0)) {
                if(publicationMap.size() == cursor.getCount())
                    Log.e("LOG", "PublicationController: populateAllPublicationCollections(): all publications inserted successfully");
                else
                    Log.e("LOG", "PublicationController: populateAllPublicationCollections(): was inserted " + publicationMap.size() + " publications");

                if(publicationsPopulateListener != null)
                    publicationsPopulateListener.onPublicationsPopulateSuccess();
                else
                    Log.e("LOG", "PublicationController: populateAllPublicationCollections(): publicationsPopulateListener is null");
            }
            else {
                //Log.e("LOG", "PublicationController: populateAllPublicationCollections(): publicationMap is null");
                publicationsPopulateListener.onPublicationsPopulateError(ErrorConstants.PUBLICATION_MAP_NULL_ERROR);
            }
        }
        else {
            //Log.e("LOG", "PublicationController: populateAllPublicationCollections(): cursor size = 0");

            if(publicationsPopulateListener != null)
                publicationsPopulateListener.onPublicationsPopulateSuccess();
            else
                Log.e("LOG", "PublicationController: populateAllPublicationCollections(): publicationsPopulateListener is null");
        }
    }

    public boolean createPublication(String              publicationText,
                                     int                 badgeId,
                                     ArrayList<String>   quizAnswerList,
                                     ArrayList<Bitmap>   photoList,
                                     boolean             isPublicationAnonymous) throws Exception {

//        if(publicationCreateListener == null)
//            throw new Exception(ErrorConstants.PUBLICATION_CREATE_INTERFACE_NULL_ERROR);

        Cursor cursor   = null;

        boolean result  = false;

        sqLiteDatabase.beginTransaction();

        try {

            UserModel currentUser = userController.getCurrentUser();

            if(currentUser != null) {

                String publicationLatitude  = "";
                String publicationLongitude = "";

                String userMapLatitude  = currentUser.getUserMapLatitude();
                String userMapLongitude = currentUser.getUserMapLongitude();

                if((!TextUtils.isEmpty(userMapLatitude)) && (!TextUtils.isEmpty(userMapLongitude))) {
                    publicationLatitude  = userMapLatitude;
                    publicationLongitude = userMapLongitude;
                }

                String[] columnsArr = { DatabaseConstants.PUBLICATION_TEXT,
                                        DatabaseConstants.PUBLICATION_AUTHOR_ID,
                                        DatabaseConstants.PUBLICATION_BADGE_ID,
                                        DatabaseConstants.PUBLICATION_CREATED_AT,
                                        DatabaseConstants.PUBLICATION_LATITUDE,
                                        DatabaseConstants.PUBLICATION_LONGITUDE,
                                        DatabaseConstants.PUBLICATION_REGION_NAME,
                                        DatabaseConstants.PUBLICATION_STREET_NAME,
                                        DatabaseConstants.PUBLICATION_HAS_QUIZ,
                                        DatabaseConstants.PUBLICATION_HAS_IMAGES,
                                        DatabaseConstants.PUBLICATION_IS_ANONYMOUS};

                String[] dataArr  = {   publicationText,
                                        String.valueOf(currentUser.getUserId()),
                                        String.valueOf(badgeId),
                                        String.valueOf(System.currentTimeMillis()),
                                        publicationLatitude,
                                        publicationLongitude,
                                        currentUser.getUserRegionName(),
                                        currentUser.getUserStreetName(),
                                        (quizAnswerList.size() == 0)  ? "0" : "1",
                                        (photoList.size() == 0)  ? "0" : "1",
                                        (!isPublicationAnonymous) ? "0" : "1"};

                cursor = databaseHandler.insertRow(sqLiteDatabase, DatabaseConstants.PUBLICATION_TABLE, columnsArr, dataArr);



            }
            else
                throw new Exception(ErrorConstants.USER_NULL_ERROR);

            sqLiteDatabase.setTransactionSuccessful();
            //publicationCreateListener.onPublicationCreateSuccess();
        } catch(SQLiteException sqliteExc) {
            //publicationCreateListener.onPublicationCreateError(sqliteExc.toString());
        } catch(Exception exc) {
            //Error in between database transaction
            //publicationCreateListener.onPublicationCreateError(exc.getMessage());
        } finally {
            sqLiteDatabase.endTransaction();
        }

        if((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();

            addPublicationToCollections(cursor);

//            int userId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.ROW_ID));

//            addUserToMap(cursor);
//            setCurrentUser(userMap.get(userId));

            result = true;
        }

        return result;
    }

    /*public static PublicationController getInstance()  {

        if(publicationController == null) {
            publicationController = new PublicationController();
        }

        return publicationController;
    }*/

    /*public void populateAllPublicationsList() {
        Log.e("LOG", "-------------------------------------");
        Log.e("LOG", "PublicationController: populateAllPublicationsList()");

        allPublicationsList.clear();

        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(),
                                                             Constants.DataBase.PUBLICATION_TABLE,
                                                             null);

        if((cursor != null) && (cursor.getCount() > 0)) {
            Log.e("LOG", "PublicationController: populateAllPublicationsList(): cursor.getCount()= " + cursor.getCount());
            cursor.moveToFirst();

            try {

                do {

                    int publicationId = cursor.getInt(cursor.getColumnIndex("_ID"));

                    Publication.Builder publicationBuilder = new Publication.Builder();
                    publicationBuilder.publicationId(publicationId);
                    publicationBuilder.publicationAuthorId(cursor.getInt(cursor.getColumnIndex("AUTHOR_ID")));
                    publicationBuilder.publicationBadgeId(cursor.getInt(cursor.getColumnIndex("BADGE_ID")));
                    publicationBuilder.publicationCommentsSum(CommentController.getCommentsControllerInstance().getPublicationCommentsSum(publicationId));
                    publicationBuilder.publicationLikesSum(LikeController.getInstance().getPublicationLikesSum(publicationId));
                    publicationBuilder.publicationText(cursor.getString(cursor.getColumnIndex("TEXT")));
                    publicationBuilder.publicationLatitude(cursor.getString(cursor.getColumnIndex("LATITUDE")));
                    publicationBuilder.publicationLongitude(cursor.getString(cursor.getColumnIndex("LONGITUDE")));
                    publicationBuilder.publicationRegionName(cursor.getString(cursor.getColumnIndex("REGION_NAME")));
                    publicationBuilder.publicationStreetName(cursor.getString(cursor.getColumnIndex("STREET_NAME")));
                    publicationBuilder.publicationCreatedAt(cursor.getString(cursor.getColumnIndex("CREATED_AT")));
                    publicationBuilder.publicationHasQuiz(cursor.getInt(cursor.getColumnIndex("HAS_QUIZ")) > 0);
                    publicationBuilder.publicationHasImages(cursor.getInt(cursor.getColumnIndex("HAS_IMAGES")) > 0);
                    publicationBuilder.publicationIsAnonymous(cursor.getInt(cursor.getColumnIndex("IS_ANONYMOUS")) > 0);

                } while (cursor.moveToNext());

                Log.e("LOG", "PublicationController: populateAllPublicationsList(): allPublicationsList size= " +allPublicationsList.size());

            } catch(Exception exc) {
                Log.e("LOG", "PublicationController: populateAllPublicationsList(): error: " +exc.toString());
            } finally {
                cursor.close();
            }

        }
    }*/
}
