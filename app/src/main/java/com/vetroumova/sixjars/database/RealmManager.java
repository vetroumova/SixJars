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
import com.vetroumova.sixjars.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
    //private static final String EXPORT_PREFS_FILE_NAME = "preferences_sixjars.xml";
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

                realm.where(User.class).findAll().deleteAllFromRealm();
                realm.where(Jar.class).findAll().deleteAllFromRealm();
                realm.where(Cashflow.class).findAll().deleteAllFromRealm();

                User user = new User();
                //todo work with multiple users
                user.setLogin("test");
                //for backup without sharedprefs export/import
                Prefs.with(context).setPrefUser(user.getLogin());

                Jar jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_NEC));
                jar.setJar_float_id(0f);
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_NEC));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_NEC));
                jar.setUser(user);
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_PLAY));
                jar.setJar_float_id(1f);
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_PLAY));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_PLAY));
                jar.setUser(user);
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_GIVE));
                jar.setJar_float_id(2f);
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_GIVE));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_GIVE));
                jar.setUser(user);
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id("EDU");
                jar.setJar_float_id(3f);
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_EDU));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_EDU));
                jar.setUser(user);
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_LTSS));
                jar.setJar_float_id(4f);
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_LTSS));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_LTSS));
                jar.setUser(user);
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_FFA));
                jar.setJar_float_id(5f);
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_FFA));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_FFA));
                jar.setUser(user);
                realm.copyToRealmOrUpdate(jar);

                //setUserPrefsFromSharedPrefs(context, user);
            }
        });
    }

    public static void setUserPrefsFromSharedPrefs(Context context, User user) {
        final Realm realm = RealmManager.getInstance().getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Prefs prefs = Prefs.with(context);
                user.setLanguage(prefs.getPrefLanguage());
                user.setPreLoad(prefs.getPreLoad());
                user.setNecPerc(prefs.getPercentJar("NEC"));
                user.setPlayPerc(prefs.getPercentJar("PLAY"));
                user.setGivePerc(prefs.getPercentJar("GIVE"));
                user.setEduPerc(prefs.getPercentJar("EDU"));
                user.setLtssPerc(prefs.getPercentJar("LTSS"));
                user.setFfaPerc(prefs.getPercentJar("FFA"));
                user.setNecMaxVolume(prefs.getMaxVolumeJar("NEC"));
                user.setPlayMaxVolume(prefs.getMaxVolumeJar("PLAY"));
                user.setGiveMaxVolume(prefs.getMaxVolumeJar("GIVE"));
                user.setEduMaxVolume(prefs.getMaxVolumeJar("EDU"));
                user.setLtssMaxVolume(prefs.getMaxVolumeJar("LTSS"));
                user.setFfaMaxVolume(prefs.getMaxVolumeJar("FFA"));


                /*Toast.makeText(context,"perc " + prefs.getPercentJar("NEC"),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"perc " + prefs.getPercentJar("PLAY"),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"perc " + prefs.getPercentJar("GIVE"),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"perc " + prefs.getPercentJar("EDU"),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"perc " + prefs.getPercentJar("LTSS"),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"perc " + prefs.getPercentJar("FFA"),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"max " + prefs.getMaxVolumeJar("NEC"),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"max " + prefs.getMaxVolumeJar("PLAY"),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"max " + prefs.getMaxVolumeJar("GIVE"),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"max " + prefs.getMaxVolumeJar("EDU"),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"max " + prefs.getMaxVolumeJar("LTSS"),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"max " + prefs.getMaxVolumeJar("FFA"),Toast.LENGTH_SHORT).show();

                Toast.makeText(context,prefs.getPrefLanguage(),Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "restore " + prefs.getPrefRestoreMark(),Toast.LENGTH_SHORT).show();*/

                Log.d("VOlga", "perc " + prefs.getPercentJar("NEC"));
                Log.d("VOlga", "perc " + prefs.getPercentJar("PLAY"));
                Log.d("VOlga", "perc " + prefs.getPercentJar("GIVE"));
                Log.d("VOlga", "perc " + prefs.getPercentJar("EDU"));
                Log.d("VOlga", "perc " + prefs.getPercentJar("LTSS"));
                Log.d("VOlga", "perc " + prefs.getPercentJar("FFA"));
                Log.d("VOlga", "max " + prefs.getMaxVolumeJar("NEC"));
                Log.d("VOlga", "max " + prefs.getMaxVolumeJar("PLAY"));
                Log.d("VOlga", "max " + prefs.getMaxVolumeJar("GIVE"));
                Log.d("VOlga", "max " + prefs.getMaxVolumeJar("EDU"));
                Log.d("VOlga", "max " + prefs.getMaxVolumeJar("LTSS"));
                Log.d("VOlga", "max " + prefs.getMaxVolumeJar("FFA"));

                Log.d("VOlga", prefs.getPrefLanguage());
                Log.d("VOlga", "restore " + prefs.getPrefRestoreMark());

                Log.d("VOlga", "perc " + user.getNecPerc());
                Log.d("VOlga", "perc " + user.getPlayPerc());
                Log.d("VOlga", "perc " + user.getGivePerc());
                Log.d("VOlga", "perc " + user.getEduPerc());
                Log.d("VOlga", "perc " + user.getLtssPerc());
                Log.d("VOlga", "perc " + user.getFfaPerc());
                Log.d("VOlga", "max " + user.getNecMaxVolume());
                Log.d("VOlga", "max " + user.getPlayMaxVolume());
                Log.d("VOlga", "max " + user.getGiveMaxVolume());
                Log.d("VOlga", "max " + user.getEduMaxVolume());
                Log.d("VOlga", "max " + user.getLtssMaxVolume());
                Log.d("VOlga", "max " + user.getFfaMaxVolume());

                Log.d("VOlga", user.getLanguage());

            }
        });
    }

    public static void loadUserPrefsToSharedPrefs(Context context, User user) {
        Prefs prefs = Prefs.with(context);
        prefs.setPrefLanguage(user.getLanguage());
        prefs.setPreLoad(true); //todo check need
        List<Integer> percentageList = Arrays.asList(user.getNecPerc(), user.getPlayPerc(),
                user.getGivePerc(), user.getEduPerc(), user.getLtssPerc(), user.getFfaPerc());
        prefs.setPercentage(percentageList);
        //change a max volumes for bottles from user data
        float[] sums = {user.getNecMaxVolume(), user.getPlayMaxVolume(), user.getGiveMaxVolume(),
                user.getEduMaxVolume(), user.getLtssMaxVolume(), user.getFfaMaxVolume()};
        prefs.with(context).setMaxVolumes(sums);

        Log.d("VOlga", "perc " + prefs.getPercentJar("NEC"));
        Log.d("VOlga", "perc " + prefs.getPercentJar("PLAY"));
        Log.d("VOlga", "perc " + prefs.getPercentJar("GIVE"));
        Log.d("VOlga", "perc " + prefs.getPercentJar("EDU"));
        Log.d("VOlga", "perc " + prefs.getPercentJar("LTSS"));
        Log.d("VOlga", "perc " + prefs.getPercentJar("FFA"));
        Log.d("VOlga", "max " + prefs.getMaxVolumeJar("NEC"));
        Log.d("VOlga", "max " + prefs.getMaxVolumeJar("PLAY"));
        Log.d("VOlga", "max " + prefs.getMaxVolumeJar("GIVE"));
        Log.d("VOlga", "max " + prefs.getMaxVolumeJar("EDU"));
        Log.d("VOlga", "max " + prefs.getMaxVolumeJar("LTSS"));
        Log.d("VOlga", "max " + prefs.getMaxVolumeJar("FFA"));

        Log.d("VOlga", prefs.getPrefLanguage());
        Log.d("VOlga", "restore " + prefs.getPrefRestoreMark());

        Log.d("VOlga", "perc " + user.getNecPerc());
        Log.d("VOlga", "perc " + user.getPlayPerc());
        Log.d("VOlga", "perc " + user.getGivePerc());
        Log.d("VOlga", "perc " + user.getEduPerc());
        Log.d("VOlga", "perc " + user.getLtssPerc());
        Log.d("VOlga", "perc " + user.getFfaPerc());
        Log.d("VOlga", "max " + user.getNecMaxVolume());
        Log.d("VOlga", "max " + user.getPlayMaxVolume());
        Log.d("VOlga", "max " + user.getGiveMaxVolume());
        Log.d("VOlga", "max " + user.getEduMaxVolume());
        Log.d("VOlga", "max " + user.getLtssMaxVolume());
        Log.d("VOlga", "max " + user.getFfaMaxVolume());

        Log.d("VOlga", user.getLanguage());
    }

    public void changeUserPassword(User user, String newPass) {
        realm.beginTransaction();
        user.setPassword(newPass);
        realm.commitTransaction();
    }

    /**
     * localisation name
     */
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
                //File exportPrefsFile = new File(EXPORT_REALM_PATH, EXPORT_PREFS_FILE_NAME);

                // if backup file already exists, delete it
                exportRealmFile.delete();
                //exportPrefsFile.delete();

                // copy current realm to backup file
                realm.writeCopyTo(exportRealmFile);
                //Prefs.with(context).saveSharedPreferencesToFile(exportPrefsFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
            String msg = context.getString(R.string.db_saved_text) + EXPORT_REALM_PATH + "/"
                    + EXPORT_REALM_FILE_NAME /*+ context.getString(R.string.prefs_saved_text) +
                    EXPORT_REALM_PATH + "/" + EXPORT_PREFS_FILE_NAME*/;
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            Log.d(TAG, msg);
            //realm.close();
        } else {
            // Failure to find a directory
        }
    }

    public void restore(Context context) {
        String restoreFilePath = EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;
        //String restorePrefsPath = EXPORT_REALM_PATH + "/" + EXPORT_PREFS_FILE_NAME;
        Log.d(TAG, "oldFilePath = " + restoreFilePath);

        File file = new File(restoreFilePath);
        if (file.exists() && file.isFile()) {
            copyBundledRealmFile(context, restoreFilePath, IMPORT_REALM_FILE_NAME);
            Log.d(TAG, "DB restore is done");
            Toast.makeText(context, R.string.db_restore_done_text, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "DB Restore file is not found");
            Toast.makeText(context, context.getString(R.string.restore_not_found), Toast.LENGTH_SHORT).show();
        }
        /*File filePrefs = new File(restorePrefsPath);
        if (filePrefs.exists() && filePrefs.isFile()) {
            //Prefs.with(context).loadSharedPreferencesFromFile(filePrefs);
            boolean result = Prefs.with(context).restoreUserPrefs(filePrefs);
            if (result) {
                Log.d(TAG, "Preferences restore is done");
                Toast.makeText(context, R.string.prefs_restore_done_text, Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Preferences not restored");
                Toast.makeText(context, R.string.prefs_not_restored_text, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Log.d(TAG, "Preferences restore file is not found");
            Toast.makeText(context, context.getString(R.string.restore_not_found), Toast.LENGTH_SHORT).show();
        }*//*
        //todo check
        loadUserPrefsToSharedPrefs(context,realmManagerInstance.getJar("NEC").getUser());*/
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
                    inputStream.close();    //todo check
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

        /*File exportPrefsFile = new File(activity.getExternalCacheDir(), "export_prefs.xml");
        exportPrefsFile.delete();
        Prefs.with(activity).saveSharedPreferencesToFile(exportPrefsFile);*/

        ArrayList<Uri> arrayUri = new ArrayList<Uri>();
        Uri realmUri = Uri.fromFile(exportRealmFile);
        //Uri prefsUri = Uri.fromFile(exportPrefsFile);
        if (realmUri != null) {
            arrayUri.add(realmUri);
        }/*
        if (prefsUri != null) {
            arrayUri.add(prefsUri);
        }*/
        // init email intent and add export.realm as attachment
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_EMAIL, activity.getString(R.string.export_mail_text));
        intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.export_subject_text));
        intent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.export_message_text));
        if (arrayUri.isEmpty()) {
            //Send email without file attached
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("plain/text");
        } else if (arrayUri.size() == 1) {
            //Send email with ONE file attached
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, arrayUri.get(0));
            intent.setType("plain/*");
        } else {
            //Send email with MULTI files attached
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayUri);
            intent.setType("plain/*");
        }
        // start email intent
        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.send_base_text)));
    }

    /*public String savePrefs(Context context) {
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
    }*/

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

        Jar jar = new Jar();
        try {
            jar = realm.where(Jar.class).equalTo("jar_id", id).findFirst();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return jar;
    }

    /*public void changeColorOfJar(String jar_id, int color) {
        Jar jar = realm.where(Jar.class).equalTo("jar_id",jar_id).findFirst();
        realm.beginTransaction();
        jar.setJar_color(color);
        realm.commitTransaction();
    }

    public List<Integer> getColors() {
        List<Integer> listOfColors = new ArrayList<Integer>();
        RealmResults<Jar> jars = realm.where(Jar.class).findAll();
        for (Jar jar:jars) {
            listOfColors.add(jar.getJar_color());
        }
        return listOfColors;
    }*/

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
        calendar.add(Calendar.MONTH, -3);
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
