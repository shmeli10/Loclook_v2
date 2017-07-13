package com.androiditgroup.loclook.v2.data;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.constants.ErrorConstants;

/**
 * Created by Serghei Ostrovschi on 7/5/17.
 */

public class InitAppController {

    private SharedPreferencesController sharedPreferencesController;

    public InitAppController(SharedPreferencesController sharedPreferencesController) throws Exception {

        if(sharedPreferencesController == null)
            throw new Exception(ErrorConstants.SHARED_PREFERENCES_CONTROLLER_NULL_ERROR);

        this.sharedPreferencesController        = sharedPreferencesController;
    }

    public void init() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("InitAppController: init()");

        // is it a "undefined user mode" (user is not registered/authenticated)
        if((!sharedPreferencesController.containsParam("is_undefined_user_mode")) || sharedPreferencesController.isUserUndefinedMode()) {

            LocLookApp.showLog("InitAppController: init(): is_undefined_user_mode");
        }
        // initialization application mode without Database population
        else {

            // get flag value
            boolean isUndefinedUserMode = sharedPreferencesController.getBooleanValue("is_undefined_user_mode");

            LocLookApp.showLog("InitAppController: init(): is_undefined_user_mode: " +isUndefinedUserMode);

            // user is not registered/authenticated yet or it is logged out
            /*if(isUndefinedUserMode) {

            }
            // user is registered / authorized yet
            else {

            }*/
        }
    }
}
