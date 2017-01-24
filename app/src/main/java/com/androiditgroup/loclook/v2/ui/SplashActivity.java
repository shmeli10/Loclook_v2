package com.androiditgroup.loclook.v2.ui;

import android.os.Bundle;
import android.os.AsyncTask;
import android.content.Intent;
import java.util.concurrent.TimeUnit;
import android.support.v7.app.AppCompatActivity;
import com.androiditgroup.loclook.v2.ui.auth.AuthActivity;

/**
 * Created by sostrovschi on 10.01.2017.
 */

public class SplashActivity extends AppCompatActivity {
    private MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                // переходим вперед
                startActivity(new Intent(SplashActivity.this, AuthActivity.class));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
