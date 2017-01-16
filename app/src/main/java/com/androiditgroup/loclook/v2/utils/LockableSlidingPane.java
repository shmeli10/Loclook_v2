package com.androiditgroup.loclook.v2.utils;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by sostrovschi on 1/16/17.
 */

public class LockableSlidingPane extends SlidingPaneLayout {

    private boolean mEnableTouchEvents;

    public LockableSlidingPane(Context context) {
        super(context);
        mEnableTouchEvents = true;
    }

    public LockableSlidingPane(Context context, AttributeSet attrs) {
        super(context, attrs);
        mEnableTouchEvents = true;
    }

    public LockableSlidingPane(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mEnableTouchEvents = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.onTouchEvent(ev);
    }

    public void setEnableTouchEvents(boolean enableTouchEvents) {
        mEnableTouchEvents = enableTouchEvents;
    }

    public boolean isEnableTouchEvents() {
        return mEnableTouchEvents;
    }
}
