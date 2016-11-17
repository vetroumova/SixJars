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
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Jar;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.MainActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Implementation of MainApplication Widget functionality.
 * MainApplication Widget Configuration implemented in {@link JarsWidgetConfigureActivity JarsWidgetConfigureActivity}
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
    //Set the text
    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols();
    //s.setDecimalSeparator('.'); not needed
    private static final DecimalFormat FLOAT_FORMAT = new DecimalFormat("##,##0.00", SYMBOLS);
    private static final DecimalFormat INT_FORMAT = new DecimalFormat("##,##0", SYMBOLS);
    public static String TO_ACTIVITY_CLICK = "toActivity";
    public static String NEXT_JARS_CLICK = "nextThreeJars";
    private static Realm realm = Realm.getDefaultInstance();
    private static Boolean isNextJars = false;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int widgetId) {
        Log.d(LOG_TAG, "updating widget with id " + widgetId);
        RealmResults<Jar> jars = realm.where(Jar.class).findAllSorted("jar_float_id", Sort.ASCENDING);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.jars_widget);

        //TODO use locale
        Locale locale = Locale.getDefault();

        if (isNextJars) {
            // TODO ProgressBar to animate arrow
            remoteViews.setImageViewResource(R.id.appwidget_go, R.drawable.go_back);
            remoteViews.setTextViewText(R.id.jar_textView1, String.valueOf(jars.get(3).getJar_id()));
            remoteViews.setTextViewText(R.id.jar_infoView1, getLoyalJarName(jars.get(3)));
            remoteViews.setTextViewTextSize(R.id.jar_infoView1, 1, getLoyalJarSize(jars.get(3)));
            //remoteViews.setTextViewText(R.id.jar_sumView1, sum3.concat(" ГРН"));
            remoteViews.setTextViewText(R.id.jar_sumView1, getLoyalSum(jars.get(3)));
            remoteViews.setTextViewTextSize(R.id.jar_sumView1, 1, getLoyalSumSize(jars.get(3)));

            remoteViews.setTextViewText(R.id.jar_textView2, String.valueOf(jars.get(4).getJar_id()));
            remoteViews.setTextViewText(R.id.jar_infoView2, getLoyalJarName(jars.get(4)));
            remoteViews.setTextViewTextSize(R.id.jar_infoView2, 1, getLoyalJarSize(jars.get(4)));
            remoteViews.setTextViewText(R.id.jar_sumView2, getLoyalSum(jars.get(4)));
            remoteViews.setTextViewTextSize(R.id.jar_sumView2, 1, getLoyalSumSize(jars.get(4)));

            remoteViews.setTextViewText(R.id.jar_textView3, String.valueOf(jars.get(5).getJar_id()));
            remoteViews.setTextViewText(R.id.jar_infoView3, getLoyalJarName(jars.get(5)));
            remoteViews.setTextViewTextSize(R.id.jar_infoView3, 1, getLoyalJarSize(jars.get(5)));
            remoteViews.setTextViewText(R.id.jar_sumView3, getLoyalSum(jars.get(5)));
            remoteViews.setTextViewTextSize(R.id.jar_sumView3, 1, getLoyalSumSize(jars.get(5)));
        } else {
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
        }

        //Register an onClickListener
            /*Intent intent = new Intent(context, JarsWidget.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast
                    (context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);*/

            /*Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);  // Identifies the particular widget...
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Make the pending intent unique...
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent pendIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);*/
        Intent intentStartActivity = new Intent(context, MainActivity.class);
        //intentStartActivity.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);  // Identifies the particular widget...
        //intentStartActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intentStartActivity.setAction(TO_ACTIVITY_CLICK);
        // Make the pending intent unique...
        //intentStartActivity.setData(Uri.parse(intentStartActivity.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingToActivity = PendingIntent.getActivity(context, 0, intentStartActivity, 0);
        //RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.activity_main);

        remoteViews.setOnClickPendingIntent(R.id.appwidget_first_jar, pendingToActivity);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_second_jar, pendingToActivity);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_third_jar, pendingToActivity);

        //to set next jars
        Intent intentNextJars = new Intent(context, JarsWidget.class);
        intentNextJars.putExtra("widgetId", widgetId);
        intentNextJars.setAction(NEXT_JARS_CLICK);
        PendingIntent pendingIntentNextJars = PendingIntent.getBroadcast(context, 0,
                intentNextJars, 0);

        remoteViews.setOnClickPendingIntent(R.id.appwidget_go_layout, pendingIntentNextJars);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);

        //realm.close();

        /*String taskListId = prefHelper.getWidgetTaskListId(appWidgetId);

        // Set up the intent that starts the TasksWidgetService, which will
        // provide the views for this collection.
        Intent intent = new Intent(context, TasksWidgetService.class);

        intent.putExtra(TaskDetailActivity.EXTRA_TASK_LIST_ID, taskListId);

        // Add the app widget ID to the intent extras.
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
*/
        // Instantiate the RemoteViews object for the app widget layout.
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jars_widget);

        // Set up the RemoteViews object to use a RemoteViews adapter.
        // This adapter connects to a RemoteViewsService  through the specified intent.
        // This is how you populate the data.
        //views.setRemoteAdapter(R.id.widget_task_list, intent);

        // The empty view is displayed when the collection has no items.
        // It should be in the same layout used to instantiate the RemoteViews
        // object above.
//        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        // This section makes it possible for items to have individualized behavior.
        // It does this by setting up a pending intent template. Individuals items of a collection
        // cannot set up their own pending intents. Instead, the collection as a whole sets
        // up a pending intent template, and the individual items set a fillInIntent
        // to create unique behavior on an item-by-item basis.
        //Intent taskDetailIntent = TaskDetailActivity.getIntentTemplate(context, taskListId);
        //taskDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //PendingIntent taskDetailPendingIntent = PendingIntent.getActivity(context, 0, taskDetailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //views.setPendingIntentTemplate(R.id.widget_task_list, taskDetailPendingIntent);

        //
        // Do additional processing specific to this app widget...
        //

        //RealmResults<Jar> jarList = realm.where(Jar.class).findAll();

        // The task list can be null when upgrading the Realm scheme, prevent crashing
        /*if (taskList != null) {
            // Set the task list title
            views.setTextViewText(R.id.task_list_title, taskList.getTitle());
        }*/

        // Setup the header
        /*Intent viewTaskListIntent = new Intent(context, MainActivity.class);
        viewTaskListIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent viewTaskListPendingIntent = PendingIntent.getActivity(context, 0, viewTaskListIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.task_list_header, viewTaskListPendingIntent);*/

        // Setup the Add Task button
        // Set the icon
        /*views.setImageViewResource(R.id.add_task, R.drawable.ic_add_white_24dp);
        // Set the click action
        Intent addTaskIntent = EditTaskActivity.getTaskCreateIntent(context, taskListId);
        addTaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, addTaskIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.add_task, pendingIntent);*/
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
        int textSize = 11;
        if (jarName.length() > 22) {
            textSize = 8;
        } else if (jarName.length() > 12) {
            textSize = 10;
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
        if (jarSum.length() > 12) {
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
            updateAppWidget(context, appWidgetManager, widgetId);
        }
        realm.close();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(NEXT_JARS_CLICK)) {
            ComponentName thisWidget = new ComponentName(context, JarsWidget.class);
            int[] allWidgetsIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisWidget);
            isNextJars = !isNextJars;
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

