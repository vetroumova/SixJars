package com.vetroumova.sixjars.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.vetroumova.sixjars.R;
import com.vetroumova.sixjars.model.Jar;
import com.vetroumova.sixjars.ui.MainActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * One Widget that is available for use in a RemoteView is the ProgressBar.
 * It will animate itself and will not chew up resources. An in-determinant ProgressBar which
 * is a square will overlay quite well on a homescreen appwidget.
 * Create custom animation. Create ProgressBar and set in android:indeterminateDrawable your animation.
 * Add ProgressBar to your widget layout and make it visible(invisible)
 * Another option to animate a widget is the use of ViewFlipper,
 * where one can use inAnimation and outAnimation:
 * <ViewFlipper xmlns:android="http://schemas.android.com/apk/res/android"
 * android:layout_width="match_parent"
 * android:layout_height="match_parent"
 * android:flipInterval="5000"
 * android:autoStart="true"
 * android:inAnimation="@android:anim/fade_in"
 * android:outAnimation="@android:anim/fade_out"
 * android:animateFirstView="true"/>
 */
public class JarsWidget extends AppWidgetProvider {

    public static final String LOG_TAG = "VOlga";
    //Set the text
    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols();
    //s.setDecimalSeparator('.'); not needed
    private static final DecimalFormat FLOAT_FORMAT = new DecimalFormat("##,##0.00", SYMBOLS);
    private static final DecimalFormat INT_FORMAT = new DecimalFormat("##,##0", SYMBOLS);
    public static String TO_ACTIVITY_CLICK = "toActivity";
    public static String NEXT_JARS_CLICK = "nextThreeJars";
    //private static Boolean isNextJars = false;
    public static HashMap widgetsMap = new HashMap();
    private static Realm realm = Realm.getDefaultInstance();        // TODO use nonstatic

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int widgetId) {
        Log.d(LOG_TAG, "updating widget with id " + widgetId);
        RealmResults<Jar> jars = realm.where(Jar.class).findAllSorted("jar_float_id", Sort.ASCENDING);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.jars_widget);
        boolean isNextJars = (boolean) widgetsMap.get(widgetId);

        // TODO ProgressBar to animate arrow
        remoteViews.setImageViewResource(R.id.appwidget_go, R.drawable.go);
        remoteViews.setTextViewText(R.id.jar_textView1, String.valueOf(jars.get(0).getJar_id()));
        remoteViews.setTextViewText(R.id.jar_infoView1, getLoyalJarName(jars.get(0)));
        remoteViews.setTextViewTextSize(R.id.jar_infoView1, 1, getLoyalJarSize(jars.get(0)));
        remoteViews.setTextViewText(R.id.jar_sumView1, getLoyalSum(jars.get(0)));
        remoteViews.setTextViewTextSize(R.id.jar_sumView1, 1, getLoyalSumSize(jars.get(0)));

        remoteViews.setTextViewText(R.id.jar_textView2, String.valueOf(jars.get(1).getJar_id()));
        remoteViews.setTextViewText(R.id.jar_infoView2, getLoyalJarName(jars.get(1)));
        remoteViews.setTextViewTextSize(R.id.jar_infoView2, 1, getLoyalJarSize(jars.get(1)));
        remoteViews.setTextViewText(R.id.jar_sumView2, getLoyalSum(jars.get(1)));
        remoteViews.setTextViewTextSize(R.id.jar_sumView2, 1, getLoyalSumSize(jars.get(1)));

        remoteViews.setTextViewText(R.id.jar_textView3, String.valueOf(jars.get(2).getJar_id()));
        remoteViews.setTextViewText(R.id.jar_infoView3, getLoyalJarName(jars.get(2)));
        remoteViews.setTextViewTextSize(R.id.jar_infoView3, 1, getLoyalJarSize(jars.get(2)));
        remoteViews.setTextViewText(R.id.jar_sumView3, getLoyalSum(jars.get(2)));
        remoteViews.setTextViewTextSize(R.id.jar_sumView3, 1, getLoyalSumSize(jars.get(2)));

        remoteViews.setTextViewText(R.id.jar_textView4, String.valueOf(jars.get(3).getJar_id()));
        remoteViews.setTextViewText(R.id.jar_infoView4, getLoyalJarName(jars.get(3)));
        remoteViews.setTextViewTextSize(R.id.jar_infoView4, 1, getLoyalJarSize(jars.get(3)));
        remoteViews.setTextViewText(R.id.jar_sumView4, getLoyalSum(jars.get(3)));
        remoteViews.setTextViewTextSize(R.id.jar_sumView4, 1, getLoyalSumSize(jars.get(3)));

        remoteViews.setTextViewText(R.id.jar_textView5, String.valueOf(jars.get(4).getJar_id()));
        remoteViews.setTextViewText(R.id.jar_infoView5, getLoyalJarName(jars.get(4)));
        remoteViews.setTextViewTextSize(R.id.jar_infoView5, 1, getLoyalJarSize(jars.get(4)));
        remoteViews.setTextViewText(R.id.jar_sumView5, getLoyalSum(jars.get(4)));
        remoteViews.setTextViewTextSize(R.id.jar_sumView5, 1, getLoyalSumSize(jars.get(4)));

        remoteViews.setTextViewText(R.id.jar_textView6, String.valueOf(jars.get(5).getJar_id()));
        remoteViews.setTextViewText(R.id.jar_infoView6, getLoyalJarName(jars.get(5)));
        remoteViews.setTextViewTextSize(R.id.jar_infoView6, 1, getLoyalJarSize(jars.get(5)));
        remoteViews.setTextViewText(R.id.jar_sumView6, getLoyalSum(jars.get(5)));
        remoteViews.setTextViewTextSize(R.id.jar_sumView6, 1, getLoyalSumSize(jars.get(5)));

        if (isNextJars) {
            remoteViews.setViewVisibility(R.id.appwidget_first_jar, View.GONE);
            remoteViews.setViewVisibility(R.id.appwidget_second_jar, View.GONE);
            remoteViews.setViewVisibility(R.id.appwidget_third_jar, View.GONE);
            remoteViews.setViewVisibility(R.id.appwidget_fourth_jar, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.appwidget_fifth_jar, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.appwidget_sixth_jar, View.VISIBLE);
            remoteViews.setImageViewResource(R.id.appwidget_go, R.drawable.go_back);

        } else {
            remoteViews.setViewVisibility(R.id.appwidget_first_jar, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.appwidget_second_jar, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.appwidget_third_jar, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.appwidget_fourth_jar, View.GONE);
            remoteViews.setViewVisibility(R.id.appwidget_fifth_jar, View.GONE);
            remoteViews.setViewVisibility(R.id.appwidget_sixth_jar, View.GONE);
            remoteViews.setImageViewResource(R.id.appwidget_go, R.drawable.go);
        }
        Intent intentStartActivityJar1 = new Intent(context, MainActivity.class);
        Intent intentStartActivityJar2 = new Intent(context, MainActivity.class);
        Intent intentStartActivityJar3 = new Intent(context, MainActivity.class);
        Intent intentStartActivityJar4 = new Intent(context, MainActivity.class);
        Intent intentStartActivityJar5 = new Intent(context, MainActivity.class);
        Intent intentStartActivityJar6 = new Intent(context, MainActivity.class);

        //intentStartActivityFirst.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intentStartActivityJar1.putExtra("JarInfoFragment", jars.get(0).getJar_id());
        intentStartActivityJar2.putExtra("JarInfoFragment", jars.get(1).getJar_id());
        intentStartActivityJar3.putExtra("JarInfoFragment", jars.get(2).getJar_id());
        intentStartActivityJar4.putExtra("JarInfoFragment", jars.get(3).getJar_id());
        intentStartActivityJar5.putExtra("JarInfoFragment", jars.get(4).getJar_id());
        intentStartActivityJar6.putExtra("JarInfoFragment", jars.get(5).getJar_id());

        intentStartActivityJar1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK);
        intentStartActivityJar2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK);
        intentStartActivityJar3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK);
        intentStartActivityJar4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK);
        intentStartActivityJar5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK);
        intentStartActivityJar6.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK);
        // Identifies the particular widget...

        //intentStartActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intentStartActivityFirstJar.setAction(TO_ACTIVITY_CLICK);
        // Make the pending intent unique...
        //intentStartActivity.setData(Uri.parse(intentStartActivity.toUri(Intent.URI_INTENT_SCHEME)));
        /*Bundle bundle = new Bundle();
        bundle.putString("JarInfoFragment",first);*/

        PendingIntent pendingToActivity1 = PendingIntent.getActivity(context, 0, intentStartActivityJar1, FLAG_UPDATE_CURRENT);
        PendingIntent pendingToActivity2 = PendingIntent.getActivity(context, 1, intentStartActivityJar2, FLAG_UPDATE_CURRENT);
        PendingIntent pendingToActivity3 = PendingIntent.getActivity(context, 2, intentStartActivityJar3, FLAG_UPDATE_CURRENT);
        PendingIntent pendingToActivity4 = PendingIntent.getActivity(context, 3, intentStartActivityJar4, FLAG_UPDATE_CURRENT);
        PendingIntent pendingToActivity5 = PendingIntent.getActivity(context, 4, intentStartActivityJar5, FLAG_UPDATE_CURRENT);
        PendingIntent pendingToActivity6 = PendingIntent.getActivity(context, 5, intentStartActivityJar6, FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.appwidget_first_jar, pendingToActivity1);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_second_jar, pendingToActivity2);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_third_jar, pendingToActivity3);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_fourth_jar, pendingToActivity4);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_fifth_jar, pendingToActivity5);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_sixth_jar, pendingToActivity6);

        //to set next jars
        Intent intentNextJars = new Intent(context, JarsWidget.class);
        intentNextJars.putExtra("widgetId", widgetId);
        intentNextJars.setAction(NEXT_JARS_CLICK);
        PendingIntent pendingIntentNextJars = PendingIntent.getBroadcast(context, 0,
                intentNextJars, 0);

        remoteViews.setOnClickPendingIntent(R.id.appwidget_go_layout, pendingIntentNextJars);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }

    public static String getLoyalJarName(Jar jar) {
        String jarName = jar.getJar_name();
        if (jarName.length() > 22) {
            String[] jarNameSplited = jarName.split(" ");
            jarName = jarNameSplited[0].concat(" ").concat(jarNameSplited[1]);
            Log.d("VOlga", "jarnameSplited " + jarNameSplited[0] + " ___ " + jarNameSplited[1]);
        }
        Log.d("VOlga", "jarname " + jarName);
        return jarName;
    }

    public static int getLoyalJarSize(Jar jar) {
        String jarName = getLoyalJarName(jar);
        int textSize = 12;
        if (jarName.length() > 20) {
            textSize = 9;
        } else if (jarName.length() > 12) {
            textSize = 11;
        } else if (jarName.length() > 9 && !jarName.contains(" ")) {
            textSize = 9;
        }
        return textSize;
    }

    public static String getLoyalSum(Jar jar) {
        float jarSum = jar.getTotalCash();
        if (jarSum % 1 < 0.01) {
            return INT_FORMAT.format(jarSum);
        } else {
            return FLOAT_FORMAT.format(jarSum);
        }
    }

    public static int getLoyalSumSize(Jar jar) {
        String jarSum = getLoyalSum(jar);
        int textSize = 14;
        if (jarSum.length() > 11) {
            textSize = 6;
        } else if (jarSum.length() > 9) {
            textSize = 9;
        } else if (jarSum.length() > 6) {
            textSize = 12;
        }
        return textSize;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        realm = Realm.getDefaultInstance();
        //ComponentName thisWidget = new ComponentName(context, JarsWidget.class);
        // There may be multiple widgets active, so update all of them
        for (int widgetId : appWidgetIds) {
            if (!widgetsMap.containsKey(widgetId)) {
                widgetsMap.put(widgetId, false);
            }
            updateAppWidget(context, appWidgetManager, widgetId);
        }
        realm.close();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(NEXT_JARS_CLICK)) {
            ComponentName thisWidget = new ComponentName(context, JarsWidget.class);
            int[] allWidgetsIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisWidget);
            //TODO CHECK
            Log.d(LOG_TAG, "Value of widgets array in onReceive " + allWidgetsIds.length);
            boolean isNextJars = false;
            if (widgetsMap.get(allWidgetsIds[0]) != null) {
                isNextJars = (boolean) widgetsMap.get(allWidgetsIds[0]);
            }
            Log.d(LOG_TAG, "Value of isNextJars of widget " + thisWidget + " is " + isNextJars);
            widgetsMap.put(allWidgetsIds[0], !isNextJars);
            // old isNextJars = !isNextJars;
            this.onUpdate(context, AppWidgetManager.getInstance(context), allWidgetsIds);
        } else if (intent.getAction().equals(TO_ACTIVITY_CLICK)) {
            Log.d(LOG_TAG, "open an activity");

            super.onReceive(context, intent);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            JarsWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
            widgetsMap.remove(appWidgetId);
        }
    }
}

