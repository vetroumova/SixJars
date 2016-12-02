package com.vetroumova.sixjars.ui;

import android.Manifest;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiLink;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

import com.vetroumova.sixjars.R;
import com.vetroumova.sixjars.database.RealmManager;
import com.vetroumova.sixjars.ui.adapters.RxRecyclerAdapter;
import com.vetroumova.sixjars.ui.fragments.AddCashFragment;
import com.vetroumova.sixjars.ui.fragments.CashInfoFragment;
import com.vetroumova.sixjars.ui.fragments.HelpFragment;
import com.vetroumova.sixjars.ui.fragments.JarInfoFragment;
import com.vetroumova.sixjars.ui.fragments.RecyclerFragment;
import com.vetroumova.sixjars.ui.fragments.SettingsFragment;
import com.vetroumova.sixjars.ui.fragments.ShareFragment;
import com.vetroumova.sixjars.ui.fragments.SpendFragment;
import com.vetroumova.sixjars.ui.fragments.StatisticsFragment;
import com.vetroumova.sixjars.ui.widget.JarsWidget;
import com.vetroumova.sixjars.utils.DebugLogger;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;


public class MainActivity extends AppCompatActivity {
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 810;
    private static final int GOOGLEPLUS_REQUEST_CODE = 1001;
    private static final String TAG = "VOlga";
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static long back_pressed;
    FrameLayout contentLayout;
    AHBottomNavigation bottomNavigation;
    AHBottomNavigationItem itemSettings;
    AHBottomNavigationItem itemStatistics;
    AHBottomNavigationItem itemTutorial;
    AHBottomNavigationItem itemAbout;
    AlphaAnimation animationDisapear;
    AlphaAnimation animationGetVisible;
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
    private AddCashFragment addCashFragment;
    private SpendFragment spendFragment;
    private CashInfoFragment cashInfoFragment;
    private ShareFragment shareFragment;
    private int menuItem = 0;

    public static Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            /*Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);*/
            Bitmap bitmap = Bitmap.createBitmap(300, 400, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //todo check if needed
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //WindowManager.LayoutParams params = getWindow().getAttributes();
        //params.rotationAnimation = WindowManager.LayoutParams.ROTATION_ANIMATION_ROTATE;
        //params.rotationAnimation = WindowManager.LayoutParams.ROTATION_ANIMATION_CROSSFADE;
        //getWindow().setAttributes(params);

        fragmentManager = getSupportFragmentManager();
        /*int count = fragmentManager.getBackStackEntryCount();
        Log.d(TAG,"In stack : " + count);*//*

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
        }*/
        recyclerFragment = new RecyclerFragment();
        jarInfoFragment = new JarInfoFragment();
        settingsFragment = new SettingsFragment();
        statisticsFragment = new StatisticsFragment();
        helpFragment = new HelpFragment();
        addCashFragment = new AddCashFragment();
        spendFragment = new SpendFragment();
        cashInfoFragment = new CashInfoFragment();


        fab = (FloatingActionButton) findViewById(R.id.fab);
        animationDisapear = new AlphaAnimation(1, 0);
        animationGetVisible = new AlphaAnimation(0, 1);
        animationDisapear.setDuration(500);
        animationDisapear.setStartOffset(100);
        animationDisapear.setFillAfter(true);
        animationGetVisible.setDuration(500);
        animationGetVisible.setStartOffset(200);
        animationGetVisible.setFillAfter(true);
        //fab.setAnimation(animation1);
        //add new income
        fab.setOnClickListener(view -> {

            //Snackbar.make(view, "Add to JARS", Snackbar.LENGTH_LONG)
            //        .setAction("Cashflow", null).show();
            fab.startAnimation(animationDisapear);
            Log.d(TAG, "FAB");
            fragmentManager.beginTransaction()
                    .replace(R.id.content_layout, addCashFragment)
                    /*.setCustomAnimations(android.R.animator
                    .fade_in, android.R.animator.fade_out)*/
                    .addToBackStack("addCash")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            //fab.setVisibility(View.INVISIBLE);
            fab.hide();
        });

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
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
                while (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStackImmediate();
                }
                Log.d("VOlga", "on Tab - backstack size after immediate "
                        + fragmentManager.getBackStackEntryCount());
                switch (position) {
                    case 0: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_layout, recyclerFragment, "RECYCLER")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        /*if (fab.getVisibility() == View.INVISIBLE) {
                            fab.setVisibility(View.VISIBLE);
                            fab.startAnimation(animationGetVisible);
                        }*/
                        if (!fab.isShown()) {
                            fab.show();
                            fab.startAnimation(animationGetVisible);
                        }
                        break;
                    }
                    case 1: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_layout, settingsFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        if (!fab.isShown()) {
                            fab.show();
                            fab.startAnimation(animationGetVisible);
                        }
                        break;
                    }
                    case 2: {
                        statisticsFragment = StatisticsFragment.newInstance();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_layout, statisticsFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        if (!fab.isShown()) {
                            fab.show();
                            fab.startAnimation(animationGetVisible);
                        }
                        break;
                    }
                    case 3: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_layout, helpFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        if (!fab.isShown()) {
                            fab.show();
                            fab.startAnimation(animationGetVisible);
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
                return true;
            }
        });

        /*Realm realm = RealmManager.with(this).getRealm();
        Log.d(TAG,"Path to db: " + realm.getPath() + " schema : " + realm.getSchema());*/

        //if (savedInstanceState == null && fragmentManager.getBackStackEntryCount() == 0) {
        if (fragmentManager.getBackStackEntryCount() == 0
                && !(fragmentManager.findFragmentById(R.id.content_layout) instanceof RecyclerFragment)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_layout, recyclerFragment, "RECYCLER")
                    //.addToBackStack("Recycler")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
        //updateAllWidgets();

        Subscription jarInRecyclerSubscription = recyclerFragment.getJar()
                .subscribe(jar -> {
                            DebugLogger.log("opening a JAR info: " + jar.getJar_id());
                            // Toast.makeText(getApplicationContext(), "opening a JAR info: " + jar.getJar_id(),
                            //Toast.LENGTH_SHORT).show();
                            jarInfoFragment.setJarID(jar.getJar_id());
                            //Toast.makeText(getApplicationContext(), jarInfoFragment.toString(),
                            //Toast.LENGTH_SHORT).show();
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
                            //Toast.makeText(getApplicationContext(),
                            //"cash clicked : " + cash.getId() + ", " + cash.getSum(),
                            //Toast.LENGTH_SHORT).show();

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
                    //Toast.makeText(getApplicationContext(), "cash was edited : " + isEdited,
                    //Toast.LENGTH_SHORT).show();

                    //fragmentManager.popBackStackImmediate();
                    recyclerFragment.refreshRecycler();
                    jarInfoFragment.refreshData();
                    jarInfoFragment.refreshRecyclerArterDeleteItem();
                    updateAllWidgets();
                });

        Subscription cashDeleteSubscription = jarInfoFragment.refreshRecyclerArterDeleteItem()
                .subscribe(deletedCashID -> {
                            DebugLogger.log("refreshing mainRecycler after deleted cash : "
                                    + deletedCashID);
                            Log.d(TAG, "refreshing mainRecycler after deleted cash : "
                                    + deletedCashID);
                            //Toast.makeText(getApplicationContext(),
                            //"refreshing mainRecycler after deleted cash : " + deletedCashID,
                            //Toast.LENGTH_SHORT).show();
                            //rxRecyclerAdapter.notifyDataSetChanged();
                            recyclerFragment.refreshRecycler();
                            updateAllWidgets();
                        },
                        error -> DebugLogger.log(error.getMessage())
                );

        Subscription spendCashInJar = jarInfoFragment.spendCashInJar()
                .subscribe(jar -> {
                    DebugLogger.log("open spendcash fragment : " + jar.getJar_id());
                    Log.d(TAG, "open spendcash fragment : " + jar.getJar_id());
                    //Toast.makeText(getApplicationContext(), "open spendcash fragment : " + jar.getJar_id(),
                    //Toast.LENGTH_SHORT).show();
                    spendFragment = SpendFragment.newInstance(jar.getJar_id());
                    //spendFragment.setJarId(jar.getJar_id());
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_layout, spendFragment, "spend")
                            .addToBackStack("Spend")
                            .commit();
                });

        Subscription finishSpendCashSubscription = spendFragment.finishSpend()
                .subscribe(isSpend -> {
                    DebugLogger.log("spend cash and close : " + isSpend);
                    Log.d(TAG, "spend cash and close : " + isSpend);
                    //Toast.makeText(getApplicationContext(), "spend cash and close : " + isSpend,
                    //Toast.LENGTH_SHORT).show();
                    //TODO CHECK if working and close the spend
                    fragmentManager.popBackStackImmediate();
                    fab.show();
                    //rxRecyclerAdapter.notifyDataSetChanged();
                    recyclerFragment.refreshRecycler();
                    jarInfoFragment.refreshData();
                    jarInfoFragment.refreshRecyclerArterDeleteItem();
                    updateAllWidgets();
                });

        subscriptions.add(jarInRecyclerSubscription);
        subscriptions.add(cashClickSubscription);
        subscriptions.add(finishEditCashSubscription);
        subscriptions.add(cashDeleteSubscription);
        subscriptions.add(spendCashInJar);
        subscriptions.add(finishSpendCashSubscription);
    }

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
        if (id == R.id.action_share) {

            shareFragment = ShareFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_layout, shareFragment)
                    .addToBackStack("Share")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

            /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.share_title_text))
                    .setMessage(getString(R.string.share_message_text))
                    .setIcon(R.mipmap.ic_launcher)
                    //.setCancelable(false)

                    .setNegativeButton(getString(R.string.share_cancel_text),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();*/

            return true;
        } else if (id == R.id.action_save_base) {
            menuItem = 1;
            checkStoragePermissions(this);
            return true;
        } else if (id == R.id.action_restore_base) {
            menuItem = 2;
            checkStoragePermissions(this);
            return true;
        } else if (id == R.id.action_send_base) {
            menuItem = 3;
            checkStoragePermissions(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            if (menuItem == 1) {
                RealmManager.with(this).backup(this);
            } else if (menuItem == 2) {
                startActivity(new Intent(MainActivity.this, RestoreActivity.class)
                        .addFlags(FLAG_ACTIVITY_NO_HISTORY));
            } else if (menuItem == 3) {
                RealmManager.with(this).exportDatabase(this);
            }
            menuItem = 0;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (menuItem == 1) {
                    RealmManager.with(this).backup(this);
                } else if (menuItem == 2) {
                    startActivity(new Intent(MainActivity.this, RestoreActivity.class)
                            .addFlags(FLAG_ACTIVITY_NO_HISTORY));
                } else if (menuItem == 3) {
                    RealmManager.with(this).exportDatabase(this);
                }
                menuItem = 0;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onShareClick(View view) {
        /*Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText(text)
                .setContentUrl(Uri.parse(link))
                .getIntent();
        startActivityForResult(shareIntent, GOOGLEPLUS_REQUEST_CODE);*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == GOOGLEPLUS_REQUEST_CODE) && (resultCode == -1)) {
            //Do something if success
        }
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                uploadPhotoToVk(res.userId);
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getBaseContext(), getString(R.string.vk_cant_authorize_text),
                        Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void uploadPhotoToVk(String userID) {
        //Bitmap photo = ((BitmapDrawable) ContextCompat.getDrawable(this,R.drawable.logo_on_bg_64)).getBitmap();
        Bitmap photo = getBitmapFromDrawable(getApplicationContext(), R.mipmap.ic_launcher);
        Log.w("VK photo", "owner id " + userID);
        Log.w("VK photo", "photo " + photo);
        VKRequest request = VKApi.uploadWallPhotoRequest(new VKUploadImage(photo, VKImageParameters.pngImage()),
                Integer.parseInt(userID), 0);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
                makeVKWallPost(new VKAttachments(photoModel));
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
                Toast.makeText(getBaseContext(), getString(R.string.vk_cant_upload_text), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.w("VK photo", "error " + error.toString());
                Toast.makeText(getBaseContext(), getString(R.string.vk_cant_upload_text), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void makeVKWallPost(VKAttachments attachments) {
        attachments.add(new VKApiLink("http://com.vetroumova.github.io/SixJars/"));
        VKRequest request = VKApi.wall().post(VKParameters.from(VKAccessToken.currentToken().userId,
                -1,
                VKApiConst.ATTACHMENTS, attachments,
                VKApiConst.MESSAGE, getString(R.string.share_message_text)));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                //Toast.makeText(getBaseContext(),"Пост успешно размещен на Вашей стене",Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), getString(R.string.vk_posted_text), Toast.LENGTH_LONG).show();
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
                //Toast.makeText(getBaseContext(),"Создание поста прервано",Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), getString(R.string.vk_not_posted_text), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.w("VK post", "error " + error.toString());
                //Toast.makeText(getBaseContext(),"Ошибка при создании поста",Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), getString(R.string.vk_error_posted_text), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //TODO work with fragments
        outState.putBoolean("IsSavedInst", true);
        //nullpointer
        /*fragmentManager.putFragment(outState, "backstackfragment", fragmentManager
                .findFragmentByTag("RECYCLER"));*/
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
            /*// TODO how to clear backstack to recycler
            try {
                fragmentManager.popBackStackImmediate();
            } catch (NullPointerException e) {
                Log.d("VOlga", "No backstack onBackPressed");
            }*/
            //fab.setVisibility(View.VISIBLE);

            if (!fab.isShown()) {
                fab.show();
                fab.startAnimation(animationGetVisible);
            }
            super.onBackPressed();
        }
    }

    //TODO check
    private void updateAllWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, JarsWidget.class));
        if (appWidgetIds.length > 0) {
            new JarsWidget().onUpdate(this, appWidgetManager, appWidgetIds);
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
