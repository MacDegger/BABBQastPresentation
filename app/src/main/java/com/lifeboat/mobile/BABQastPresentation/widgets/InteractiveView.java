package com.lifeboat.mobile.BABQastPresentation.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.lifeboat.mobile.BABQastPresentation.R;

/**
 * Created by Maarten Edgar on 10-11-2015.
 */
public class InteractiveView extends View {

    private int scrW = 100;
    private int scrH = 100;

    private int xpos = 0;
    private int ypos = 0;

    private Bitmap androidBMP = null;
    private int bmpSize = 10;
    private int halfBMPSize = 10;

    private Bitmap bgBMP = null;
    private Rect dstRect = new Rect(0,0,0,00);

    private final static int fudgeInt = 7;

    private Matrix rotator = new Matrix();
    private int rotationInt = 0;

    private boolean showBG = false;

    public InteractiveView(Context context) {
        super(context);
    }

    public InteractiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InteractiveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InteractiveView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void InitSize(int xs, int ys) {
        scrW = xs;
        scrH = ys;
        dstRect = new Rect(0,0,xs,ys);

        //size the puppet
        bmpSize = (int) ((float) xs / 15f);
        androidBMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.android_top), bmpSize, bmpSize, true);
        bgBMP = BitmapFactory.decodeResource(getResources(), R.drawable.bg_640);

        //set it's position
        halfBMPSize = (int) ((float) bmpSize * 0.5f);
        xpos = (int) ((float) xs * 0.5f) - halfBMPSize;
        ypos = (int) ((float) ys * 0.5f) - halfBMPSize;

        UpdatePosition(0,0);
    }

    public void SetShowBG(boolean show){
        showBG = show;
    }

    public void UpdatePosition(int x, int y) {
        xpos += (x * fudgeInt);
        ypos += (y * fudgeInt);

        //boundscheck
        if (xpos < 0) {
            xpos = 0;
        } else if (xpos + bmpSize > scrW) {
            xpos = scrW - bmpSize;
        }

        if (ypos < 0) {
            ypos = 0;
        } else if (ypos + bmpSize > scrH) {
            ypos = scrH - bmpSize;
        }

        if (x<0){
            if (y<0){
                //-1,-1
                rotationInt = 315;
            } else if (y>0){
                //-1,1
                rotationInt = 225;
            } else {
                //left
                rotationInt = 270;
            }
        } else if (x>0){
            if (y<0){
                //1,-1
                rotationInt = 45;
            } else if (y>0){
                //1,-1
                rotationInt = 135;
            } else {
                //right
                rotationInt = 90;
            }
        } else {
            //x = 0
            if (y<0){
                //up
                rotationInt = 0;
            } else if (y>0){
                //down
                rotationInt = 180;
            } else {
                //who cares
                rotationInt = 0;
            }
        }

        rotator.postRotate(rotationInt, halfBMPSize, halfBMPSize);
        rotator.postTranslate(xpos, ypos);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //bg
        if (showBG) {
            canvas.drawBitmap(bgBMP, null, dstRect, null);
        }

        //draw it
        canvas.drawBitmap(androidBMP, rotator, null);

        rotator.reset();
    }
}