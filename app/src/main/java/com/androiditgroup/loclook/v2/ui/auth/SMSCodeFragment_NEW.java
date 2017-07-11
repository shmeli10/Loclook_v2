package com.androiditgroup.loclook.v2.ui.auth;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.LocLookApp_NEW;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.controllers.PublicationController;
import com.androiditgroup.loclook.v2.controllers.SharedPreferencesController;
import com.androiditgroup.loclook.v2.interfaces.PublicationsPopulateInterface;

/**
 * Created by sostrovschi on 19.01.2017.
 */

public class SMSCodeFragment_NEW    extends     Fragment
                                    implements  PublicationsPopulateInterface {

    private LocLookApp_NEW              locLookApp_NEW;
    private PublicationController       publicationController;
    private SharedPreferencesController sharedPreferencesController;

    private AuthActivity_NEW    mAuthActivity;

    private EditText        smsCodeET;

    private String authCode = "";


    public SMSCodeFragment_NEW() {
        // Required empty public constructor
    }

    public static SMSCodeFragment_NEW newInstance() {
        Bundle args = new Bundle();
        SMSCodeFragment_NEW fragment = new SMSCodeFragment_NEW();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_code, container, false);

        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp_NEW.showLog("SMSCodeFragment_NEW: onCreateView()");

        mAuthActivity   = (AuthActivity_NEW) getActivity();
        locLookApp_NEW  = ((LocLookApp_NEW) mAuthActivity.getApplication());

        sharedPreferencesController = locLookApp_NEW.getAppManager().getSharedPreferencesController();

        // ----------------------------------------------------------------------------------- //

        // определить переменную для работы с полем "СМС код"
        smsCodeET = (EditText) view.findViewById(R.id.SMSCodeFragment_Code_ET);
        smsCodeET.addTextChangedListener(smsCodeChangeListener);

       /* smsCodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // если введены 6 символов в поле
                if(smsCodeET.getText().length() == 6) {
                    // сворачиваем клавиатуру
                    InputMethodManager imm = (InputMethodManager) mAuthActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(smsCodeET.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });*/

        generateCode();

        // кнопка "Назад"
        (view.findViewById(R.id.SMSCodeFragment_Back_IV)).setOnClickListener(backButtonClickListener);

        /*(view.findViewById(R.id.SMSCodeFragment_Back_IV)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthActivity.onBackPressed();
            }
        });*/

        // кнопка "Вперед"
        (view.findViewById(R.id.SMSCodeFragment_Forward_IV)).setOnClickListener(forwardButtonClickListener);

        /*(view.findViewById(R.id.SMSCodeFragment_Forward_IV)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // если введенный код авторизации не совпадает с ожидаемым системой
                if((!smsCodeET.getText().toString().equals(authCode)))
                    // стоп
                    return;

                // фиксируем вход пользователя
                LocLookApp.getInstance().setLoginStatus(true);

                // двигаемся к следующему окну приложения
                mAuthActivity.moveForward();
            }
        });*/

        return view;
    }

    private void generateCode() {
        int minVal = 111111;
        int maxVal = 999999;

        authCode = String.valueOf(minVal + (int)(Math.random() * ((maxVal - minVal) + 1)));

        if(!TextUtils.isEmpty(authCode))
        //if((authCode != null) && (!authCode.equals("")))
            // вывести на экран код авторизации, который должен ввести пользователь
            //Toast.makeText(LocLookApp.context, authCode, Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(), authCode, Toast.LENGTH_LONG).show();
        // если код авторизации не получен
        else
            // вывести на экран предупреждение
            //Toast.makeText(LocLookApp.context, R.string.no_sms_code_text, Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(), R.string.no_sms_code_text, Toast.LENGTH_LONG).show();
    }

    TextWatcher smsCodeChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if(smsCodeET.getText().length() == 6) {
                InputMethodManager imm = (InputMethodManager) mAuthActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(smsCodeET.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    View.OnClickListener backButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //LocLookApp_NEW.showLog("SMSCodeFragment_NEW: backButtonClickListener: onClick()");
            mAuthActivity.onBackPressed();
        }
    };

    View.OnClickListener forwardButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            LocLookApp_NEW.showLog("SMSCodeFragment_NEW: forwardButtonClickListener: onClick()");

            // если введенный код авторизации не совпадает с ожидаемым системой
            if((!smsCodeET.getText().toString().equals(authCode)))
                // стоп
                return;

            // фиксируем вход пользователя
            // LocLookApp.getInstance().setLoginStatus(true);
            sharedPreferencesController.setNewBooleanValue("is_undefined_user_mode", false);

            publicationController = locLookApp_NEW.getAppManager().getPublicationController();
            try {
                publicationController.setPublicationsPopulateListener(SMSCodeFragment_NEW.this);
                publicationController.populatePublicationMap();
            } catch (Exception exc) {
                LocLookApp_NEW.showLog("SMSCodeFragment_NEW: forwardButtonClickListener(): setPublicationsPopulateListener error: " +exc.getMessage());
            }
        }
    };

    @Override
    public void onPublicationsPopulateSuccess() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp_NEW.showLog("SMSCodeFragment_NEW: onPublicationsPopulateSuccess()");

        // двигаемся к следующему окну приложения
        mAuthActivity.moveForward();
    }

    @Override
    public void onPublicationsPopulateError(String error) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp_NEW.showLog("SMSCodeFragment_NEW: onPublicationsPopulateError(): error: " +error);
    }
}
