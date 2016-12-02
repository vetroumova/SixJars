package com.vetroumova.sixjars.utils;

import android.support.design.BuildConfig;
import android.util.Log;

/**
 * Created by OLGA on 01.09.2016.
 */
public class DebugLogger {
    private static final String TAG = "VOlga";

    public static void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static void logThread(String message) {
        if (BuildConfig.DEBUG) {
            String thread = Thread.currentThread().getName();
            Log.d(TAG, thread + ": " + message);
        }
    }

}
