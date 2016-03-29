package com.lifeboat.mobile.BABQastPresentation.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Maarten Edgar on 10-11-2015.
 */
public class TouchView extends View {

    public boolean isBeingTouched = false;

    public TouchView(Context context) {
        super(context);
    }
    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public TouchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean notInterested = true;
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            isBeingTouched = true;
            return true;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            isBeingTouched = false;
            return false;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
            return true;
        }
        return super.onTouchEvent(event);
    }
}
