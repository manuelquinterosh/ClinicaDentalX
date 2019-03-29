package com.developer.manuelquinteros.clinicadentalx;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.developer.manuelquinteros.clinicadentalx.model.Users;
import com.developer.manuelquinteros.clinicadentalx.prefs.UserSessionManager;
import com.developer.manuelquinteros.clinicadentalx.volley.VolleyUse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;

    private TextView registerText;
    private TextInputEditText editTextUser, editTextPassword;
    private Button btnLogIn;
    private ImageView mLogoView;
    private TextInputLayout mLabelUser;
    private TextInputLayout mLabelPassword;

    UserSessionManager session;
    private RequestQueue request;
    private VolleyUse volley;
    String URL = "http://codonticapp.000webhostapp.com/logeo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogoView = (ImageView) findViewById(R.id.imgLogo);
        editTextUser = (TextInputEditText) findViewById(R.id.edUsername);
        editTextPassword = (TextInputEditText) findViewById(R.id.edPassword);
        btnLogIn = (Button) findViewById(R.id.bt_login);
        registerText = (TextView) findViewById(R.id.tv_register);
        mLabelUser = (TextInputLayout) findViewById(R.id.label_user_msg);
        mLabelPassword = (TextInputLayout) findViewById(R.id.label_password_msg);

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);

        volley = VolleyUse.getInstance(this);
        request = volley.getRequestQueue();

        session = new UserSessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isUserLogedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterActivity();
            }
        });


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOnline()) {
                    showLoginError("Error de red, por favor, verifique su conexion");
                    return;
                }
                Authentication();
            }

        });
    }

    private void Authentication () {

        // Reset errors.
        mLabelUser.setError(null);
        mLabelPassword.setError(null);

        final String username = editTextUser.getText().toString();
        final String password = editTextPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mLabelPassword.setError(getString(R.string.error_campo_vacio));
            focusView = mLabelPassword;
            cancel = true;
        } else if (!passwordValid(password)) {
            mLabelPassword.setError(getString(R.string.error_password_invalida));
            focusView = mLabelPassword;
            cancel = true;
        }

        //Verificar si el usuario no esta vacio
        if (TextUtils.isEmpty(username)) {
            mLabelUser.setError(getString(R.string.error_campo_vacio));
            focusView = mLabelUser;
            cancel = true;
        } else if (!nameUserValid(username)) {
            mLabelUser.setError(getString(R.string.error_usuario_campo));
            focusView = mLabelUser;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();
        } else {


            showProgress(true);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    // ocultar progreso
                    showProgress(false);

                    Users user = new Users();

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        int success = jsonObject.getInt("success");

                        if (success == 1) {
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);

                                user.setIdUser(jsonObject.getJSONArray("login").getJSONObject(0).getInt("idUser"));

                                String usuario = c.getString("usuario").trim();
                                String nombre = c.getString("nombre").trim();
                                String email = c.getString("email").trim();
                                String telefono = c.getString("telefono").trim();
                                String direccion = c.getString("direccion").trim();
                                String contrasena = c.getString("contrasena").trim();
                                String id = c.getString("idUser").trim();

                                session.createUserLoginSession(usuario, nombre, email, telefono, direccion, contrasena, id);
                            }
                            showMainActivity();

                        } else {
                            showLoginError("Usuario y Contraseña Incorrecta");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Un error", "" + error);
                    Toast.makeText(getApplicationContext(), "error " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("usuario", username);
                    param.put("contrasena", password);
                    return param;
                }
            };

            VolleyUse.addToQueue(stringRequest, request, this, volley);
        }
    }

    private boolean nameUserValid(String name){
        return name.length() >=4;
    }

    private boolean passwordValid(String password) {
        return password.length() >= 4;
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public void showRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
        startActivity(intent);
    }

    public void showMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        int visibility = show ? View.GONE : View.VISIBLE;
        mLogoView.setVisibility(visibility);
        mLoginFormView.setVisibility(visibility);
    }
}
