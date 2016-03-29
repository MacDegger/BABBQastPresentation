package com.lifeboat.mobile.BABQastPresentation;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Maarten Edgar on 24-10-2015.
 */
public class MainActivity extends AppCompatActivity {

    private CardView mStartCardView32;
    private TextView mStartCardTextView32;

    private CardView mStartCardView16;
    private TextView mStartCardTextView16;

    private CardView mStartCardViewBG;
    private TextView mStartCardTextViewBG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);
        getWindow().setSharedElementExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.trans_move));
        getWindow().setSharedElementReenterTransition(TransitionInflater.from(this).inflateTransition(R.transition.trans_move));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartCardView32 = (CardView)findViewById(R.id.activity_main_startcard_32);
        mStartCardView32.setTransitionName("titlecardone");
        mStartCardTextView32 = (TextView)findViewById(R.id.activity_main_startcard_textview_32);

        mStartCardView16 = (CardView)findViewById(R.id.activity_main_startcard_16);
        mStartCardView16.setTransitionName("titlecardone");
        mStartCardTextView16 = (TextView)findViewById(R.id.activity_main_startcard_textview_16);

        mStartCardViewBG = (CardView)findViewById(R.id.activity_main_startcard_bg);
        mStartCardViewBG.setTransitionName("titlecardone");
        mStartCardTextViewBG = (TextView)findViewById(R.id.activity_main_startcard_textview_bg);
    }

    public void onCardClick(View v){
        switch (v.getId()){
            case R.id.activity_main_startcard_32:
                Intent intent = new Intent(this, LandscapeActivity.class);
                intent.putExtra("heroname", "titlecardone");
                intent.putExtra("herotext", mStartCardTextView32.getText());
                intent.putExtra("type", "32");
                ((ViewGroup) mStartCardView32.getParent()).setTransitionGroup(true);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, mStartCardView32, "titlecardone");
                startActivity(intent, options.toBundle());
                break;
            case R.id.activity_main_startcard_16:
                Intent intent2 = new Intent(this, LandscapeActivity.class);
                intent2.putExtra("heroname", "titlecardone");
                intent2.putExtra("herotext", mStartCardTextView16.getText());
                intent2.putExtra("type", "16");
                ((ViewGroup) mStartCardView16.getParent()).setTransitionGroup(true);
                ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(this, mStartCardView16, "titlecardone");
                startActivity(intent2, options2.toBundle());
                break;
            case R.id.activity_main_startcard_bg:
                Intent intent3 = new Intent(this, LandscapeActivity.class);
                intent3.putExtra("heroname", "titlecardone");
                intent3.putExtra("herotext", mStartCardTextViewBG.getText());
                intent3.putExtra("type", "bg");
                ((ViewGroup) mStartCardTextViewBG.getParent()).setTransitionGroup(true);
                ActivityOptions options3 = ActivityOptions.makeSceneTransitionAnimation(this, mStartCardTextViewBG, "titlecardone");
                startActivity(intent3, options3.toBundle());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
