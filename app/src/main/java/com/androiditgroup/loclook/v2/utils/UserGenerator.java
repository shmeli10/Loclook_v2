package com.androiditgroup.loclook.v2.utils;

import android.database.Cursor;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.models.User;

/**
 * Created by sostrovschi on 3/3/17.
 */

public class UserGenerator {

    public static User getUserById(String userId) {

        if(userId != null)
            return getUserFromCursor(DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.USER_TABLE, null, "_ID", userId));
        else
            return null;
    }

    public static User getUserFromCursor(Cursor cursor) {

        try {

            cursor.moveToFirst();

            String userId       = cursor.getString(cursor.getColumnIndex("_ID"));
            String name         = cursor.getString(cursor.getColumnIndex("NAME"));
            String phoneNumber  = cursor.getString(cursor.getColumnIndex("PHONE_NUMBER"));
            // String rate         = cursor.getString(cursor.getColumnIndex("RATE"));
            int rate            = cursor.getInt(cursor.getColumnIndex("RATE"));
            // String bgImgUrl     = data.getString(data.getColumnIndex("BG_IMG_URL"));
            // String avatarUrl    = data.getString(data.getColumnIndex("AVATAR_URL"));
            String description  = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
            String siteUrl      = cursor.getString(cursor.getColumnIndex("SITE_URL"));
            String latitude     = cursor.getString(cursor.getColumnIndex("LATITUDE"));
            String longitude    = cursor.getString(cursor.getColumnIndex("LONGITUDE"));
            // String radius       = cursor.getString(cursor.getColumnIndex("RADIUS"));
            int radius          = cursor.getInt(cursor.getColumnIndex("RADIUS"));
            String regionName   = cursor.getString(cursor.getColumnIndex("REGION_NAME"));
            String streetName   = cursor.getString(cursor.getColumnIndex("STREET_NAME"));

            //////////////////////////////////////////////////////////////////////////////////////

            User.Builder mUserBuilder = new User.Builder();

            if((userId != null) && (!userId.equals("")))
                mUserBuilder.userId(userId);
            else
                return null;

            if((name != null) && (!name.equals("")))
                mUserBuilder.name(name);

            if((phoneNumber != null) && (!phoneNumber.equals("")))
                mUserBuilder.phone(phoneNumber);

//            if((rate != null) && (!rate.equals("")))
//                mUserBuilder.rate(rate);

            mUserBuilder.rate(rate);

            //        if((bgImgUrl != null) && (!bgImgUrl.equals("")))
            //            mUserBuilder.bgImgUrl(bgImgUrl);
            //
            //        if((avatarUrl != null) && (!avatarUrl.equals("")))
            //            mUserBuilder.avatarUrl(avatarUrl);

            if((description != null) && (!description.equals("")))
                mUserBuilder.description(description);

            if((siteUrl != null) && (!siteUrl.equals("")))
                mUserBuilder.siteUrl(siteUrl);

            if((latitude != null) && (!latitude.equals("")))
                mUserBuilder.latitude(latitude);

            if((longitude != null) && (!longitude.equals("")))
                mUserBuilder.longitude(longitude);

//            if((radius != null) && (!radius.equals("")))
//                mUserBuilder.radius(radius);

            mUserBuilder.radius(radius);

            if((regionName != null) && (!regionName.equals("")))
                mUserBuilder.regionName(regionName);

            if((streetName != null) && (!streetName.equals("")))
                mUserBuilder.streetName(streetName);

            // добавляем список избранного
            mUserBuilder.favoriteslist(FavoritesUtility.getUserFavorites());

            // добавляем список публикаций, в которых пользователь оставлял комментарии
            mUserBuilder.commentedPublicationsList(CommentsUtility.getUserCommentedPublicationsList());

            // добавляем список понравившегося
            mUserBuilder.likeslist(LikesUtility.getUserLikes());

            // возвращаем пользователя
            return mUserBuilder.build();

        } catch(Exception exc) {
            LocLookApp.showLog("UserGenerator: getUserFromCursor(): error: " +exc.toString());
        } finally {
            cursor.close();
        }

        return null;
    }
}
