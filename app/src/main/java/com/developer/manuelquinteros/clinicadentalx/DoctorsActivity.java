package com.developer.manuelquinteros.clinicadentalx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.developer.manuelquinteros.clinicadentalx.adapter.DoctorsAdapter;
import com.developer.manuelquinteros.clinicadentalx.model.Doctors;
import com.developer.manuelquinteros.clinicadentalx.volley.VolleyUse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorsActivity extends AppCompatActivity implements DoctorsAdapter.OnItemListenerDoctor {

    private RequestQueue request;
    private VolleyUse volley;

    private RecyclerView recyclerDoctors;
    private ArrayList<Doctors> listDoctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_doctors);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listDoctors = new ArrayList<>();
        volley = VolleyUse.getInstance(this);
        request = volley.getRequestQueue();

        recyclerDoctors = (RecyclerView) findViewById(R.id.doctors_schedules_list);
        recyclerDoctors.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerDoctors.setHasFixedSize(true);


        loadDoctors();
    }

    private void loadDoctors() {
        String JSON_URL = "https://codonticapp.000webhostapp.com/wsJSONListDoctor.php";

        JsonObjectRequest jReq = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray list = response.getJSONArray("selecdoctor");
                    for (int i =0; i < list.length(); i++) {
                        JSONObject jsonObject = list.getJSONObject(i);
                        Doctors doctors = new Doctors();
                        doctors.setIdDoctor(jsonObject.getString("iddoctor"));
                        doctors.setNombre(jsonObject.getString("nombre"));
                        doctors.setEspecialidad(jsonObject.getString("especialidad"));
                        doctors.setDescripcion(jsonObject.getString("descripcion"));
                        doctors.setImage_url(jsonObject.getString("ruta_imagen"));

                        listDoctors.add(doctors);
                    }

                    setupRecyclerView(listDoctors);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleyUse.addToQueue(jReq, request, this, volley);
    }

    private void setupRecyclerView(List<Doctors> list) {
        DoctorsAdapter doctorsAdapter = new DoctorsAdapter(list, this, this);
        recyclerDoctors.setAdapter(doctorsAdapter);
    }

    @Override
    public void onInfoDoctorClicked(Doctors doctorClicked) {

        Toast.makeText(getApplicationContext(),"CLICN en"+doctorClicked.getIdDoctor() ,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DoctorsActivity.this, ScheduleDoctorsActivity.class);
        intent.putExtra("iddoctor", doctorClicked.getIdDoctor());
        intent.putExtra("nombre", doctorClicked.getNombre());
        intent.putExtra("especialidad", doctorClicked.getEspecialidad());
        intent.putExtra("descripcion", doctorClicked.getDescripcion());
        startActivity(intent);
    }
}
