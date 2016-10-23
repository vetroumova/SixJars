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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

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
    public static int FIRST_JARS = 0;
    /* private static final String ACTION_CLICK
             = "org.itstep.android5.vetroumova.newbeginning.sixjars.ui.widget.JarsWidget.ACTION_CLICK";*/
    public static String NEXT_JARS_CLICK = "nextThreeJars";
    private static Realm realm = Realm.getDefaultInstance();
    private static Boolean isNextJars = false;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int widgetId) {
        Log.d("VOlga", "updating widget with id " + widgetId);

        realm = Realm.getDefaultInstance();
        RealmResults<Jar> jars = realm.where(Jar.class).findAllSorted("jar_float_id", Sort.ASCENDING);
        /*ArrayList<Float> jarsSumList = new ArrayList<>();

        for (Jar jar : jars) {
            jarsSumList.add(jar.getTotalCash());
            Log.d(LOG_TAG, "add " + jar.getJar_id() + " sum " + jar.getTotalCash() + " in " + widgetId);
            Toast.makeText(context, "add " + jar.getJar_id() + " sum " + jar.getTotalCash()
                    + " in " + widgetId, Toast.LENGTH_SHORT).show();
        }*/

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.jars_widget);

        //Set the text
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        //s.setDecimalSeparator('.');
        DecimalFormat f = new DecimalFormat("##,##0.00", s);
        String sum0 = (f.format(jars.get(0).getTotalCash()));
        String sum1 = (f.format(jars.get(1).getTotalCash()));
        String sum2 = (f.format(jars.get(2).getTotalCash()));
        String sum3 = (f.format(jars.get(3).getTotalCash()));
        String sum4 = (f.format(jars.get(4).getTotalCash()));
        String sum5 = (f.format(jars.get(5).getTotalCash()));

        //TODO use locale
        Locale locale = Locale.getDefault();

        if (isNextJars) {
            remoteViews.setTextViewText(R.id.jar_textView1, String.valueOf(jars.get(3).getJar_id()));
            remoteViews.setTextViewText(R.id.jar_infoView1, String.valueOf(jars.get(3).getJar_name()));
            remoteViews.setTextViewText(R.id.jar_sumView1, sum3.concat(" ГРН"));

            remoteViews.setTextViewText(R.id.jar_textView2, String.valueOf(jars.get(4).getJar_id()));
            remoteViews.setTextViewText(R.id.jar_infoView2, String.valueOf(jars.get(4).getJar_name()));
            remoteViews.setTextViewText(R.id.jar_sumView2, sum4.concat(" ГРН"));

            remoteViews.setTextViewText(R.id.jar_textView3, String.valueOf(jars.get(5).getJar_id()));
            remoteViews.setTextViewText(R.id.jar_infoView3, String.valueOf(jars.get(5).getJar_name()));
            remoteViews.setTextViewText(R.id.jar_sumView3, sum5.concat(" ГРН"));
        } else {
            remoteViews.setTextViewText(R.id.jar_textView1, String.valueOf(jars.get(0).getJar_id()));
            remoteViews.setTextViewText(R.id.jar_infoView1, String.valueOf(jars.get(0).getJar_name()));
            remoteViews.setTextViewText(R.id.jar_sumView1, sum0.concat(" ГРН"));

            remoteViews.setTextViewText(R.id.jar_textView2, String.valueOf(jars.get(1).getJar_id()));
            remoteViews.setTextViewText(R.id.jar_infoView2, String.valueOf(jars.get(1).getJar_name()));
            remoteViews.setTextViewText(R.id.jar_sumView2, sum1.concat(" ГРН"));

            remoteViews.setTextViewText(R.id.jar_textView3, String.valueOf(jars.get(2).getJar_id()));
            remoteViews.setTextViewText(R.id.jar_infoView3, String.valueOf(jars.get(2).getJar_name()));
            remoteViews.setTextViewText(R.id.jar_sumView3, sum2.concat(" ГРН"));
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

        //to set next jars
        Intent intentNextJars = new Intent(context, JarsWidget.class);
        intentNextJars.putExtra("widgetId", widgetId);
        intentNextJars.setAction(NEXT_JARS_CLICK);
        PendingIntent pendingIntentNextJars = PendingIntent.getBroadcast(context, 0,
                intentNextJars, 0);

        remoteViews.setOnClickPendingIntent(R.id.appwidget_go, pendingIntentNextJars);

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

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        ComponentName thisWidget = new ComponentName(context, JarsWidget.class);

        //int[] allWidgetsIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // There may be multiple widgets active, so update all of them
        for (int widgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, widgetId);
        }
        realm.close();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(NEXT_JARS_CLICK)) {
            ComponentName thisWidget = new ComponentName(context, JarsWidget.class);
            int[] allWidgetsIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisWidget);
            isNextJars = !isNextJars;
            this.onUpdate(context, AppWidgetManager.getInstance(context), allWidgetsIds);
            /*this.updateAppWidget(context,AppWidgetManager.getInstance(context),
                    intent.getIntExtra("widgetID",allWidgetsIds[0]));*/
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

