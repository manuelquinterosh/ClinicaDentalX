package com.developer.manuelquinteros.clinicadentalx;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.developer.manuelquinteros.clinicadentalx.adapter.AppointmentAdapter;
import com.developer.manuelquinteros.clinicadentalx.model.Appointment;
import com.developer.manuelquinteros.clinicadentalx.prefs.UserSessionManager;
import com.developer.manuelquinteros.clinicadentalx.volley.VolleyUse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity implements AppointmentAdapter.OnItemClickListener {

    private RecyclerView recyclerAppointment;
    private ArrayList<Appointment> listAppointment;
    private RequestQueue request;
    private VolleyUse volley;
    private UserSessionManager session;
    ProgressDialog progress;
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        mEmptyView = findViewById(R.id.noAppointment);

        // Permisos.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1000);
        } else {
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_appointment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        volley = VolleyUse.getInstance(this);
        request = volley.getRequestQueue();

        listAppointment = new ArrayList<>();

        recyclerAppointment = (RecyclerView) findViewById(R.id.list_appointment);
        recyclerAppointment.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerAppointment.setHasFixedSize(true);

        session = new UserSessionManager(getApplicationContext());

        FloatingActionButton c = (FloatingActionButton) findViewById(R.id.create_appointment);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddAppointmentActivity.class);
                startActivity(intent);
            }
        });


        HashMap<String, String> detail = session.getUserDetail();

        final String id = detail.get(UserSessionManager.KEY_ID);


        loadAppointment(id);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listAppointment.clear();
                swipeRefreshLayout.setRefreshing(false);
                loadAppointment(id);
            }
        });



    }


    private void loadAppointment(final String id_usuario) {

        //mostrar estado de la carga
        showLoadingIndicator(true);

        String JSON_URL = "https://codonticapp.000webhostapp.com/wsJSONAppointment.php?user="+id_usuario;
        JsonObjectRequest jReq = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("citando");

                    for (int i =0; i < list.length(); i++) {
                        JSONObject jsonObject = list.getJSONObject(i);
                        Appointment appointment = new Appointment();
                        appointment.setIdcitas(jsonObject.getString("idcitas"));
                        appointment.setFecha(jsonObject.getString("fecha"));
                        appointment.setHora(jsonObject.getString("hora"));
                        appointment.setNpersonas(jsonObject.getString("npersonas"));
                        appointment.setStatus(jsonObject.getString("estado"));
                        appointment.setNdoctor(jsonObject.getString("nombre"));
                        appointment.setEspecialidad(jsonObject.getString("especialidad"));
                        listAppointment.add(appointment);

                        }
                        setupRecyclerView(listAppointment);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showLoadingIndicator(false);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showLoadingIndicator(false);
            }
        });

        VolleyUse.addToQueue(jReq, request, this, volley);


    }

    private void setupRecyclerView(List<Appointment> list) {
        AppointmentAdapter adapter = new AppointmentAdapter(this, list, this);
        recyclerAppointment.setAdapter(adapter);
        adapter.swapItems(list);

        //Mostrar un mensaje si la lista esta vacía [cuando carga la app]
        int numItems =  adapter.getItemCount();
        if(numItems == 0)
            emptyList();
        else
            dateList();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);

                if(itemCount == 0)
                    emptyList();
                else
                    dateList();
            }

        });

    }

    private void showLoadingIndicator(final boolean show){
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(show);
            }
        });
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


    @Override
    public void onItemClick(Appointment clickedAppointment) {

    }

    @Override
    public void onCancelAppointment(Appointment canceledAppointment) {
        eliminarCita(canceledAppointment.getIdcitas());

    }

    @Override
    public void onPrintReportAppointment(Appointment editAppointment) {
        Intent intent = new Intent(AppointmentActivity.this, GenerateReportActivity.class);
        intent.putExtra("idcitas", editAppointment.getIdcitas());
        intent.putExtra("fecha", editAppointment.getFecha());
        intent.putExtra("hora", editAppointment.getHora());
        intent.putExtra("nombre", editAppointment.getNdoctor());
        intent.putExtra("npersonas", editAppointment.getNpersonas());
        startActivity(intent);
    }


    public void eliminarCita(String idCita){


       showLoadingIndicator(true);

        String url = "https://codonticapp.000webhostapp.com/swJSONdeleteAppointment.php?idcitas="+idCita;


        JsonObjectRequest jDelReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    int success = response.getInt("success");
                    if (success==1){

                        showLoadingIndicator(false);
                        Toast.makeText(getApplicationContext(),"delete success",Toast.LENGTH_LONG).show();
                        Intent list = new Intent(getApplicationContext(),AppointmentActivity.class);
                        list.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(list);

                    } else {
                        showLoadingIndicator(false);
                        Toast.makeText(getApplicationContext(),"delete failed",Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showLoadingIndicator(false);
            }
        });

        VolleyUse.addToQueue(jDelReq, request, getApplicationContext(), volley);

    }


    public void emptyList(){

        mEmptyView.setVisibility(View.VISIBLE);
        recyclerAppointment.setVisibility(View.GONE);
    }

    public void dateList(){

        mEmptyView.setVisibility(View.GONE);
        recyclerAppointment.setVisibility(View.VISIBLE);
    }


}
