package com.androiditgroup.loclook.v2.utils;

import android.app.Activity;
import android.view.View;

/**
 * Created by OS1 on 25.03.2017.
 */
public class UiUtils {
    public static <T extends View> T findView(View root, int id)      {
        return (T) root.findViewById(id); }

    public static <T extends View> T findView(Activity activity, int id)      {
        return (T) activity.getWindow().getDecorView().getRootView().findViewById(id); }
}
