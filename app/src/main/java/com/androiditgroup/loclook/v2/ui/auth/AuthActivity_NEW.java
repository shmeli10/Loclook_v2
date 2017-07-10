package com.androiditgroup.loclook.v2.ui.auth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.LocLookApp_NEW;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;
import com.androiditgroup.loclook.v2.ui.general.MainActivity_NEW;
import com.androiditgroup.loclook.v2.utils.ParentActivity;

/**
 * Created by sostrovschi on 10.01.2017.
 */

public class AuthActivity_NEW extends ParentActivity {
//                            implements  FragmentManager.OnBackStackChangedListener{


    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp_NEW.showLog("AuthActivity_NEW: onCreate()");

//        getFragmentManager().addOnBackStackChangedListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.AuthActivity_ProgressBar);
        setFragment(PhoneNumberFragment_NEW.newInstance(), false, false);
    }

    @Override
    public void setToolbarTitle(String title) { }

    @Override
    public void setFragment(Fragment fragment, boolean animate, boolean addToBackStack) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (animate) {
            transaction.setCustomAnimations(R.animator.slide_in, R.animator.slide_out, R.animator.slide_in, R.animator.slide_out);
        } else {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }

        // transaction.add(R.id.AuthActivity_FragmentContainer, fragment, fragment.getClass().getName());
        transaction.replace(R.id.AuthActivity_FragmentContainer, fragment, fragment.getClass().getName());

        if(addToBackStack)
            transaction.addToBackStack(fragment.getClass().getName());

        transaction.commit();
    }

    @Override
    public void changeFragment(Fragment fragment) {
        LocLookApp_NEW.showLog("AuthActivity_NEW: changeFragment()");
    }

    @Override
    public void toggleLoadingProgress(boolean show) {
        LocLookApp_NEW.showLog("AuthActivity_NEW: toggleLoadingProgress(): show: " +show);
    }

//    @Override
//    public void onBackStackChanged() {
//            FragmentManager manager = getFragmentManager();
//            // get index of the last fragment to be able to get it's tag
//            int currentStackIndex = manager.getBackStackEntryCount() - 1;
//            // if we don't have fragments in back stack just return
//            if (manager.getBackStackEntryCount() == 0) {
//                setToolbarTitle(LocalizationManager.getInstance().getLocalizedString("categories"));
//                selectedFragment = SelectedFragment.supercategory;
//                toggleSearchBtnVisibility(false);
//                return;
//            }
//            // otherwise get the framgent from backstack and cast it to ParentFragment so we could get it's title
//            ParentFragment fragment = (ParentFragment) manager.findFragmentByTag(
//                    manager.getBackStackEntryAt(currentStackIndex).getName());
//            // finaly set the title in the toolbar
//            setToolbarTitle(formatToolbarTitle(fragment.getFragmentTitle()));
//            toggleSearchBtnVisibility(true);
//            // now we need to update the current selected fragment
//            selectedFragment = fragment.getSelectedFragment();
//    }

    @Override
    public void onBackPressed() {
        //LocLookApp_NEW.showLog("AuthActivity_NEW: onBackPressed()");

        FragmentManager fragmentManager = getFragmentManager();

        for (int f = 0; f < fragmentManager.getBackStackEntryCount(); f++)
            fragmentManager.popBackStack();
    }

    // двигаемся к следующему окну приложения
    public void moveForward() {
        //LocLookApp_NEW.showLog("AuthActivity_NEW: moveForward()");

        startActivity(new Intent(getApplicationContext(), MainActivity_NEW.class));
    }
}
