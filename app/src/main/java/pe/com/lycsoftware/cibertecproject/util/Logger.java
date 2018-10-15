package pe.com.lycsoftware.cibertecproject.util;

import android.util.Log;

import pe.com.lycsoftware.cibertecproject.BuildConfig;

public class Logger
{
    
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable t) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg, t);
        }
    }
}
