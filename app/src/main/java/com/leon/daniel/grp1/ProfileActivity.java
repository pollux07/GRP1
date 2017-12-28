package com.leon.daniel.grp1;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.leon.daniel.grp1.Utils.Common;
import com.leon.daniel.grp1.Utils.VolleySingelton;
import com.leon.daniel.grp1.Utils.WebService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ProfileActivity extends AppCompatActivity {
    Context mCtx;
    LinearLayout mLinearProfile;

    ImageView mProfileImage;

    private int CROP_IMAGE = 1;
    public static final int GET_FROM_GALLERY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mCtx = this;
        VolleySingelton.getInstance(getApplicationContext());
        mLinearProfile = findViewById(R.id.linear_profile);
        mProfileImage = findViewById(R.id.iv_profile_picture);

        final AutoCompleteTextView etName = findViewById(R.id.et_name);
        final AutoCompleteTextView etLastName = findViewById(R.id.et_last1);
        final AutoCompleteTextView etUsername = findViewById(R.id.et_username);
        final AutoCompleteTextView etPhone = findViewById(R.id.et_phone);
        final AutoCompleteTextView etCellphone = findViewById(R.id.et_cellphone);

        Button btnUpdate = findViewById(R.id.bt_continue_profile);

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.checkExternalStoragePermission(mCtx)) {
                    requestPermission();
                } else {
                    startActivityForResult(new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                            GET_FROM_GALLERY);
                }
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageProfile = data.getData();
            Bitmap bitmap;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(),
                        selectedImageProfile);

                mProfileImage.setImageBitmap(getCircleBitmap(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void CropImage() {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            //intent.setDataAndType(picUri, "image/*");

            intent.putExtra("crop", "true");
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 3);
            intent.putExtra("aspectY", 4);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("return-data", true);

            startActivityForResult(intent, CROP_IMAGE);

        } catch (ActivityNotFoundException e) {
            //Common.showToast(this, "Your device doesn't support the crop action!");
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }


    //METODOS PARA PEDIR PERMISOS PARA TENER ACCESO AL ALMACENAMIENTO DEL TELEFONO

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE},
                Common.PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults){
        switch (requestCode){
            case Common.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0){

                    boolean writeESAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (!writeESAccepted){
                        showSnackBar();
                    } else{
                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                                GET_FROM_GALLERY);
                    }
                }
        }
    }

    private void showSnackBar() {

        Snackbar.make(mLinearProfile, R.string.permissions,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openSettings();
                    }
                })
                .show();
    }

    public void openSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
