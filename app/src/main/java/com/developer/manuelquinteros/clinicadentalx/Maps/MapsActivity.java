package com.developer.manuelquinteros.clinicadentalx.Maps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.developer.manuelquinteros.clinicadentalx.Maps.Fragment.MapsDialogFragment;
import com.developer.manuelquinteros.clinicadentalx.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends AppCompatActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker markerHito, InfoMoncagua, InfoChapeltique, InfoCuidadBarrios, InfoSanMiguel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_maps);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //Moncagua
        LatLng moncagua = new LatLng(13.52177, -88.244283);
        InfoMoncagua =  googleMap.addMarker(new MarkerOptions()
                .position(moncagua)
                .title("Moncagua")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.officemarker)));

        //Chapeltique
        LatLng chapeltique = new LatLng(13.629898, -88.269003);
        InfoChapeltique =  googleMap.addMarker(new MarkerOptions()
                .position(chapeltique)
                .title("Chapeltique")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.officemarkerdos)));

        //Cuidad Barrios
        LatLng barrios = new LatLng(13.755319, -88.255956);
        InfoCuidadBarrios=  googleMap.addMarker(new MarkerOptions()
                .position(barrios)
                .title("Cuidad Barrios")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.officemarkertres)));

        //San Miguel
        LatLng sanmiguel = new LatLng(13.454465, -88.1477);
        InfoSanMiguel=  googleMap.addMarker(new MarkerOptions()
                .position(sanmiguel)
                .title("San Miguel")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.officemarkercuatro)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moncagua, 10));

        googleMap.setOnMarkerClickListener(this);

        googleMap.setOnInfoWindowClickListener(this);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.equals(markerHito)) {

        }

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        if (marker.equals(InfoMoncagua)) {

            MapsDialogFragment.newInstance(marker.getTitle(),
                    getString(R.string.MoncaguaInfo))
                    .show(getSupportFragmentManager(), null);
        }
        if (marker.equals(InfoChapeltique)) {
            MapsDialogFragment.newInstance(marker.getTitle(),
                    getString(R.string.ChapeltiqueInfo))
                    .show(getSupportFragmentManager(), null);
        }

        if (marker.equals(InfoCuidadBarrios)) {
            MapsDialogFragment.newInstance(marker.getTitle(),
                    getString(R.string.BarriosInfo))
                    .show(getSupportFragmentManager(), null);
        }

        if (marker.equals(InfoSanMiguel)) {
            MapsDialogFragment.newInstance(marker.getTitle(),
                    getString(R.string.SanMiguelInfo))
                    .show(getSupportFragmentManager(), null);
        }
    }
}
