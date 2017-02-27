package com.androiditgroup.loclook.v2.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.app.Fragment;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.EditText;
import android.database.Cursor;
import android.view.LayoutInflater;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;

/**
 * Created by sostrovschi on 10.01.2017.
 */

public class PhoneNumberFragment extends Fragment {

    private EditText     phoneBodyET;
    private Button       enterButton;
    private AuthActivity mAuthActivity;

    public PhoneNumberFragment() {
        // Required empty public constructor
    }

    public static PhoneNumberFragment newInstance() {
        Bundle args = new Bundle();
        PhoneNumberFragment fragment = new PhoneNumberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_number, container, false);
        mAuthActivity = (AuthActivity) getActivity();

        // поле "Номер телефона"
        phoneBodyET = (EditText) view.findViewById(R.id.PhoneNumberFragment_PhoneTail_ET);

        // кнопка "ВОЙТИ"
        enterButton = (Button) view.findViewById(R.id.PhoneNumberFragment_Enter_BTN);
        enterButton.setOnClickListener(myClickListener);

        return view;
    }

    View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // если номер телефона при этом не был введен
            if((phoneBodyET.length() == 0) || (phoneBodyET.length() < 10))
                // стоп
                return;

            LocLookApp.getInstance().savePhoneNumber(phoneBodyET.getText().toString());

            // двигаемся к следующему окну приложения
            moveForward();
        }
    };

    private void moveForward() {
        Cursor data = DBManager.getInstance().queryColumns(Constants.DataBase.USER_TABLE, null, "PHONE_NUMBER", phoneBodyET.getText().toString());

        // если это существующий в БД пользователь
        if(data.getCount() > 0) {
            // Log.e("ABC", "PhoneNumberFragment: moveForward(): это существующий в БД пользователь");
            if(mAuthActivity.setUserData(data)) {
                mAuthActivity.setFragment(SMSCodeFragment.newInstance(), false, true);
            }
        }
        // если это новый пользователь
        else {
            // Log.e("ABC", "PhoneNumberFragment: moveForward(): это новый пользователь");
            mAuthActivity.setFragment(UserNameFragment.newInstance(), false, true);
        }
    }
}
