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

/**
 * Web service que realiza la conexion asincrona a un servidor remoto o local por medio
 * de la libreria VolleySingelton.
 */
public class WebService {
    private static String URL_DEF = "http://192.168.0.84/ws/gs/1.0";
    private static int DEFAULT_TIME = 40000;

    public interface RequestListener {
        void onSucces(String response);
        void onError();
    }

    /**
     * Método que realiza la llamada al Web Service de registro para poder dar de alta a un usuario
     * que no se encuentra registrado en la base de datos mandando como parametros
     * Correo y Contraseña.
     * @param context contexto de la actividad que lo esta mandando a llamar.
     * @param params parametros que se enviaran al web service (email, password).
     * @param requestListener el resultado que nos arroja el web service.
     */
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

    /**
     * Método que realiza el inicio de sesión de un usuario por medio de su correo y contraseña
     * enviando como resultado la existencia de dicho correo en la base de datos.
     * @param context contexto de la actividad que lo esta mandando a llamar.
     * @param params parametros que se enviaran al web service (email, password).
     * @param requestListener el resultado que nos arroja el web service.
     */
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

    /**
     * Método que se encarga de cargar los datos del perfil del usuario por medio de su ID único
     * @param context contexto de la actividad que lo esta mandando a llamar.
     * @param params parametros que se enviaran al web service (email, password).
     * @param requestListener el resultado que nos arroja el web service.
     */
    public static void loadProfile(final Context context,
                                   final Map<String, String> params,
                                   final RequestListener requestListener) {
        String url = String.format("%s/loadProfile", URL_DEF);
        StringRequest loadProfileAction = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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

        loadProfileAction.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIME,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingelton.getInstance(context).addToRequestQueue(loadProfileAction);
    }

    /**
     * Método que realiza la actualizacion del perfil del usuario
     * @param context contexto de la actividad que lo esta mandando a llamar.
     * @param params parametros que se enviaran al web service (datos del usuario).
     * @param requestListener el resultado que nos arroja el web service.
     */
    public static void sendUserInfo(final Context context,
                                    final Map<String, String> params,
                                    final RequestListener requestListener) {
        String url = String.format("%s/updateUserProfile", URL_DEF);
        StringRequest sendUserInfoAction = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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

        sendUserInfoAction.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIME,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingelton.getInstance(context).addToRequestQueue(sendUserInfoAction);
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
