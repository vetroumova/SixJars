package org.itstep.android5.vetroumova.newbeginning.sixjars.app;

import android.app.Application;

import org.itstep.android5.vetroumova.newbeginning.sixjars.database.DatabaseConfigurator;

/**
 * Created by OLGA on 12.09.2016.
 */
public class App extends Application {

    //Application is a Singleton

    @Override
    public void onCreate() {
        super.onCreate();
        // in "Realm" all stuff in App, not in DatabaseConfigurator
        DatabaseConfigurator.configureDatabase(getApplicationContext());
    }
}
