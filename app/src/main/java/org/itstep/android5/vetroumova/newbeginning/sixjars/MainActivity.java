package org.itstep.android5.vetroumova.newbeginning.sixjars;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Volya";
    //NEC,FFA,EDU,LTSS,PLAY,GIVE
    ImageView imageNEC;
    ImageView imageFFA;
    ImageView imageEDU;
    ImageView imageLTSS;
    ImageView imagePLAY;
    ImageView imageGIVE;

    ArrayList<AnimationDrawable> jars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imageNEC = (ImageView) findViewById(R.id.imageViewNEC);
        imageFFA = (ImageView) findViewById(R.id.imageViewFFA);
        imageEDU = (ImageView) findViewById(R.id.imageViewEDU);
        imageLTSS = (ImageView) findViewById(R.id.imageViewLTSS);
        imagePLAY = (ImageView) findViewById(R.id.imageViewPLAY);
        imageGIVE = (ImageView) findViewById(R.id.imageViewGIVE);

        imageNEC.setBackgroundResource(R.drawable.jar_anim);
        imageFFA.setBackgroundResource(R.drawable.jar_anim);
        imageEDU.setBackgroundResource(R.drawable.jar_anim);
        imageLTSS.setBackgroundResource(R.drawable.jar_anim);
        imagePLAY.setBackgroundResource(R.drawable.jar_anim);
        imageGIVE.setBackgroundResource(R.drawable.jar_anim);

        AnimationDrawable animationNEC = (AnimationDrawable) imageNEC.getBackground();
        AnimationDrawable animationFFA = (AnimationDrawable) imageFFA.getBackground();
        AnimationDrawable animationEDU = (AnimationDrawable) imageEDU.getBackground();
        AnimationDrawable animationLTSS = (AnimationDrawable) imageLTSS.getBackground();
        AnimationDrawable animationPLAY = (AnimationDrawable) imagePLAY.getBackground();
        AnimationDrawable animationGIVE = (AnimationDrawable) imageGIVE.getBackground();

        jars = new ArrayList<>();
        jars.add(animationNEC);
        jars.add(animationFFA);
        jars.add(animationEDU);
        jars.add(animationLTSS);
        jars.add(animationPLAY);
        jars.add(animationGIVE);

        //ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(6);
        // Execute some code after 2 seconds have passed
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                my_button.setBackgroundResource(R.drawable.defaultcard);
            }
        }, 2000);*/

        //Управлять объектом AnimationDrawable можно через методы start() и stop().
        /*animationNEC.start();
        animationFFA.start();
        animationEDU.start();
        animationLTSS.start();
        animationPLAY.start();
        animationGIVE.start();*/

        CountDownTimer countDownTimer = new CountDownTimer(3500, 500) {
            int i = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                // do something after 1s
                if (i < jars.size()) {
                    jars.get(i).start();
                    i++;
                    Log.d(TAG,"Tick " + i + " animation");
                }
            }
            @Override
            public void onFinish() {
                // do something end times 5s
                Log.d(TAG,"Finish animation");
            }
        }.start();
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
