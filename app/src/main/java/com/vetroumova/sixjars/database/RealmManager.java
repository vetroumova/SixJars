package com.vetroumova.sixjars.database;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.vetroumova.sixjars.R;
import com.vetroumova.sixjars.app.Prefs;
import com.vetroumova.sixjars.model.Cashflow;
import com.vetroumova.sixjars.model.Jar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.internal.IOException;

/**
 * Created by OLGA on 11.09.2016.
 */
public class RealmManager {
    /*private static final File EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DOCUMENTS);*/
    private static final File EXPORT_REALM_PATH = new File(Environment.getExternalStorageDirectory()
            + "/SixJars");
    private static final String EXPORT_REALM_FILE_NAME = "backup_sixjars.realm";
    private static final String EXPORT_PREFS_FILE_NAME = "preferences_sixjars.xml";
    private static final String IMPORT_REALM_FILE_NAME = Realm.DEFAULT_REALM_NAME;
    private static final String TAG = "VOlga";

    //from Realm
    private static RealmManager realmManagerInstance;
    //private static RealmChangeListener listener;
    private Realm realm;

    public RealmManager(Application application) {

        // Automatically run migration if needed
        realm = Realm.getDefaultInstance();
        realm.setAutoRefresh(true);
    }

    public static RealmManager with(Fragment fragment) {

        if (realmManagerInstance == null) {
            realmManagerInstance = new RealmManager(fragment.getActivity().getApplication());
        }
        return realmManagerInstance;
    }

    public static RealmManager with(Activity activity) {

        if (realmManagerInstance == null) {
            realmManagerInstance = new RealmManager(activity.getApplication());
        }
        return realmManagerInstance;
    }

    public static RealmManager with(Application application) {

        if (realmManagerInstance == null) {
            realmManagerInstance = new RealmManager(application);
        }
        return realmManagerInstance;
    }

    public static RealmManager getInstance() {
        return realmManagerInstance;
    }

    @NonNull
    public static void initialiseJars(final Context context) {
        final Realm realm = RealmManager.getInstance().getRealm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.where(Jar.class).findAll().deleteAllFromRealm();

                Jar jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_NEC));
                jar.setJar_float_id(0f);
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_NEC));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_NEC));
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_PLAY));
                jar.setJar_float_id(1f);
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_PLAY));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_PLAY));
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_GIVE));
                jar.setJar_float_id(2f);
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_GIVE));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_GIVE));
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id("EDU");
                jar.setJar_float_id(3f);
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_EDU));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_EDU));
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_LTSS));
                jar.setJar_float_id(4f);
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_LTSS));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_LTSS));
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_FFA));
                jar.setJar_float_id(5f);
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_FFA));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_FFA));
                realm.copyToRealmOrUpdate(jar);
            }
        });
    }

    public void changeJarName(Jar jar, String newName) {
        realm.beginTransaction();
        jar.setJar_name(newName);
        realm.commitTransaction();

    }

    public void setRealm() {
        realm = Realm.getDefaultInstance();
        realm.setAutoRefresh(true);
    }

    public void backup(Context context) {
        boolean isPresent = true;
        if (!EXPORT_REALM_PATH.exists()) {
            isPresent = EXPORT_REALM_PATH.mkdir();
        }
        if (isPresent) {
            try {
                // create a backup file
                File exportRealmFile = new File(EXPORT_REALM_PATH, EXPORT_REALM_FILE_NAME);
                File exportPrefsFile = new File(EXPORT_REALM_PATH, EXPORT_PREFS_FILE_NAME);

                // if backup file already exists, delete it
                exportRealmFile.delete();
                exportPrefsFile.delete();

                // copy current realm to backup file
                realm.writeCopyTo(exportRealmFile);
                Prefs.with(context).saveSharedPreferencesToFile(exportPrefsFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
            String msg = "DB File exported to Path: " + EXPORT_REALM_PATH + "/"
                    + EXPORT_REALM_FILE_NAME + " , \nPreferences exported to Path: " +
                    EXPORT_REALM_PATH + "/" + EXPORT_PREFS_FILE_NAME;
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            Log.d(TAG, msg);
            //realm.close();
        } else {
            // Failure to find a directory
        }
    }

    public void restore(Context context) {
        String restoreFilePath = EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;
        String restorePrefsPath = EXPORT_REALM_PATH + "/" + EXPORT_PREFS_FILE_NAME;
        Log.d(TAG, "oldFilePath = " + restoreFilePath);
        copyBundledRealmFile(context, restoreFilePath, IMPORT_REALM_FILE_NAME);
        Prefs.with(context).loadSharedPreferencesFromFile(new File(restorePrefsPath));
        Log.d(TAG, "Data restore is done");
    }

    private String copyBundledRealmFile(Context context, String restoreFilePath, String outFileName) {
        realm.close();
        try {
            try {
                File file = new File(context.getFilesDir(), outFileName);

                FileOutputStream outputStream = new FileOutputStream(file);

                FileInputStream inputStream = new FileInputStream(new File(restoreFilePath));

                byte[] buf = new byte[1024];
                int bytesRead;
                try {
                    while ((bytesRead = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, bytesRead);
                    }
                    outputStream.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                return file.getAbsolutePath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setRealm();
        return null;
    }

    //export by email
    public void exportDatabase(Activity activity) {

        File exportRealmFile = null;
        try {
            // get or create an export.realm" file
            exportRealmFile = new File(activity.getExternalCacheDir(), "export.realm");

            // if "export.realm" already exists, delete
            exportRealmFile.delete();

            // copy current realm to "export.realm"
            realm.writeCopyTo(exportRealmFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //realm.close();

        // init email intent and add export.realm as attachment
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, "YOUR MAIL");
        intent.putExtra(Intent.EXTRA_SUBJECT, "YOUR SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT");
        Uri u = Uri.fromFile(exportRealmFile);
        intent.putExtra(Intent.EXTRA_STREAM, u);

        // start email intent
        //activity.startActivity(Intent.createChooser(intent, "YOUR CHOOSER TITLE"));
        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.send_base_text)));
    }

    public String savePrefs(Context context) {
        //restore preferences from totalSum
        /*RealmResults<Jar> jars = RealmManager.getInstance().getJars();
        float[] sums = new float[6];
        for(int i=0;i<sums.length;i++) {
            sums[i]=jars.get(i).getTotalCash();
        }
        Prefs.with(context).setMaxVolumes(sums);*/
        String outFileName = "prefs.xml";
        String restoreFilePath = "";
        try {
            try {
                File file = new File(context.getFilesDir(), outFileName);

                FileOutputStream outputStream = new FileOutputStream(file);

                FileInputStream inputStream = new FileInputStream(new File(restoreFilePath));

                byte[] buf = new byte[1024];
                int bytesRead;
                try {
                    while ((bytesRead = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, bytesRead);
                    }
                    outputStream.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                return file.getAbsolutePath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Realm getRealm() {
        if (realm.isClosed()) {
            setRealm();
        }
        return realm;
    }

    //find all objects in the Jar.class
    public RealmResults<Jar> getJars() {

        return realm.where(Jar.class).findAll();
    }

    //query a single item with the given id
    public Jar getJar(String id) {

        return realm.where(Jar.class).equalTo("jar_id", id).findFirst();
    }

    public boolean editCashflow(long cashID, Date newDate, float newSum, String description, String jarID) {

        Cashflow oldCashflow = realm.where(Cashflow.class).equalTo("id", cashID).findFirst();
        Cashflow newCashflow = new Cashflow();
        Jar oldJar = oldCashflow.getJar();
        Jar newJar = realm.where(Jar.class).equalTo("jar_id", jarID).findFirst();
        newCashflow.setId(cashID);
        //TODO if date is less than first income - how to correct
        newCashflow.setDate(newDate);
        newCashflow.setSum(newSum);
        newCashflow.setCurrpercent(oldCashflow.getCurrpercent());
        newCashflow.setDescription(description);
        newCashflow.setPhoto("");
        newCashflow.setJar(newJar);

        if (newJar.equals(oldJar)) {
            //check for negatives
            if ((oldJar.getTotalCash() - oldCashflow.getSum() + newSum) < 0) {
                return false;
            } else {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(newCashflow);
                realm.commitTransaction();
                checkSumTotalInJar(oldJar.getJar_id());

                return true;
            }
        }
        //if different jars
        else {
            if ((oldJar.getTotalCash() - oldCashflow.getSum()) < 0 || (newJar.getTotalCash() + newSum) < 0) {
                return false;
            } else {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(newCashflow);
                realm.commitTransaction();
                checkSumTotalInJar(oldJar.getJar_id());
                checkSumTotalInJar(newJar.getJar_id());
                return true;
            }
        }
    }

    public Cashflow getCashflowByID(long cashID) {
        return realm.where(Cashflow.class)
                .equalTo("id", cashID)
                .findFirst();
    }

    /*public RealmResults<Cashflow> getAllCashflow() {
         *//*Observable<Cashflow> result = realm.where(Cashflow.class)
                 .findAllSortedAsync("date", Sort.DESCENDING).asObservable()
                //.filter(result.isLoaded())
                .filter(result.isLoaded())
                .first()
                .subscribe(realmObject -> {
                    if (realmObject.isValid()) {
                        // Non-null realmObject
                    } else {
                        // null realmObject
                    }
                });
        return result;*//*
        return null;
    }*/

    public RealmResults<Cashflow> getCashflowInJar(String jarID) {
        //one month back
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, -1);
        return realm.where(Cashflow.class)
                .equalTo("jar.jar_id", jarID)
                .greaterThan("date", new Date(calendar.getTimeInMillis()))
                //.findAllSortedAsync("date", Sort.DESCENDING);
                .findAllSorted("date", Sort.DESCENDING);
    }

    public RealmResults<Cashflow> getCashflowInJarFromDate(String jarID, Date startDate, Date endDate) {

        return realm.where(Cashflow.class)
                //.greaterThanOrEqualTo("date", fromDayToNow)
                .between("date", startDate, endDate)
                .equalTo("jar.jar_id", jarID)
                .findAllSorted("date", Sort.DESCENDING);
    }

    public RealmResults<Cashflow> getCashflowsFromDate(Date startDate, Date endDate) {

        return realm.where(Cashflow.class)
                //.greaterThanOrEqualTo("date", fromDayToNow)
                .between("date", startDate, endDate)
                .findAllSorted("date", Sort.DESCENDING);
    }

    public void clearAllCashflow() {
        realm.delete(Cashflow.class);
    }

    public boolean deleteCashflow(long cashflowID) {
        Cashflow cash = realm.where(Cashflow.class).equalTo("id", cashflowID).findFirst();
        float cashInJar = cash.getJar().getTotalCash();
        if (cashInJar - cash.getSum() < 0) {
            return false;
        } else {
            realm.beginTransaction();
            Jar currentJar = cash.getJar();
            cash.deleteFromRealm();
            realm.commitTransaction();

            checkSumTotalInJar(currentJar.getJar_id());
            return true;
        }

    }

    public float checkSumTotalInJar(String jarID) {
        Jar jar = realm.where(Jar.class)
                .equalTo("jar_id", jarID).findFirst();
        double cashflowsInJar = (double) realm.where(Cashflow.class)
                .equalTo("jar.jar_id", jar.getJar_id())
                .findAll().sum("sum");

        realm.beginTransaction();
        //set field in jar
        jar.setTotalCash(((float) cashflowsInJar));
        realm.commitTransaction();

        return jar.getTotalCash();

    }

    public boolean addCashToJar(String jarID, float cashSum, Date date, int currPerc, String description) {

        Jar jar = realm.where(Jar.class).equalTo("jar_id", jarID).findFirst();

        //check for negative sum and rest in jar
        if ((jar.getTotalCash() + cashSum) < 0) {
            return false;
        } else {
            realm.beginTransaction();
            long id = realmManagerInstance.getJars().size() + System.currentTimeMillis();
            Cashflow cashflow = realm.createObject(Cashflow.class, id);
            //TODO path of photo
            cashflow.setDate(date);
            cashflow.setSum(cashSum);
            cashflow.setCurrpercent(currPerc);
            cashflow.setDescription(description);
            cashflow.setPhoto("");
            cashflow.setJar(jar);
            Log.d("VOlga", "New Cash : " + cashflow.getId() + ", " + cashflow.getDate() + ", "
                    + cashflow.getSum() + ", " + cashflow.getCurrpercent());

            realm.commitTransaction();
            realm.beginTransaction();
            float currTotal = realm.where(Jar.class)
                    .equalTo("jar_id", jarID)
                    .findFirst().getTotalCash();
            realm.commitTransaction();

            realm.beginTransaction();
            realm.where(Jar.class)
                    .equalTo("jar_id", jarID)
                    .findFirst().setTotalCash(currTotal + cashSum);
            realm.commitTransaction();
            return true;
        }
    }
}
