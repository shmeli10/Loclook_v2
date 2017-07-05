package com.androiditgroup.loclook.v2.models;

/**
 * Created by Serghei Ostrovschi on 7/5/16.
 */

public class AccountModel {

    private UserModel userModel;

    /**
     * AccountModel constructor.
     * Creates instances of:
     * <ul>
     *     <li>{@link UserModel} class</li>
     * </ul>
     */
    public AccountModel() {
        userModel = new UserModel();
    }

    /**
     * Method gets link on existing instance of {@link UserModel} class
     *
     * @return link on existing instance of {@link UserModel} class
     */
    public UserModel getUserModel() {
        return userModel;
    }
}
