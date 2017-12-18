package com.leon.daniel.grp1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.leon.daniel.grp1.Utils.Common;
import com.leon.daniel.grp1.Utils.VolleySingelton;
import com.leon.daniel.grp1.Utils.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mCtx = this;
        VolleySingelton.getInstance(getApplicationContext());

        final AutoCompleteTextView etName = (AutoCompleteTextView) findViewById(R.id.et_name);
        final AutoCompleteTextView etLastName = (AutoCompleteTextView) findViewById(R.id.et_last1);
        final AutoCompleteTextView etUsername = (AutoCompleteTextView) findViewById(R.id.et_username);
        final AutoCompleteTextView etPhone = (AutoCompleteTextView) findViewById(R.id.et_phone);
        final AutoCompleteTextView etCellphone = (AutoCompleteTextView) findViewById(R.id.et_cellphone);

        Button btnUpdate = (Button) findViewById(R.id.btn_register);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = Common.getPreference(mCtx, Common.USER_ID, null);

                String name = etName.getText().toString();
                String lastName = etLastName.getText().toString();
                String userName = etUsername.getText().toString();
                String phone = etPhone.getText().toString();
                String cellPhone = etCellphone.getText().toString();

                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("view", "1");
                params.put("name", name);
                params.put("last_name", lastName);
                params.put("username", userName);
                params.put("phone", phone);
                params.put("cellphone", cellPhone);
                
                sendUserInfo(params);
            }
        });
    }

    private void sendUserInfo(Map<String, String> params) {
        final ProgressDialog dialog = new ProgressDialog(mCtx);
        dialog.setMessage("Cargando...");
        dialog.show();

        WebService.sendUserInfo(mCtx, params, new WebService.RequestListener() {
            @Override
            public void onSucces(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int code = jsonResponse.getInt("code");
                    String status = jsonResponse.getString("status");

                    if (code == Common.RESPONSE_OK && status.equals(Common.OK_STATUS)) {
                        dialog.dismiss();
                        Toast.makeText(mCtx,
                                "Perfil actualizado con éxito",
                                Toast.LENGTH_SHORT)
                                .show();

                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(mainActivity);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(mCtx, "Error de comunicaciones", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError() {
                dialog.dismiss();
                Toast.makeText(mCtx, "Error de comunicaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
