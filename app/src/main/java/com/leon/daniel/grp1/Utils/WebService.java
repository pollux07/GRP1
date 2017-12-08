package com.leon.daniel.grp1.Utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Creado por Pollux.
 */

public class WebService {
    private static String URL_DEF = "10.0.2.2/ws/gs/1.0";
    private static int DEFAULT_TIME = 40000;

    public interface RequestListener {
        void onSucces(String response);
        void onError();
    }

    public static void registration(Context context,
                                    final Map<String, String> params,
                                    final RequestListener requestListener) {
        String url = String.format("%s/registration", URL_DEF);
        StringRequest registrationAction = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!isExpectedJson(response)) {
                            requestListener.onError();
                        }
                        requestListener.onSucces(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        registrationAction.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIME,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingelton.getInstance(context).addToRequestQueue(registrationAction);
    }

    public static void login(final Context context,
                                   final Map<String, String> params,
                                   final RequestListener requestListener) {
        String url = String.format("%s/login", URL_DEF);
        StringRequest loginAction = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!isExpectedJson(response)) {
                    requestListener.onError();
                }

                requestListener.onSucces(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.onError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        loginAction.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIME,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingelton.getInstance(context).addToRequestQueue(loginAction);
    }

    private static boolean isExpectedJson(String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String code = jsonResponse.getString("code");
            if (null == code) {
                return false;
            }
        } catch (JSONException e) {
            return false;
        }

        return true;
    }
}
