package com.androiditgroup.loclook.v2.ui.auth;


import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.LocLookApp_NEW;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.controllers.SharedPreferencesController;
import com.androiditgroup.loclook.v2.controllers.UserController;
import com.androiditgroup.loclook.v2.utils.DBManager;
import com.androiditgroup.loclook.v2.utils.UiUtils;

/**
 * Created by sostrovschi on 1/19/17.
 */

public class UserNameFragment_NEW extends Fragment {

    private LocLookApp_NEW              locLookApp_NEW;
    private SharedPreferencesController sharedPreferencesController;
    private UserController              userController;

    private AuthActivity_NEW    mAuthActivity;

    private EditText        userNameET;


    public UserNameFragment_NEW() {
        // Required empty public constructor
    }

    public static UserNameFragment_NEW newInstance() {
        Bundle args = new Bundle();
        UserNameFragment_NEW fragment = new UserNameFragment_NEW();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_name, container, false);

        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp_NEW.showLog("UserNameFragment_NEW: onCreateView()");

        mAuthActivity   = (AuthActivity_NEW) getActivity();
        locLookApp_NEW  = ((LocLookApp_NEW) mAuthActivity.getApplication());

        sharedPreferencesController = locLookApp_NEW.getAppManager().getSharedPreferencesController();
        userController              = locLookApp_NEW.getAppManager().getUserController();

        // ----------------------------------------------------------------------------------- //

        //userNameET  = (EditText) view.findViewById(R.id.UserNameFragment_Name_ET);
        userNameET  = UiUtils.findView(view, R.id.UserNameFragment_Name_ET);
        userNameET.setText(sharedPreferencesController.getStringValue("user_name"));
        //userNameET.setText(LocLookApp.getInstance().getEnteredUserName());

/*        (view.findViewById(R.id.UserNameFragment_Back_IV)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LocLookApp.getInstance().saveEnteredUserName(userNameET.getText().toString());
                sharedPreferencesController.setNewStringValue("user_name", userNameET.getText().toString());
                mAuthActivity.onBackPressed();
            }
        });*/

        (view.findViewById(R.id.UserNameFragment_Back_IV)).setOnClickListener(backButtonClickListener);

        (view.findViewById(R.id.UserNameFragment_Forward_IV)).setOnClickListener(forwardButtonClickListener);

        // кнопка "Вперед"
//        (view.findViewById(R.id.UserNameFragment_Forward_IV)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String typedUserName = userNameET.getText().toString();
//
//                if(TextUtils.isEmpty(typedUserName))
//                //if(enteredUserName.equals(""))
//                    return;
//
//                String userPhoneNumber = sharedPreferencesController.getStringValue("user_phone_number");
//
//                try {
//                    if(userController.createUser(typedUserName, userPhoneNumber)) {
//                        LocLookApp_NEW.showLog("UserNameFragment_NEW: onCreateView(): user created successfully");
//
//                        mAuthActivity.setFragment(SMSCodeFragment.newInstance(), false, true);
//                    }
//                    else {
//                        LocLookApp_NEW.showLog("UserNameFragment_NEW: onCreateView(): user not created");
//                    }
//                } catch (Exception exc) {
//                    LocLookApp_NEW.showLog("UserNameFragment_NEW: onCreateView(): createUser error: " +exc.getMessage());
//                }
//
//                // создаем нового пользователя
///*                Cursor data = DBManager.getInstance().createUser(enteredUserName, LocLookApp.getInstance().getPhoneNumber());
//
//                // если курсор с данными пользователя получен
//                if(data != null) {
//                    // если создан объект "пользователь"
//                    if(mAuthActivity.setUserData(data)) {
//                        // "затираем" введенное имя пользователя
//                        LocLookApp.getInstance().saveEnteredUserName("");
//
//                        // переходим вперед
//                        mAuthActivity.setFragment(SMSCodeFragment.newInstance(), false, true);
//                    }
//                }*/
//            }
//        });

        return view;
    }

    View.OnClickListener backButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //LocLookApp_NEW.showLog("UserNameFragment_NEW: backButtonClickListener: onClick()");

            //LocLookApp.getInstance().saveEnteredUserName(userNameET.getText().toString());
            sharedPreferencesController.setNewStringValue("user_name", userNameET.getText().toString());
            mAuthActivity.onBackPressed();
        }
    };

    View.OnClickListener forwardButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //LocLookApp_NEW.showLog("UserNameFragment_NEW: forwardButtonClickListener: onClick()");

            String typedUserName = userNameET.getText().toString();

            if(TextUtils.isEmpty(typedUserName))
                return;

            String userPhoneNumber = sharedPreferencesController.getStringValue("user_phone_number");

            try {
                if(userController.createUser(typedUserName, userPhoneNumber)) {
                    LocLookApp_NEW.showLog("UserNameFragment_NEW: forwardButtonClickListener: user created successfully");

                    mAuthActivity.setFragment(SMSCodeFragment_NEW.newInstance(), false, true);
                }
                else {
                    LocLookApp_NEW.showLog("UserNameFragment_NEW: forwardButtonClickListener: user not created");
                }
            } catch (Exception exc) {
                LocLookApp_NEW.showLog("UserNameFragment_NEW: forwardButtonClickListener: createUser error: " +exc.getMessage());
            }
        }
    };
}
