package org.itstep.android5.vetroumova.newbeginning.sixjars.database;

import android.content.Context;
import android.support.annotation.NonNull;

import org.itstep.android5.vetroumova.newbeginning.sixjars.app.App;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by OLGA on 18.07.2016.
 */
public final class DatabaseConfigurator {

    //private static final int SCHEMA_VERSION = 1;
    //added friends
    private static final int SCHEMA_VERSION = 0;

    private static final String REALM_NAME = App.class.getPackage().getName() + ".realm";

    private DatabaseConfigurator() {
    }

    public static void configureDatabase(@NonNull final Context context) {
        //@NonNull final Context context was in parameters
        Realm.setDefaultConfiguration(getConfiguration(context));
    }

    @NonNull
    private static RealmConfiguration getConfiguration(@NonNull final Context context) {
        /*private static RealmConfiguration getConfiguration(@NonNull final Context context) {*/
        return new RealmConfiguration.Builder()
                .name(REALM_NAME)
                .schemaVersion(SCHEMA_VERSION)
                //.deleteRealmIfMigrationNeeded()
                .build();
        /*.Builder(context)
                .name(REALM_NAME)
                .schemaVersion(SCHEMA_VERSION)*//*
                .migration(getMigration())*//*
                //added from "Realm" project
                //.deleteRealmIfMigrationNeeded()
                .build();*/
    }

/*    @NonNull
    private static RealmMigration getMigration() {
        return (realm, oldVersion, newVersion) -> {
            final RealmSchema schema = realm.getSchema();
            if(oldVersion ==1) {
                schema.get("Jar")
                        .addRealmListField(Jar.COLOR_FIELD, schema.get("Jar"));
            }
        };
    }*/

}
