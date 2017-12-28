package com.leon.daniel.grp1.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WAKE_LOCK;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Creado por Pollux.
 */

public class Common {

    //CODES
    public static final String LOG_TAG = "GSPROTO";
    public static final int RESPONSE_OK = 200;

    //STATUS
    public static final String OK_STATUS = "OK";
    public static final String USER_EXISTS_STATUS = "USER_EXISTS";
    public static final String USER_NOT_FOUND_STATUS = "USER_NOT_FOUND";

    //SHARED
    public static final String USER_EMAIL = "usermail";
    public static final String USER_ID = "user_id";

    //PERMISSIONS
    public static final int PERMISSION_REQUEST_CODE = 200;

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

    public static boolean checkExternalStoragePermission(Context context) {

        int result = ContextCompat.checkSelfPermission(context,
                WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;
    }
}
