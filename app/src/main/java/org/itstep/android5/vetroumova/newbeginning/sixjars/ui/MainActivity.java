package org.itstep.android5.vetroumova.newbeginning.sixjars.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.adapters.RxRecyclerAdapter;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.CashFlowFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.HelpFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.JarInfoFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.RecyclerFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.SettingsFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.StatisticsFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.utils.DebugLogger;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "VOlga";
    private static long back_pressed;
    FrameLayout contentLayout;
    AHBottomNavigation bottomNavigation;
    AHBottomNavigationItem itemSettings;
    AHBottomNavigationItem itemStatistics;
    AHBottomNavigationItem itemTutorial;
    AHBottomNavigationItem itemAbout;
    //RecyclerView rxList;
    ArrayList<AnimationDrawable> jars;
    private RxRecyclerAdapter rxRecyclerAdapter = new RxRecyclerAdapter();
    private LinearLayoutManager layoutManager;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private List<String> mockItems = new ArrayList<>();
    /*private int[] colors = {R.color.colorPrimary,R.color.colorAccent,
            R.color.colorPrimaryLight,R.color.colorPrimaryDark};*/
    private FloatingActionButton fab;
    //Fragments
    private FragmentManager fragmentManager;
    private RecyclerFragment recyclerFragment;
    private JarInfoFragment jarInfoFragment;
    private SettingsFragment settingsFragment;
    private StatisticsFragment statisticsFragment;
    private HelpFragment helpFragment;
    private CashFlowFragment cashFlowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //WindowManager.LayoutParams params = getWindow().getAttributes();
        //params.rotationAnimation = WindowManager.LayoutParams.ROTATION_ANIMATION_ROTATE;
        //params.rotationAnimation = WindowManager.LayoutParams.ROTATION_ANIMATION_CROSSFADE;
        //getWindow().setAttributes(params);

        fragmentManager = getSupportFragmentManager();
        /*int count = fragmentManager.getBackStackEntryCount();
        Log.d(TAG,"In stack : " + count);*/

        if (savedInstanceState != null) {
            recyclerFragment = (RecyclerFragment) fragmentManager.getFragment(savedInstanceState,
                    "backstackfragment");
        } else {
            recyclerFragment = new RecyclerFragment();
        }
        jarInfoFragment = new JarInfoFragment();
        settingsFragment = new SettingsFragment();
        statisticsFragment = new StatisticsFragment();
        helpFragment = new HelpFragment();
        cashFlowFragment = new CashFlowFragment();


        fab = (FloatingActionButton) findViewById(R.id.fab);
        //add new item
        fab.setOnClickListener(view -> {

            //Snackbar.make(view, "Add to JARS", Snackbar.LENGTH_LONG)
            //        .setAction("Cashflow", null).show();
            Log.d(TAG, "FAB");
            fragmentManager.beginTransaction()
                    .replace(R.id.content_layout, cashFlowFragment)
                    /*.setCustomAnimations(android.R.animator
                    .fade_in, android.R.animator.fade_out)*/
                    //TODO check if needed
                    .hide(recyclerFragment)
                    .hide(settingsFragment)
                    .hide(statisticsFragment)
                    .hide(helpFragment)
                    .hide(jarInfoFragment)
                    .show(cashFlowFragment)
                    .addToBackStack("RECYCLER")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            fab.setVisibility(View.INVISIBLE);

            /*inflater = MainActivity.this.getLayoutInflater();
            View content = inflater.inflate(R.layout.edit_item, null);
            final EditText editID = (EditText) content.findViewById(R.id.id_edit);
            final EditText editName = (EditText) content.findViewById(R.id.name_edit);
            final EditText editThumbnail = (EditText) content.findViewById(R.id.thumbnail_edit);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(content)
                    .setTitle("Add jar")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Jar jar = new Jar();
                            jar.setJar_id(editID.getText().toString());
                            jar.setJar_name(editName.getText().toString());
                            jar.setJar_info(editThumbnail.getText().toString());

                            if (editID.getText() == null || editID.getText().toString().equals("")
                                    || editID.getText().toString().equals(" ")) {
                                Toast.makeText(MainActivity.this, "Entry not saved, missing ID",
                                Toast.LENGTH_SHORT).show();
                            } else {
                                // Persist your data easily
                                realm.beginTransaction();
                                realm.copyToRealm(jar);
                                realm.commitTransaction();
                                realmJarsAdapter.notifyDataSetChanged();
                                // scroll the recycler view to bottom
                                realmRecycler.scrollToPosition(RealmManager.getInstance().getAllJars().size() - 1);
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();*/
        });

        //set toolbar
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        contentLayout = (FrameLayout) findViewById(R.id.content_layout);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        itemSettings = new AHBottomNavigationItem("Settings", R.drawable.ic_settings_white_24dp);
        itemStatistics = new AHBottomNavigationItem("Statistics", R.drawable.ic_statistics_chart_white_24dp);
        itemTutorial = new AHBottomNavigationItem("Tutorial", R.drawable.ic_help_white_24dp);
        itemAbout = new AHBottomNavigationItem("JARS", R.drawable.jar);

        bottomNavigation.addItem(itemAbout);
        bottomNavigation.addItem(itemSettings);
        bottomNavigation.addItem(itemStatistics);
        bottomNavigation.addItem(itemTutorial);

        //bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorBottomNavigationPrimary));
        //bottomNavigation.setAccentColor(R.color.colorBottomNavigationPrimaryDark);
        //bottomNavigation.setInactiveColor(R.color.colorBottomNavigationActiveColored);

        //icon and text colors
        /*bottomNavigation.setColoredModeColors(getResources().getColor(R.color.colorBottomNavigationPrimaryDark),
                getResources().getColor(R.color.colorPrimaryLight));*/

        // Disable the translation inside the CoordinatorLayout
        //bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setBehaviorTranslationEnabled(true);

        // Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);
        //to color an icon font
        bottomNavigation.setForceTint(true);

        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                int color = R.color.colorDivider;
                switch (position) {
                    case 0: {
                        fragmentManager.beginTransaction()
                                //TODO CHECK FOR DUPLICATES
                                .replace(R.id.content_layout, recyclerFragment, "RECYCLER")
                                .show(recyclerFragment)
                                //TODO normal checking of fragments enabled & visible
                                .hide(jarInfoFragment)
                                .hide(settingsFragment)
                                .hide(statisticsFragment)
                                .hide(helpFragment)
                                .hide(cashFlowFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        fab.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 1: {
                        /*if(fragmentManager.findFragmentById(R.id.content_layout)
                                instanceof JarInfoFragment) {
                            //do not add to backstack
                            fragmentManager.beginTransaction()
                                    .replace(R.id.content_layout, settingsFragment)
                                    .hide(recyclerFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();
                        }else {*/
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_layout, settingsFragment)
                                .show(settingsFragment)
                                .hide(recyclerFragment)
                                .hide(statisticsFragment)
                                .hide(helpFragment)
                                .hide(cashFlowFragment)
                                .hide(jarInfoFragment)
                                //.addToBackStack("RECYCLER")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        /*}*/
                        fab.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 2: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_layout, statisticsFragment)
                                .show(statisticsFragment)
                                .hide(recyclerFragment)
                                .hide(settingsFragment)
                                .hide(helpFragment)
                                .hide(cashFlowFragment)
                                .hide(jarInfoFragment)
                                //.addToBackStack("RECYCLER")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        fab.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 3: {
                        fragmentManager.beginTransaction()
                                //.replace(R.id.content_layout, helpFragment)
                                .replace(R.id.content_layout, helpFragment)
                                .show(helpFragment)
                                .hide(recyclerFragment)
                                .hide(settingsFragment)
                                .hide(statisticsFragment)
                                .hide(cashFlowFragment)
                                .hide(jarInfoFragment)
                                //.addToBackStack("RECYCLER")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        fab.setVisibility(View.INVISIBLE);
                        break;
                    }
                    default: {
                        break;
                    }
                }
                return true;
            }
        });

        if (savedInstanceState == null && fragmentManager.getBackStackEntryCount() == 0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_layout, recyclerFragment, "RECYCLER")
                    //TODO normal checking of fragments enabled & visible
                    .hide(jarInfoFragment)
                    .hide(settingsFragment)
                    .hide(statisticsFragment)
                    .hide(helpFragment)
                    .hide(cashFlowFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
        /*count = fragmentManager.getBackStackEntryCount();
        Log.d(TAG,"In stack after recyc trans: " + count);*/

        Subscription jarInRecyclerSubscription = recyclerFragment.getJar()
                .subscribe(jar -> {
                            DebugLogger.log("opening a JAR info: " + jar.getJar_id());
                            Toast.makeText(getApplicationContext(), "opening a JAR info: " + jar.getJar_id(),
                                    Toast.LENGTH_SHORT).show();
                            jarInfoFragment.setJarID(jar.getJar_id());
                            Toast.makeText(getApplicationContext(), jarInfoFragment.toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, jarInfoFragment.toString());
                            fragmentManager.beginTransaction()
                                    .hide(recyclerFragment)
                                    .hide(settingsFragment)
                                    .hide(statisticsFragment)
                                    .hide(helpFragment)
                                    .hide(cashFlowFragment)
                                    .show(jarInfoFragment)
                                    .add(R.id.content_layout, jarInfoFragment)
                                    .addToBackStack("JarInfo")
                                    .commit();
                        },
                        error -> DebugLogger.log(error.getMessage())
                );

        Subscription cashClickSubscription = jarInfoFragment.getCashflowItem()
                .subscribe(cash -> {
                            DebugLogger.log("cash clicked : " + cash.getId() + ", " + cash.getSum());
                            Log.d(TAG, "cash clicked : " + cash.getId() + ", " + cash.getSum());
                            Toast.makeText(getApplicationContext(),
                                    "cash clicked : " + cash.getId() + ", " + cash.getSum(),
                                    Toast.LENGTH_SHORT).show();
                        },
                        error -> DebugLogger.log(error.getMessage())
                );

        Subscription cashDeleteSubscription = jarInfoFragment.refreshRecyclerArterDeleteItem()
                .subscribe(deletedCashID -> {
                            DebugLogger.log("refreshing mainRecycler after deleted cash : "
                                    + deletedCashID);
                            Log.d(TAG, "refreshing mainRecycler after deleted cash : "
                                    + deletedCashID);
                            Toast.makeText(getApplicationContext(),
                                    "refreshing mainRecycler after deleted cash : " + deletedCashID,
                                    Toast.LENGTH_SHORT).show();
                            //rxRecyclerAdapter.notifyDataSetChanged();
                            recyclerFragment.refreshRecycler();
                        },
                        error -> DebugLogger.log(error.getMessage())
                );

        subscriptions.add(jarInRecyclerSubscription);
        subscriptions.add(cashClickSubscription);
        subscriptions.add(cashDeleteSubscription);
    }


        /*rxList = (RecyclerView) findViewById(R.id.rxRecycler);
        for (int i = 0; i < 20; i++) {
            mockItems.add(String.valueOf(i));
        }
        rxList.setHasFixedSize(true);
        rxList.setAdapter(rxRecyclerAdapter);
        layoutManager = new LinearLayoutManager(this);
        rxList.setLayoutManager(layoutManager);

        rxRecyclerAdapter.addAll(mockItems);

        Observable<Void> pageDetector = Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(final Subscriber<? super Void> subscriber) {
                rxList.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    int pastVisibleItems, visibleItemCount, totalItemCount;
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                        if ((visibleItemCount+pastVisibleItems) >= totalItemCount) {
                            subscriber.onNext(null);
                        }
                    }
                });
            }
        }).debounce(400, TimeUnit.MILLISECONDS);*/

        /*bindActivity(this, pageDetector);
        Observable<List<String>> listItemObservable = RepresentativeApi.paginatedThings(pageDetector);
        bindActivity(this, listItemObservable);
        subscriptions.add(listItemObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {
                //Timber.d("completed");
                Log.d(TAG,"completed");
            }

            @Override
            public void onError(Throwable e) {
                //Timber.e("error: " + e.getMessage());
                Log.d(TAG,"error: " + e.getMessage());
            }

            @Override
            public void onNext(List<String> strings) {
                rxRecyclerAdapter.addAll(strings);
            }
        }));
    }*/
        /*imageNEC = (ImageView) findViewById(R.id.imageViewNEC);
        imageFFA = (ImageView) findViewById(R.id.imageViewFFA);
        imageEDU = (ImageView) findViewById(R.id.imageViewEDU);
        imageLTSS = (ImageView) findViewById(R.id.imageViewLTSS);
        imagePLAY = (ImageView) findViewById(R.id.imageViewPLAY);
        imageGIVE = (ImageView) findViewById(R.id.imageViewGIVE);

        //imageNEC.setBackgroundResource(R.drawable.jar_anim);
        imageFFA.setBackgroundResource(R.drawable.jar_anim);
        imageEDU.setBackgroundResource(R.drawable.jar_anim);
        imageLTSS.setBackgroundResource(R.drawable.jar_anim);
        imagePLAY.setBackgroundResource(R.drawable.jar_anim);
        imageGIVE.setBackgroundResource(R.drawable.jar_anim);

        //AnimationDrawable animationNEC = (AnimationDrawable) imageNEC.getBackground();
        AnimationDrawable animationFFA = (AnimationDrawable) imageFFA.getBackground();
        AnimationDrawable animationEDU = (AnimationDrawable) imageEDU.getBackground();
        AnimationDrawable animationLTSS = (AnimationDrawable) imageLTSS.getBackground();
        AnimationDrawable animationPLAY = (AnimationDrawable) imagePLAY.getBackground();
        AnimationDrawable animationGIVE = (AnimationDrawable) imageGIVE.getBackground();

        jars = new ArrayList<>();
        //jars.add(animationNEC);
        jars.add(animationFFA);
        jars.add(animationEDU);
        jars.add(animationLTSS);
        jars.add(animationPLAY);
        jars.add(animationGIVE);

        //ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(6);
        // Execute some code after 2 seconds have passed
        *//*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                my_button.setBackgroundResource(R.drawable.defaultcard);
            }
        }, 2000);*//*

        //Управлять объектом AnimationDrawable можно через методы start() и stop().
        *//*animationNEC.start();
        animationFFA.start();
        animationEDU.start();
        animationLTSS.start();
        animationPLAY.start();
        animationGIVE.start();*//*

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
    }*/

    public void refreshRecycler() {
        rxRecyclerAdapter.notifyDataSetChanged();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //TODO work with fragments
        outState.putBoolean("IsSavedInst", true);
        fragmentManager.putFragment(outState, "backstackfragment", fragmentManager
                .findFragmentByTag("RECYCLER"));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 0) {

            if (back_pressed + 2000 > System.currentTimeMillis())
                super.onBackPressed();
            else
                Toast.makeText(getBaseContext(), "Нажмите еще раз чтобы выйти",
                        Toast.LENGTH_SHORT).show();

            back_pressed = System.currentTimeMillis();
            return;
        } else {
            // how to clear backstack to recycler
        }

        try {
            super.onBackPressed();
        } catch (IllegalStateException e) {
            Log.d(TAG, e.getMessage());
            //TODO check
            fragmentManager.popBackStackImmediate();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO CHECK
        Realm.getDefaultInstance().close();
        subscriptions.clear();
    }
}
