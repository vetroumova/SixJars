package org.itstep.android5.vetroumova.newbeginning.sixjars.database;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Cashflow;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Jar;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by OLGA on 11.09.2016.
 */
public class RealmManager {

    //from Realm
    private static RealmManager realmManagerInstance;
    private final Realm realm;

    public RealmManager(Application application) {
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
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.where(Jar.class).findAll().deleteAllFromRealm();

                Jar jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_NEC));
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_NEC));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_NEC));
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_PLAY));
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_PLAY));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_PLAY));
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_GIVE));
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_GIVE));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_GIVE));
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                //TODO check
                //jar.setJar_id(context.getResources().getString(R.string.db_jar_id_EDU));
                jar.setJar_id("EDU");
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_EDU));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_EDU));
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_LTSS));
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_LTSS));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_LTSS));
                realm.copyToRealmOrUpdate(jar);

                jar = new Jar();
                jar.setJar_id(context.getResources().getString(R.string.db_jar_id_FFA));
                jar.setJar_name(context.getResources().getString(R.string.db_jar_name_FFA));
                jar.setJar_info(context.getResources().getString(R.string.db_jar_info_FFA));
                realm.copyToRealmOrUpdate(jar);
            }
        });
    }

    // for statistics
    @NonNull
    public static RealmResults<Jar> getAllJars() {
        final Realm realm = Realm.getDefaultInstance();
        final RealmResults<Jar> result = realm.where(Jar.class)
                //was findAllSortedAsync
                .findAllSorted(Jar.ID_FIELD);
        realm.close();
        return result;
    }

    public Realm getRealm() {
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

    /*public boolean editCash(long cashflowID, float cashSum, Date newDate, String description) {

        Cashflow cash = realm.where(Cashflow.class).equalTo("id", cashflowID).findFirst();
        float cashInJar = cash.getJar().getTotalCash();
        //TODO check for negatives
        if ((cashInJar - cash.getSum() + cashSum) < 0) {
            return false;
        }
        //TODO check if needed DatePick checking
        *//*else if(newDate > ) {

            }*//*
        else {
            realm.beginTransaction();
            Jar currentJar = cash.getJar();
            cash.setSum(cashSum);
            cash.setDate(newDate);
            cash.setDescription(description);
            realm.commitTransaction();
            checkSumTotalInJar(currentJar.getJar_id());
            return true;
        }
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

    public RealmResults<Cashflow> getAllCashflow() {
        return realm.where(Cashflow.class).findAllSorted("date", Sort.DESCENDING);
    }

    public RealmResults<Cashflow> getCashflowInJar(String jarID) {
        return realm.where(Cashflow.class)
                .equalTo("jar.jar_id", jarID)
                .findAllSorted("date", Sort.DESCENDING);
    }

    public RealmResults<Cashflow> getCashflowInJarFromDate(String jarID, Date fromDayToNow) {

        return realm.where(Cashflow.class)
                .greaterThanOrEqualTo("date", fromDayToNow)
                .equalTo("jar.jar_id", jarID)
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

    public boolean addCashToJar(String jarID, float cashSum, int currPerc, String description) {

        Jar jar = realm.where(Jar.class).equalTo("jar_id", jarID).findFirst();

        //check for negative sum and rest in jar
        if ((jar.getTotalCash() + cashSum) < 0) {
            return false;
        } else {
            realm.beginTransaction();
            long id = realmManagerInstance.getJars().size() + System.currentTimeMillis();
            Cashflow cashflow = realm.createObject(Cashflow.class, id);
            //TODO DatePicker, description from edittext, path of photo
            cashflow.setDate(new Date(System.currentTimeMillis()));
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
