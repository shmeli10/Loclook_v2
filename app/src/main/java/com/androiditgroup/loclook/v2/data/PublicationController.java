package com.androiditgroup.loclook.v2.data;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.interfaces.PublicationsPopulateInterface;
import com.androiditgroup.loclook.v2.models.Publication;
import com.androiditgroup.loclook.v2.models.PublicationModel;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Serghei Ostrovschi on 7/4/17.
 */

public class PublicationController {

    private DatabaseHandler databaseHandler;
    private SQLiteDatabase  sqLiteDatabase;

    private PublicationsPopulateInterface   publicationsPopulateListener;

    private Map<Integer, PublicationModel> publicationMap = new HashMap<>();

    public PublicationController(DatabaseHandler    databaseHandler,
                                 SQLiteDatabase     sqLiteDatabase) throws Exception {
        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp.showLog("PublicationController: constructor.");

        if(databaseHandler == null)
            throw new Exception(ErrorConstants.DATABASE_HANDLER_NULL_ERROR);

        if(sqLiteDatabase == null)
            throw new Exception(ErrorConstants.SQLITE_DATABASE_NULL_ERROR);

        this.databaseHandler    = databaseHandler;
        this.sqLiteDatabase     = sqLiteDatabase;

        //populatePublicationMap();
    }

    public void setPublicationsPopulateListener(PublicationsPopulateInterface publicationsPopulateListener) throws Exception {

        if(publicationsPopulateListener == null)
            throw new Exception(ErrorConstants.PUBLICATION_POPULATE_INTERFACE_NULL_ERROR);

        this.publicationsPopulateListener = publicationsPopulateListener;
    }

    private void addPublicationToMap(Cursor cursor) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("PublicationController: addPublicationToMap()");

        boolean noErrors = true;

        PublicationModel publication = new PublicationModel();

        try {
            publication.setPublicationId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.ROW_ID)));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("PublicationController: addPublicationToMap(): set publication id error: " +exc.getMessage());
        }

        try {
            publication.setPublicationAuthorId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_AUTHOR_ID)));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("PublicationController: addPublicationToMap(): set publication author id error: " +exc.getMessage());
        }

        try {
            publication.setPublicationBadgeId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_BADGE_ID)));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("PublicationController: addPublicationToMap(): set publication badge id error: " +exc.getMessage());
        }

        try {
            publication.setPublicationCreatedAt(cursor.getString(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_CREATED_AT)));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("PublicationController: PublicationController(): set publication created at error: " +exc.getMessage());
        }

        try {
            publication.setPublicationLatitude(cursor.getString(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_LATITUDE)));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("PublicationController: PublicationController(): set publication latitude error: " +exc.getMessage());
        }

        try {
            publication.setPublicationLongitude(cursor.getString(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_LONGITUDE)));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("PublicationController: PublicationController(): set publication longitude error: " +exc.getMessage());
        }

        try {
            publication.setPublicationRegionName(cursor.getString(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_REGION_NAME)));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("PublicationController: PublicationController(): set publication region name error: " +exc.getMessage());
        }

        try {
            publication.setPublicationStreetName(cursor.getString(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_STREET_NAME)));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("PublicationController: PublicationController(): set publication street name error: " +exc.getMessage());
        }

        try {
            publication.setPublicationText(cursor.getString(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_TEXT)));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("PublicationController: PublicationController(): set publication text error: " +exc.getMessage());
        }

        try {
            publication.setPublicationHasQuiz(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_HAS_QUIZ)) > 0);
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("PublicationController: addPublicationToMap(): set publication has quiz error: " +exc.getMessage());
        }

        try {
            publication.setPublicationHasImages(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_HAS_IMAGES)) > 0);
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("PublicationController: addPublicationToMap(): set publication has images error: " +exc.getMessage());
        }

        try {
            publication.setPublicationIsAnonymous(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.PUBLICATION_IS_ANONYMOUS)) > 0);
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("PublicationController: addPublicationToMap(): set publication is anonymous error: " +exc.getMessage());
        }

        // ------------------------------------------------------------------------------- //

        if(noErrors){
            publicationMap.put(publication.getPublicationId(), publication);
            LocLookApp.showLog("PublicationController: addPublicationToMap(): publication: " +publication.getPublicationId()+ " added");
        }
        else {
            LocLookApp.showLog("PublicationController: addPublicationToMap(): publication will not be added, error occured");
        }
    }

    public void populatePublicationMap() {
        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp.showLog("PublicationController: populatePublicationMap()");

        Cursor cursor = databaseHandler.queryColumns(sqLiteDatabase, DatabaseConstants.PUBLICATION_TABLE, null);

        // ------------------------------------------------------------------------------------ //

        //LocLookApp.showLog("PublicationController: populatePublicationMap(): cursor is null: " +(cursor == null));

//        if(cursor != null)
//            LocLookApp.showLog("PublicationController: populatePublicationMap(): cursor rows: " +cursor.getCount());

        // ------------------------------------------------------------------------------------ //

        if((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();

            // LocLookApp.showLog("UserController: populateUserMap(): cursor rows: " +cursor.getCount());

            do{
                addPublicationToMap(cursor);
            } while(cursor.moveToNext());

            if((publicationMap != null) && (publicationMap.size() >= 0)) {
                if(publicationMap.size() == cursor.getCount())
                    LocLookApp.showLog("PublicationController: populatePublicationMap(): all publications inserted successfully");
                else
                    LocLookApp.showLog("PublicationController: populatePublicationMap(): was inserted " + publicationMap.size() + " publications");

                if(publicationsPopulateListener != null)
                    publicationsPopulateListener.onPublicationsPopulateSuccess();
                else
                    LocLookApp.showLog("PublicationController: populatePublicationMap(): publicationsPopulateListener is null");
            }
            else {
                //LocLookApp.showLog("PublicationController: populatePublicationMap(): publicationMap is null");
                publicationsPopulateListener.onPublicationsPopulateError(ErrorConstants.PUBLICATION_MAP_NULL_ERROR);
            }
        }
        else {
            //LocLookApp.showLog("PublicationController: populatePublicationMap(): cursor size = 0");

            if(publicationsPopulateListener != null)
                publicationsPopulateListener.onPublicationsPopulateSuccess();
            else
                LocLookApp.showLog("PublicationController: populatePublicationMap(): publicationsPopulateListener is null");
        }
    }

    private List<Publication> allPublicationsList = new ArrayList<>();

    /*public static PublicationController getInstance()  {

        if(publicationController == null) {
            publicationController = new PublicationController();
        }

        return publicationController;
    }*/

    public void populateAllPublicationsList() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("PublicationController: populateAllPublicationsList()");

        allPublicationsList.clear();

        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(),
                                                             Constants.DataBase.PUBLICATION_TABLE,
                                                             null);

        if((cursor != null) && (cursor.getCount() > 0)) {
            LocLookApp.showLog("PublicationController: populateAllPublicationsList(): cursor.getCount()= " + cursor.getCount());
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

                LocLookApp.showLog("PublicationController: populateAllPublicationsList(): allPublicationsList size= " +allPublicationsList.size());

            } catch(Exception exc) {
                LocLookApp.showLog("PublicationController: populateAllPublicationsList(): error: " +exc.toString());
            } finally {
                cursor.close();
            }

        }
    }
}
