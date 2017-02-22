package com.vetroumova.sixjars.utils;

import android.util.Log;

import com.vetroumova.sixjars.R;
import com.vetroumova.sixjars.app.Prefs;
import com.vetroumova.sixjars.database.RealmManager;
import com.vetroumova.sixjars.model.Jar;

import io.realm.RealmResults;

/**
 * Created by OLGA on 17.10.2016.
 */
public class BottleDrawableManager {
    private static RealmManager realmManager = RealmManager.getInstance();
    private static int[] jarDrawableResource = {R.drawable.water_to_jar_empty,
            R.drawable.water_to_jar8, R.drawable.water_to_jar7, R.drawable.water_to_jar6,
            R.drawable.water_to_jar5, R.drawable.water_to_jar4, R.drawable.water_to_jar3,
            R.drawable.water_to_jar2, R.drawable.water_to_jar1, R.drawable.water_to_jar};
    //no empty anim
    private static int[] jarAnimResource = {R.drawable.jar_anim_empty, R.drawable.jar_anim_minus8,
            R.drawable.jar_anim_minus7, R.drawable.jar_anim_minus6, R.drawable.jar_anim_minus5,
            R.drawable.jar_anim_minus4, R.drawable.jar_anim_minus3, R.drawable.jar_anim_minus2,
            R.drawable.jar_anim_minus1, R.drawable.jar_anim};

    public static int chooseFullnesInJar(Prefs prefs, String jarId) {
        int item = 0;
        float sum = 0;
        float maxVolume = 0;
        if (jarId.equals("AllJars")) {
            RealmResults<Jar> jars = realmManager.getJars();
            for (Jar jar : jars) {
                sum += jar.getTotalCash();
                maxVolume += prefs.getMaxVolumeJar(jarId);
            }
        } else if (jarId.equals("NoID")) {
            Log.d("VOlga", "Wrong ID in BottleDrawableManager" + jarId);
            //will be empty bottle
        } else {
            Jar jar = realmManager.getJar(jarId);
            sum += jar.getTotalCash();
            maxVolume = prefs.getMaxVolumeJar(jarId);
        }
        Log.d("VOlga", "Sum " + sum + ", maxVolume " + maxVolume);
        if (sum > 0) {
            //TODO will be more iterable
            float partOfMax = sum / maxVolume;
            if (partOfMax < 0.0005f) {
                item = 0;
            } else if (partOfMax < 0.2f) {
                item = 1;
            } else if (partOfMax < 0.3f) {
                item = 2;
            } else if (partOfMax < 0.4f) {
                item = 3;
            } else if (partOfMax < 0.5f) {
                item = 4;
            } else if (partOfMax < 0.6f) {
                item = 5;
            } else if (partOfMax < 0.7f) {
                item = 6;
            } else if (partOfMax < 0.8f) {
                item = 7;
            } else if (partOfMax < 0.9f) {
                item = 8;
            } else {
                item = 9;
            }
        }
        return item;
    }

    public static int setDrawableJar(Prefs prefs, String jarId) {
        int neededItem = chooseFullnesInJar(prefs, jarId);
        return jarDrawableResource[neededItem];
    }

    public static int setAnimationJar(Prefs prefs, String jarId) {
        int neededItem = chooseFullnesInJar(prefs, jarId);
        return jarAnimResource[neededItem];
    }
}
