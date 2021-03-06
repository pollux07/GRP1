package com.leon.daniel.grp1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.daniel.grp1.Utils.Common;
import com.leon.daniel.grp1.Utils.VolleySingelton;
import com.leon.daniel.grp1.Utils.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    Context mCtx;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCtx = this;
        VolleySingelton.getInstance(getApplicationContext());
        // Set up the login form.
        mEmailView = findViewById(R.id.usermail);

        if (null != Common.getPreference(mCtx, Common.USER_EMAIL, null)) {
            mEmailView.setText(Common.getPreference(mCtx, Common.USER_EMAIL, null));
        }

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        TextView registrationTv = findViewById(R.id.registration);
        registrationTv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        registrationTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationDialog();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    /**
     * El usuario inicara el proceso de registro si aun no tiene una cuenta dada de alta
     */
    private void registrationDialog() {
        boolean cancel = false;
        View focusView = null;

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mCtx);
        alertBuilder.setView(R.layout.registration_dialog);

        final AlertDialog dialog = alertBuilder.create();
        dialog.show();
        final TextInputEditText emailEt = dialog.findViewById(R.id.et_email);
        final TextInputEditText passEt = dialog.findViewById(R.id.et_psw);
        TextInputEditText confirmPswEt = dialog.findViewById(R.id.et_confirm_psw);
        Button registerBtn = dialog.findViewById(R.id.btn_register);
        assert emailEt != null;
        assert passEt != null;
        assert confirmPswEt != null;
        assert registerBtn != null;
        final String pass = passEt.getText().toString();
        String confirmPwd = confirmPswEt.getText().toString();
        if (pass.equals(confirmPwd)) {
            registerBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = emailEt.getText().toString();
                    String pwd = passEt.getText().toString();
                    Log.d(Common.LOG_TAG, "PASSWORD: " + pwd);
                    final Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("pwd", pwd);
                    registrationAction(params);
                    dialog.dismiss();
                }
            });
        } else {
            confirmPswEt.setError(getString(R.string.different_psw));
            focusView = confirmPswEt;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
    }

    private void registrationAction(Map<String, String> params) {
        final ProgressDialog dialog = new ProgressDialog(mCtx);
        dialog.setMessage("Cargando");
        dialog.show();
        WebService.registration(mCtx, params, new WebService.RequestListener() {
            @Override
            public void onSucces(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int code = jsonResponse.getInt("code");
                    String status= jsonResponse.getString("status");
                    if (code == Common.RESPONSE_OK && status.equals(Common.OK_STATUS)) {
                        dialog.dismiss();
                        Toast.makeText(mCtx, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    } else if (code == Common.RESPONSE_OK && status.equals(Common.USER_EXISTS_STATUS)) {
                        dialog.dismiss();
                        Toast.makeText(mCtx, "Este correo ya esta registrado", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }

            @Override
            public void onError() {
                dialog.dismiss();
                Toast.makeText(mCtx, "Error de comunicaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            loginAction(email, password);

        }
    }

    private void loginAction(final String email, String password) {
        showProgress(true);
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("pwd", password);
        WebService.login(mCtx, params, new WebService.RequestListener() {
            @Override
            public void onSucces(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int code = jsonResponse.getInt("code");
                    String status= jsonResponse.getString("status");
                    if (code == Common.RESPONSE_OK && status.equals(Common.OK_STATUS)) {
                        int userId = jsonResponse.getInt("user_id");

                        Common.putPreference(mCtx, Common.USER_ID, String.format("%s", userId));
                        Common.putPreference(mCtx, Common.USER_EMAIL, String.format("%s", email));

                        showProgress(false);

                        Intent mainIntent = new Intent(mCtx, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(mainIntent);
                        finish();

                    } else if (code == Common.RESPONSE_OK && status.equals(Common.USER_NOT_FOUND_STATUS)) {
                        showProgress(false);
                        Toast.makeText(mCtx, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showProgress(false);
                    Toast.makeText(mCtx, "Error de comunicaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {
                showProgress(false);
                Toast.makeText(mCtx, "Error de comunicaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        //int IS_PRIMARY = 1;
    }
}

