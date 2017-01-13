package com.androiditgroup.loclook.v2.models;

/**
 * Created by sostrovschi on 09.01.2017.
 */

public class User {

    private String mUserId;

    private User() { }

    public String getUserId() {
        return mUserId;
    }

    public static class Builder {
        private String mUserId;

        public Builder userId(String userId) {
            mUserId = userId;
            return this;
        }

        public User build() {
            User user = new User();
            user.mUserId = this.mUserId;
            return user;
        }


    }
}
