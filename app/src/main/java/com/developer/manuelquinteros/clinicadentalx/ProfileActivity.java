package com.developer.manuelquinteros.clinicadentalx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.developer.manuelquinteros.clinicadentalx.prefs.UserSessionManager;
import com.developer.manuelquinteros.clinicadentalx.volley.VolleyUse;

import org.json.JSONArray;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    UserSessionManager session;
    TextView userTxt, nameTxt, emailTxt, phoneTxt, addressTxt, passwordTxt, active, complete, cancel;
    ImageView backImg,  imgModify;
    CircleImageView imgProfileUser;

    private RequestQueue request;
    private VolleyUse volley;
    private JSONArray result;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new UserSessionManager(getApplicationContext());

        volley = VolleyUse.getInstance(this);
        request = volley.getRequestQueue();

        nameTxt = (TextView) findViewById(R.id.tv_name);
        userTxt = (TextView) findViewById(R.id.tv_user);
        emailTxt = (TextView) findViewById(R.id.tv_email);
        phoneTxt = (TextView) findViewById(R.id.tv_phone);
        addressTxt = (TextView) findViewById(R.id.tv_address);
        passwordTxt = (TextView) findViewById(R.id.tv_password);

        backImg = (ImageView) findViewById(R.id.btnBack);
        imgModify = (ImageView) findViewById(R.id.btnModUser);
        imgProfileUser = (CircleImageView) findViewById(R.id.imgUser);

        active = (TextView) findViewById(R.id.appointmentActive);
        complete = (TextView) findViewById(R.id.appointmentComplete);
        cancel = (TextView) findViewById(R.id.appointmentCancel);

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dashboard = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(dashboard);
            }
        });

        imgModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UpdateUserActivity.class);
                startActivity(intent);
            }
        });

        HashMap<String, String> detail = session.getUserDetail();

        String id = detail.get(UserSessionManager.KEY_ID);
        String nombre = detail.get(UserSessionManager.KEY_NAME);
        String usuario = detail.get(UserSessionManager.KEY_USER);
        String email = detail.get(UserSessionManager.KEY_EMAIL);
        String telefono = detail.get(UserSessionManager.KEY_PHONE);
        String direccion = detail.get(UserSessionManager.KEY_ADDRESS);
        String contrasena = detail.get(UserSessionManager.KEY_PASSWORD);

        nameTxt.setText(nombre);
        userTxt.setText(usuario);
        emailTxt.setText(email);
        phoneTxt.setText(telefono);
        addressTxt.setText(direccion);
        passwordTxt.setText(contrasena);

        String urlImagen = "http://codonticapp.000webhostapp.com/imagenes/" +usuario+".jpg";

        Log.i("IMAGENNN", urlImagen);

        cargarWebServiceImagen(urlImagen);
    }

    private void cargarWebServiceImagen(String urlImagen) {

        urlImagen=urlImagen.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                bitmap =response; //SE MODIFICA
                imgProfileUser.setImageBitmap(response);

            }
        }, 0, 0, CircleImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this,"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyUse.addToQueue(imageRequest, request, this, volley);
    }
}
