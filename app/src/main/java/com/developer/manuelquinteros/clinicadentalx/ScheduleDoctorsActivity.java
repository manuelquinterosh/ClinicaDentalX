package com.developer.manuelquinteros.clinicadentalx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.developer.manuelquinteros.clinicadentalx.adapter.GridCustomAdapterSchedule;
import com.developer.manuelquinteros.clinicadentalx.model.Schedule;
import com.developer.manuelquinteros.clinicadentalx.volley.VolleyUse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDoctorsActivity extends AppCompatActivity {


    private List<Schedule> scheduleList;
    GridView gridview;
    private RequestQueue request;
    private VolleyUse volley;
    int positionSelected = 0;
    private ProgressBar mProgress;
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_doctors);

        mEmptyView = findViewById(R.id.doctors_schedules_empty);

        mProgress = (ProgressBar) findViewById(R.id.progress_schedule);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list_schedule);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        gridview = (GridView) findViewById(R.id.gridViewMainActivity);

        scheduleList = new ArrayList<>();
        volley = VolleyUse.getInstance(this);
        request = volley.getRequestQueue();

        Intent DoctorsActivityIntent = getIntent();
        final String idDoc = DoctorsActivityIntent.getStringExtra("iddoctor");
        final String nombre_doctor = DoctorsActivityIntent.getStringExtra("nombre");
        final String especialidad_doctor = DoctorsActivityIntent.getStringExtra("especialidad");
        final String descripcion_doctor = DoctorsActivityIntent.getStringExtra("descripcion");

        getSupportActionBar().setTitle(nombre_doctor);
        getSupportActionBar().setSubtitle("Horarios Disponibles");

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionSelected = position;
                ((BaseAdapter)gridview.getAdapter()).notifyDataSetChanged();
                Intent intent = new Intent(ScheduleDoctorsActivity.this, AddAppointmentActivity.class);
                intent.putExtra("idhorarios", scheduleList.get(position).getIdhora());
                intent.putExtra("horario_inicio", scheduleList.get(position).getHora_inicio());
                intent.putExtra("horario_final", scheduleList.get(position).getHora_final());
                intent.putExtra("nombre", nombre_doctor);
                intent.putExtra("especialidad", especialidad_doctor);
                intent.putExtra("descripcion", descripcion_doctor);
                intent.putExtra("iddoctor", idDoc);
                startActivity(intent);

                Toast.makeText(ScheduleDoctorsActivity.this, "Position: " + scheduleList.get(position).getIdhora() +  scheduleList.get(position).getHora_inicio() + scheduleList.get(position).getHora_final(), Toast.LENGTH_SHORT).show();

            }
        });

        loadScheduleItem(idDoc);

    }

    private void loadScheduleItem(String idDoctors) {

        //mostrar estado de la carga
        showLoadingIndicator(true);

        String JSON_URL = "https://codonticapp.000webhostapp.com/wsJSONscheduleDoctors.php?iddoctor="+idDoctors;

        JsonObjectRequest jReq = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("selecthorarios");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject jsonObject = list.getJSONObject(i);
                        Schedule schedule = new Schedule();
                        schedule.setIdhora(jsonObject.getString("idhorarios"));
                        schedule.setHora_inicio(jsonObject.getString("horario_inicio"));
                        schedule.setHora_final(jsonObject.getString("horario_final"));

                        scheduleList.add(schedule);
                    }

                    setupGrid(scheduleList);

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

    private void setupGrid(List<Schedule> list){
        GridCustomAdapterSchedule customAdapterHorarios = new GridCustomAdapterSchedule(ScheduleDoctorsActivity.this, list);
        gridview.setAdapter(customAdapterHorarios);

        if (list.size()==0 ){
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }

    }


    private void showLoadingIndicator(boolean show) {
        mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        gridview.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (scheduleList.size()==0 ){
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }
}
