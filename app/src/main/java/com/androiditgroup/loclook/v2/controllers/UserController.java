package com.androiditgroup.loclook.v2.controllers;

import com.androiditgroup.loclook.v2.models.UserModel;

/**
 * Created by Serghei Ostrovschi on 7/5/16.
 */

public class UserController {

    private static UserController userController = null;

    public static UserController getInstance()  {

        if(userController == null) {
            userController = new UserController();
        }

        return userController;
    }

    public UserModel getUserModel() {

        //Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.USER_TABLE, null, "PHONE_NUMBER", phoneNumber);

        return null;
    }
}
