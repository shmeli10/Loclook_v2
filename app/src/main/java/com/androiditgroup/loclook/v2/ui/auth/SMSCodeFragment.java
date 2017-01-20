package com.androiditgroup.loclook.v2.ui.auth;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;

/**
 * Created by sostrovschi on 1/19/17.
 */

public class SMSCodeFragment extends Fragment {

    private EditText        smsCodeET;
    private AuthActivity    mAuthActivity;

    public SMSCodeFragment() {
        // Required empty public constructor
    }

    public static SMSCodeFragment newInstance() {
        Bundle args = new Bundle();
        SMSCodeFragment fragment = new SMSCodeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sms_code, container, false);
        mAuthActivity = (AuthActivity) getActivity();

        // определить переменную для работы с полем "СМС код"
        smsCodeET = (EditText) view.findViewById(R.id.SMSCodeFragment_Code_ET);
        smsCodeET.addTextChangedListener(new TextWatcher() {
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
        });

        if((LocLookApp.authCode != null) && (!LocLookApp.authCode.equals("")))
            // вывести на экран код авторизации, который должен ввести пользователь
            Toast.makeText(LocLookApp.context, LocLookApp.authCode, Toast.LENGTH_LONG).show();
        // если код авторизации не получен
        else
            // вывести на экран предупреждение
            Toast.makeText(LocLookApp.context, R.string.no_sms_code_text, Toast.LENGTH_LONG).show();

        // кнопка "Назад"
        (view.findViewById(R.id.SMSCodeFragment_Back_IV)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuthActivity.onBackPressed();
            }
        });

        // кнопка "Вперед"
        (view.findViewById(R.id.SMSCodeFragment_Forward_IV)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // если введенный код авторизации не совпадает с ожидаемым системой
                if((!smsCodeET.getText().toString().equals(LocLookApp.authCode)))
                    // стоп
                    return;

                // переход на Главный экран
                startActivity(new Intent(LocLookApp.context, MainActivity.class));
            }
        });

        return view;
    }
}
