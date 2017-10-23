package com.leon.daniel.grp1.Utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Creado por Pollux.
 */

public class VolleySingelton {
    @SuppressLint("StaticFieldLeak")
    private static VolleySingelton myInstance;

    private RequestQueue queue;

    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;

    private VolleySingelton(Context context) {
        mCtx = context;
        queue = getRequestQueue();
    }

    public static synchronized VolleySingelton getInstance(Context context) {
        if (null == myInstance) {
            myInstance = new VolleySingelton(context);
        }

        return myInstance;
    }

    private RequestQueue getRequestQueue() {
        if (null == queue) {
            queue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return queue;
    }

    <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
