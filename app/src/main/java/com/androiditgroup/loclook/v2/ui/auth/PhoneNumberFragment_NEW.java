package com.androiditgroup.loclook.v2.ui.auth;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.LocLookApp_NEW;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.controllers.SharedPreferencesController;
import com.androiditgroup.loclook.v2.controllers.UserController;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;
import com.androiditgroup.loclook.v2.utils.UiUtils;

/**
 * Created by sostrovschi on 10.01.2017.
 */

public class PhoneNumberFragment_NEW extends Fragment {

    private LocLookApp_NEW              locLookApp_NEW;
    private SharedPreferencesController sharedPreferencesController;
    private UserController              userController;

    private AuthActivity_NEW    mAuthActivity;

    private EditText            phoneBodyET;
    private Button              enterButton;


    public PhoneNumberFragment_NEW() {
        // Required empty public constructor
    }

    public static PhoneNumberFragment_NEW newInstance() {
        Bundle args = new Bundle();
        PhoneNumberFragment_NEW fragment = new PhoneNumberFragment_NEW();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_number, container, false);

        LocLookApp.showLog("-------------------------------------");
        LocLookApp_NEW.showLog("PhoneNumberFragment_NEW: onCreateView()");

        mAuthActivity   = (AuthActivity_NEW) getActivity();
        locLookApp_NEW  = ((LocLookApp_NEW) mAuthActivity.getApplication());

        sharedPreferencesController = locLookApp_NEW.getAppManager().getSharedPreferencesController();
        userController              = locLookApp_NEW.getAppManager().getUserController();

        // ----------------------------------------------------------------------------------- //

        // phoneBodyET = (EditText) view.findViewById(R.id.PhoneNumberFragment_PhoneTail_ET);
        phoneBodyET = UiUtils.findView(view, R.id.PhoneNumberFragment_PhoneTail_ET);

        //enterButton = (Button) view.findViewById(R.id.PhoneNumberFragment_Enter_BTN);
        enterButton = UiUtils.findView(view, R.id.PhoneNumberFragment_Enter_BTN);
        enterButton.setOnClickListener(myClickListener);

        return view;
    }

    View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if((phoneBodyET.length() == 0) || (phoneBodyET.length() < 10))
                return;

            //sharedPreferencesController.setNewStringValue("user_phone_number", phoneBodyET.getText().toString());
            // LocLookApp.getInstance().savePhoneNumber(phoneBodyET.getText().toString());

            moveForward(phoneBodyET.getText().toString());
        }
    };

    private void moveForward(String typedPhoneNumber) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp_NEW.showLog("PhoneNumberFragment_NEW: moveForward()");

        sharedPreferencesController.setNewStringValue("user_phone_number", phoneBodyET.getText().toString());

        try {
            if(userController.isUserRegistered(typedPhoneNumber)){
                LocLookApp_NEW.showLog("PhoneNumberFragment_NEW: moveForward(): user is registered yet");
                mAuthActivity.setFragment(SMSCodeFragment_NEW.newInstance(), false, true);
            }
            else {
                LocLookApp_NEW.showLog("PhoneNumberFragment_NEW: moveForward(): user is not registered yet");
                mAuthActivity.setFragment(UserNameFragment_NEW.newInstance(), false, true);
            }

        } catch (Exception exc) {
            LocLookApp_NEW.showLog("PhoneNumberFragment_NEW: moveForward(): isUserRegistered error: " +exc.getMessage());
        }

        /*Cursor data = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.USER_TABLE, null, "PHONE_NUMBER", phoneBodyET.getText().toString());

        // если это существующий в БД пользователь
        if(data.getCount() > 0) {
            // LocLookApp.showLog("PhoneNumberFragment: moveForward(): это существующий в БД пользователь");

            if(mAuthActivity.setUserData(data)) {
                mAuthActivity.setFragment(SMSCodeFragment.newInstance(), false, true);
            }
        }
        // если это новый пользователь
        else {
            // LocLookApp.showLog("PhoneNumberFragment: moveForward(): это новый пользователь");
            mAuthActivity.setFragment(UserNameFragment.newInstance(), false, true);
        }*/
    }
}
