package org.itstep.android5.vetroumova.newbeginning.sixjars.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OLGA on 14.09.2016.
 */
public class Prefs {

    private static final String PRE_LOAD = "preLoad";
    private static final String PREFS_NAME = "prefs";
    private static Prefs prefsInstance;
    private final SharedPreferences sharedPreferences;
    private static final String PERC_NEC = "NEC";
    private static final String PERC_PLAY = "PLAY";
    private static final String PERC_GIVE = "GIVE";
    private static final String PERC_EDU = "EDU";
    private static final String PERC_LTSS = "LTSS";
    private static final String PERC_FFA = "FFA";

    public Prefs(Context context) {

        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static Prefs with(Context context) {

        if (prefsInstance == null) {
            prefsInstance = new Prefs(context);
        }
        return prefsInstance;
    }

    public void setPreLoad(boolean totalTime) {

        sharedPreferences
                .edit()
                .putBoolean(PRE_LOAD, totalTime)
                .apply();
    }

    public boolean getPreLoad() {
        return sharedPreferences.getBoolean(PRE_LOAD, false);
    }

    public void setPercentage(List<Integer> percentJars) {
        sharedPreferences
                .edit()
                .putInt(PERC_NEC, percentJars.get(0))
                .putInt(PERC_PLAY, percentJars.get(1))
                .putInt(PERC_GIVE, percentJars.get(2))
                .putInt(PERC_EDU, percentJars.get(3))
                .putInt(PERC_LTSS, percentJars.get(4))
                .putInt(PERC_FFA, percentJars.get(5))
                .apply();
    }

    public List<Integer> getPercentage() {
        List<Integer> percentJars = new ArrayList<>();
        percentJars.add(sharedPreferences.getInt(PERC_NEC, 55));
        percentJars.add(sharedPreferences.getInt(PERC_PLAY, 10));
        percentJars.add(sharedPreferences.getInt(PERC_GIVE, 5));
        percentJars.add(sharedPreferences.getInt(PERC_EDU, 10));
        percentJars.add(sharedPreferences.getInt(PERC_LTSS, 10));
        percentJars.add(sharedPreferences.getInt(PERC_FFA, 10));
        return percentJars;
    }

    public int getPercentJar(String id) {
        int percent = 0;
        switch (id) {
            case PERC_NEC: {
                percent = sharedPreferences.getInt(PERC_NEC, 55);
                break;
            }
            case PERC_PLAY: {
                percent = sharedPreferences.getInt(PERC_PLAY, 10);
                break;
            }
            case PERC_GIVE: {
                percent = sharedPreferences.getInt(PERC_GIVE, 5);
                break;
            }
            case PERC_EDU: {
                percent = sharedPreferences.getInt(PERC_EDU, 10);
                break;
            }
            case PERC_LTSS: {
                percent = sharedPreferences.getInt(PERC_LTSS, 10);
                break;
            }
            case PERC_FFA: {
                percent = sharedPreferences.getInt(PERC_FFA, 10);
                break;
            }
        }
        return percent;
    }
}
