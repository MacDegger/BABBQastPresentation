package com.lifeboat.mobile.BABQastPresentation.widgets;

/**
 * Created by Maarten Edgar on 24-10-2015.
 */

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.cast.CastPresentation;
import com.lifeboat.mobile.BABQastPresentation.R;

/**
 * The presentation to show on the secondary display.
 * <p>
 * Note that this display may have different metrics from the display on which
 * the main activity is showing so we must be careful to use the presentation's
 * own {@link Context} whenever we load resources.
 * </p>
 */
public final class CustomCastPresentation extends CastPresentation {

    private int scrW;
    private int scrH;

    private int xMove = 0;
    private int yMove = 0;

    private InteractiveView mIV;

    public CustomCastPresentation(Context context, Display display, int data) {
        super(context, display);

        scrW = display.getWidth();
        scrH = display.getHeight();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);

        // Get the resources for the context of the presentation.
        // Notice that we are getting the resources from the context of the presentation.
        Resources r = getContext().getResources();

        // Inflate the layout.
        setContentView(R.layout.presentation_with_media_router_content);

        mIV = (InteractiveView)findViewById(R.id.interactive_view);
        mIV.InitSize(scrW, scrH);
    }

    public void SetShowBG(boolean show){
        mIV.SetShowBG(show);
    }

    public void DetermindeDirection(boolean left, boolean right, boolean top, boolean bottom){
        if (left == right) {
            //no horizontal movement
            xMove = 0;
        } else {
            xMove = (left)?-1:1;
        }

        if (top == bottom) {
            //no vertical movement
            yMove = 0;
        } else {
            yMove = (top)?-1:1;
        }

        if (xMove != 0 || yMove != 0) {
            //invalidate the presentation view
            mIV.UpdatePosition(xMove, yMove);
            mIV.invalidate();
        }
    }
}