package com.androiditgroup.loclook.v2.utils;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * Created by sostrovschi on 10.01.2017
 */

public abstract class ParentActivity extends AppCompatActivity {
    public void expandTextView(TextView tv) {
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", 300);
        animation.setDuration(200).start();
    }

    public void collapseTextView(TextView tv, int numLines) {
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", numLines);
        animation.setDuration(200).start();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public abstract void setToolbarTitle(final String title);

    public abstract void addFragment(Fragment fragment, boolean animate);

    public abstract void changeFragment(Fragment fragment);

    public abstract void toggleLoadingProgress(final boolean show);
}
