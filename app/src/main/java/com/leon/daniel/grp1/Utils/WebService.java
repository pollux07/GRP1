package com.leon.daniel.grp1.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Creado por Pollux.
 */

public class WebService {
    private static String URL_DEF = "http://www.pollux792.000webhostapp.com/ws/gs/1.0";
    private static int DEFAULT_TIME = 40000;

    public interface RequestListener {
        void onSucces(String response);
        void onError();
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
