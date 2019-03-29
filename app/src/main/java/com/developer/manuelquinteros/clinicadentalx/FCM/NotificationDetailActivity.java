package com.developer.manuelquinteros.clinicadentalx.FCM;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.developer.manuelquinteros.clinicadentalx.R;

public class NotificationDetailActivity extends AppCompatActivity {

       TextView textViewTitle, textViewFecha, textViewContenido, textViewDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textViewTitle = (TextView) findViewById(R.id.titulo);
        textViewFecha = (TextView) findViewById(R.id.fecha);
        textViewContenido = (TextView) findViewById(R.id.contenido);
        textViewDiscount = (TextView) findViewById(R.id.discount);

        Intent PromotionsActivityIntent = getIntent();
        String title = PromotionsActivityIntent.getStringExtra("title");
        String description = PromotionsActivityIntent.getStringExtra("description");
        String expiry_date = PromotionsActivityIntent.getStringExtra("expiry_date");
        String discount =  PromotionsActivityIntent.getStringExtra("discount");


        textViewTitle.setText(title);
        textViewFecha.setText(expiry_date);
        textViewContenido.setText(description);
        textViewDiscount.setText(discount);
    }
}
