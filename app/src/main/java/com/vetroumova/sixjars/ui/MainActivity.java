package com.vetroumova.sixjars.ui;

import android.Manifest;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.vetroumova.sixjars.R;
import com.vetroumova.sixjars.app.Prefs;
import com.vetroumova.sixjars.database.RealmManager;
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

import java.util.Locale;

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
    private LinearLayoutManager layoutManager;
    private Subscription jarInRecyclerSubscription;
    private Subscription cashClickSubscription;
    private Subscription finishEditCashSubscription;
    private Subscription cashDeleteSubscription;
    private Subscription spendCashInJar;
    private Subscription finishSpendCashSubscription;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    //private List<String> mockItems = new ArrayList<>();
    private int[] colors = {R.color.colorPrimary, R.color.colorAccent,
            R.color.colorPrimaryLight, R.color.colorPrimaryDark};
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
        //recyclerFragment = new RecyclerFragment();
        recyclerFragment = RecyclerFragment.newInstance();


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
            fab.startAnimation(animationDisapear);
            DebugLogger.log("DebugLogger");
            Log.d(TAG, "FAB");
            addCashFragment = AddCashFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_layout, addCashFragment)
                    /*.setCustomAnimations(android.R.animator
                    .fade_in, android.R.animator.fade_out)*/
                    .addToBackStack("addCash")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            fab.hide();
        });

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setLogo(R.mipmap.ic_launcher);

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
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setColored(true);
        bottomNavigation.setColoredModeColors(colors[2], colors[3]);
        //to color an icon font
        //bottomNavigation.setForceTint(true); // FIXME: 31.01.2017
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
                        if (!fab.isShown()) {
                            fab.show();
                            fab.startAnimation(animationGetVisible);
                        }
                        break;
                    }
                    case 1: {
                        Log.d(TAG, "settings instance lang pref - "
                                + Prefs.with(getApplicationContext()).getPrefLanguage());
                        settingsFragment = SettingsFragment.newInstance(
                                Prefs.with(getApplicationContext()).getPrefLanguage());
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
                        helpFragment = HelpFragment.newInstance();
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

        if (fragmentManager.getBackStackEntryCount() == 0
                && !(fragmentManager.findFragmentById(R.id.content_layout) instanceof RecyclerFragment)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_layout, recyclerFragment, "RECYCLER")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
        updateAllWidgets();

        jarInRecyclerSubscription = recyclerFragment.getJar()
                .subscribe(jar -> {
                            DebugLogger.log("opening a JAR info: " + jar.getJar_id());
                            jarInfoFragment = JarInfoFragment.newInstance(jar.getJar_id());
                            Log.d(TAG, jarInfoFragment.toString());
                            fragmentManager.beginTransaction()
                                    .replace(R.id.content_layout, jarInfoFragment)
                                    .addToBackStack("JarInfo")
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();

                            getInnerSubscriptionsJar(jar.getJar_id());
                        },
                        error -> DebugLogger.log(error.getMessage())
                );

        subscriptions.add(jarInRecyclerSubscription);
    }

    private void getInnerSubscriptionsJar(String jar_id) {

        cashClickSubscription = jarInfoFragment.getCashflowItem()
                .subscribe(cash -> {
                            DebugLogger.log("cash clicked : " + cash.getId()
                                    + ", " + cash.getSum());
                            Log.d(TAG, "cash clicked : " + cash.getId() + ", "
                                    + cash.getSum());

                            cashInfoFragment = CashInfoFragment.newInstance(cash.getId());
                            fragmentManager.beginTransaction()
                                    .replace(R.id.content_layout, cashInfoFragment)
                                    .addToBackStack("CashEdit")
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();

                            finishEditCashSubscription = cashInfoFragment.isFinishEdit()
                                    .subscribe(isEdited -> {
                                        DebugLogger.log("DL cash was edited : " + isEdited);
                                        Log.d(TAG, "cash was edited : " + isEdited);
                                        recyclerFragment.refreshRecycler();
                                        jarInfoFragment.refreshData();
                                        jarInfoFragment.refreshRecyclerArterDeleteItem();
                                        updateAllWidgets();
                                    });

                            subscriptions.add(finishEditCashSubscription);
                        },
                        error -> DebugLogger.log(error.getMessage())
                );

        cashDeleteSubscription = jarInfoFragment.refreshRecyclerArterDeleteItem()
                .subscribe(deletedCashID -> {
                            DebugLogger.log("DL refreshing mainRecycler after deleted cash : "
                                    + deletedCashID);
                            Log.d(TAG, "refreshing mainRecycler after deleted cash : "
                                    + deletedCashID);
                            recyclerFragment.refreshRecycler();
                            updateAllWidgets();
                        },
                        error -> DebugLogger.log(error.getMessage())
                );

        spendCashInJar = jarInfoFragment.spendCashInJar()
                .subscribe(jarSpend -> {
                    DebugLogger.log("DL open spendcash fragment : " + jar_id);
                    Log.d(TAG, "open spendcash fragment : " + jar_id);
                    spendFragment = SpendFragment.newInstance(jar_id);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_layout, spendFragment, "spend")
                            .addToBackStack("Spend")
                            .commit();

                    finishSpendCashSubscription = spendFragment.finishSpend()
                            .subscribe(isSpend -> {
                                DebugLogger.log("spend cash and close : " + isSpend);
                                Log.d(TAG, "spend cash and close : " + isSpend);
                                //TODO CHECK if working and close the spend
                                fragmentManager.popBackStackImmediate();
                                fab.show();
                                recyclerFragment.refreshRecycler();
                                jarInfoFragment.refreshData();
                                jarInfoFragment.refreshRecyclerArterDeleteItem();
                                updateAllWidgets();
                            });
                    subscriptions.add(finishSpendCashSubscription);
                });
        subscriptions.add(cashClickSubscription);
        subscriptions.add(cashDeleteSubscription);
        subscriptions.add(spendCashInJar);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == GOOGLEPLUS_REQUEST_CODE) && (resultCode == -1)) {
            Log.d(TAG, "Success on Google posting");
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
        final String appPackageName = getApplicationContext().getPackageName();
        attachments.add(new VKApiLink("https://play.google.com/store/apps/details?id=" + appPackageName));
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
                Toast.makeText(getBaseContext(), getString(R.string.vk_error_posted_text), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("IsSavedInst", true);   //don't needed anymore, I guess - todo check
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
            if (!fab.isShown()) {
                fab.show();
                fab.startAnimation(animationGetVisible);
            }
            //to clear subscriptions to JarInfo cashflows
            // FIXME: 31.01.2017
            if (fragmentManager.findFragmentById(R.id.content_layout) instanceof CashInfoFragment) {
            } else if (fragmentManager.findFragmentById(R.id.content_layout) instanceof SpendFragment) {
            } else if (fragmentManager.findFragmentById(R.id.content_layout) instanceof JarInfoFragment) {
                /*if (!cashClickSubscription.isUnsubscribed()) {
                    finishEditCashSubscription.unsubscribe();
                    cashClickSubscription.unsubscribe();
                }
                if (!spendCashInJar.isUnsubscribed()) {
                    finishSpendCashSubscription.unsubscribe();
                    spendCashInJar.unsubscribe();
                }*/
                cashDeleteSubscription.unsubscribe();
            }
            super.onBackPressed();
        }
    }

    //TODO check
    private void updateAllWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName thisWidget = new ComponentName(this, JarsWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            for (int appWidgetId : appWidgetIds) {
                JarsWidget.updateAppWidget(this, appWidgetManager, appWidgetId);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Prefs.with(getApplicationContext()).getPrefRestoreMark()) {
            Log.d(TAG, "onStart - restore true");
            //todo check
            RealmManager.loadUserPrefsToSharedPrefs(getApplicationContext(),
                    RealmManager.with(this).getJar("NEC").getUser());
            Log.d(TAG, "");
            Prefs.with(getApplicationContext()).setPrefRestoreMark(false);
            Log.d(TAG, "onStart - restore false");
            Configuration config = getApplicationContext().getResources().getConfiguration();
            Locale locale = new Locale(Prefs.with(this).getPrefLanguage());
            /*Locale previousLocale = getApplicationContext().
            if (!"".equals(lang) && !Prefs.with(getContext().getApplicationContext()).equals(lang))*/
            Locale.setDefault(locale);
            config.locale = locale;
            getApplicationContext().getResources().updateConfiguration(config,
                    getApplicationContext().getResources().getDisplayMetrics());
            Intent intent = getApplicationContext().getApplicationContext().getPackageManager()
                    .getLaunchIntentForPackage(getApplicationContext().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (Prefs.with(getApplicationContext()).getPrefRestoreMark()) {
            Log.d(TAG, "onPostResume - restore true");
            RealmManager.loadUserPrefsToSharedPrefs(getApplicationContext(),
                    RealmManager.with(this).getJar("NEC").getUser());
            Log.d(TAG, "");
            Prefs.with(getApplicationContext()).setPrefRestoreMark(false);
            Log.d(TAG, "onPostResume - restore false");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //todo check
        Intent intentFromWidget = getIntent();
        if (intentFromWidget.getStringExtra("JarInfoFragment") != null) {
            String value = intentFromWidget.getStringExtra("JarInfoFragment");
            if (value != null) {
                Log.d(TAG, "    widget value from intent " + value);
                if (fragmentManager.findFragmentById(R.id.content_layout) instanceof JarInfoFragment) {
                    fragmentManager.popBackStackImmediate();
                }
                jarInfoFragment = JarInfoFragment.newInstance(value);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_layout, jarInfoFragment)
                        .addToBackStack("jarInfo")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                getInnerSubscriptionsJar(value);
                updateAllWidgets();
                intentFromWidget.removeExtra("JarInfoFragment"); //todo check
            } else {
                Log.d(TAG, "     widget value from intent null");
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /*Can not perform this (widget value from intent) action after onSaveInstanceState*/
        super.onNewIntent(intent);
    }
}
