package com.androiditgroup.loclook.v2.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.ui.auth.AuthActivity;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;

import java.util.concurrent.TimeUnit;

/**
 * Created by sostrovschi on 10.01.2017.
 */

public class SplashActivity extends AppCompatActivity {

    private String userId;
    private MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // получаем номер телефона из Preferences
        userId = LocLookApp.getInstance().getPhoneNumber();
    }

    @Override
    protected void onResume() {
        super.onResume();

        myTask = new MyTask();
        myTask.execute();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {

                // задержка перехода в 3 секунды
                TimeUnit.SECONDS.sleep(3);

                ///////////////////////////////////////////////////////////////////////////////

                Intent intent = null;

                // если идентификатор пользователя получен
                if((userId != null) && (!userId.equals("")))
                    // переход к ленте
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    // если идентификатор пользователя отсутствует
                else
                    // переход к окну ввода номера телефона
                    intent = new Intent(SplashActivity.this, AuthActivity.class);

                if(intent != null)
                    startActivity(intent);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
