package org.itstep.android5.vetroumova.newbeginning.sixjars.app;

import android.app.Application;

import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Migration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by OLGA on 12.09.2016.
 */
public class App extends Application {

    //Application is a Singleton

    private static final int SCHEMA_VERSION = 0;

    //public static final String REALM_NAME = App.class.getPackage().getName() + ".realm";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());

        //todo normal migration
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                //.name(REALM_NAME)
                .schemaVersion(SCHEMA_VERSION)
                //you can set .deleteRealmIfMigrationNeeded() if you don't want to bother with migrations.
                // WARNING: This will delete all data in the Realm though.
                //.deleteRealmIfMigrationNeeded()

                // Or you can add the migration code to the configuration.
                // This will run the migration code without throwing
                // a RealmMigrationNeededException.
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);


    }
}
