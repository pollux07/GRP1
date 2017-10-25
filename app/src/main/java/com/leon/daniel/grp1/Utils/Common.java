package com.leon.daniel.grp1.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Creado por Pollux.
 */

public class Common {
    public static final String LOG_TAG = "GSPROTO";
    public static final int RESPONSE_OK = 200;

    public static final String USERMAIL = "usermail";
    public static final String USER_ID = "user_id";

    public static void putPreference(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOG_TAG,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getPreference(Context context, String key, String defaultPrefer) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOG_TAG,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultPrefer);
    }

    public static void deletePreference(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOG_TAG,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(key);
        edit.apply();
    }
}
