package org.itstep.android5.vetroumova.newbeginning.sixjars.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
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
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.AddCashFlowFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.CashInfoFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.HelpFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.JarInfoFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.RecyclerFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.SettingsFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.SpendFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments.StatisticsFragment;
import org.itstep.android5.vetroumova.newbeginning.sixjars.utils.DebugLogger;

import java.util.ArrayList;
import java.util.List;

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
    //ArrayList<AnimationDrawable> jars;
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
    private AddCashFlowFragment addCashFlowFragment;
    private SpendFragment spendFragment;
    private CashInfoFragment cashInfoFragment;

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
            if (fragmentManager.getFragment(savedInstanceState, "backstackfragment")
                    instanceof RecyclerFragment) {
                recyclerFragment = (RecyclerFragment) fragmentManager.getFragment(savedInstanceState,
                        "backstackfragment");
            } else {
                recyclerFragment = new RecyclerFragment();
            }

        } else {
            recyclerFragment = new RecyclerFragment();
        }
        jarInfoFragment = new JarInfoFragment();
        settingsFragment = new SettingsFragment();
        statisticsFragment = new StatisticsFragment();
        helpFragment = new HelpFragment();
        addCashFlowFragment = new AddCashFlowFragment();
        spendFragment = new SpendFragment();
        cashInfoFragment = new CashInfoFragment();


        fab = (FloatingActionButton) findViewById(R.id.fab);
        //add new item
        fab.setOnClickListener(view -> {

            //Snackbar.make(view, "Add to JARS", Snackbar.LENGTH_LONG)
            //        .setAction("Cashflow", null).show();
            Log.d(TAG, "FAB");
            fragmentManager.beginTransaction()
                    .replace(R.id.content_layout, addCashFlowFragment)
                    /*.setCustomAnimations(android.R.animator
                    .fade_in, android.R.animator.fade_out)*/
                    .addToBackStack("addCash")
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contentLayout = (FrameLayout) findViewById(R.id.content_layout);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        itemSettings = new AHBottomNavigationItem(getString(R.string.action_settings),
                R.drawable.ic_settings_white_24dp);
        itemStatistics = new AHBottomNavigationItem(getString(R.string.statistics_text),
                R.drawable.ic_statistics_chart_white_24dp);
        itemTutorial = new AHBottomNavigationItem(getString(R.string.tutorial_text),
                R.drawable.ic_help_white_24dp);
        itemAbout = new AHBottomNavigationItem(getString(R.string.jars_text),
                R.drawable.water_to_jar_empty);

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
                //fragmentManager.popBackStackImmediate();
                switch (position) {
                    case 0: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_layout, recyclerFragment, "RECYCLER")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        fab.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 1: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_layout, settingsFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        /*}*/
                        fab.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 2: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_layout, statisticsFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        fab.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 3: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_layout, helpFragment)
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
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }

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
                                    .replace(R.id.content_layout, jarInfoFragment)
                                    .addToBackStack("JarInfo")
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
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

                            cashInfoFragment = CashInfoFragment.newInstance(cash.getId());
                            //cashInfoFragment.setCashflowID(cash.getId());
                            fragmentManager.beginTransaction()
                                    .replace(R.id.content_layout, cashInfoFragment)
                                    .addToBackStack("CashEdit")
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();
                        },
                        error -> DebugLogger.log(error.getMessage())
                );

        Subscription finishEditCashSubscription = cashInfoFragment.isFinishEdit()
                .subscribe(isEdited -> {
                    DebugLogger.log("cash was edited : " + isEdited);
                    Log.d(TAG, "cash was edited : " + isEdited);
                    Toast.makeText(getApplicationContext(), "cash was edited : " + isEdited,
                            Toast.LENGTH_SHORT).show();

                    fragmentManager.popBackStackImmediate();
                    recyclerFragment.refreshRecycler();
                    jarInfoFragment.refreshData();
                    jarInfoFragment.refreshRecyclerArterDeleteItem();
                });

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

        Subscription spendCashInJar = jarInfoFragment.spendCashInJar()
                .subscribe(jar -> {
                    DebugLogger.log("open spendcash fragment : " + jar.getJar_id());
                    Log.d(TAG, "open spendcash fragment : " + jar.getJar_id());
                    Toast.makeText(getApplicationContext(), "open spendcash fragment : " + jar.getJar_id(),
                            Toast.LENGTH_SHORT).show();
                    spendFragment.setJarId(jar.getJar_id());
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_layout, spendFragment, "spend")
                            .addToBackStack("Spend")
                            .commit();
                });

        Subscription finishSpendCashSubscription = spendFragment.finishSpend()
                .subscribe(isSpend -> {
                    DebugLogger.log("spend cash and close : " + isSpend);
                    Log.d(TAG, "spend cash and close : " + isSpend);
                    Toast.makeText(getApplicationContext(), "spend cash and close : " + isSpend,
                            Toast.LENGTH_SHORT).show();
                    fragmentManager.popBackStackImmediate();
                    fab.show();
                    //rxRecyclerAdapter.notifyDataSetChanged();
                    recyclerFragment.refreshRecycler();
                    jarInfoFragment.refreshData();
                    jarInfoFragment.refreshRecyclerArterDeleteItem();
                });

        subscriptions.add(jarInRecyclerSubscription);
        subscriptions.add(cashClickSubscription);
        subscriptions.add(finishEditCashSubscription);
        subscriptions.add(cashDeleteSubscription);
        subscriptions.add(spendCashInJar);
        subscriptions.add(finishSpendCashSubscription);
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

    public void refreshRxRecycler() {
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
        //nullpointer
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
                Toast.makeText(getBaseContext(), R.string.click_to_exit_text,
                        Toast.LENGTH_SHORT).show();

            back_pressed = System.currentTimeMillis();
            return;
        } else {
            // TODO how to clear backstack to recycler
            try {
                fragmentManager.popBackStackImmediate();
            } catch (NullPointerException e) {
                Log.d("VOlga", "No backstack onBackPressed");
            }
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO CHECK
        //Realm.getDefaultInstance().close();
        subscriptions.clear();
    }
}
