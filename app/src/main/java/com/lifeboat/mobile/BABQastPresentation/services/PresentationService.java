package com.lifeboat.mobile.BABQastPresentation.services;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Display;

import com.google.android.gms.cast.CastRemoteDisplayLocalService;
import com.lifeboat.mobile.BABQastPresentation.widgets.CustomCastPresentation;

/**
 * Created by Maarten Edgar on 24-10-2015.
 */
public class PresentationService extends CastRemoteDisplayLocalService {

    public CustomCastPresentation mPresentation;

    public int mFrameMillis = 32;
    private Handler mHandler;

    private static final String STATUS_READY = "com.lifeboat.mobile.BABQastPresentation.ready";
    private static final String STATUS_UPDATE = "com.lifeboat.mobile.BABQastPresentation.gatherupdates";

    @Override
    public void onCreatePresentation(Display display) {
        createPresentation(display);

        mHandler = new Handler();
    }

    @Override
    public void onDismissPresentation() {
        dismissPresentation();
    }

    public void dismissPresentation() {
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
        }
    }

    private void createPresentation(Display display) {
        dismissPresentation();
        mPresentation = new CustomCastPresentation(this, display, 0);
        mPresentation.show();

        Intent intent = new Intent(STATUS_READY);
        //just to show we can add extra data:
        intent.putExtra("message", "data");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void startRepeatingTask() {
        mStatusChecker.run();
    }

    public void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            Update(); //this function can change value of mInterval.
            mHandler.postDelayed(mStatusChecker, mFrameMillis);
        }
    };

    private void Update(){
        Intent intent = new Intent(STATUS_UPDATE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
