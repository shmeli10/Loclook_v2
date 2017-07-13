package com.androiditgroup.loclook.v2.models;

import com.androiditgroup.loclook.v2.constants.ErrorConstants;

/**
 * Created by Serghei Ostrovschi on 7/13/16.
 */

public class BadgeModel {

    private int badgeId;

    private String badgeName;

    // ------------------------------------- GETTERS ----------------------------------------- //

    public int getBadgeId() {
        return badgeId;
    }

    public String getBadgeName() {
        return badgeName;
    }

    // ------------------------------------- SETTERS ----------------------------------------- //

    public void setBadgeId(int badgeId) throws Exception {
        if(badgeId > 0)
            this.badgeId = badgeId;
        else
            throw new Exception(ErrorConstants.BADGE_ID_ERROR);
    }

    public void setBadgeName(String badgeName) throws Exception {
        if(badgeName != null)
            this.badgeName = badgeName;
        else
            throw new Exception(ErrorConstants.BADGE_NAME_NULL_ERROR);
    }
}
