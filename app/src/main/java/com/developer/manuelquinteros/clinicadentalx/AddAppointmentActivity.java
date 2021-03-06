package com.developer.manuelquinteros.clinicadentalx;


import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.developer.manuelquinteros.clinicadentalx.prefs.UserSessionManager;
import com.developer.manuelquinteros.clinicadentalx.prefs.persistenceField;
import com.developer.manuelquinteros.clinicadentalx.volley.VolleyUse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddAppointmentActivity extends AppCompatActivity {

    String url_citar = "https://codonticapp.000webhostapp.com/wsJSONsaveAppointment.php";


    private TextView txtNameDoc, txtTime;
    private TextInputEditText codeAppointment, dateAppointment, nPersonAppointment;


    private View mCardDoctor;
    private View mDoctorContentView;
    private View mEmptyDoctorView;

    private ProgressBar mProgressBar;

    private Button bCrearCita;

    ProgressDialog progreso;

    private RequestQueue request;
    private VolleyUse volley;
    UserSessionManager session;


    private int Mdia, Mmes, Mano;

    persistenceField fieldManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        fieldManager = new persistenceField(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_appointment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        volley = VolleyUse.getInstance(this);
        request = volley.getRequestQueue();


        codeAppointment = (TextInputEditText) findViewById(R.id.edCodeAppointment);
        dateAppointment = (TextInputEditText) findViewById(R.id.edDateAppointment);
        nPersonAppointment = (TextInputEditText) findViewById(R.id.edNpersonAppointment);


        bCrearCita = (Button) findViewById(R.id.btnQuote);

        txtNameDoc = (TextView) findViewById(R.id.appointment_doctor_name);
        txtTime = (TextView) findViewById(R.id.appointment_doctor_time_schedule);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        mCardDoctor = findViewById(R.id.citadoctor_card);
        mEmptyDoctorView = findViewById(R.id.empty_doctor);
        mDoctorContentView = findViewById(R.id.doctor_content);

        FloatingActionButton btnBuscarDoctor = (FloatingActionButton) findViewById(R.id.search_doctor_button);
        btnBuscarDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAppointmentActivity.this, DoctorsActivity.class);
                startActivity(intent);
            }
        });


        dateAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarFecha(v);
            }
        });


        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> detail = session.getUserDetail();
        final String id = detail.get(UserSessionManager.KEY_ID);


        Intent DoctorActivityIntent = getIntent();
        final String idDoc = DoctorActivityIntent.getStringExtra("iddoctor");
        String ndoc = DoctorActivityIntent.getStringExtra("nombre");
        final String time_ini = DoctorActivityIntent.getStringExtra("horario_inicio");
        final String time_fin = DoctorActivityIntent.getStringExtra("horario_final");

        showIntentHora(ndoc, time_ini, time_fin);



        progreso = new ProgressDialog(AddAppointmentActivity.this);
        progreso.setMessage("Agregar Cita");
        progreso.setCancelable(false);

        bCrearCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 String cod = codeAppointment.getText().toString();
                 String fch = dateAppointment.getText().toString();
                 String pern = nPersonAppointment.getText().toString();



                if (cod.trim().length() > 0 && fch.trim().length() > 0 && pern.trim().length() > 0 ) {

                    if (isOnline()) {
                        cargarWebService(cod, fch, time_ini, pern, id, idDoc);
                        fieldManager.cleanField();
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(AddAppointmentActivity.this);
                        alert.setTitle("Ups....");
                        alert.setMessage("No hay conexion a Internet.");
                        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progreso.dismiss();
                            }
                        });
                        alert.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Por favor! Ingrese las credenciales", Toast.LENGTH_LONG).show();
                }
            }
        });

        HashMap<String, String> field = fieldManager.getValueField();
        String codField = field.get(persistenceField.KEY_COD);
        String datField = field.get(persistenceField.KEY_DAT);
        String perField = field.get(persistenceField.KEY_PER);

        codeAppointment.setText(codField);
        dateAppointment.setText(datField);
        nPersonAppointment.setText(perField);

        codeAppointment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fieldManager.saveCODE(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        dateAppointment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fieldManager.saveDATE(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        nPersonAppointment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 fieldManager.savePERSONA(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progreso!=null && progreso.isShowing() ){
            progreso.cancel();
        }
    }

    private void showIntentHora(String name_doctor, String init_time, String end_time) {
        if (name_doctor ==null && init_time == null && end_time == null){
            mEmptyDoctorView.setVisibility(View.VISIBLE);
            mDoctorContentView.setVisibility(View.GONE);

        } else {
            mEmptyDoctorView.setVisibility(View.GONE);
            mDoctorContentView.setVisibility(View.VISIBLE);
            txtNameDoc.setText(name_doctor);
            txtTime.setText(init_time + " a " + end_time);
        }

    }

    private void mostrarFecha(View v) {
        if (v == dateAppointment) {
            final Calendar c = Calendar.getInstance();
            Mdia = c.get(Calendar.DAY_OF_YEAR);
            Mmes = c.get(Calendar.MONTH);
            Mano = c.get(Calendar.DAY_OF_YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    dateAppointment.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                }
            }
                    , Mdia, Mmes, Mano);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

            datePickerDialog.show();
        }

    }

    private void cargarWebService(final String code, final String fech, final String hor, final String nper, final String idUser, final String idDoctor) {
        progreso.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_citar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progreso.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), "Agregando cita", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), AppointmentActivity.class);
                        startActivity(i);
                        finish();
                        displayNotification();

                    } else {
                        Toast.makeText(getApplicationContext(), "Fallo en agregar", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<>();
                parametros.put("idcitas", code);
                parametros.put("fecha", fech);
                parametros.put("hora", hor);
                parametros.put("npersonas", nper);
                parametros.put("iddoctor", idDoctor );
                parametros.put("idUser", idUser);

                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyUse.addToQueue(stringRequest, request, this, volley);
    }


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            //calling logout method when the logout button is clicked
            session.logout();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void displayNotification() {

        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        int NOTIFICATION_ID = 234;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(mChannel);
            }

        }


        Intent resultIntent = new Intent(this, AppointmentActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AppointmentActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Clinica Dental X")
                .setContentText("Citas Odontologo en Linea/MedicAPP")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("doctor de cita : " +txtNameDoc.getText().toString()+ " Revisa tu Bandeja de MSJ"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setColor(getResources().getColor(android.R.color.holo_red_dark))
                .addAction(R.drawable.ic_launcher_foreground, "Ver Citas", resultPendingIntent);


        if (notificationManager != null) {

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }

    }

}
