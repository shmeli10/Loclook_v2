package com.androiditgroup.loclook.v2.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.data.DatabaseConstants;
import com.androiditgroup.loclook.v2.data.DatabaseHandler;
import com.androiditgroup.loclook.v2.models.UserModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Serghei Ostrovschi on 7/5/16.
 */

public class UserController {


    /*private static UserController userController = null;

    public static UserController getInstance()  {

        if(userController == null) {
            userController = new UserController();
        }

        return userController;
    }*/

    private DatabaseHandler databaseHandler;
    private SQLiteDatabase  sqLiteDatabase;

    private Map<Integer, UserModel> userMap = new HashMap<>();

    private UserModel currentUser;

    public UserController(DatabaseHandler databaseHandler,
                          SQLiteDatabase sqLiteDatabase) throws Exception {
        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp.showLog("UserController: constructor.");

        if(databaseHandler == null)
            throw new Exception(ErrorConstants.DATABASE_HANDLER_NULL_ERROR);

        if(sqLiteDatabase == null)
            throw new Exception(ErrorConstants.SQLITE_DATABASE_NULL_ERROR);

        this.databaseHandler    = databaseHandler;
        this.sqLiteDatabase     = sqLiteDatabase;

        populateUserMap();
    }

    public UserModel getCurrentUser() {

        //Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.USER_TABLE, null, "PHONE_NUMBER", phoneNumber);

        return currentUser;
    }

    private void setCurrentUser(UserModel user) throws Exception {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("UserController: setCurrentUser()");

        if(user == null)
            throw new Exception(ErrorConstants.USER_NULL_ERROR);

        this.currentUser = user;
    }

    private void addUserToMap(Cursor cursor) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("UserController: addUserToMap()");

        boolean noErrors = true;

        UserModel user = new UserModel();

        try {
            user.setUserId(cursor.getInt(cursor.getColumnIndex("_ID")));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user id error: " +exc.getMessage());
        }

        try {
            user.setUserRate(cursor.getInt(cursor.getColumnIndex("RATE")));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user rate error: " +exc.getMessage());
        }

        try {
            user.setUserBackgroundPhotoId(cursor.getInt(cursor.getColumnIndex("BG_PHOTO_ID")));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user background photo id error: " +exc.getMessage());
        }

        try {
            user.setUserAvatarPhotoId(cursor.getInt(cursor.getColumnIndex("AVATAR_PHOTO_ID")));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user avatar photo id error: " +exc.getMessage());
        }

        try {
            user.setUserMapRadiusSize(cursor.getInt(cursor.getColumnIndex("RADIUS")));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user map radius size error: " +exc.getMessage());
        }

        try {
            user.setUserName(cursor.getString(cursor.getColumnIndex("NAME")));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user name error: " +exc.getMessage());
        }

        try {
            user.setUserPhoneNumber(cursor.getString(cursor.getColumnIndex("PHONE_NUMBER")));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user phone number error: " +exc.getMessage());
        }

        try {

            String userDescription = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));

            if(userDescription == null)
                user.setUserDescription("");
            else
                user.setUserDescription(userDescription);
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user description error: " +exc.getMessage());
        }

        try {

            String userSiteURL = cursor.getString(cursor.getColumnIndex("SITE_URL"));

            if(userSiteURL == null)
                user.setUserSiteURL("");
            else
                user.setUserSiteURL(userSiteURL);
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user site URL error: " +exc.getMessage());
        }

        try {

            String userMapLatitude = cursor.getString(cursor.getColumnIndex("LATITUDE"));

            if(userMapLatitude == null)
                user.setUserMapLatitude("");
            else
                user.setUserMapLatitude(userMapLatitude);
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user map latitude error: " +exc.getMessage());
        }

        try {

            String userMapLongitude = cursor.getString(cursor.getColumnIndex("LONGITUDE"));

            if(userMapLongitude == null)
                user.setUserMapLongitude("");
            else
                user.setUserMapLongitude(userMapLongitude);
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user map longitude error: " +exc.getMessage());
        }

        try {

            String userRegionName = cursor.getString(cursor.getColumnIndex("REGION_NAME"));

            if(userRegionName == null)
                user.setUserRegionName("");
            else
                user.setUserRegionName(userRegionName);
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user region name error: " +exc.getMessage());
        }

        try {

            String userStreetName = cursor.getString(cursor.getColumnIndex("STREET_NAME"));

            if(userStreetName == null)
                user.setUserStreetName("");
            else
                user.setUserStreetName(userStreetName);
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("UserController: addUserToMap(): set user street name error: " +exc.getMessage());
        }

        // ------------------------------------------------------------------------------- //

        if(noErrors){
            userMap.put(user.getUserId(), user);
            LocLookApp.showLog("UserController: addUserToMap(): user: " + user.getUserId() + " added");
        }
        else {
            LocLookApp.showLog("UserController: addUserToMap(): user will not be added, error occured");
        }
    }

    private void populateUserMap() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("UserController: populateUserMap()");

        Cursor cursor = databaseHandler.queryColumns(sqLiteDatabase, DatabaseConstants.USER_TABLE, null);

        // ------------------------------------------------------------------------------------ //

        LocLookApp.showLog("UserController: populateUserMap(): cursor is null: " +(cursor == null));

        if(cursor != null)
            LocLookApp.showLog("UserController: populateUserMap(): cursor rows: " +cursor.getCount());

        // ------------------------------------------------------------------------------------ //

        if((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();

            // LocLookApp.showLog("UserController: populateUserMap(): cursor rows: " +cursor.getCount());

            do{
                addUserToMap(cursor);
            } while(cursor.moveToNext());
        }
    }

    public boolean isUserRegistered(String phoneNumber) throws Exception {
        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp.showLog("UserController: isUserRegistered()");

        if(TextUtils.isEmpty(phoneNumber))
            throw new Exception(ErrorConstants.USER_PHONE_NUMBER_NULL_ERROR);

        boolean result = false;

        for(Map.Entry<Integer, UserModel> entry: userMap.entrySet()){

            UserModel user = entry.getValue();

            if(user.getUserPhoneNumber().equals(phoneNumber)) {
                setCurrentUser(user);
                result = true;
                break;
            }
        }

        return result;
    }

    public boolean createUser(String userName,
                              String userPhoneNumber) throws Exception {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("UserController: createUser()");

        boolean result = false;

        String[] columnsArr = {DatabaseConstants.USER_NAME, DatabaseConstants.USER_PHONE_NUMBER};
        String[] dataArr    = {userName, userPhoneNumber};

        Cursor cursor = databaseHandler.insertRow(sqLiteDatabase, DatabaseConstants.USER_TABLE, columnsArr, dataArr);

        if((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();

            int userId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.ROW_ID));

            addUserToMap(cursor);
            setCurrentUser(userMap.get(userId));

            result = true;
        }

        return result;
    }
}
