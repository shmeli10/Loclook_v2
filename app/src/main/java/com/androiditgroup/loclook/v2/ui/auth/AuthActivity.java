package com.androiditgroup.loclook.v2.ui.auth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.utils.ParentActivity;

/**
 * Created by sostrovschi on 10.01.2017.
 */

public class AuthActivity   extends     ParentActivity
                            implements  FragmentManager.OnBackStackChangedListener{

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        getFragmentManager().addOnBackStackChangedListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.AuthActivity_ProgressBar);

        PhoneNumberFragment fragment = PhoneNumberFragment.newInstance();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.AuthActivity_FragmentContainer, fragment, fragment.getClass().getName());
        transaction.commit();
    }

    @Override
    public void setToolbarTitle(String title) { }

    @Override
    public void addFragment(Fragment fragment, boolean animate) {

    }

    @Override
    public void changeFragment(Fragment fragment) {

    }

    @Override
    public void toggleLoadingProgress(boolean show) {

    }

    @Override
    public void onBackStackChanged() {

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
    }
}
