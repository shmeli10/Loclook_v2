package com.androiditgroup.loclook.v2.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androiditgroup.loclook.v2.ui.auth.AuthActivity;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;

import java.util.concurrent.TimeUnit;

/**
 * Created by sostrovschi on 10.01.2017.
 */

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences shPref;

    private String userId;

    private MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // определить переменную для работы с Preferences
        shPref = getSharedPreferences("user_data", MODE_PRIVATE);

        // подгрузить данные из Preferences
        loadTextFromPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();

        myTask = new MyTask();
        myTask.execute();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * загрузка сохраненных значений из Preferences
     */
    private void loadTextFromPreferences() {
        // если параметр существует
        if(shPref.contains("user_id"))
            // получаем его значение
            userId = shPref.getString("user_id", "");
    }

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
