package com.androiditgroup.loclook.v2.ui.auth;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.utils.DBManager;

/**
 * Created by sostrovschi on 1/19/17.
 */

public class UserNameFragment extends Fragment {

    private EditText        userNameET;
    private AuthActivity    mAuthActivity;

    public UserNameFragment() {
        // Required empty public constructor
    }

    public static UserNameFragment newInstance() {
        Bundle args = new Bundle();
        UserNameFragment fragment = new UserNameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_name, container, false);
        mAuthActivity = (AuthActivity) getActivity();

        // определить переменную для работы с полем "Имя пользователя"
        userNameET  = (EditText) view.findViewById(R.id.UserNameFragment_Name_ET);
        userNameET.setText(LocLookApp.getInstance().getEnteredUserName());

        // кнопка "Назад"
        (view.findViewById(R.id.UserNameFragment_Back_IV)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocLookApp.getInstance().saveEnteredUserName(userNameET.getText().toString());

                mAuthActivity.onBackPressed();
            }
        });

        // кнопка "Вперед"
        (view.findViewById(R.id.UserNameFragment_Forward_IV)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // получаем имя введенное пользователем
                String enteredUserName = userNameET.getText().toString();

                // если имя пользователя не было введено
                if(enteredUserName.equals(""))
                    // стоп
                    return;

                // сохраняем введенное имя пользователя
                LocLookApp.getInstance().saveEnteredUserName(enteredUserName);

                Cursor data = DBManager.getInstance().createUser(enteredUserName, LocLookApp.getInstance().getPhoneNumber());

                // если курсор с данными пользователя получен
                if(data != null) {

                    if(mAuthActivity.setUserData(data)) {
                        // переключаемся на другой фрагмент
                        mAuthActivity.setFragment(SMSCodeFragment.newInstance(), false, false);
                        // MainActivity.selectedFragment = MainActivity.SelectedFragment.subcategory;
                    }
                }
            }
        });

        return view;
    }
}
