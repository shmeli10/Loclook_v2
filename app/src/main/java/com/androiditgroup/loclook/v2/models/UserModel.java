package com.androiditgroup.loclook.v2.models;

import com.androiditgroup.loclook.v2.constants.ErrorConstants;

/**
 * Created by Serghei Ostrovschi on 7/5/16.
 */

public class UserModel {

    private int userId;
    private int userRate;
    private int userBackgroundPhotoId;
    private int userAvatarPhotoId;
    private int userMapRadiusSize;

    private String userName;
    private String userPhoneNumber;
    private String userDescription;
    private String userSiteURL;
    private String userMapLatitude;
    private String userMapLongitude;
    private String userRegionName;
    private String userStreetName;

    // ------------------------------------- GETTERS ----------------------------------------- //

    public int getUserId() {
        return userId;
    }

    public int getUserRate() {
        return userRate;
    }

    public int getUserBackgroundPhotoId() {
        return userBackgroundPhotoId;
    }

    public int getUserAvatarPhotoId() {
        return userAvatarPhotoId;
    }

    public int getUserMapRadiusSize() {
        return userMapRadiusSize;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public String getUserSiteURL() {
        return userSiteURL;
    }

    public String getUserMapLatitude() {
        return userMapLatitude;
    }

    public String getUserMapLongitude() {
        return userMapLongitude;
    }

    public String getUserRegionName() {
        return userRegionName;
    }

    public String getUserStreetName() {
        return userStreetName;
    }

    // ------------------------------------- SETTERS ----------------------------------------- //

    public void setUserId(int userId) throws Exception {
        if(userId > 0)
            this.userId = userId;
        else
            throw new Exception(ErrorConstants.USER_ID_ERROR);
    }

    public void setUserRate(int userRate) throws Exception {
        if(userRate >= 0)
            this.userRate = userRate;
        else
            throw new Exception(ErrorConstants.USER_RATE_ERROR);
    }

    public void setUserBackgroundPhotoId(int userBackgroundPhotoId) throws Exception {
        if(userBackgroundPhotoId >= 0)
            this.userBackgroundPhotoId = userBackgroundPhotoId;
        else
            throw new Exception(ErrorConstants.USER_BG_PHOTO_ID_ERROR);
    }

    public void setUserAvatarPhotoId(int userAvatarPhotoId) throws Exception {
        if(userAvatarPhotoId >= 0)
            this.userAvatarPhotoId = userAvatarPhotoId;
        else
            throw new Exception(ErrorConstants.USER_AVATAR_PHOTO_ID_ERROR);
    }

    public void setUserMapRadiusSize(int userMapRadiusSize) throws Exception {
        if(userMapRadiusSize >= 0)
            this.userMapRadiusSize = userMapRadiusSize;
        else
            throw new Exception(ErrorConstants.USER_MAP_RADIUS_SIZE_ERROR);
    }

    public void setUserName(String userName) throws Exception {
        if(userName != null)
            this.userName = userName;
        else
            throw new Exception(ErrorConstants.USER_NAME_NULL_ERROR);
    }

    public void setUserPhoneNumber(String userPhoneNumber) throws Exception {
        if(userPhoneNumber != null)
            this.userPhoneNumber = userPhoneNumber;
        else
            throw new Exception(ErrorConstants.USER_PHONE_NUMBER_NULL_ERROR);
    }

    public void setUserDescription(String userDescription) throws Exception {
        if(userDescription != null)
            this.userDescription = userDescription;
        else
            throw new Exception(ErrorConstants.USER_DESCRIPTION_NULL_ERROR);
    }

    public void setUserSiteURL(String userSiteURL) throws Exception {
        if(userSiteURL != null)
            this.userSiteURL = userSiteURL;
        else
            throw new Exception(ErrorConstants.USER_SITE_URL_NULL_ERROR);
    }

    public void setUserMapLatitude(String userMapLatitude) throws Exception {
        if(userMapLatitude != null)
            this.userMapLatitude = userMapLatitude;
        else
            throw new Exception(ErrorConstants.USER_MAP_LATITUDE_NULL_ERROR);
    }

    public void setUserMapLongitude(String userMapLongitude) throws Exception {
        if(userMapLongitude != null)
            this.userMapLongitude = userMapLongitude;
        else
            throw new Exception(ErrorConstants.USER_MAP_LONGITUDE_NULL_ERROR);
    }

    public void setUserRegionName(String userRegionName) throws Exception {
        if(userRegionName != null)
            this.userRegionName = userRegionName;
        else
            throw new Exception(ErrorConstants.USER_REGION_NAME_NULL_ERROR);
    }

    public void setUserStreetName(String userStreetName) throws Exception {
        if(userStreetName != null)
            this.userStreetName = userStreetName;
        else
            throw new Exception(ErrorConstants.USER_STREET_NAME_NULL_ERROR);
    }
}
