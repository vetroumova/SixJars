package org.itstep.android5.vetroumova.newbeginning.sixjars.app;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by OLGA on 12.09.2016.
 */
public class App extends Application {

    //Application is a Singleton

    private static final int SCHEMA_VERSION = 0;

    private static final String REALM_NAME = App.class.getPackage().getName() + ".realm";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        // in "Realm" all stuff in App, not in DatabaseConfigurator
        /*DatabaseConfigurator.configureDatabase(getApplicationContext());*/
        //getApplicationContext() was in parameters

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
