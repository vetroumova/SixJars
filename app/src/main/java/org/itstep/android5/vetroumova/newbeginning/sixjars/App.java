package org.itstep.android5.vetroumova.newbeginning.sixjars;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by OLGA on 12.09.2016.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(config);
    }
}
