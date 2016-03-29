package com.lifeboat.mobile.BABQastPresentation;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;
import com.google.android.gms.common.api.Status;
import com.lifeboat.mobile.BABQastPresentation.services.PresentationService;
import com.lifeboat.mobile.BABQastPresentation.widgets.CustomCastPresentation;
import com.lifeboat.mobile.BABQastPresentation.widgets.TouchView;

/**
 * Created by Maarten Edgar on 24-10-2015.
 */
public class LandscapeActivity extends FragmentActivity {

    private CastDevice mSelectedDevice;
    private MediaRouteButton mRBtn;
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MyMediaRouterCallback mMediaRouterCallback;

    private PresentationService mService;

    private CustomCastPresentation mPresentation;
    private boolean mReady;

    private CardView titleCard;

    private TouchView leftTV;
    private TouchView rightTV;
    private TouchView topTV;
    private TouchView bottomTV;

    private String REMOTE_DISPLAY_APP_ID = "8ABF532B";

    private static final String STATUS_READY = "com.lifeboat.mobile.BABQastPresentation.ready";
    private static final String STATUS_UPDATE = "com.lifeboat.mobile.BABQastPresentation.gatherupdates";

    private String type = "32";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(STATUS_READY)) {
                mReady = true;
                if (type.equals("16"))
                {
                    mService.mFrameMillis = 16;
                } else if (type.equals("bg")){
                    mService.mPresentation.SetShowBG(true);
                }
                mService.startRepeatingTask();
            }
            if(intent.getAction().equals(STATUS_UPDATE)) {
                if (mReady) {
                    mService.mPresentation.DetermindeDirection(
                            leftTV.isBeingTouched,
                            rightTV.isBeingTouched,
                            topTV.isBeingTouched,
                            bottomTV.isBeingTouched);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);
        getWindow().setExitTransition(new Slide());
        getWindow().setEnterTransition(new Slide());
        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.trans_move));
        getWindow().setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.trans_move));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape);

        type = getIntent().getStringExtra("type");

        titleCard = (CardView)findViewById(R.id.directory_cardview_title);
        titleCard.setTransitionName(getIntent().getStringExtra("heroname"));

        if (type.equals("16"))
        {
            ((TextView)titleCard.getChildAt(0)).setText("60 FPS");
        } else if (type.equals("bg")){
            ((TextView)titleCard.getChildAt(0)).setText("30 FPS w BG");
        } else {
            ((TextView)titleCard.getChildAt(0)).setText("30 FPS");
        }

        leftTV = (TouchView) findViewById(R.id.touchview_left);
        rightTV = (TouchView) findViewById(R.id.touchview_right);
        topTV = (TouchView) findViewById(R.id.touchview_top);
        bottomTV = (TouchView) findViewById(R.id.touchview_bottom);

        mMediaRouter = MediaRouter.getInstance(getApplicationContext());

        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(REMOTE_DISPLAY_APP_ID))
                .build();

        mMediaRouterCallback = new MyMediaRouterCallback();

        mRBtn = (MediaRouteButton) findViewById(R.id.castbutton);
        mRBtn.setRouteSelector(mMediaRouteSelector);

        mReady = false;
    }

    public void onListTitleCardClick(View v){
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landscape, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        // Be sure to call the super class.
        super.onResume();

        // Listen for changes to media routes.
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback, MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }

    @Override
    protected void onPause() {
        // Stop listening for changes to media routes.
        if (isFinishing()) {
            mMediaRouter.removeCallback(mMediaRouterCallback);
        }

        // Pause input/rendering.
        mReady = false;

        // Be sure to call the super class.
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback, MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
        IntentFilter filter = new IntentFilter(STATUS_READY);
        filter.addAction(STATUS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), filter);
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        mMediaRouter.removeCallback(mMediaRouterCallback);
        // Be sure to call the super class.
        super.onStop();

        // Dismiss the presentation when the activity is not visible.
        if (mPresentation != null) {
            //Log.i(TAG, "Dismissing presentation because the activity is no longer visible.");
            mPresentation.dismiss();
            mPresentation = null;
        }
    }

    private class MyMediaRouterCallback extends MediaRouter.Callback {

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
            String routeId = info.getId();
            mMediaRouter.selectRoute(info);
            mMediaRouter.updateSelectedRoute(mMediaRouteSelector);
            if (mSelectedDevice != null)
                InitPresentation();
            super.onRouteSelected(router, info);
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            mReady = false;
            mSelectedDevice = null;
            CastRemoteDisplayLocalService.stopService();
            if (mService != null) {
                mService.stopRepeatingTask();
                mService.dismissPresentation();
                mService.stopService();
            }
            super.onRouteUnselected(router, info);
        }

        @Override
        public void onRoutePresentationDisplayChanged(MediaRouter router, MediaRouter.RouteInfo route) {
            super.onRoutePresentationDisplayChanged(router, route);
        }
    }

    private void InitPresentation() {
        Intent intent = new Intent(LandscapeActivity.this, LandscapeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(LandscapeActivity.this, 1, intent, 0);

        CastRemoteDisplayLocalService.NotificationSettings settings =
                new CastRemoteDisplayLocalService.NotificationSettings.Builder()
                        .setNotificationPendingIntent(notificationPendingIntent).build();

        CastRemoteDisplayLocalService.startService(
                getApplicationContext(),
                PresentationService.class, REMOTE_DISPLAY_APP_ID,
                mSelectedDevice, settings,
                new CastRemoteDisplayLocalService.Callbacks() {
                    @Override
                    public void onRemoteDisplaySessionStarted(CastRemoteDisplayLocalService service) {
                        // initialize sender UI
                        mService = (PresentationService) service;
                    }

                    @Override
                    public void onRemoteDisplaySessionError(Status errorReason) {
                        //initError();
                        mSelectedDevice = null;
                        LandscapeActivity.this.finish();
                    }
                });
    }

    /**
     * Listens for when presentations are dismissed.
     */
    private final DialogInterface.OnDismissListener mOnDismissListener =
            new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (dialog == mPresentation) {
                        //Log.i(TAG, "Presentation was dismissed.");
                        mPresentation = null;
                    }
                }
            };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, objEvent);
    }

    @Override
    public void onBackPressed() {
        GoBack();
    }

    private void GoBack() {
        ((TextView)titleCard.getChildAt(0)).setText("returning ...");
        mReady = false;
        mSelectedDevice = null;
        CastRemoteDisplayLocalService.stopService();
        if (mService != null) {
            mService.stopRepeatingTask();
            mService.dismissPresentation();
            mService.stopService();
        }
        super.onBackPressed();
    }
}
