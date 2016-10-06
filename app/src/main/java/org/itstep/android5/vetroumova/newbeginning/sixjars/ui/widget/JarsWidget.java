package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;

import java.util.Random;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link JarsWidgetConfigureActivity JarsWidgetConfigureActivity}
 * <p>
 * stackoverflow
 * One Widget that is available for use in a RemoteView is the ProgressBar.
 * It will animate itself and will not chew up resources. An in-determinant ProgressBar which
 * is a square will overlay quite well on a homescreen appwidget. See sample code from Android site
 * <p>
 * Create custom animation. Create ProgressBar and set in android:indeterminateDrawable your animation.
 * Add ProgressBar to your widget layout and make it visible(invisible)
 * <p>
 * Another option to animate a widget is the use of ViewFlipper,
 * where one can use inAnimation and outAnimation:
 * <p>
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
    private static final String ACTION_CLICK = "ACTION_CLICK";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = JarsWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jars_widget);
        views.setTextViewText(R.id.appwidget_TextView, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        ComponentName thisWidget = new ComponentName(context, JarsWidget.class);

        int[] allWidgetsIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // There may be multiple widgets active, so update all of them
        /*for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }*/
        for (int widgetId : allWidgetsIds) {
            //create some random data
            int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.jars_widget);

            Log.d(LOG_TAG, "Clicking " + String.valueOf(number) + " " + widgetId);

            //Set the text
            remoteViews.setTextViewText(R.id.jar_textView1, String.valueOf(number));

            //Register an onClickListener
            Intent intent = new Intent(context, JarsWidget.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast
                    (context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setOnClickPendingIntent(R.id.appwidget_first_jar, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ACTION_CLICK)) {

        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.jars_widget);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            JarsWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

